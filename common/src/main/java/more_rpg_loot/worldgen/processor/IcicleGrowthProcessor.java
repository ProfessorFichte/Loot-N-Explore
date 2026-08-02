package more_rpg_loot.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.blocks.frozen_depths.IcicleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Thickness;
import net.minecraft.registry.Registries;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Reusable structure processor that grows this mod's IcicleBlock formations off floor/ceiling
// blocks encountered while a structure is placed. Ground and ceiling each get their own spawn
// chance and size range so a structure JSON can tune them independently.
public class IcicleGrowthProcessor extends StructureProcessor {

    private static final int BLOCK_UPDATE_FLAGS = Block.NOTIFY_LISTENERS | Block.FORCE_STATE;

    public static final MapCodec<IcicleGrowthProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.DOUBLE.optionalFieldOf("ground_spawn_chance", 0.0).forGetter(p -> p.groundSpawnChance),
                    Codec.INT.optionalFieldOf("ground_min_size", 1).forGetter(p -> p.groundMinSize),
                    Codec.INT.optionalFieldOf("ground_max_size", 1).forGetter(p -> p.groundMaxSize),
                    Codec.DOUBLE.optionalFieldOf("ceiling_spawn_chance", 0.0).forGetter(p -> p.ceilingSpawnChance),
                    Codec.INT.optionalFieldOf("ceiling_min_size", 1).forGetter(p -> p.ceilingMinSize),
                    Codec.INT.optionalFieldOf("ceiling_max_size", 1).forGetter(p -> p.ceilingMaxSize),
                    Codec.STRING.listOf().optionalFieldOf("blocked_anchor_blocks", List.of()).forGetter(p -> p.blockedAnchorBlockIds)
            ).apply(instance, IcicleGrowthProcessor::new)
    );

    private final double groundSpawnChance;
    private final int groundMinSize;
    private final int groundMaxSize;
    private final double ceilingSpawnChance;
    private final int ceilingMinSize;
    private final int ceilingMaxSize;
    private final List<String> blockedAnchorBlockIds;
    private final Set<Block> blockedAnchorBlocks;

    public IcicleGrowthProcessor(
            double groundSpawnChance, int groundMinSize, int groundMaxSize,
            double ceilingSpawnChance, int ceilingMinSize, int ceilingMaxSize,
            List<String> blockedAnchorBlockIds
    ) {
        this.groundSpawnChance = groundSpawnChance;
        this.groundMinSize = groundMinSize;
        this.groundMaxSize = groundMaxSize;
        this.ceilingSpawnChance = ceilingSpawnChance;
        this.ceilingMinSize = ceilingMinSize;
        this.ceilingMaxSize = ceilingMaxSize;
        this.blockedAnchorBlockIds = blockedAnchorBlockIds;
        this.blockedAnchorBlocks = blockedAnchorBlockIds.stream()
                .map(id -> Registries.BLOCK.get(Identifier.of(id)))
                .collect(Collectors.toSet());
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(
            WorldView world,
            BlockPos pos,
            BlockPos pivot,
            StructureTemplate.StructureBlockInfo originalBlockInfo,
            StructureTemplate.StructureBlockInfo currentBlockInfo,
            StructurePlacementData placementData
    ) {
        if (!currentBlockInfo.state().isAir() || !(world instanceof WorldAccess worldAccess)) {
            return currentBlockInfo;
        }

        BlockPos blockPos = currentBlockInfo.pos();
        Random random = Random.create(blockPos.asLong());

        if (ceilingSpawnChance > 0 && random.nextDouble() < ceilingSpawnChance
                && isValidAnchor(world, blockPos.up(), Direction.DOWN)) {
            growIcicle(worldAccess, blockPos, Direction.DOWN, randomSize(random, ceilingMinSize, ceilingMaxSize));
            return null;
        }

        if (groundSpawnChance > 0 && random.nextDouble() < groundSpawnChance
                && isValidAnchor(world, blockPos.down(), Direction.UP)) {
            growIcicle(worldAccess, blockPos, Direction.UP, randomSize(random, groundMinSize, groundMaxSize));
            return null;
        }

        return currentBlockInfo;
    }

    private boolean isValidAnchor(WorldView world, BlockPos anchorPos, Direction exposedFace) {
        BlockState anchorState = world.getBlockState(anchorPos);
        if (blockedAnchorBlocks.contains(anchorState.getBlock())) {
            return false;
        }
        return anchorState.isSideSolidFullSquare(world, anchorPos, exposedFace);
    }

    private void growIcicle(WorldAccess world, BlockPos startPos, Direction pointingDirection, int size) {
        Block icicleBlock = ModBlocks.ICICLE.block();
        BlockPos.Mutable mutable = startPos.mutableCopy();

        for (int i = 0; i < size; i++) {
            BlockState current = world.getBlockState(mutable);
            if (!current.isAir() && !current.isOf(Blocks.CAVE_AIR)) {
                break; // ran into existing structure content, stop the chain here
            }

            BlockState icicleState = icicleBlock.getDefaultState()
                    .with(IcicleBlock.VERTICAL_DIRECTION, pointingDirection)
                    .with(IcicleBlock.THICKNESS, thicknessFor(i, size));
            world.setBlockState(mutable, icicleState, BLOCK_UPDATE_FLAGS);
            mutable.move(pointingDirection);
        }
    }

    // Mirrors the base/frustum/middle/tip shape IcicleBlock itself derives from neighbor states,
    // computed upfront here since the chain isn't in the world yet for it to read back.
    private static Thickness thicknessFor(int index, int size) {
        if (size <= 1 || index == size - 1) {
            return Thickness.TIP;
        }
        if (index == size - 2) {
            return Thickness.FRUSTUM;
        }
        if (index == 0) {
            return Thickness.BASE;
        }
        return Thickness.MIDDLE;
    }

    private static int randomSize(Random random, int min, int max) {
        if (max <= min) {
            return Math.max(1, min);
        }
        return min + random.nextInt(max - min + 1);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessorTypes.ICICLE_GROWTH;
    }
}

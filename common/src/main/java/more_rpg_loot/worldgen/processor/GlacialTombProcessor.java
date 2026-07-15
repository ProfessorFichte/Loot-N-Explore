package more_rpg_loot.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import more_rpg_loot.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlacialTombProcessor extends StructureProcessor {

    private static final Block[] ICE_BLOCKS = {Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE};

    private static final int BLOCK_UPDATE_FLAGS = Block.NOTIFY_LISTENERS | Block.FORCE_STATE;

    private static final int BLEND_DISTANCE = 8;
    private static final double BLEND_INTENSITY = 0.75;

    private static final int ICE_DETECTION_RADIUS = 12;
    private static final double ICE_OVERGROWTH_THRESHOLD = 0.08;
    private static final Block[] OVERGROWTH_ICE_BLOCKS = {Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.ICE};

    private final Map<Long, Double> icyTerrainCache = new HashMap<>();

    public static final MapCodec<GlacialTombProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.fieldOf("top_filler").forGetter(p -> p.topFillerId),
                    Codec.STRING.fieldOf("bottom_filler").forGetter(p -> p.bottomFillerId),
                    Codec.STRING.fieldOf("chain_block").forGetter(p -> p.chainBlockId),
                    Codec.STRING.fieldOf("pillar_block").forGetter(p -> p.pillarBlockId),
                    Codec.STRING.optionalFieldOf("ceiling_block", "minecraft:deepslate_bricks").forGetter(p -> p.ceilingBlockId),
                    Codec.STRING.optionalFieldOf("ceiling_wall_block", "minecraft:deepslate_brick_wall").forGetter(p -> p.ceilingWallBlockId),
                    Codec.INT.optionalFieldOf("max_chain_length", 32).forGetter(p -> p.maxChainLength),
                    Codec.INT.optionalFieldOf("max_pillar_length", 32).forGetter(p -> p.maxPillarLength),
                    Codec.INT.optionalFieldOf("surface_margin", 5).forGetter(p -> p.surfaceMargin),
                    Codec.BOOL.optionalFieldOf("enable_surface_culling", true).forGetter(p -> p.enableSurfaceCulling),
                    Codec.BOOL.optionalFieldOf("remove_water", true).forGetter(p -> p.removeWater),
                    Codec.BOOL.optionalFieldOf("spiral_pillar", true).forGetter(p -> p.spiralPillar),
                    Codec.DOUBLE.optionalFieldOf("spiral_radius", 2.0).forGetter(p -> p.spiralRadius),
                    Codec.DOUBLE.optionalFieldOf("spiral_speed", 0.4).forGetter(p -> p.spiralSpeed),
                    Codec.BOOL.optionalFieldOf("randomize_ice", true).forGetter(p -> p.randomizeIce)
            ).apply(instance, GlacialTombProcessor::new)
    );

    private final String topFillerId;
    private final String bottomFillerId;
    private final String chainBlockId;
    private final String pillarBlockId;
    private final String ceilingBlockId;
    private final String ceilingWallBlockId;
    private final int maxChainLength;
    private final int maxPillarLength;
    private final int surfaceMargin;
    private final boolean enableSurfaceCulling;
    private final boolean removeWater;
    private final boolean spiralPillar;
    private final double spiralRadius;
    private final double spiralSpeed;
    private final boolean randomizeIce;

    private final Block topFiller;
    private final Block bottomFiller;
    private final Block chainBlock;
    private final Block pillarBlock;
    private final Block ceilingBlock;
    private final Block ceilingWallBlock;

    public GlacialTombProcessor(
            String topFillerId,
            String bottomFillerId,
            String chainBlockId,
            String pillarBlockId,
            String ceilingBlockId,
            String ceilingWallBlockId,
            int maxChainLength,
            int maxPillarLength,
            int surfaceMargin,
            boolean enableSurfaceCulling,
            boolean removeWater,
            boolean spiralPillar,
            double spiralRadius,
            double spiralSpeed,
            boolean randomizeIce
    ) {
        this.topFillerId = topFillerId;
        this.bottomFillerId = bottomFillerId;
        this.chainBlockId = chainBlockId;
        this.pillarBlockId = pillarBlockId;
        this.ceilingBlockId = ceilingBlockId;
        this.ceilingWallBlockId = ceilingWallBlockId;
        this.maxChainLength = maxChainLength;
        this.maxPillarLength = maxPillarLength;
        this.surfaceMargin = surfaceMargin;
        this.enableSurfaceCulling = enableSurfaceCulling;
        this.removeWater = removeWater;
        this.spiralPillar = spiralPillar;
        this.spiralRadius = spiralRadius;
        this.spiralSpeed = spiralSpeed;
        this.randomizeIce = randomizeIce;

        this.topFiller = Registries.BLOCK.get(Identifier.of(topFillerId));
        this.bottomFiller = Registries.BLOCK.get(Identifier.of(bottomFillerId));
        this.chainBlock = Registries.BLOCK.get(Identifier.of(chainBlockId));
        this.pillarBlock = Registries.BLOCK.get(Identifier.of(pillarBlockId));
        this.ceilingBlock = Registries.BLOCK.get(Identifier.of(ceilingBlockId));
        this.ceilingWallBlock = Registries.BLOCK.get(Identifier.of(ceilingWallBlockId));
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
        BlockState state = currentBlockInfo.state();
        BlockPos blockPos = currentBlockInfo.pos();

        if (isUnderTreeCanopy(world, blockPos)) {
            // Don't place any structure blocks under trees
            if (!state.isAir()) {
                return null;
            }
            // For air blocks return null
            return null;
        }

        if (enableSurfaceCulling) {
            SurfaceCheckResult surfaceResult = checkSurfaceAndBlend(world, blockPos);

            if (surfaceResult == SurfaceCheckResult.CULL) {
                if (world instanceof ServerWorld serverWorld) {
                    removeEntitiesAt(serverWorld, blockPos);
                }
                return null;
            } else if (surfaceResult == SurfaceCheckResult.BLEND) {
                if (shouldCullSpecialBlock(state, currentBlockInfo.nbt())) {
                    if (world instanceof ServerWorld serverWorld) {
                        removeEntitiesAt(serverWorld, blockPos);
                    }
                    return null;
                }
                if (state.isOf(topFiller)) {
                    if (isExposedToSky(world, blockPos)) {
                        return null;
                    }
                    return new StructureTemplate.StructureBlockInfo(blockPos, ceilingBlock.getDefaultState(), null);
                }
                if (state.isOf(bottomFiller)) {
                    return new StructureTemplate.StructureBlockInfo(blockPos, getRandomIceBlock(world, blockPos), null);
                }
                if (!state.isAir()) {
                    BlockState blendedState = getBlendedBlockState(world, blockPos, state);
                    if (blendedState != state) {
                        if (blendedState.isAir()) {
                            return null;
                        }
                        return new StructureTemplate.StructureBlockInfo(blockPos, blendedState, null);
                    }
                }
            }
        }

        if (state.isOf(topFiller)) {
            return processTopFiller(world, blockPos);
        }

        if (state.isOf(bottomFiller)) {
            return processBottomFiller(world, blockPos);
        }

        if (removeWater) {
            BlockState worldState = world.getBlockState(blockPos);
            if (worldState.getFluidState().isIn(FluidTags.WATER)) {
                if (state.isAir()) {
                    return new StructureTemplate.StructureBlockInfo(blockPos, Blocks.AIR.getDefaultState(), null);
                }
                return currentBlockInfo;
            }
        }

        return currentBlockInfo;
    }

    //Checks if a block should be culled in the blend zone.
    private boolean shouldCullSpecialBlock(BlockState state, @Nullable net.minecraft.nbt.NbtCompound nbt) {
        if (state.isAir()) return false;

        if (state.isOf(Blocks.JIGSAW)) {
            return true;
        }

        if (state.isOf(Blocks.STRUCTURE_BLOCK)) {
            return true;
        }

        if (nbt != null) return true;

        if (state.isOf(Blocks.SPAWNER)) return true;
        if (state.isOf(Blocks.TRIAL_SPAWNER)) return true;
        if (state.isOf(Blocks.VAULT)) return true;
        if (state.isOf(ModBlocks.FROZEN_TRIAL_SPAWNER.block())) return true;
        if (state.isOf(ModBlocks.FROZEN_VAULT.block())) return true;

        if (state.isOf(Blocks.CHEST)) return true;
        if (state.isOf(Blocks.TRAPPED_CHEST)) return true;
        if (state.isOf(Blocks.BARREL)) return true;
        if (state.isOf(Blocks.DECORATED_POT)) return true;
        if (state.isOf(Blocks.SUSPICIOUS_SAND)) return true;
        if (state.isOf(Blocks.SUSPICIOUS_GRAVEL)) return true;

        if (state.isOf(Blocks.LECTERN)) return true;
        if (state.isOf(Blocks.JUKEBOX)) return true;
        if (state.isOf(Blocks.BREWING_STAND)) return true;
        if (state.isOf(Blocks.ENCHANTING_TABLE)) return true;
        if (state.isOf(Blocks.BEACON)) return true;
        if (state.isOf(Blocks.CONDUIT)) return true;
        if (state.isOf(Blocks.END_PORTAL_FRAME)) return true;

        String blockId = Registries.BLOCK.getId(state.getBlock()).toString().toLowerCase();
        if (blockId.contains("spawner") || blockId.contains("vault") ||
            blockId.contains("trial") || blockId.contains("loot") ||
            blockId.contains("jigsaw")) {
            return true;
        }

        return false;
    }


     // Removes entities at or near the given position.
    private void removeEntitiesAt(ServerWorld world, BlockPos pos) {
        Box searchBox = new Box(pos).expand(1.5);

        List<Entity> entities = world.getEntitiesByClass(Entity.class, searchBox, entity -> {
            if (entity instanceof ItemFrameEntity) return true;
            if (entity instanceof ArmorStandEntity) return true;
            if (entity instanceof BatEntity) return true;
            String entityType = Registries.ENTITY_TYPE.getId(entity.getType()).toString().toLowerCase();
            if (entityType.contains("frame") || entityType.contains("painting") ||
                entityType.contains("stand") || entityType.contains("display")) {
                return true;
            }
            return false;
        });

        for (Entity entity : entities) {
            entity.discard();
        }
    }

    private StructureTemplate.StructureBlockInfo processTopFiller(WorldView world, BlockPos blockPos) {
        if (world instanceof WorldAccess worldAccess) {
            BlockPos abovePos = blockPos.up();
            generateChainUpward(worldAccess, abovePos);
        }

        return new StructureTemplate.StructureBlockInfo(blockPos, ceilingBlock.getDefaultState(), null);
    }

    private StructureTemplate.StructureBlockInfo processBottomFiller(WorldView world, BlockPos blockPos) {
        if (world instanceof WorldAccess worldAccess) {
            BlockPos belowPos = blockPos.down();
            if (spiralPillar) {
                generateSpiralPillarDownward(worldAccess, belowPos);
            } else {
                generatePillarDownward(worldAccess, belowPos);
            }
        }

        return new StructureTemplate.StructureBlockInfo(blockPos, getRandomIceBlock(world, blockPos), null);
    }

    private void generateChainUpward(WorldAccess world, BlockPos wallPos) {
        BlockState wallPosState = world.getBlockState(wallPos);
        if (!canChainReplace(wallPosState)) {
            return;
        }

        BlockState wallState = ceilingWallBlock.getDefaultState();
        if (wallState.contains(Properties.UP)) {
            wallState = wallState.with(Properties.UP, true);
        }
        world.setBlockState(wallPos, wallState, BLOCK_UPDATE_FLAGS);

        BlockPos.Mutable mutablePos = wallPos.up().mutableCopy();

        for (int i = 0; i < maxChainLength; i++) {
            BlockState currentState = world.getBlockState(mutablePos);
            if (canChainReplace(currentState)) {
                world.setBlockState(mutablePos, chainBlock.getDefaultState(), BLOCK_UPDATE_FLAGS);
                mutablePos.move(Direction.UP);
            } else {
                break;
            }
        }
    }

    private void generatePillarDownward(WorldAccess world, BlockPos startPos) {
        BlockPos.Mutable mutablePos = startPos.mutableCopy();

        for (int i = 0; i < maxPillarLength; i++) {
            BlockState currentState = world.getBlockState(mutablePos);

            if (canPillarReplace(currentState)) {
                world.setBlockState(mutablePos, getRandomIceBlock(world, mutablePos), BLOCK_UPDATE_FLAGS);
                mutablePos.move(Direction.DOWN);
            } else {
                break;
            }
        }
    }

    private void generateSpiralPillarDownward(WorldAccess world, BlockPos centerPos) {
        int centerX = centerPos.getX();
        int centerZ = centerPos.getZ();
        int startY = centerPos.getY();

        for (int i = 0; i < maxPillarLength; i++) {
            int currentY = startY - i;
            BlockPos pillarPos = new BlockPos(centerX, currentY, centerZ);
            BlockState currentState = world.getBlockState(pillarPos);

            if (!canPillarReplace(currentState)) {
                break;
            }

            world.setBlockState(pillarPos, getRandomIceBlock(world, pillarPos), BLOCK_UPDATE_FLAGS);

            double angle = i * spiralSpeed;
            int offsetX = (int) Math.round(Math.cos(angle) * spiralRadius);
            int offsetZ = (int) Math.round(Math.sin(angle) * spiralRadius);

            if (offsetX != 0 || offsetZ != 0) {
                BlockPos spiralPos = new BlockPos(centerX + offsetX, currentY, centerZ + offsetZ);
                BlockState spiralState = world.getBlockState(spiralPos);

                if (canPillarReplace(spiralState)) {
                    world.setBlockState(spiralPos, getRandomIceBlock(world, spiralPos), BLOCK_UPDATE_FLAGS);
                }

                if (i > 0) {
                    double prevAngle = (i - 1) * spiralSpeed;
                    int prevOffsetX = (int) Math.round(Math.cos(prevAngle) * spiralRadius);
                    int prevOffsetZ = (int) Math.round(Math.sin(prevAngle) * spiralRadius);
                    fillSpiralGap(world, centerX, centerZ, currentY, prevOffsetX, prevOffsetZ, offsetX, offsetZ);
                }
            }
        }
    }

    private void fillSpiralGap(WorldAccess world, int centerX, int centerZ, int y,
                               int prevOffsetX, int prevOffsetZ, int currOffsetX, int currOffsetZ) {
        int dx = currOffsetX - prevOffsetX;
        int dz = currOffsetZ - prevOffsetZ;

        if (Math.abs(dx) > 1 || Math.abs(dz) > 1) {
            BlockPos gapPos = new BlockPos(centerX + prevOffsetX, y, centerZ + prevOffsetZ);
            BlockState gapState = world.getBlockState(gapPos);
            if (canPillarReplace(gapState)) {
                world.setBlockState(gapPos, getRandomIceBlock(world, gapPos), BLOCK_UPDATE_FLAGS);
            }
        }
    }

    private BlockState getRandomIceBlock(WorldView world, BlockPos pos) {
        if (!randomizeIce) {
            return pillarBlock.getDefaultState();
        }
        long seed = pos.asLong();
        Random random = Random.create(seed);
        return ICE_BLOCKS[random.nextInt(ICE_BLOCKS.length)].getDefaultState();
    }

    private boolean isAirOrCaveAir(BlockState state) {
        return state.isAir() || state.isOf(Blocks.CAVE_AIR);
    }


    private boolean shouldStartChain(BlockState state) {
        if (state.isAir()) return true;
        if (state.isOf(Blocks.CAVE_AIR)) return true;
        if (state.isOf(Blocks.WATER)) return true;
        if (!state.getFluidState().isEmpty() && state.getFluidState().isIn(FluidTags.WATER)) return true;
        return false;
    }

    private boolean shouldStartPillar(BlockState state) {
        if (state.isAir()) return true;
        if (state.isOf(Blocks.CAVE_AIR)) return true;
        if (state.isOf(Blocks.WATER)) return true;
        if (state.isOf(Blocks.LAVA)) return true;
        if (!state.getFluidState().isEmpty()) return true;
        return false;
    }

    private boolean canChainReplace(BlockState state) {
        if (isTreeBlock(state)) return false;
        if (isTerrainBlock(state)) return false;
        if (state.isAir()) return true;
        if (state.isOf(Blocks.CAVE_AIR)) return true;
        if (state.isOf(Blocks.WATER)) return true;
        if (!state.getFluidState().isEmpty() && state.getFluidState().isIn(FluidTags.WATER)) return true;
        return false;
    }

    private boolean canPillarReplace(BlockState state) {
        if (state.isAir() || state.isOf(Blocks.CAVE_AIR)) {
            return true;
        }
        if (state.isOf(Blocks.WATER) || state.isOf(Blocks.LAVA)) {
            return true;
        }
        if (state.getFluidState().isIn(FluidTags.WATER) || state.getFluidState().isIn(FluidTags.LAVA)) {
            return true;
        }
        if (state.isOf(Blocks.SNOW) || state.isOf(Blocks.POWDER_SNOW)) {
            return true;
        }
        if (state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.SMALL_FLOWERS) ||
            state.isIn(BlockTags.TALL_FLOWERS) || state.isIn(BlockTags.REPLACEABLE_BY_TREES)) {
            return true;
        }
        if (state.isOf(Blocks.SHORT_GRASS) || state.isOf(Blocks.TALL_GRASS) ||
            state.isOf(Blocks.FERN) || state.isOf(Blocks.LARGE_FERN)) {
            return true;
        }
        if (state.isOf(Blocks.DEAD_BUSH) || state.isOf(Blocks.VINE) ||
            state.isOf(Blocks.GLOW_LICHEN) || state.isOf(Blocks.MOSS_CARPET)) {
            return true;
        }
        if (state.isOf(Blocks.SEAGRASS) || state.isOf(Blocks.TALL_SEAGRASS) ||
            state.isOf(Blocks.KELP) || state.isOf(Blocks.KELP_PLANT)) {
            return true;
        }
        return false;
    }

    private enum SurfaceCheckResult {
        KEEP,
        BLEND,
        CULL
    }

    private SurfaceCheckResult checkSurfaceAndBlend(WorldView world, BlockPos blockPos) {
        int x = blockPos.getX();
        int z = blockPos.getZ();
        int y = blockPos.getY();

        int worldSurfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, z);
        int oceanFloorY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);

        boolean hasWaterAbove = worldSurfaceY > oceanFloorY + 1;

        if (hasWaterAbove && y >= oceanFloorY - surfaceMargin && y <= worldSurfaceY) {
            return SurfaceCheckResult.CULL;
        }

        if (hasWaterAbove && y >= oceanFloorY - (surfaceMargin + BLEND_DISTANCE)) {
            BlockState aboveState = world.getBlockState(blockPos.up());
            if (aboveState.getFluidState().isIn(FluidTags.WATER) || aboveState.isOf(Blocks.WATER)) {
                return SurfaceCheckResult.CULL;
            }
        }

        int effectiveSurfaceY = (worldSurfaceY > oceanFloorY) ? oceanFloorY : worldSurfaceY;

        int actualGroundY = findActualGroundLevel(world, x, z, y, effectiveSurfaceY);

        if (actualGroundY < effectiveSurfaceY) {
            effectiveSurfaceY = actualGroundY;
        }

        int distanceFromSurface = effectiveSurfaceY - y;

        if (distanceFromSurface <= surfaceMargin) {
            return SurfaceCheckResult.CULL;
        }

        if (distanceFromSurface <= surfaceMargin + BLEND_DISTANCE) {
            return SurfaceCheckResult.BLEND;
        }

        return SurfaceCheckResult.KEEP;
    }

    private int findActualGroundLevel(WorldView world, int x, int z, int startY, int heightmapSurface) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, heightmapSurface, z);

        for (int checkY = heightmapSurface; checkY >= startY - 5; checkY--) {
            mutable.setY(checkY);
            BlockState state = world.getBlockState(mutable);

            if (state.isAir() || state.isOf(Blocks.CAVE_AIR)) {
                continue;
            }
            if (isTreeBlock(state)) {
                continue;
            }

            return checkY;
        }

        return heightmapSurface;
    }

    private boolean isTreeBlock(BlockState state) {
        if (state.isIn(BlockTags.LEAVES)) return true;
        if (state.isIn(BlockTags.LOGS)) return true;
        if (state.isOf(Blocks.VINE)) return true;
        if (state.isOf(Blocks.MOSS_CARPET)) return true;
        if (state.isOf(Blocks.HANGING_ROOTS)) return true;
        if (state.isOf(Blocks.MANGROVE_ROOTS)) return true;
        if (state.isOf(Blocks.BEE_NEST)) return true;
        if (state.isOf(Blocks.SHROOMLIGHT)) return true;
        if (state.isOf(Blocks.NETHER_WART_BLOCK)) return true;
        if (state.isOf(Blocks.WARPED_WART_BLOCK)) return true;

        String blockId = Registries.BLOCK.getId(state.getBlock()).toString().toLowerCase();
        if (blockId.contains("leaves") || blockId.contains("log") || blockId.contains("wood")) {
            return true;
        }

        return false;
    }


    private boolean isUnderTreeCanopy(WorldView world, BlockPos pos) {
        if (isColumnUnderTreeCanopy(world, pos)) {
            return true;
        }

        for (Direction dir : Direction.Type.HORIZONTAL) {
            if (isColumnUnderTreeCanopy(world, pos.offset(dir))) {
                BlockPos.Mutable checkPos = pos.up().mutableCopy();
                for (int i = 0; i < 8; i++) {
                    BlockState state = world.getBlockState(checkPos);
                    if (isTreeBlock(state)) {
                        return true; // Found tree block nearby above us
                    }
                    if (isTerrainBlock(state)) {
                        break; // Hit terrain, not under canopy from this direction
                    }
                    checkPos.move(Direction.UP);
                }
            }
        }

        return false;
    }

    private boolean isColumnUnderTreeCanopy(WorldView world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.up().mutableCopy();
        boolean foundTreeBlocks = false;
        int airAfterTree = 0;

        for (int i = 0; i < 25; i++) {
            BlockState state = world.getBlockState(mutable);

            if (isTerrainBlock(state)) {
                return false;
            }

            if (isTreeBlock(state)) {
                foundTreeBlocks = true;
                airAfterTree = 0; // Reset air counter when we see more tree blocks
            }

            if (foundTreeBlocks && (state.isAir() || state.isOf(Blocks.CAVE_AIR))) {
                airAfterTree++;
                if (airAfterTree >= 3) {
                    return true;
                }
            }

            mutable.move(Direction.UP);
        }

        return foundTreeBlocks;
    }

    private BlockState getBlendedBlockState(WorldView world, BlockPos blockPos, BlockState originalState) {
        int x = blockPos.getX();
        int z = blockPos.getZ();
        int y = blockPos.getY();

        int worldSurfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, z);
        int oceanFloorY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);
        int heightmapSurface = (worldSurfaceY > oceanFloorY) ? oceanFloorY : worldSurfaceY;

        int effectiveSurfaceY = findActualGroundLevel(world, x, z, y, heightmapSurface);

        if (isUnderTreeCanopy(world, blockPos)) {
            return Blocks.AIR.getDefaultState();
        }

        if (y >= effectiveSurfaceY - 1) {
            return Blocks.AIR.getDefaultState();
        }

        int distanceFromSurface = effectiveSurfaceY - y;
        double blendFactor = 1.0 - ((double)(distanceFromSurface - surfaceMargin) / BLEND_DISTANCE);
        blendFactor = Math.max(0.0, Math.min(1.0, blendFactor)); // Clamp to 0-1

        BlockState aboveState = world.getBlockState(blockPos.up());
        boolean hasTerrainAbove = isTerrainBlock(aboveState);
        boolean hasIceAbove = isIcyBlock(aboveState);
        boolean hasTreeAbove = isTreeBlock(aboveState);
        boolean hasAirAbove = aboveState.isAir() || aboveState.isOf(Blocks.CAVE_AIR);

        if (hasTreeAbove) {
            return Blocks.AIR.getDefaultState();
        }

        Random random = Random.create(blockPos.asLong());
        double chance = random.nextDouble();

        double icyRatio = detectIcyTerrainRatio(world, blockPos);


        if (icyRatio >= ICE_OVERGROWTH_THRESHOLD && !hasAirAbove) {
            boolean hasSupport = hasTerrainAbove || hasIceAbove ||
                                  hasAdjacentTerrainOrIce(world, blockPos) ||
                                  isTerrainBlock(world.getBlockState(blockPos.down())) ||
                                  isIcyBlock(world.getBlockState(blockPos.down()));

            if (hasSupport) {
                BlockState iceOvergrowth = getIceOvergrowthState(world, blockPos, icyRatio, distanceFromSurface);
                if (iceOvergrowth != null) {
                    return iceOvergrowth;
                }
            }
        }

        if (hasAirAbove && !hasTerrainAbove && !hasIceAbove) {
            if (isExposedToSky(world, blockPos)) {
                return Blocks.AIR.getDefaultState(); // Will become null
            }
        }

        if ((hasTerrainAbove || hasIceAbove) && y < effectiveSurfaceY - 2) {
            double terrainDrapeChance = Math.pow(blendFactor, 0.7) * 0.95;
            if (chance < terrainDrapeChance) {
                if (icyRatio > 0.1) {
                    double iceChance = Math.min(0.8, icyRatio * 2.0) * blendFactor;
                    if (random.nextDouble() < iceChance) {
                        return getRandomOvergrowthIce(random, distanceFromSurface);
                    }
                }
                return getTerrainFromAbove(world, blockPos);
            }
            return originalState;
        }

        if (!hasTerrainAbove && !hasIceAbove) {
            return Blocks.AIR.getDefaultState();
        }

        double removeChance = Math.pow(blendFactor, 0.5) * 0.6;
        if (chance < removeChance) {
            return Blocks.AIR.getDefaultState();
        }
        return originalState;
    }

    private BlockState getRandomOvergrowthIce(Random random, int distanceFromSurface) {
        float f = random.nextFloat();

        if (distanceFromSurface <= 3) {
            if (f < 0.5) {
                return Blocks.ICE.getDefaultState();
            } else if (f < 0.8) {
                return Blocks.PACKED_ICE.getDefaultState();
            } else {
                return Blocks.BLUE_ICE.getDefaultState();
            }
        }
        else if (distanceFromSurface <= 6) {
            if (f < 0.25) {
                return Blocks.ICE.getDefaultState();
            } else if (f < 0.65) {
                return Blocks.PACKED_ICE.getDefaultState();
            } else {
                return Blocks.BLUE_ICE.getDefaultState();
            }
        }
        else {
            if (f < 0.1) {
                return Blocks.ICE.getDefaultState();
            } else if (f < 0.5) {
                return Blocks.PACKED_ICE.getDefaultState();
            } else {
                return Blocks.BLUE_ICE.getDefaultState();
            }
        }
    }

    private BlockState getRandomOvergrowthIce(Random random) {
        return getRandomOvergrowthIce(random, 5);
    }

    private boolean isExposedToSky(WorldView world, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        int y = pos.getY();

        int worldSurfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, z);
        int oceanFloorY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);

        int actualGroundY = findActualGroundLevel(world, x, z, y, Math.max(worldSurfaceY, oceanFloorY));


        if (y >= actualGroundY - 1) {
            return true;
        }


        BlockPos.Mutable mutable = pos.up().mutableCopy();
        int maxCheck = Math.min(actualGroundY - y + 10, 25);

        boolean foundOnlySoftBlocks = true;

        for (int i = 0; i < maxCheck; i++) {
            BlockState state = world.getBlockState(mutable);

            if (isTerrainBlock(state)) {
                return false;
            }

            if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.LOGS)) {
                mutable.move(Direction.UP);
                continue;
            }

            if (state.isAir() || state.isOf(Blocks.CAVE_AIR)) {
                mutable.move(Direction.UP);
                continue;
            }

            if (state.isOf(Blocks.VINE) || state.isOf(Blocks.MOSS_CARPET) ||
                state.isOf(Blocks.HANGING_ROOTS) || state.isOf(Blocks.BEE_NEST) ||
                state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.SAPLINGS)) {
                mutable.move(Direction.UP);
                continue;
            }

            mutable.move(Direction.UP);
        }

        return true;
    }

    private boolean isTerrainBlock(BlockState state) {
        if (state.isAir()) return false;

        // EXCLUDE non-terrain blocks first (leaves, logs, plants, etc.)
        if (state.isIn(BlockTags.LEAVES)) return false;
        if (state.isIn(BlockTags.LOGS)) return false;
        if (state.isIn(BlockTags.PLANKS)) return false;
        if (state.isIn(BlockTags.FLOWERS)) return false;
        if (state.isIn(BlockTags.SAPLINGS)) return false;
        if (state.isIn(BlockTags.WOOL)) return false;
        if (state.isIn(BlockTags.FENCES)) return false;
        if (state.isIn(BlockTags.WALLS)) return false;

        // Check vanilla tags that mods typically use for terrain
        if (state.isIn(BlockTags.DIRT)) return true;
        if (state.isIn(BlockTags.BASE_STONE_OVERWORLD)) return true;
        if (state.isIn(BlockTags.BASE_STONE_NETHER)) return true;
        if (state.isIn(BlockTags.SAND)) return true;
        if (state.isIn(BlockTags.TERRACOTTA)) return true;
        if (state.isIn(BlockTags.ICE)) return true;
        if (state.isIn(BlockTags.SNOW)) return true;

        // Common vanilla terrain blocks (fallback for untagged)
        if (state.isOf(Blocks.GRASS_BLOCK)) return true;
        if (state.isOf(Blocks.GRAVEL)) return true;
        if (state.isOf(Blocks.CLAY)) return true;
        if (state.isOf(Blocks.MOSS_BLOCK)) return true;
        if (state.isOf(Blocks.MUD)) return true;
        if (state.isOf(Blocks.MUDDY_MANGROVE_ROOTS)) return true;

        if (state.isOpaqueFullCube(null, BlockPos.ORIGIN)) {
            Block block = state.getBlock();
            String blockId = Registries.BLOCK.getId(block).toString();

            String path = blockId.toLowerCase();

            if (path.contains("leaf") || path.contains("leaves") || path.contains("log") ||
                path.contains("wood") || path.contains("plank") || path.contains("fence") ||
                path.contains("brick") || path.contains("tile") || path.contains("block")) {
                if (path.contains("brick") || path.contains("tile")) {
                    return false;
                }
            }

            if (path.contains("dirt") || path.contains("soil") || path.contains("grass") ||
                path.contains("stone") || path.contains("rock") || path.contains("sand") ||
                path.contains("gravel") || path.contains("clay") || path.contains("mud") ||
                path.contains("loam") || path.contains("peat") || path.contains("humus") ||
                path.contains("slate") || path.contains("granite") || path.contains("diorite") ||
                path.contains("andesite") || path.contains("basalt") || path.contains("tuff")) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAdjacentTerrain(WorldView world, BlockPos pos) {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            if (isTerrainBlock(world.getBlockState(pos.offset(dir)))) {
                return true;
            }
        }
        return false;
    }

    private BlockState getTerrainFromAbove(WorldView world, BlockPos blockPos) {
        BlockPos checkPos = blockPos.up();
        BlockState aboveState = world.getBlockState(checkPos);

        int surfaceY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, blockPos.getX(), blockPos.getZ());
        int worldSurfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, blockPos.getX(), blockPos.getZ());

        if (isGrassLikeBlock(aboveState)) {
            int effectiveSurface = Math.max(surfaceY, worldSurfaceY);
            if (blockPos.getY() >= effectiveSurface - 3) {
                return aboveState;
            }
            BlockState belowGrass = findBlockBelowGrass(world, checkPos);
            if (belowGrass != null) {
                return belowGrass;
            }
            return Blocks.DIRT.getDefaultState(); // Fallback
        }

        if (isSnowLikeBlock(aboveState)) {
            BlockState nearby = sampleNearbyTerrain(world, blockPos);
            return nearby != null ? nearby : Blocks.STONE.getDefaultState();
        }

        if (isTerrainBlock(aboveState)) {
            return aboveState;
        }

        BlockState sampled = sampleNearbyTerrain(world, blockPos);
        if (sampled != null) {
            return sampled;
        }

        return getTerrainBlockForY(world, blockPos);
    }

    private boolean isGrassLikeBlock(BlockState state) {
        if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.PODZOL) ||
            state.isOf(Blocks.MYCELIUM) || state.isOf(Blocks.CRIMSON_NYLIUM) ||
            state.isOf(Blocks.WARPED_NYLIUM)) {
            return true;
        }

        String blockId = Registries.BLOCK.getId(state.getBlock()).toString().toLowerCase();
        return blockId.contains("grass") && !blockId.contains("tall") && !blockId.contains("short") ||
               blockId.contains("nylium") || blockId.contains("podzol") || blockId.contains("mycelium");
    }


    private boolean isSnowLikeBlock(BlockState state) {
        if (state.isIn(BlockTags.SNOW) || state.isOf(Blocks.SNOW_BLOCK) || state.isOf(Blocks.POWDER_SNOW)) {
            return true;
        }
        String blockId = Registries.BLOCK.getId(state.getBlock()).toString().toLowerCase();
        return blockId.contains("snow") || blockId.contains("frost") || blockId.contains("frozen");
    }


    private BlockState findBlockBelowGrass(WorldView world, BlockPos grassPos) {
        BlockPos.Mutable mutable = grassPos.down().mutableCopy();
        for (int i = 0; i < 5; i++) {
            BlockState state = world.getBlockState(mutable);
            if (isTerrainBlock(state) && !isGrassLikeBlock(state)) {
                return state;
            }
            mutable.move(Direction.DOWN);
        }
        return null;
    }


    private BlockState sampleNearbyTerrain(WorldView world, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        int[] offsets = {-2, -1, 0, 1, 2};
        for (int dx : offsets) {
            for (int dz : offsets) {
                if (dx == 0 && dz == 0) continue;

                for (int dy = -1; dy <= 1; dy++) {
                    mutable.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    BlockState state = world.getBlockState(mutable);

                    if (isTerrainBlock(state) && !isGrassLikeBlock(state) && !isSnowLikeBlock(state)) {
                        return state;
                    }
                }
            }
        }

        return null;
    }


    private BlockState getTerrainBlockForY(WorldView world, BlockPos pos) {
        // Try to sample nearby terrain first
        BlockState sampled = sampleNearbyTerrain(world, pos);
        if (sampled != null) {
            return sampled;
        }

        int y = pos.getY();
        if (y < -8) {
            return Blocks.DEEPSLATE.getDefaultState();
        } else if (y < 0) {
            return Random.create(y).nextBoolean() ? Blocks.DEEPSLATE.getDefaultState() : Blocks.STONE.getDefaultState();
        } else {
            return Blocks.STONE.getDefaultState();
        }
    }


    private boolean isIcyBlock(BlockState state) {
        if (state.isAir()) return false;

        // Check vanilla ice/snow tags and blocks
        if (state.isIn(BlockTags.ICE)) return true;
        if (state.isIn(BlockTags.SNOW)) return true;
        if (state.isOf(Blocks.SNOW_BLOCK)) return true;
        if (state.isOf(Blocks.POWDER_SNOW)) return true;
        if (state.isOf(Blocks.SNOW)) return true;
        if (state.isOf(Blocks.ICE)) return true;
        if (state.isOf(Blocks.PACKED_ICE)) return true;
        if (state.isOf(Blocks.BLUE_ICE)) return true;
        if (state.isOf(Blocks.FROSTED_ICE)) return true;

        // Check for modded icy blocks by name
        String blockId = Registries.BLOCK.getId(state.getBlock()).toString().toLowerCase();
        return blockId.contains("ice") || blockId.contains("snow") ||
               blockId.contains("frost") || blockId.contains("frozen") ||
               blockId.contains("glacial") || blockId.contains("icicle");
    }

    private double detectIcyTerrainRatio(WorldView world, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        long cacheKey = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);

        Double cached = icyTerrainCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        int icyCount = 0;
        int totalSamples = 0;

        for (int dx = -ICE_DETECTION_RADIUS; dx <= ICE_DETECTION_RADIUS; dx += 3) {
            for (int dz = -ICE_DETECTION_RADIUS; dz <= ICE_DETECTION_RADIUS; dz += 3) {
                int x = pos.getX() + dx;
                int z = pos.getZ() + dz;


                int surfaceY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, z);

                for (int dy = -3; dy <= 2; dy++) {
                    BlockState state = world.getBlockState(new BlockPos(x, surfaceY + dy, z));
                    if (isIcyBlock(state)) {
                        icyCount++;
                    }
                    totalSamples++;
                }

                BlockState topState = world.getBlockState(new BlockPos(x, surfaceY, z));
                if (topState.isOf(Blocks.SNOW) || topState.isOf(Blocks.POWDER_SNOW)) {
                    icyCount += 2;
                }
            }
        }

        double ratio = totalSamples > 0 ? (double) icyCount / totalSamples : 0.0;

        if (icyTerrainCache.size() < 1000) {
            icyTerrainCache.put(cacheKey, ratio);
        }

        return ratio;
    }

    private BlockState getIceOvergrowthState(WorldView world, BlockPos blockPos, double icyRatio, int distanceFromSurface) {
        if (icyRatio < ICE_OVERGROWTH_THRESHOLD) {
            return null; // Not enough icy terrain to trigger overgrowth
        }

        int overgrowthDepth = (int) (3 + (icyRatio * 17));
        overgrowthDepth = Math.max(3, Math.min(20, overgrowthDepth));

        if (distanceFromSurface > overgrowthDepth || distanceFromSurface < 0) {
            return null;
        }

        BlockState aboveState = world.getBlockState(blockPos.up());
        BlockState belowState = world.getBlockState(blockPos.down());

        boolean hasTerrainAbove = isTerrainBlock(aboveState);
        boolean hasIceAbove = isIcyBlock(aboveState);
        boolean hasTerrainBelow = isTerrainBlock(belowState);
        boolean hasIceBelow = isIcyBlock(belowState);
        boolean hasAdjacentSupport = hasAdjacentTerrainOrIce(world, blockPos);

        if (!hasTerrainAbove && !hasIceAbove && !hasTerrainBelow && !hasIceBelow && !hasAdjacentSupport) {
            return null;
        }

        long seed = blockPos.asLong() ^ (blockPos.getY() * 31L);
        Random random = Random.create(seed);

        double depthRatio = (double) distanceFromSurface / overgrowthDepth;
        double depthFactor = Math.pow(1.0 - depthRatio, 1.5);

        double baseProbability = Math.min(0.95, icyRatio * 3.5);

        double supportBonus = 0;
        if (hasTerrainAbove || hasIceAbove) supportBonus += 0.35;
        if (hasAdjacentSupport) supportBonus += 0.25;
        if (hasTerrainBelow || hasIceBelow) supportBonus += 0.15;

        double probability = (baseProbability * depthFactor) + supportBonus;
        probability = Math.min(0.95, probability); // Cap at 95%

        if (distanceFromSurface <= 2 && (hasTerrainAbove || hasIceAbove || hasAdjacentSupport)) {
            probability = Math.max(probability, 0.85);
        }

        if (random.nextDouble() < probability) {
            return getRandomOvergrowthIce(random, distanceFromSurface);
        }

        return null;
    }

    private boolean hasAdjacentTerrainOrIce(WorldView world, BlockPos pos) {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockState state = world.getBlockState(pos.offset(dir));
            if (isTerrainBlock(state) || isIcyBlock(state)) {
                return true;
            }
        }
        // Also check diagonally above for better draping
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockState state = world.getBlockState(pos.offset(dir).up());
            if (isTerrainBlock(state) || isIcyBlock(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessorTypes.GLACIAL_TOMB;
    }
}

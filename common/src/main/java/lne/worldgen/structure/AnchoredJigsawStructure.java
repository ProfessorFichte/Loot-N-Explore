package lne.worldgen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.DimensionPadding;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AnchoredJigsawStructure extends Structure {

    public static final int MAX_SIZE = 20;

    public record Stairway(StructurePoolElement element, int topOffset) {
        public static final Codec<Stairway> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                StructurePoolElement.CODEC.fieldOf("element").forGetter(Stairway::element),
                Codec.INT.fieldOf("top_offset").forGetter(Stairway::topOffset)
        ).apply(instance, Stairway::new));
    }

    public static final MapCodec<AnchoredJigsawStructure> CODEC = RecordCodecBuilder.<AnchoredJigsawStructure>mapCodec(instance ->
            instance.group(
                    configCodecBuilder(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
                    Identifier.CODEC.fieldOf("start_jigsaw_name").forGetter(s -> s.startJigsawName),
                    Codec.intRange(0, MAX_SIZE).fieldOf("size").forGetter(s -> s.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startHeight),
                    Heightmap.Type.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(s -> s.projectStartToHeightmap),
                    Codec.intRange(1, 128).optionalFieldOf("max_distance_from_center", 80).forGetter(s -> s.maxDistanceFromCenter),
                    StructurePool.REGISTRY_CODEC.optionalFieldOf("required_pool").forGetter(s -> s.requiredPool),
                    Codec.intRange(1, 32).optionalFieldOf("attempts", 1).forGetter(s -> s.attempts),
                    Stairway.CODEC.optionalFieldOf("stairway").forGetter(s -> s.stairway)
            ).apply(instance, AnchoredJigsawStructure::new)
    ).validate(AnchoredJigsawStructure::validate);

    private final RegistryEntry<StructurePool> startPool;
    private final Identifier startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Type> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final Optional<RegistryEntry<StructurePool>> requiredPool;
    private final int attempts;
    private final Optional<Stairway> stairway;

    public AnchoredJigsawStructure(
            Config config,
            RegistryEntry<StructurePool> startPool,
            Identifier startJigsawName,
            int size,
            HeightProvider startHeight,
            Optional<Heightmap.Type> projectStartToHeightmap,
            int maxDistanceFromCenter,
            Optional<RegistryEntry<StructurePool>> requiredPool,
            int attempts,
            Optional<Stairway> stairway
    ) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.requiredPool = requiredPool;
        this.attempts = attempts;
        this.stairway = stairway;
    }

    private static DataResult<AnchoredJigsawStructure> validate(AnchoredJigsawStructure structure) {
        int maxPossibleExtent = switch (structure.getTerrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_THIN, BEARD_BOX, ENCAPSULATE -> 12;
        };
        if (structure.maxDistanceFromCenter + maxPossibleExtent > 128) {
            return DataResult.error(() -> "Anchored jigsaw structure size too large for chosen terrain adaptation");
        }
        return DataResult.success(structure);
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getStartX();
        int z = chunkPos.getStartZ();
        int y = this.startHeight.get(context.random(), new HeightContext(context.chunkGenerator(), context.world()));
        int surfaceY = context.chunkGenerator().getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        BlockPos biomeCheckPos = new BlockPos(x, surfaceY, z);
        RegistryEntry<Biome> biome = context.biomeSource().getBiome(BiomeCoords.fromBlock(x), BiomeCoords.fromBlock(surfaceY),
                BiomeCoords.fromBlock(z), context.noiseConfig().getMultiNoiseSampler());
        if (!this.getValidBiomes().contains(biome)) {
            return Optional.empty();
        }
        BlockRotation rotation = sharedRotation(context);

        if (this.size == 0) {
            return singlePiece(context, new BlockPos(x, y, z), rotation).map(piece -> {
                StructurePiecesCollector pieces = new StructurePiecesCollector();
                pieces.addPiece(piece);
                return new StructurePosition(biomeCheckPos, Either.right(pieces));
            });
        }

        StructureTemplateManager manager = context.structureTemplateManager();
        int stairwayTop = surfaceY + this.stairway.map(Stairway::topOffset).orElse(0);
        int segmentHeight = this.stairway.map(s -> s.element().getBoundingBox(manager, BlockPos.ORIGIN, BlockRotation.NONE).getBlockCountY()).orElse(1);
        if (this.stairway.isPresent() && stairwayTop - 1 - y >= segmentHeight) {
            y = stairwayTop - 1 - segmentHeight * Math.floorDiv(stairwayTop - 1 - y, segmentHeight);
        }
        BlockPos anchor = new BlockPos(x, y, z);

        Set<StructurePoolElement> required = Collections.newSetFromMap(new IdentityHashMap<>());
        this.requiredPool.ifPresent(pool -> required.addAll(pool.value().getElementIndicesInRandomOrder(Random.create(0L))));

        StructurePiecesCollector fallback = null;
        StructurePiecesCollector unaligned = null;
        for (int attempt = 0; attempt < this.attempts; attempt++) {
            Optional<StructurePosition> result = StructurePoolBasedGenerator.generate(
                    withStartRotation(context, rotation),
                    this.startPool,
                    Optional.of(this.startJigsawName),
                    this.size,
                    anchor,
                    false,
                    this.projectStartToHeightmap,
                    this.maxDistanceFromCenter,
                    StructurePoolAliasLookup.EMPTY,
                    DimensionPadding.NONE,
                    StructureLiquidSettings.IGNORE_WATERLOGGING
            );
            if (result.isEmpty()) {
                continue;
            }
            StructurePiecesCollector pieces = result.get().generate();
            List<StructurePiece> list = pieces.toList().pieces();
            if (list.isEmpty()) {
                continue;
            }
            if (list.getFirst().getRotation() != rotation) {
                if (unaligned == null) {
                    unaligned = pieces;
                }
                continue;
            }
            long count = list.stream()
                    .filter(piece -> piece instanceof PoolStructurePiece pool && required.contains(pool.getPoolElement()))
                    .count();
            if (required.isEmpty() || count == 1) {
                return Optional.of(finish(context, anchor, stairwayTop, segmentHeight, biomeCheckPos, pieces));
            }
            if (fallback == null || count > 1) {
                fallback = pieces;
            }
        }
        return Optional.ofNullable(fallback != null ? fallback : unaligned)
                .map(pieces -> finish(context, anchor, stairwayTop, segmentHeight, biomeCheckPos, pieces));
    }

    private StructurePosition finish(Context context, BlockPos anchor, int stairwayTop, int segmentHeight,
                                     BlockPos biomeCheckPos, StructurePiecesCollector pieces) {
        BlockRotation rotation = pieces.toList().pieces().getFirst().getRotation();
        this.stairway.ifPresent(stairway -> {
            StructureTemplateManager manager = context.structureTemplateManager();
            UnadaptedPoolElement element = new UnadaptedPoolElement(stairway.element());
            Optional<BlockPos> local = element.getStructureBlockInfos(manager, BlockPos.ORIGIN, rotation, context.random()).stream()
                    .map(StructureTemplate.StructureBlockInfo::pos)
                    .min(Comparator.comparingInt(BlockPos::getY));
            int bottom = startJigsawY(context, pieces).orElse(anchor.getY());
            if (local.isEmpty() || stairwayTop - 1 - bottom < segmentHeight) {
                return;
            }
            int shift = Math.floorMod(stairwayTop - 1 - bottom, segmentHeight);
            if (shift != 0) {
                pieces.shift(shift);
                bottom += shift;
            }
            int segments = (stairwayTop - 1 - bottom) / segmentHeight;
            for (int i = 0; i < segments; i++) {
                BlockPos origin = new BlockPos(anchor.getX(), bottom + 1 + i * segmentHeight, anchor.getZ()).subtract(local.get());
                pieces.addPiece(new PoolStructurePiece(manager, element, origin, 0, rotation,
                        element.getBoundingBox(manager, origin, rotation), StructureLiquidSettings.IGNORE_WATERLOGGING));
            }
        });
        return new StructurePosition(biomeCheckPos, Either.right(pieces));
    }

    private Optional<Integer> startJigsawY(Context context, StructurePiecesCollector pieces) {
        if (!(pieces.toList().pieces().getFirst() instanceof PoolStructurePiece start)) {
            return Optional.empty();
        }
        String name = this.startJigsawName.toString();
        return start.getPoolElement().getStructureBlockInfos(context.structureTemplateManager(), start.getPos(), start.getRotation(),
                        context.random()).stream()
                .filter(info -> info.nbt() != null && name.equals(info.nbt().getString("name")))
                .map(info -> info.pos().getY())
                .findFirst();
    }

    private Optional<PoolStructurePiece> singlePiece(Context context, BlockPos anchor, BlockRotation rotation) {
        StructureTemplateManager manager = context.structureTemplateManager();
        StructurePoolElement element = this.startPool.value().getRandomElement(context.random());
        String name = this.startJigsawName.toString();
        Optional<BlockPos> local = element.getStructureBlockInfos(manager, BlockPos.ORIGIN, rotation, context.random()).stream()
                .filter(info -> info.nbt() != null && name.equals(info.nbt().getString("name")))
                .map(StructureTemplate.StructureBlockInfo::pos)
                .findFirst();
        if (local.isEmpty()) {
            return Optional.empty();
        }
        BlockPos origin = anchor.subtract(local.get());
        BlockBox box = element.getBoundingBox(manager, origin, rotation);
        if (this.projectStartToHeightmap.isPresent()) {
            int ground = anchor.getY() + context.chunkGenerator().getHeightOnGround(anchor.getX(), anchor.getZ(),
                    this.projectStartToHeightmap.get(), context.world(), context.noiseConfig());
            origin = origin.up(ground - (box.getMinY() + element.getGroundLevelDelta()));
            box = element.getBoundingBox(manager, origin, rotation);
        }
        return Optional.of(new PoolStructurePiece(manager, element, origin, element.getGroundLevelDelta(), rotation, box,
                StructureLiquidSettings.IGNORE_WATERLOGGING));
    }

    private static BlockRotation sharedRotation(Context context) {
        return BlockRotation.values()[Random.create(context.seed() ^ context.chunkPos().toLong()).nextInt(4)];
    }

    // The jigsaw generator draws the start rotation as its first random int, so this pins it while every later draw stays random.
    private static Context withStartRotation(Context context, BlockRotation rotation) {
        return new Context(context.dynamicRegistryManager(), context.chunkGenerator(), context.biomeSource(), context.noiseConfig(),
                context.structureTemplateManager(), new FirstIntRandom(context.random(), rotation.ordinal()), context.seed(),
                context.chunkPos(), context.world(), context.biomePredicate());
    }

    private static final class FirstIntRandom extends ChunkRandom {
        private int first;

        private FirstIntRandom(Random base, int first) {
            super(base);
            this.first = first;
        }

        @Override
        public int nextInt(int bound) {
            if (this.first >= 0 && this.first < bound) {
                int value = this.first;
                this.first = -1;
                return value;
            }
            return super.nextInt(bound);
        }
    }

    @Override
    public StructureType<?> getType() {
        return ModStructureTypes.ANCHORED_JIGSAW;
    }
}

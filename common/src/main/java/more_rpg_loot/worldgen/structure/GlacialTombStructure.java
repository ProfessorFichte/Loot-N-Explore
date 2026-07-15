package more_rpg_loot.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.List;
import java.util.Optional;

public class GlacialTombStructure extends Structure {

    public static final int MAX_SIZE = 20;

    public static final MapCodec<GlacialTombStructure> CODEC = RecordCodecBuilder.<GlacialTombStructure>mapCodec(instance ->
            instance.group(
                    configCodecBuilder(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(s -> s.startPool),
                    Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(s -> s.startJigsawName),
                    Codec.intRange(0, MAX_SIZE).fieldOf("size").forGetter(s -> s.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startHeight),
                    Codec.intRange(1, 256).optionalFieldOf("max_distance_from_center", 116).forGetter(s -> s.maxDistanceFromCenter),
                    Codec.INT.optionalFieldOf("min_y_level", -64).forGetter(s -> s.minYLevel),
                    Codec.BOOL.optionalFieldOf("use_expansion_hack", false).forGetter(s -> s.useExpansionHack),
                    Codec.BOOL.optionalFieldOf("force_underground", true).forGetter(s -> s.forceUnderground),
                    Codec.INT.optionalFieldOf("surface_buffer", 10).forGetter(s -> s.surfaceBuffer),
                    Codec.BOOL.optionalFieldOf("use_heightmap", false).forGetter(s -> s.useHeightmap),
                    TagKey.codec(RegistryKeys.STRUCTURE).listOf().optionalFieldOf("excluded_structures", List.of()).forGetter(s -> s.excludedStructures),
                    Codec.INT.optionalFieldOf("exclusion_check_radius", 2).forGetter(s -> s.exclusionCheckRadius)
            ).apply(instance, GlacialTombStructure::new)
    ).validate(GlacialTombStructure::validate);

    private final RegistryEntry<StructurePool> startPool;
    private final Optional<Identifier> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final int maxDistanceFromCenter;
    private final int minYLevel;
    private final boolean useExpansionHack;
    private final boolean forceUnderground;
    private final int surfaceBuffer;
    private final boolean useHeightmap;
    private final List<TagKey<Structure>> excludedStructures;
    private final int exclusionCheckRadius;

    public GlacialTombStructure(
            Config config,
            RegistryEntry<StructurePool> startPool,
            Optional<Identifier> startJigsawName,
            int size,
            HeightProvider startHeight,
            int maxDistanceFromCenter,
            int minYLevel,
            boolean useExpansionHack,
            boolean forceUnderground,
            int surfaceBuffer,
            boolean useHeightmap,
            List<TagKey<Structure>> excludedStructures,
            int exclusionCheckRadius
    ) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.minYLevel = minYLevel;
        this.useExpansionHack = useExpansionHack;
        this.forceUnderground = forceUnderground;
        this.surfaceBuffer = surfaceBuffer;
        this.useHeightmap = useHeightmap;
        this.excludedStructures = excludedStructures;
        this.exclusionCheckRadius = exclusionCheckRadius;
    }

    private static DataResult<GlacialTombStructure> validate(GlacialTombStructure structure) {
        int maxPossibleExtent = switch (structure.getTerrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_THIN, BEARD_BOX, ENCAPSULATE -> 12;
        };

        if (structure.maxDistanceFromCenter + maxPossibleExtent > 128) {
            return DataResult.error(() ->
                    "Glacial Tomb structure size too large for chosen terrain adaptation"
            );
        }
        return DataResult.success(structure);
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunkPos = context.chunkPos();
        int centerX = chunkPos.getCenterX();
        int centerZ = chunkPos.getCenterZ();

        int surfaceY = context.chunkGenerator().getHeightInGround(
                centerX,
                centerZ,
                Heightmap.Type.WORLD_SURFACE_WG,
                context.world(),
                context.noiseConfig()
        );

        int startY;

        if (useHeightmap) {
            startY = surfaceY;
        } else {
            startY = this.startHeight.get(
                    context.random(),
                    new HeightContext(context.chunkGenerator(), context.world())
            );

            if (forceUnderground) {
                int maxAllowedY = surfaceY - surfaceBuffer;
                if (startY > maxAllowedY) {
                    startY = maxAllowedY;
                }
                if (startY < minYLevel) {
                    startY = minYLevel;
                }
            }
        }

        BlockPos startPos = new BlockPos(centerX, startY, centerZ);

        return TerrainAwareJigsawGenerator.generate(
                context,
                this.startPool,
                this.startJigsawName,
                this.size,
                startPos,
                this.useExpansionHack,
                this.maxDistanceFromCenter,
                this.surfaceBuffer
        );
    }

    @Override
    public StructureType<?> getType() {
        return ModStructureTypes.GLACIAL_TOMB;
    }
}

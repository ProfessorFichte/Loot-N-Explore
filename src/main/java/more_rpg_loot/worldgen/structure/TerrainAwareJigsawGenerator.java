package more_rpg_loot.worldgen.structure;

import com.mojang.datafixers.util.Either;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.DimensionPadding;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;
import java.util.function.Consumer;

public class TerrainAwareJigsawGenerator {
    public static Optional<Structure.StructurePosition> generate(
            Structure.Context context,
            RegistryEntry<StructurePool> startPool,
            Optional<Identifier> startJigsawName,
            int size,
            BlockPos startPos,
            boolean useExpansionHack,
            int maxDistanceFromCenter,
            int surfaceMargin
    ) {
        Optional<Structure.StructurePosition> result = StructurePoolBasedGenerator.generate(
                context,
                startPool,
                startJigsawName,
                size,
                startPos,
                useExpansionHack,
                Optional.empty(),
                maxDistanceFromCenter,
                StructurePoolAliasLookup.EMPTY,
                DimensionPadding.NONE,
                StructureLiquidSettings.IGNORE_WATERLOGGING
        );

        if (result.isEmpty()) {
            return result;
        }

        final BlockPos structureOrigin = startPos;

        return result.map(structurePosition -> {
            Either<Consumer<StructurePiecesCollector>, StructurePiecesCollector> generator = structurePosition.generator();

            Consumer<StructurePiecesCollector> filteringGenerator = collector -> {

                generator.ifLeft(consumer -> {

                    FilteringPiecesCollector filteringCollector = new FilteringPiecesCollector(
                            collector, context, surfaceMargin, structureOrigin
                    );
                    consumer.accept(filteringCollector);
                });
                generator.ifRight(existingCollector -> {

                    filterExistingPieces(existingCollector, collector, context, surfaceMargin, structureOrigin);
                });
            };

            return new Structure.StructurePosition(
                    structurePosition.position(),
                    Either.left(filteringGenerator)
            );
        });
    }

    private static void filterExistingPieces(
            StructurePiecesCollector source,
            StructurePiecesCollector target,
            Structure.Context context,
            int surfaceMargin,
            BlockPos structureOrigin
    ) {
        source.toList().pieces().forEach(piece -> {
            if (!isPieceAboveSurface(piece, context, surfaceMargin, structureOrigin)) {
                target.addPiece(piece);
            }
        });
    }

    private static boolean isPieceAboveSurface(
            StructurePiece piece,
            Structure.Context context,
            int surfaceMargin,
            BlockPos structureOrigin
    ) {
        BlockBox box = piece.getBoundingBox();

        int centerX = (box.getMinX() + box.getMaxX()) / 2;
        int centerZ = (box.getMinZ() + box.getMaxZ()) / 2;
        double horizontalDistance = Math.sqrt(
                Math.pow(centerX - structureOrigin.getX(), 2) +
                Math.pow(centerZ - structureOrigin.getZ(), 2)
        );

        int samplesAboveSurface = 0;
        int samplesInWater = 0;
        int totalSamples = 0;

        int[][] samplePoints = {
                {box.getMinX(), box.getMinZ()},
                {box.getMaxX(), box.getMinZ()},
                {box.getMinX(), box.getMaxZ()},
                {box.getMaxX(), box.getMaxZ()},
                {centerX, centerZ},
                {(box.getMinX() + centerX) / 2, centerZ},
                {(box.getMaxX() + centerX) / 2, centerZ},
                {centerX, (box.getMinZ() + centerZ) / 2},
                {centerX, (box.getMaxZ() + centerZ) / 2}
        };

        for (int[] point : samplePoints) {
            int x = point[0];
            int z = point[1];

            int oceanFloorY = context.chunkGenerator().getHeightInGround(
                    x, z,
                    Heightmap.Type.OCEAN_FLOOR_WG,
                    context.world(),
                    context.noiseConfig()
            );

            int worldSurfaceY = context.chunkGenerator().getHeightInGround(
                    x, z,
                    Heightmap.Type.WORLD_SURFACE_WG,
                    context.world(),
                    context.noiseConfig()
            );

            boolean hasWaterAbove = worldSurfaceY > oceanFloorY + 1;

            if (box.getMinY() > oceanFloorY - surfaceMargin) {
                samplesAboveSurface++;

                if (hasWaterAbove && box.getMinY() < worldSurfaceY) {
                    samplesInWater++;
                }
            }
            totalSamples++;
        }

        if (samplesInWater > 0) {
            return true;
        }

        double aboveSurfaceRatio = (double) samplesAboveSurface / totalSamples;

        if (horizontalDistance < 16) {
            return aboveSurfaceRatio > 0.7;
        }

        if (box.getMinY() <= structureOrigin.getY()) {
            return aboveSurfaceRatio > 0.5;
        }

        return aboveSurfaceRatio > 0.35;
    }

    private static class FilteringPiecesCollector extends StructurePiecesCollector {
        private final StructurePiecesCollector delegate;
        private final Structure.Context context;
        private final int surfaceMargin;
        private final BlockPos structureOrigin;

        public FilteringPiecesCollector(
                StructurePiecesCollector delegate,
                Structure.Context context,
                int surfaceMargin,
                BlockPos structureOrigin
        ) {
            this.delegate = delegate;
            this.context = context;
            this.surfaceMargin = surfaceMargin;
            this.structureOrigin = structureOrigin;
        }

        @Override
        public void addPiece(StructurePiece piece) {
            if (!isPieceAboveSurface(piece, context, surfaceMargin, structureOrigin)) {
                delegate.addPiece(piece);
            }
        }
    }
}

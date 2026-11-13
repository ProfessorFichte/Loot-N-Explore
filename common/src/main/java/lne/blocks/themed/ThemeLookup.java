package lne.blocks.themed;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThemeLookup {
    private static final int SPAWNER_RADIUS_XZ = 9;
    private static final int SPAWNER_RADIUS_Y = 5;

    public static ParticleTheme at(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof ThemedBlock themed ? themed.theme() : null;
    }

    public static ParticleTheme nearestSpawner(World world, BlockPos pos) {
        ParticleTheme here = at(world, pos);
        if (here != null) {
            return here;
        }
        ParticleTheme nearest = null;
        double best = Double.MAX_VALUE;
        for (BlockPos check : BlockPos.iterate(pos.add(-SPAWNER_RADIUS_XZ, -SPAWNER_RADIUS_Y, -SPAWNER_RADIUS_XZ),
                pos.add(SPAWNER_RADIUS_XZ, SPAWNER_RADIUS_Y, SPAWNER_RADIUS_XZ))) {
            if (world.getBlockState(check).getBlock() instanceof ThemedTrialSpawnerBlock spawner) {
                double distance = check.getSquaredDistance(pos);
                if (distance < best) {
                    best = distance;
                    nearest = spawner.theme();
                }
            }
        }
        return nearest;
    }
}

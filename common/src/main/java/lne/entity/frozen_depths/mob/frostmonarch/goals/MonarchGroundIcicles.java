package lne.entity.frozen_depths.mob.frostmonarch.goals;

import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.entity.frozen_depths.projectile.StraightIcicleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

final class MonarchGroundIcicles {
    private MonarchGroundIcicles() {
    }

    static void line(FrostMonarchEntity monarch, Vec3d start, float angle, int length, double minY, double maxY, int warmupOffset) {
        int telegraph = Math.max(0, monarch.tuning().getInt(FrozenDepthsTuning.GROUND_ICICLE_TELEGRAPH, 10));
        boolean sound = true;
        for (int i = 0; i < length; i++) {
            double distance = 1.5 * (double) (i + 1);
            double x = start.x + (double) MathHelper.cos(angle) * distance;
            double z = start.z + (double) MathHelper.sin(angle) * distance;
            if (spawn(monarch, x, z, minY, maxY, angle, warmupOffset + i * 2, telegraph, sound)) {
                sound = false;
            }
        }
    }

    private static boolean spawn(FrostMonarchEntity monarch, double x, double z, double minY, double maxY, float yaw, int warmup, int telegraph, boolean sound) {
        World world = monarch.getWorld();
        BlockPos blockPos = BlockPos.ofFloored(x, maxY, z);
        double yOffset = 0.0;

        do {
            BlockPos belowPos = blockPos.down();
            BlockState blockState = world.getBlockState(belowPos);
            if (blockState.isSideSolidFullSquare(world, belowPos, Direction.UP)) {
                if (!world.isAir(blockPos)) {
                    VoxelShape voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos);
                    if (!voxelShape.isEmpty()) {
                        yOffset = voxelShape.getMax(Direction.Axis.Y);
                    }
                }
                double y = (double) blockPos.getY() + yOffset;
                StraightIcicleEntity icicle = new StraightIcicleEntity(world, x, y, z, yaw, warmup, monarch).withTelegraph(telegraph);
                if (sound) {
                    icicle.withEruptSound();
                }
                world.spawnEntity(icicle);
                world.emitGameEvent(GameEvent.ENTITY_PLACE, new Vec3d(x, y, z), GameEvent.Emitter.of(monarch));
                return true;
            }
            blockPos = blockPos.down();
        } while (blockPos.getY() >= MathHelper.floor(minY) - 1);
        return false;
    }
}

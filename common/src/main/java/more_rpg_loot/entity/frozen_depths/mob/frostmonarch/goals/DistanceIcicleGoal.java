package more_rpg_loot.entity.frozen_depths.mob.frostmonarch.goals;

import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import more_rpg_loot.entity.frozen_depths.projectile.StraightIcicleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.event.GameEvent;

import java.util.EnumSet;

public class DistanceIcicleGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private boolean hasStarted;
    private LivingEntity cachedTarget;
    private static final float MIN_RANGE = 12.0F;
    private static final float MAX_RANGE = 40.0F;

    public DistanceIcicleGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!monarch.isPerformingAbility() && !isCasting) {
            LivingEntity target = monarch.getTarget();
            if (monarch.distanceIcicleCooldown <= 0 && target instanceof PlayerEntity && target.isAlive()) {
                double distance = monarch.squaredDistanceTo(target);
                if (distance >= MIN_RANGE * MIN_RANGE && distance <= MAX_RANGE * MAX_RANGE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.cachedTarget = monarch.getTarget();
        this.isCasting = true;
        this.channelTicks = 10;
        this.hasStarted = false;
        monarch.setCasting(true);

        LivingEntity target = cachedTarget;
        if (target != null) {
            double dx = target.getX() - monarch.getX();
            double dz = target.getZ() - monarch.getZ();
            float yaw = (float)(MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;

            monarch.setYaw(yaw);
            monarch.setHeadYaw(yaw);
            monarch.setBodyYaw(yaw);
            monarch.prevYaw = yaw;
            monarch.prevHeadYaw = yaw;
            monarch.prevBodyYaw = yaw;
        }
    }

    @Override
    public void tick() {
        if (!hasStarted) {
            hasStarted = true;
        }

        if (!monarch.isCasting()) {
            monarch.setCasting(true);
        }

        if (channelTicks == 5) {
            conjureIcicleLine();
        }

        channelTicks--;
    }

    private void conjureIcicleLine() {
        if (!monarch.getWorld().isClient()) {
            LivingEntity target = this.cachedTarget;
            if (target == null || !target.isAlive()) {
                return;
            }

            Vec3d monarchPos = monarch.getPos();
            Vec3d targetPos = target.getPos();
            Vec3d direction = targetPos.subtract(monarchPos).normalize();

            double distanceToTarget = monarchPos.distanceTo(targetPos);

            Vec3d startPos = targetPos.subtract(direction.multiply(6.0));

            double targetY = Math.min(target.getY(), monarch.getY());
            double maxY = Math.max(target.getY(), monarch.getY()) + 1.0;

            float angleToTarget = (float) MathHelper.atan2(
                targetPos.z - startPos.z,
                targetPos.x - startPos.x
            );

            int lineLength = monarch.getScaledCount((int) Math.min(distanceToTarget - 4.0, 8.0));
            spawnIcicleLine(startPos, angleToTarget, lineLength, targetY, maxY);
        }
    }

    private void spawnIcicleLine(Vec3d startPos, float angle, int length, double minY, double maxY) {
        for (int i = 0; i < length; i++) {
            double distance = 1.5 * (double)(i + 1);
            int warmup = i * 2;

            double x = startPos.x + (double)MathHelper.cos(angle) * distance;
            double z = startPos.z + (double)MathHelper.sin(angle) * distance;

            spawnIcicle(x, z, minY, maxY, angle, warmup);
        }
    }

    private void spawnIcicle(double x, double z, double minY, double maxY, float yaw, int warmup) {
        BlockPos blockPos = BlockPos.ofFloored(x, maxY, z);
        boolean foundGround = false;
        double yOffset = 0.0;

        do {
            BlockPos belowPos = blockPos.down();
            BlockState blockState = monarch.getWorld().getBlockState(belowPos);

            if (blockState.isSideSolidFullSquare(monarch.getWorld(), belowPos, Direction.UP)) {
                if (!monarch.getWorld().isAir(blockPos)) {
                    BlockState aboveState = monarch.getWorld().getBlockState(blockPos);
                    VoxelShape voxelShape = aboveState.getCollisionShape(monarch.getWorld(), blockPos);
                    if (!voxelShape.isEmpty()) {
                        yOffset = voxelShape.getMax(Direction.Axis.Y);
                    }
                }
                foundGround = true;
                break;
            }

            blockPos = blockPos.down();
        } while(blockPos.getY() >= MathHelper.floor(minY) - 1);

        if (foundGround) {
            StraightIcicleEntity icicle = new StraightIcicleEntity(
                monarch.getWorld(),
                x,
                (double)blockPos.getY() + yOffset,
                z,
                yaw,
                warmup,
                monarch
            );

            monarch.getWorld().spawnEntity(icicle);
            monarch.getWorld().emitGameEvent(
                GameEvent.ENTITY_PLACE,
                new Vec3d(x, (double)blockPos.getY() + yOffset, z),
                GameEvent.Emitter.of(monarch)
            );
        }
    }

    @Override
    public boolean shouldContinue() {
        return !hasStarted || channelTicks > 0;
    }

    @Override
    public void stop() {
        this.isCasting = false;
        this.channelTicks = 0;
        this.hasStarted = false;
        this.cachedTarget = null;
        monarch.setCasting(false);

        monarch.distanceIcicleCooldown = monarch.getScaledCooldown(250);
        monarch.globalAbilityCooldown = 20;
    }
}

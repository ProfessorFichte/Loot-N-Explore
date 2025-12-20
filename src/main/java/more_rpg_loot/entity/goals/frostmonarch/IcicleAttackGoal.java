package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.projectile.StraightIcicleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.event.GameEvent;

import java.util.EnumSet;

public class IcicleAttackGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private boolean hasStarted;
    private LivingEntity cachedTarget;
    private static final float MAX_RANGE = 10.0F;

    public IcicleAttackGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!monarch.isPerformingAbility() && !isCasting) {
            LivingEntity target = monarch.getTarget();
            if (monarch.icicleCooldown <= 0 && target != null && target.isAlive()) {
                double distance = monarch.squaredDistanceTo(target);
                if (distance <= MAX_RANGE * MAX_RANGE) {
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
            conjureIcicles();
        }

        channelTicks--;
    }

    private void conjureIcicles() {
        if (!monarch.getWorld().isClient()) {
            LivingEntity target = this.cachedTarget;
            if (target == null || !target.isAlive()) {
                return;
            }

            double targetY = Math.min(target.getY(), monarch.getY());
            double maxY = Math.max(target.getY(), monarch.getY()) + 1.0;
            float angleToTarget = (float) MathHelper.atan2(
                target.getZ() - monarch.getZ(),
                target.getX() - monarch.getX()
            );

            float healthPercent = monarch.getHealth() / monarch.getMaxHealth();

            if (healthPercent > 0.7F) {
                spawnLinePattern(angleToTarget, targetY, maxY);
            } else if (healthPercent > 0.35F) {
                spawnPlusPattern(angleToTarget, targetY, maxY);
            } else {
                spawnPlusPattern(angleToTarget, targetY, maxY);
                spawnXPattern(angleToTarget, targetY, maxY);
            }
        }
    }

    private void spawnPlusPattern(float angleToTarget, double minY, double maxY) {
        int scaledLength = monarch.getScaledCount(12);

        for (int i = 0; i < 4; i++) {
            float angle = angleToTarget + (float)i * MathHelper.PI / 2.0F;
            spawnIcicleLine(angle, scaledLength, minY, maxY, i * 2);
        }
    }

    private void spawnXPattern(float angleToTarget, double minY, double maxY) {
        int scaledLength = monarch.getScaledCount(12);

        for (int i = 0; i < 4; i++) {
            float angle = angleToTarget + (float)i * MathHelper.PI / 2.0F + MathHelper.PI / 4.0F;
            spawnIcicleLine(angle, scaledLength, minY, maxY, i * 2 + 1);
        }
    }

    private void spawnLinePattern(float angleToTarget, double minY, double maxY) {
        int scaledLength = monarch.getScaledCount(12);

        spawnIcicleLine(angleToTarget, scaledLength, minY, maxY, 0);
    }

    private void spawnIcicleLine(float angle, int length, double minY, double maxY, int warmupOffset) {
        for (int i = 0; i < length; i++) {
            double distance = 1.5 * (double)(i + 1);
            int warmup = warmupOffset + i * 2;

            double x = monarch.getX() + (double)MathHelper.cos(angle) * distance;
            double z = monarch.getZ() + (double)MathHelper.sin(angle) * distance;

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

        monarch.icicleCooldown = monarch.getScaledCooldown(200);
        monarch.globalAbilityCooldown = 20;
    }
}

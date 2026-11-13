package lne.entity.frozen_depths.mob.frostmonarch.goals;

import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchAnimationTimes;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class DistanceIcicleGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private boolean hasStarted;
    private LivingEntity cachedTarget;

    public DistanceIcicleGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public static boolean isReady(FrostMonarchEntity monarch, LivingEntity target) {
        if (monarch.distanceIcicleCooldown > 0 || !(target instanceof PlayerEntity) || monarch.isTargetOutOfReach()) {
            return false;
        }
        double distance = monarch.horizontalDistanceTo(target);
        return distance > FrostMonarchEntity.CLOSE_RANGE && distance <= FrostMonarchEntity.MID_RANGE
                && monarch.tuning().getBoolean(FrozenDepthsTuning.DISTANCE_ICICLE_ENABLED, true);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        if (monarch.isPerformingAbility() || isCasting) {
            return false;
        }
        LivingEntity target = monarch.getTarget();
        return target != null && target.isAlive() && isReady(monarch, target);
    }

    @Override
    public void start() {
        this.cachedTarget = monarch.getTarget();
        this.isCasting = true;
        this.channelTicks = FrostMonarchAnimationTimes.GROUND_ICICLE_RELEASE_TICK + 5;
        this.hasStarted = false;
        monarch.setCasting(true);
        monarch.playCastAnimation(FrostMonarchAnimationTimes.Cast.QUICK);
        monarch.playCastFx();

        if (cachedTarget != null) {
            monarch.getLookControl().lookAt(cachedTarget, 30.0F, 30.0F);
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

        if (cachedTarget != null && cachedTarget.isAlive()) {
            monarch.getLookControl().lookAt(cachedTarget, 30.0F, 30.0F);
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
            MonarchGroundIcicles.line(monarch, startPos, angleToTarget, lineLength, targetY, maxY, 0);
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

        monarch.distanceIcicleCooldown = monarch.getScaledCooldown(monarch.tuning().getInt(FrozenDepthsTuning.DISTANCE_ICICLE_COOLDOWN, 250));
        monarch.startAbilityPause();
    }
}

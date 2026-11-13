package lne.entity.frozen_depths.mob.frostmonarch.goals;

import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchAnimationTimes;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class IcicleAttackGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private boolean hasStarted;
    private LivingEntity cachedTarget;

    public IcicleAttackGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public static boolean isReady(FrostMonarchEntity monarch, LivingEntity target) {
        return monarch.icicleCooldown <= 0
                && !monarch.isTargetOutOfReach()
                && monarch.horizontalDistanceTo(target) <= FrostMonarchEntity.CLOSE_RANGE
                && monarch.tuning().getBoolean(FrozenDepthsTuning.ICICLE_ATTACK_ENABLED, true);
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
            MonarchGroundIcicles.line(monarch, monarch.getPos(), angle, scaledLength, minY, maxY, i * 2);
        }
    }

    private void spawnXPattern(float angleToTarget, double minY, double maxY) {
        int scaledLength = monarch.getScaledCount(12);

        for (int i = 0; i < 4; i++) {
            float angle = angleToTarget + (float)i * MathHelper.PI / 2.0F + MathHelper.PI / 4.0F;
            MonarchGroundIcicles.line(monarch, monarch.getPos(), angle, scaledLength, minY, maxY, i * 2 + 1);
        }
    }

    private void spawnLinePattern(float angleToTarget, double minY, double maxY) {
        MonarchGroundIcicles.line(monarch, monarch.getPos(), angleToTarget, monarch.getScaledCount(12), minY, maxY, 0);
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

        monarch.icicleCooldown = monarch.getScaledCooldown(monarch.tuning().getInt(FrozenDepthsTuning.ICICLE_ATTACK_COOLDOWN, 200));
        monarch.startAbilityPause();
    }
}

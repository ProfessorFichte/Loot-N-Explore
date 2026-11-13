package lne.entity.frozen_depths.mob.frostmonarch.goals;

import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchAnimationTimes;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class BasicIcicleGoal extends Goal {
    private static final int RECOVERY_TICKS = 6;

    private final FrostMonarchEntity monarch;
    private LivingEntity target;
    private int castTicks;
    private boolean casting;

    public BasicIcicleGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        if (casting) {
            return false;
        }
        LivingEntity candidate = monarch.getTarget();
        if (candidate == null || !canUseBasicAttack(monarch, candidate)
                || !monarch.tuning().getBoolean(FrozenDepthsTuning.BASIC_ICICLE_ENABLED, true) || monarch.usesStaffSpell()) {
            return false;
        }
        this.target = candidate;
        return true;
    }

    public static boolean canUseBasicAttack(FrostMonarchEntity monarch, LivingEntity candidate) {
        if (monarch.isPerformingAbility() || monarch.basicIcicleCooldown > 0 || !candidate.isAlive()) {
            return false;
        }
        double distance = monarch.horizontalDistanceTo(candidate);
        if (distance > FrostMonarchEntity.MAX_RANGE) {
            return false;
        }
        if (!monarch.isTargetOutOfReach() && distance <= FrostMonarchEntity.CLOSE_RANGE) {
            return false;
        }
        if (coreAbilityReady(monarch, candidate)) {
            return false;
        }
        return monarch.getVisibilityCache().canSee(candidate);
    }

    public static boolean coreAbilityReady(FrostMonarchEntity monarch, LivingEntity candidate) {
        return IceBarrierGoal.hasPendingThreshold(monarch)
                || DistanceIcicleGoal.isReady(monarch, candidate) || HailstormGoal.isReady(monarch, candidate)
                || ScreechGoal.isReady(monarch, candidate) || IcicleAttackGoal.isReady(monarch, candidate);
    }

    @Override
    public void start() {
        casting = true;
        castTicks = 0;
        monarch.setCasting(true);
        monarch.playCastAnimation(FrostMonarchAnimationTimes.Cast.QUICK);
        monarch.playCastFx();
    }

    @Override
    public void tick() {
        if (!monarch.isCasting()) {
            monarch.setCasting(true);
        }
        if (target != null && target.isAlive()) {
            monarch.getLookControl().lookAt(target, 30.0F, 30.0F);
        }
        castTicks++;
        if (castTicks == FrostMonarchAnimationTimes.SUMMON_RELEASE_TICK) {
            shoot();
        }
    }

    private void shoot() {
        if (monarch.getWorld().isClient || target == null || !target.isAlive()) {
            return;
        }
        float scale = monarch.getHeight() / 3.3F;
        float yawRad = monarch.getBodyYaw() * MathHelper.RADIANS_PER_DEGREE;
        Vec3d forward = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
        Vec3d right = new Vec3d(-MathHelper.cos(yawRad), 0.0, -MathHelper.sin(yawRad));
        Vec3d origin = monarch.getPos()
                .add(forward.multiply(1.0 * scale))
                .add(right.multiply(0.5 * scale))
                .add(0.0, 2.6 * scale, 0.0);
        monarch.getWorld().spawnEntity(new TrackingIcicleEntity(monarch.getWorld(), monarch, target, origin.x, origin.y, origin.z));
        monarch.getWorld().playSound(null, origin.x, origin.y, origin.z, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.HOSTILE, 0.8F, 1.6F);
    }

    @Override
    public boolean shouldContinue() {
        return casting && castTicks < FrostMonarchAnimationTimes.SUMMON_RELEASE_TICK + RECOVERY_TICKS;
    }

    @Override
    public void stop() {
        casting = false;
        castTicks = 0;
        target = null;
        monarch.setCasting(false);
        monarch.basicIcicleCooldown = monarch.tuning().getInt(FrozenDepthsTuning.BASIC_ICICLE_COOLDOWN, 160);
        monarch.startAbilityPause();
    }
}

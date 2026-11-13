package lne.entity.frozen_depths.mob.frozen_magician;

import com.mrpg_lib.entity.config.CustomValues;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.conjured.EternalWinterPortalEntity;
import lne.entity.frozen_depths.conjured.PortalPlacement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

class WinterPortalGoal extends Goal {
    private final GeneralFrozenMagicianEntity mage;
    private LivingEntity target;
    private int castTicks;
    private boolean casting;

    WinterPortalGoal(GeneralFrozenMagicianEntity mage) {
        this.mage = mage;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        if (mage.portalCooldown > 0 || mage.postActionCooldown > 0 || mage.isCasting()) {
            return false;
        }
        LivingEntity candidate = mage.getTarget();
        if (candidate == null || !candidate.isAlive()) {
            return false;
        }
        CustomValues tuning = mage.tuning();
        if (mage.getHealth() > mage.getMaxHealth() * tuning.getDouble(FrozenDepthsTuning.PORTAL_HEALTH_THRESHOLD, 0.3)
                || !tuning.getBoolean(FrozenDepthsTuning.PORTAL_ENABLED, true)) {
            return false;
        }
        this.target = candidate;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return casting && castTicks < FrozenMagicianAnimationTimes.CAST_RELEASE_TICK + FrozenMagicianAnimationTimes.CAST_RECOVERY_TICKS;
    }

    @Override
    public boolean canStop() {
        return !casting;
    }

    @Override
    public void start() {
        casting = true;
        castTicks = 0;
        mage.setCasting(true);
        mage.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target != null && target.isAlive()) {
            mage.getLookControl().lookAt(target, 30.0F, 30.0F);
        }
        castTicks++;
        if (castTicks == FrozenMagicianAnimationTimes.CAST_RELEASE_TICK) {
            openPortals();
        }
    }

    private void openPortals() {
        CustomValues tuning = mage.tuning();
        int count = tuning.getInt(FrozenDepthsTuning.PORTAL_COUNT, 1);
        var type = PortalPlacement.resolveType(tuning.getString(FrozenDepthsTuning.PORTAL_ENTITY, ""), ModEntities.FROST_HOUND);
        double radius = tuning.getDouble(FrozenDepthsTuning.PORTAL_SPAWN_RADIUS, 10.0);
        int lifetime = Math.max(20, tuning.getInt(FrozenDepthsTuning.PORTAL_LIFETIME, EternalWinterPortalEntity.DEFAULT_LIFETIME));
        int firstDelay = tuning.getInt(FrozenDepthsTuning.PORTAL_FIRST_SPAWN_DELAY, EternalWinterPortalEntity.DEFAULT_FIRST_SPAWN_DELAY);
        int interval = tuning.getInt(FrozenDepthsTuning.PORTAL_SPAWN_INTERVAL, EternalWinterPortalEntity.DEFAULT_SPAWN_INTERVAL);
        PortalPlacement.open(mage, type, count, radius, lifetime, firstDelay, interval);
    }

    @Override
    public void stop() {
        casting = false;
        castTicks = 0;
        target = null;
        mage.setCasting(false);
        mage.portalCooldown = Math.max(20, mage.tuning().getInt(FrozenDepthsTuning.PORTAL_COOLDOWN, 1200));
        mage.postActionCooldown = 10;
    }
}

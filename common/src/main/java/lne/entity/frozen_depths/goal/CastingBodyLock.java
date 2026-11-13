package lne.entity.frozen_depths.goal;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.MobEntity;

public final class CastingBodyLock {
    private static final int RELEASE_HOLD_TICKS = 10;

    private final MobEntity mob;
    private final TrackedData<Boolean> casting;
    private int holdTicks;

    public CastingBodyLock(MobEntity mob, TrackedData<Boolean> casting) {
        this.mob = mob;
        this.casting = casting;
    }

    public void set(boolean value) {
        mob.getDataTracker().set(casting, value);
    }

    public boolean isCasting() {
        return mob.getDataTracker().get(casting);
    }

    public void tick() {
        if (isCasting()) {
            holdTicks = RELEASE_HOLD_TICKS;
        } else if (holdTicks > 0) {
            holdTicks--;
        } else {
            return;
        }
        mob.bodyYaw = mob.headYaw;
        mob.prevBodyYaw = mob.prevHeadYaw;
    }
}

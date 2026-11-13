package lne.entity.frozen_depths.goal;

import com.mrpg_lib.entity.goal.WindupMeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

import java.util.function.BooleanSupplier;

public class ToggleableMeleeGoal extends WindupMeleeAttackGoal {
    private final BooleanSupplier enabled;

    public ToggleableMeleeGoal(PathAwareEntity mob, BooleanSupplier enabled, double speed, boolean pauseWhenMobIdle, double extraReach, int[] impactTicks) {
        super(mob, speed, pauseWhenMobIdle, extraReach, impactTicks);
        this.enabled = enabled;
    }

    public ToggleableMeleeGoal(PathAwareEntity mob, BooleanSupplier enabled, double speed, boolean pauseWhenMobIdle, double extraReach, int[] impactTicks, ImpactHandler handler) {
        super(mob, speed, pauseWhenMobIdle, extraReach, impactTicks, handler);
        this.enabled = enabled;
    }

    @Override
    public boolean canStart() {
        return enabled.getAsBoolean() && super.canStart();
    }
}

package lne.entity.frozen_depths.mob.frostmonarch.goals;

import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.minecraft.entity.ai.goal.Goal;

public class ConditionalGoal extends Goal {
    private final Goal wrapped;
    private final FrostMonarchEntity monarch;

    public ConditionalGoal(FrostMonarchEntity monarch, Goal wrapped) {
        this.monarch = monarch;
        this.wrapped = wrapped;
        this.setControls(wrapped.getControls());
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        return !monarch.isBusy() && wrapped.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return !monarch.isBusy() && wrapped.shouldContinue();
    }

    @Override
    public void start() {
        wrapped.start();
    }

    @Override
    public void stop() {
        wrapped.stop();
    }

    @Override
    public void tick() {
        wrapped.tick();
    }
}
package lne.entity.frozen_depths.goal;

import net.minecraft.entity.ai.goal.Goal;

import java.util.function.BooleanSupplier;

public class GatedGoal extends Goal {
    private final Goal wrapped;
    private final BooleanSupplier gate;

    public GatedGoal(Goal wrapped, BooleanSupplier gate) {
        this.wrapped = wrapped;
        this.gate = gate;
        this.setControls(wrapped.getControls());
    }

    public Goal wrapped() {
        return wrapped;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return wrapped.shouldRunEveryTick();
    }

    @Override
    public boolean canStart() {
        return gate.getAsBoolean() && wrapped.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return gate.getAsBoolean() && wrapped.shouldContinue();
    }

    @Override
    public boolean canStop() {
        return wrapped.canStop();
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

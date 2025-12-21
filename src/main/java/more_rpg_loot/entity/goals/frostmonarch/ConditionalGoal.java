package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.minecraft.entity.ai.goal.Goal;

public class ConditionalGoal extends Goal {
    private final Goal wrapped;
    private final FrostMonarchEntity monarch;

    public ConditionalGoal(FrostMonarchEntity monarch, Goal wrapped) {
        this.monarch = monarch;
        this.wrapped = wrapped;
    }

    @Override
    public boolean canStart() {
        // Prevent starting during any ability
        return !monarch.isPerformingAbility() && wrapped.canStart();
    }

    @Override
    public boolean shouldContinue() {
        // Stop continuing if any ability activates
        return !monarch.isPerformingAbility() && wrapped.shouldContinue();
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
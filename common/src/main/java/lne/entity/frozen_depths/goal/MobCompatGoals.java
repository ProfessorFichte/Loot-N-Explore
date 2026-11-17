package lne.entity.frozen_depths.goal;

import com.mrpg_lib.compat.better_combat.BetterCombatCompat;
import com.mrpg_lib.compat.combat_roll.CombatRollCompat;
import lne.internal.platform.LNEPlatform;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

public final class MobCompatGoals {
    public static final CombatRollCompat.RollSettings RANGER_ROLL = CombatRollCompat.RollSettings.DEFAULT
            .withDefensiveHealthThreshold(0.5F)
            .withDodgeProjectiles(true);
    public static final CombatRollCompat.RollSettings GUARD_ROLL = CombatRollCompat.RollSettings.DEFAULT
            .withEngage(true, 5.0, 0.15F)
            .withDodgeProjectiles(true);

    private MobCompatGoals() {
    }

    @Nullable
    public static Goal betterCombat(PathAwareEntity mob, double speed, BooleanSupplier gate) {
        if (!LNEPlatform.isModLoaded("bettercombat")) return null;
        Goal melee = BetterCombatCompat.createMeleeGoal(mob, speed, false);
        return new GatedGoal(melee, gate) {
            @Override
            public boolean canStart() {
                if (!gate.getAsBoolean()) {
                    BetterCombatCompat.onGoalRemoved(melee);
                    return false;
                }
                return melee.canStart();
            }
        };
    }

    @Nullable
    public static Goal combatRoll(PathAwareEntity mob, CombatRollCompat.RollSettings settings, BooleanSupplier gate) {
        return CombatRollCompat.createRollGoal(mob, settings).map(goal -> (Goal) new GatedGoal(goal, gate)).orElse(null);
    }
}

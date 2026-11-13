package lne.compat.spell_engine;

import com.mrpg_lib.compat.spell_engine.MobEvasion;
import net.minecraft.entity.LivingEntity;

public final class LneMobEvasion {
    private LneMobEvasion() {
    }

    public static void apply(LivingEntity entity, double chance) {
        if (chance > 0.0) {
            MobEvasion.setEvasionChance(entity, chance);
        }
    }
}

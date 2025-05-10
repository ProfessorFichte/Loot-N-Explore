package more_rpg_loot.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EnderDragonScalesEffect extends StatusEffect {
    protected EnderDragonScalesEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public boolean applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.getHealth() < pLivingEntity.getMaxHealth()) {
            pLivingEntity.heal(1.0F);
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 50 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}

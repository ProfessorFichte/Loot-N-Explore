package more_rpg_loot.effects.frozen_depths;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FrostResistanceEffect extends StatusEffect {

    public FrostResistanceEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public boolean applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        livingEntity.setFrozenTicks(0);
        super.applyUpdateEffect(livingEntity, pAmplifier);
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

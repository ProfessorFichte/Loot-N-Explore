package lne.effects.frozen_depths;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import lne.util.LneTags;

public class FrostResistanceEffect extends StatusEffect {

    public FrostResistanceEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        entity.getStatusEffects().stream()
                .map(instance -> instance.getEffectType())
                .filter(effect -> effect.isIn(LneTags.BLOCKED_BY_FROST_RESISTANCE))
                .toList()
                .forEach(entity::removeStatusEffect);
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

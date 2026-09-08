package more_rpg_loot.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class SpecialStatusEffect extends StatusEffect {
    public SpecialStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    // 1.20.1: `onApplied` also receives the entity's AttributeContainer, and `getEffectType()`
    // returns the raw StatusEffect.
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        List<StatusEffectInstance> currentEffects = new ArrayList<>(entity.getStatusEffects());
        for (StatusEffectInstance instance : currentEffects) {
            StatusEffect effect = instance.getEffectType();
            if (effect instanceof SpecialStatusEffect && effect != this) {
                entity.removeStatusEffect(instance.getEffectType());
            }
        }
    }
}

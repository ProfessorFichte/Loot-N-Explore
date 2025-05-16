package more_rpg_loot.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class SpecialStatusEffect extends StatusEffect {
    protected SpecialStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        List<StatusEffectInstance> currentEffects = new ArrayList<>(entity.getStatusEffects());
        for (StatusEffectInstance instance : currentEffects) {
            StatusEffect effect = instance.getEffectType().value();
            if (effect instanceof SpecialStatusEffect && effect != this) {
                entity.removeStatusEffect(instance.getEffectType());
            }
        }
    }
}

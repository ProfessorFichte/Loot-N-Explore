package lne.effects.generic;

import lne.ability.RelicAbilities;
import lne.effects.Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RainbowCoralEffect extends StatusEffect {
    public RainbowCoralEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        if (!entity.getWorld().isClient) {
            var ability = RelicAbilities.get("rainbow_coral");
            double percent = ability != null ? ability.value("absorption_percent") : 0.2;
            grant(entity, entity.getMaxHealth() * percent);
        }
    }

    public static void grant(LivingEntity entity, double amount) {
        var attribute = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_ABSORPTION);
        if (attribute == null || amount <= 0) return;
        var id = Effects.RAINBOW_CORAL.modifierId();
        attribute.removeModifier(id);
        attribute.addPersistentModifier(new EntityAttributeModifier(id, amount, EntityAttributeModifier.Operation.ADD_VALUE));
        entity.setAbsorptionAmount((float) Math.min(Math.max(entity.getAbsorptionAmount(), amount), entity.getMaxAbsorption()));
    }

    public static void revoke(LivingEntity entity) {
        var attribute = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_ABSORPTION);
        if (attribute == null) return;
        var id = Effects.RAINBOW_CORAL.modifierId();
        var modifier = attribute.getModifier(id);
        if (modifier == null) return;
        attribute.removeModifier(id);
        float remaining = Math.max(0.0F, entity.getAbsorptionAmount() - (float) modifier.value());
        entity.setAbsorptionAmount(Math.min(remaining, entity.getMaxAbsorption()));
    }
}

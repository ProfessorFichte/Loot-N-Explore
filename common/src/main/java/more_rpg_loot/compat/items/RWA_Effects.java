package more_rpg_loot.compat.items;

import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.effectsConfig;

public class RWA_Effects {
    private static final List<CompatEffectEntry> entries = new ArrayList<>();

    public static List<CompatEffectEntry> getEntries() {
        return entries;
    }

    private static CompatEffectEntry entry(String name, String title, String description, StatusEffect effect) {
        return CompatEffectEntry.add(entries, name, title, description, effect);
    }

    public static final CompatEffectEntry APPLE_JUICE = entry("apple_juice",
            "Apple Juice", "Increases Ranged Weapon Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry WALDMEISTER = entry("waldmeister",
            "Waldmeister", "Increases Ranged Weapon Haste",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry FORREST_SPIRIT = entry("forrest_spirit",
            "Forrest Spirit", "Increases Ranged Weapon Damage and Ranged Weapon Haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry WOODSNAKE_POTION = entry("woodsnake_potion",
            "Wood Snake Potion", "Increases Ranged Damage, Ranged Haste and Arrow Velocity.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register() {
        APPLE_JUICE.effect.addAttributeModifier(
                EntityAttributes_RangedWeapon.DAMAGE.entry, APPLE_JUICE.modifierId(),
                effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        WALDMEISTER.effect.addAttributeModifier(
                EntityAttributes_RangedWeapon.HASTE.entry, WALDMEISTER.modifierId(),
                effectsConfig.value.drinks_haste_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        FORREST_SPIRIT.effect.addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.entry, FORREST_SPIRIT.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, FORREST_SPIRIT.modifierId(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        WOODSNAKE_POTION.effect.addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.entry, WOODSNAKE_POTION.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, WOODSNAKE_POTION.modifierId(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.VELOCITY.entry, WOODSNAKE_POTION.modifierId(),
                        effectsConfig.value.drinks_arrow_velocity_t3_boost, EntityAttributeModifier.Operation.ADD_VALUE);

        CompatEffectEntry.registerAll("Ranged Weapon API", entries);
    }
}

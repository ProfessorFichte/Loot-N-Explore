package more_rpg_loot.compat.items;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class RWA_Effects {
    private static final ArrayList<RWAEntry> entries = new ArrayList<RWAEntry>();

    // Entry class for Ranged Weapon API compatible effects with datagen support
    public static class RWAEntry {
        public final Identifier id;
        public final String title;
        public final String description;
        public final StatusEffect effect;

        public RWAEntry(String name, String title, String description, StatusEffect effect) {
            this.id = new Identifier(MOD_ID, name);
            this.title = title;
            this.description = description;
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            // 1.20.1: every status-effect API takes the raw StatusEffect, so there is no
            // RegistryEntry to hold on to.
            Registry.register(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return new Identifier(MOD_ID, "effect." + id.getPath());
        }

        /// 1.20.1: attribute modifiers are UUID-keyed, not Identifier-keyed. Derived from the same
        /// Identifier so the value is stable across runs.
        public String modifierUuid() {
            return UUID.nameUUIDFromBytes(modifierId().toString().getBytes(StandardCharsets.UTF_8)).toString();
        }
    }

    // Getter to access all registered entries (used by datagen)
    public static ArrayList<RWAEntry> getEntries() {
        return entries;
    }

    /// T0 BUFF EFFECTS
    public static final RWAEntry APPLE_JUICE =  new RWAEntry("apple_juice",
            "Apple Juice",
            "Increases Ranged Weapon Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T1 BUFF EFFECTS
    public static final RWAEntry WALDMEISTER =  new RWAEntry("waldmeister",
            "Waldmeister",
            "Increases Ranged Weapon Haste",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final RWAEntry FORREST_SPIRIT =  new RWAEntry("forrest_spirit",
            "Forrest Spirit",
            "Increases Ranged Weapon Damage and Ranged Weapon Haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final RWAEntry WOODSNAKE_POTION =  new RWAEntry("woodsnake_potion",
            "Wood Snake Potion",
            "Increases Ranged Damage, Ranged Haste and Arrow Velocity.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Ranged Weapon API Compat Effects for " + MOD_ID);
        /// T0 BUFF EFFECTS
        APPLE_JUICE.effect
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.attribute, APPLE_JUICE.modifierUuid(),
                effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        /// T1 BUFF EFFECTS
        WALDMEISTER.effect
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.attribute, WALDMEISTER.modifierUuid(),
                        effectsConfig.value.drinks_haste_t1_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        /// T2 BUFF EFFECTS
        FORREST_SPIRIT.effect
                .addAttributeModifier(
                EntityAttributes_RangedWeapon.DAMAGE.attribute, FORREST_SPIRIT.modifierUuid(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.attribute, FORREST_SPIRIT.modifierUuid(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        /// T3 BUFF EFFECTS
        WOODSNAKE_POTION.effect
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.attribute, WOODSNAKE_POTION.modifierUuid(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.attribute, WOODSNAKE_POTION.modifierUuid(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.VELOCITY.attribute, WOODSNAKE_POTION.modifierUuid(),
                effectsConfig.value.drinks_arrow_velocity_t3_boost, EntityAttributeModifier.Operation.ADDITION);


        for (RWAEntry entry: entries) {
            entry.register();
        }
    }
}

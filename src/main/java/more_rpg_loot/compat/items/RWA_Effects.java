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

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class RWA_Effects {
    private static final ArrayList<RWAEntry> entries = new ArrayList<RWAEntry>();
    public static class RWAEntry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public RWAEntry(String name, StatusEffect effect) {
            this.id = Identifier.of(MOD_ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(MOD_ID, "effect." + id.getPath());
        }
    }

    /// T0 BUFF EFFECTS
    public static final RWAEntry APPLE_JUICE =  new RWAEntry("apple_juice",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T1 BUFF EFFECTS
    public static final RWAEntry WALDMEISTER =  new RWAEntry("waldmeister",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final RWAEntry FORREST_SPIRIT =  new RWAEntry("forrest_spirit",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final RWAEntry WOODSNAKE_POTION =  new RWAEntry("woodsnake_potion",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Ranged Weapon API Compat Effects for " + MOD_ID);
        /// T0 BUFF EFFECTS
        APPLE_JUICE.effect
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.entry, APPLE_JUICE.modifierId(),
                effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        /// T1 BUFF EFFECTS
        WALDMEISTER.effect
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, WALDMEISTER.modifierId(),
                        effectsConfig.value.drinks_haste_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        /// T2 BUFF EFFECTS
        FORREST_SPIRIT.effect
                .addAttributeModifier(
                EntityAttributes_RangedWeapon.DAMAGE.entry, FORREST_SPIRIT.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, FORREST_SPIRIT.modifierId(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        /// T3 BUFF EFFECTS
        WOODSNAKE_POTION.effect
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.DAMAGE.entry, WOODSNAKE_POTION.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.HASTE.entry, WOODSNAKE_POTION.modifierId(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes_RangedWeapon.VELOCITY.entry, WOODSNAKE_POTION.modifierId(),
                effectsConfig.value.drinks_arrow_velocity_t3_boost, EntityAttributeModifier.Operation.ADD_VALUE);


        for (RWAEntry entry: entries) {
            entry.register();
        }
    }
}

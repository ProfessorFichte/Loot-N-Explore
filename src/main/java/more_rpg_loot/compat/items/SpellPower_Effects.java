package more_rpg_loot.compat.items;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class SpellPower_Effects {
    private static final ArrayList<SPEntry> entries = new ArrayList<SPEntry>();

    // Entry class for Spell Power API compatible effects with datagen support
    public static class SPEntry {
        public final Identifier id;
        public final String title;
        public final String description;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public SPEntry(String name, String title, String description, StatusEffect effect) {
            this.id = Identifier.of(MOD_ID, name);
            this.title = title;
            this.description = description;
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

    // Getter to access all registered entries (used by datagen)
    public static ArrayList<SPEntry> getEntries() {
        return entries;
    }

    /// T0 BUFF EFFECTS
    public static final SPEntry ORANGE_JUICE =  new SPEntry("orange_juice",
            "Orange Juice",
            "Increases Spell Haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T1 BUFF EFFECTS
    public static final SPEntry SWEET_CHILLI =  new SPEntry("sweet_chilli",
            "Sweet Chilli",
            "Increases Spell Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final SPEntry FRUIT_ICEWATER =  new SPEntry("fruit_icewater",
            "Fruit Icewater",
            "Increases Frost Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final SPEntry CHORUS_EXTRACT =  new SPEntry("chorus_extract",
            "Chorus Extract",
            "Increases Arcane Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final SPEntry HOT_CHILLI =  new SPEntry("hot_chilli",
            "Hot Chilli",
            "Increases Fire Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final SPEntry HOLY_WATER =  new SPEntry("holy_water",
            "Holy Water",
            "Increases Healing Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final SPEntry ENCHANTED_ALE =  new SPEntry("enchanted_ale",
            "Enchanted Ale",
            "Increases Spell Critical Chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final SPEntry WIZARDS_ELIXIR =  new SPEntry("wizards_elixir",
            "Wizards Elixir",
            "Increases Spell Power and Spell Haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final SPEntry MERLINS_FLASK =  new SPEntry("merlins_flask",
            "Merlin's Flask",
            "Increases Spell Power, Spell Haste and Spell Critical Damage.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final SPEntry CRUSADERS_REST =  new SPEntry("crusaders_rest",
            "Crusader's Rest",
            "Increases Spell Healing Power, Attack Damage and Max Health.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));


    public static void register(){
        RPGLoot.LOGGER.info("Registering Spell Power Compat Effects for " + MOD_ID);
        /// T0 BUFF EFFECTS
        ORANGE_JUICE.effect.addAttributeModifier(
                SpellPowerMechanics.HASTE.attributeEntry, ORANGE_JUICE.modifierId(),
                effectsConfig.value.drinks_haste_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T1 BUFF EFFECTS
        SWEET_CHILLI.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, SWEET_CHILLI.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        FRUIT_ICEWATER.effect
                .addAttributeModifier(
                        SpellSchools.FROST.attributeEntry, FRUIT_ICEWATER.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        CHORUS_EXTRACT.effect
                .addAttributeModifier(
                        SpellSchools.ARCANE.attributeEntry, CHORUS_EXTRACT.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        HOT_CHILLI.effect
                .addAttributeModifier(
                        SpellSchools.FIRE.attributeEntry, HOT_CHILLI.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        HOLY_WATER.effect
                .addAttributeModifier(
                        SpellSchools.HEALING.attributeEntry, HOLY_WATER.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        ENCHANTED_ALE.effect
                .addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, ENCHANTED_ALE.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T2 BUFF EFFECTS
        WIZARDS_ELIXIR.effect
                .addAttributeModifier(
                        SpellSchools.GENERIC.attributeEntry, WIZARDS_ELIXIR.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        SpellPowerMechanics.HASTE.attributeEntry, WIZARDS_ELIXIR.modifierId(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T3 BUFF EFFECTS
        MERLINS_FLASK.effect
                .addAttributeModifier(
                        SpellSchools.GENERIC.attributeEntry, MERLINS_FLASK.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        SpellPowerMechanics.HASTE.attributeEntry, MERLINS_FLASK.modifierId(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, MERLINS_FLASK.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        CRUSADERS_REST.effect
                .addAttributeModifier(
                        SpellSchools.HEALING.attributeEntry, CRUSADERS_REST.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, CRUSADERS_REST.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, CRUSADERS_REST.modifierId(),
                        effectsConfig.value.drinks_health_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        for (SpellPower_Effects.SPEntry entry: entries) {
            entry.register();
        }
    }
}

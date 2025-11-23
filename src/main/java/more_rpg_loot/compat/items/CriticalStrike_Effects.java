package more_rpg_loot.compat.items;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.critical_strike.api.CriticalStrikeAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class CriticalStrike_Effects {
    private static final ArrayList<CritEntry> entries = new ArrayList<CritEntry>();

    // Entry class for Critical Strike compatible effects with datagen support
    public static class CritEntry {
        public final Identifier id;
        public final String title;
        public final String description;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public CritEntry(String name, String title, String description, StatusEffect effect) {
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
    public static ArrayList<CritEntry> getEntries() {
        return entries;
    }

    /// T1 BUFF EFFECTS
    public static final CritEntry BLACKPEPPER_BREAD = new CritEntry("blackpepper_bread",
            "Blackpepper Bread",
            "Increases Critical Chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CritEntry INFERNO_CHILLI_OIL = new CritEntry("inferno_chilli_oil",
            "Inferno Chilli Oil",
            "Increases Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final CritEntry GRILLED_EEL_SUSHI = new CritEntry("grilled_eel_sushi",
            "Grilled Eel Sushi Roll",
            "Increases Critical Chance and Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final CritEntry PHANTOMSTRIKE_TONIC = new CritEntry("phantomstrike_tonic",
            "Phantom Strike Tonic",
            "Increases Critical Chance, Critical Damage and Movement Speed.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register() {
        RPGLoot.LOGGER.info("Registering Critical Strike Effects for " + MOD_ID);
        /// T1 BUFF EFFECTS
        BLACKPEPPER_BREAD.effect
                .addAttributeModifier(
                        CriticalStrikeAttributes.CHANCE.attributeEntry, BLACKPEPPER_BREAD.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        INFERNO_CHILLI_OIL.effect
                .addAttributeModifier(
                        CriticalStrikeAttributes.DAMAGE.attributeEntry, INFERNO_CHILLI_OIL.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T2 BUFF EFFECTS
        GRILLED_EEL_SUSHI.effect
                .addAttributeModifier(
                        CriticalStrikeAttributes.CHANCE.attributeEntry, GRILLED_EEL_SUSHI.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        CriticalStrikeAttributes.DAMAGE.attributeEntry, GRILLED_EEL_SUSHI.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T3 BUFF EFFECTS
        PHANTOMSTRIKE_TONIC.effect
                .addAttributeModifier(
                        CriticalStrikeAttributes.CHANCE.attributeEntry, PHANTOMSTRIKE_TONIC.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        CriticalStrikeAttributes.DAMAGE.attributeEntry, PHANTOMSTRIKE_TONIC.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED, PHANTOMSTRIKE_TONIC.modifierId(),
                        effectsConfig.value.drinks_speed_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        for (CritEntry entry : entries) {
            entry.register();
        }
    }
}

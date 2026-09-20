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
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class Witcher_Effects {
    private static final ArrayList<WitcherEntry> entries = new ArrayList<WitcherEntry>();

    // Entry class for Witcher RPG compatible effects with datagen support
    public static class WitcherEntry {
        public final Identifier id;
        public final String title;
        public final String description;
        public final StatusEffect effect;

        public WitcherEntry(String name, String title, String description, StatusEffect effect) {
            this.id = new Identifier(MOD_ID, name);
            this.title = title;
            this.description = description;
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            Registry.register(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return new Identifier(MOD_ID, "effect." + id.getPath());
        }

        public String modifierUuid() {
            return UUID.nameUUIDFromBytes(modifierId().toString().getBytes(StandardCharsets.UTF_8)).toString();
        }
    }

    // Getter to access all registered entries (used by datagen)
    public static ArrayList<WitcherEntry> getEntries() {
        return entries;
    }
    /// T0 BUFF EFFECTS
    public static final WitcherEntry BEAUCLAIR_WHITE =  new WitcherEntry("beauclair_white",
            "Beauclair White",
            "Increases Sign Intensity.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T1 BUFF EFFECTS
    public static final WitcherEntry RIVIAN_KRIEK =  new WitcherEntry("rivian_kriek",
            "Rivian Kriek",
            "Increases Adrenaline.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final WitcherEntry BUTCHER_OF_BLAVIKEN =  new WitcherEntry("butcher_of_blaviken",
            "Butcher of Blaviken",
            "Increases Sign Intensity and Adrenaline.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final WitcherEntry WHITE_WOLF =  new WitcherEntry("white_wolf",
            "White Wolf",
            "Increases Sign Intensity, Adrenaline and Attack Damage.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Witcher Compat Effects for " + MOD_ID);
        /// T0 BUFF EFFECTS
        BEAUCLAIR_WHITE.effect
                .addAttributeModifier(
                    WitcherAttributes.SIGN_INTENSITY, BEAUCLAIR_WHITE.modifierUuid(),
                    effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        /// T1 BUFF EFFECTS
        RIVIAN_KRIEK.effect
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, RIVIAN_KRIEK.modifierUuid(),
                        effectsConfig.value.drinks_special_attribute_t1_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        /// T2 BUFF EFFECTS
        BUTCHER_OF_BLAVIKEN.effect
                .addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, BUTCHER_OF_BLAVIKEN.modifierUuid(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, BUTCHER_OF_BLAVIKEN.modifierUuid(),
                        effectsConfig.value.drinks_special_attribute_t2_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        /// T3 BUFF EFFECTS
        WHITE_WOLF.effect
                .addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, WHITE_WOLF.modifierUuid(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, WHITE_WOLF.modifierUuid(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, WHITE_WOLF.modifierUuid(),
                        effectsConfig.value.drinks_special_attribute_t3_boost, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        for (WitcherEntry entry: entries) {
            entry.register();
        }
    }
}

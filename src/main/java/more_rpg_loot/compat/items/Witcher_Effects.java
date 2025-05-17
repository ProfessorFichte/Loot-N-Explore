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

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class Witcher_Effects {
    private static final ArrayList<WitcherEntry> entries = new ArrayList<WitcherEntry>();
    public static class WitcherEntry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public WitcherEntry(String name, StatusEffect effect) {
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
    public static final WitcherEntry BEAUCLAIR_WHITE =  new WitcherEntry("beauclair_white",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T1 BUFF EFFECTS
    public static final WitcherEntry RIVIAN_KRIEK =  new WitcherEntry("rivian_kriek",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final WitcherEntry BUTCHER_OF_BLAVIKEN =  new WitcherEntry("butcher_of_blaviken",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final WitcherEntry WHITE_WOLF =  new WitcherEntry("white_wolf",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Witcher Compat Effects for " + MOD_ID);
        /// T0 BUFF EFFECTS
        BEAUCLAIR_WHITE.effect
                .addAttributeModifier(
                    WitcherAttributes.SIGN_INTENSITY, BEAUCLAIR_WHITE.modifierId(),
                    effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        /// T1 BUFF EFFECTS
        RIVIAN_KRIEK.effect
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, RIVIAN_KRIEK.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        /// T2 BUFF EFFECTS
        BUTCHER_OF_BLAVIKEN.effect
                .addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, BUTCHER_OF_BLAVIKEN.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, BUTCHER_OF_BLAVIKEN.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        /// T3 BUFF EFFECTS
        WHITE_WOLF.effect
                .addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, WHITE_WOLF.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, WHITE_WOLF.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, WHITE_WOLF.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        for (WitcherEntry entry: entries) {
            entry.register();
        }
    }
}

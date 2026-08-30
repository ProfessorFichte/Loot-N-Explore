package more_rpg_loot.compat.items;

import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.effectsConfig;

public class Witcher_Effects {
    private static final List<CompatEffectEntry> entries = new ArrayList<>();

    public static List<CompatEffectEntry> getEntries() {
        return entries;
    }

    private static CompatEffectEntry entry(String name, String title, String description, StatusEffect effect) {
        return CompatEffectEntry.add(entries, name, title, description, effect);
    }

    public static final CompatEffectEntry BEAUCLAIR_WHITE = entry("beauclair_white",
            "Beauclair White", "Increases Sign Intensity.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry RIVIAN_KRIEK = entry("rivian_kriek",
            "Rivian Kriek", "Increases Adrenaline.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry BUTCHER_OF_BLAVIKEN = entry("butcher_of_blaviken",
            "Butcher of Blaviken", "Increases Sign Intensity and Adrenaline.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry WHITE_WOLF = entry("white_wolf",
            "White Wolf", "Increases Sign Intensity, Adrenaline and Attack Damage.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register() {
        BEAUCLAIR_WHITE.effect.addAttributeModifier(
                WitcherAttributes.SIGN_INTENSITY, BEAUCLAIR_WHITE.modifierId(),
                effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        RIVIAN_KRIEK.effect.addAttributeModifier(
                WitcherAttributes.ADRENALINE_MODIFIER, RIVIAN_KRIEK.modifierId(),
                effectsConfig.value.drinks_special_attribute_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        BUTCHER_OF_BLAVIKEN.effect.addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, BUTCHER_OF_BLAVIKEN.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, BUTCHER_OF_BLAVIKEN.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        WHITE_WOLF.effect.addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, WHITE_WOLF.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, WHITE_WOLF.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, WHITE_WOLF.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        CompatEffectEntry.registerAll("Witcher", entries);
    }
}

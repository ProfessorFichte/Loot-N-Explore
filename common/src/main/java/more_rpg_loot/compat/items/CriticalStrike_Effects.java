package more_rpg_loot.compat.items;

import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.critical_strike.api.CriticalStrikeAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.effectsConfig;

public class CriticalStrike_Effects {
    private static final List<CompatEffectEntry> entries = new ArrayList<>();

    public static List<CompatEffectEntry> getEntries() {
        return entries;
    }

    private static CompatEffectEntry entry(String name, String title, String description, StatusEffect effect) {
        return CompatEffectEntry.add(entries, name, title, description, effect);
    }

    public static final CompatEffectEntry BLACKPEPPER_BREAD = entry("blackpepper_bread",
            "Blackpepper Bread", "Increases Critical Chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry INFERNO_CHILLI_OIL = entry("inferno_chilli_oil",
            "Inferno Chilli Oil", "Increases Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry GRILLED_EEL_SUSHI = entry("grilled_eel_sushi",
            "Grilled Eel Sushi Roll", "Increases Critical Chance and Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry PHANTOMSTRIKE_TONIC = entry("phantomstrike_tonic",
            "Phantom Strike Tonic", "Increases Critical Chance, Critical Damage and Movement Speed.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register() {
        BLACKPEPPER_BREAD.effect.addAttributeModifier(
                CriticalStrikeAttributes.CHANCE.attributeEntry, BLACKPEPPER_BREAD.modifierId(),
                effectsConfig.value.drinks_crit_rate_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        INFERNO_CHILLI_OIL.effect.addAttributeModifier(
                CriticalStrikeAttributes.DAMAGE.attributeEntry, INFERNO_CHILLI_OIL.modifierId(),
                effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        GRILLED_EEL_SUSHI.effect.addAttributeModifier(
                        CriticalStrikeAttributes.CHANCE.attributeEntry, GRILLED_EEL_SUSHI.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        CriticalStrikeAttributes.DAMAGE.attributeEntry, GRILLED_EEL_SUSHI.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        PHANTOMSTRIKE_TONIC.effect.addAttributeModifier(
                        CriticalStrikeAttributes.CHANCE.attributeEntry, PHANTOMSTRIKE_TONIC.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        CriticalStrikeAttributes.DAMAGE.attributeEntry, PHANTOMSTRIKE_TONIC.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED, PHANTOMSTRIKE_TONIC.modifierId(),
                        effectsConfig.value.drinks_speed_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        CompatEffectEntry.registerAll("Critical Strike", entries);
    }
}

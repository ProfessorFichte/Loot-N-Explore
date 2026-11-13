package lne.compat.items;

import lne.effects.CustomStatusEffect;
import lne.effects.SpecialStatusEffect;
import lne.item.CommonItems;
import lne.platform.LNEEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static lne.LneMod.MOD_ID;
import static lne.LneMod.effectsConfig;
import static lne.config.EffectAttributeConfigs.modifier;

public class SpellPower_Effects {
    private static final List<CompatEffectEntry> entries = new ArrayList<>();

    public static List<CompatEffectEntry> getEntries() {
        return entries;
    }

    private static CompatEffectEntry entry(String name, String title, String description, StatusEffect effect) {
        return CompatEffectEntry.add(entries, name, title, description, effect);
    }

    public static final CompatEffectEntry ORANGE_JUICE = entry("orange_juice",
            "Orange Juice", "Increases Spell Haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry SWEET_CHILLI = entry("sweet_chilli",
            "Sweet Chilli", "Increases Spell Critical Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry FRUIT_ICEWATER = entry("fruit_icewater",
            "Fruit Icewater", "Increases Frost Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry CHORUS_EXTRACT = entry("chorus_extract",
            "Chorus Extract", "Increases Arcane Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry HOT_CHILLI = entry("hot_chilli",
            "Hot Chilli", "Increases Fire Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry HOLY_WATER = entry("holy_water",
            "Holy Water", "Increases Healing Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry ENCHANTED_ALE = entry("enchanted_ale",
            "Enchanted Ale", "Increases Spell Critical Chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry WIZARDS_ELIXIR = entry("wizards_elixir",
            "Wizards Elixir", "Increases Spell Power and Spell Haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry MERLINS_FLASK = entry("merlins_flask",
            "Merlin's Flask", "Increases Spell Power, Spell Haste and Spell Critical Damage.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry CRUSADERS_REST = entry("crusaders_rest",
            "Crusader's Rest", "Increases Spell Healing Power, Attack Damage and Max Health.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register() {
        ORANGE_JUICE.applyAttributes(
                modifier(SpellPowerMechanics.HASTE.attributeEntry, effectsConfig.value.drinks_haste_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        SWEET_CHILLI.applyAttributes(
                modifier(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        FRUIT_ICEWATER.applyAttributes(
                modifier(SpellSchools.FROST.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        CHORUS_EXTRACT.applyAttributes(
                modifier(SpellSchools.ARCANE.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        HOT_CHILLI.applyAttributes(
                modifier(SpellSchools.FIRE.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        HOLY_WATER.applyAttributes(
                modifier(SpellSchools.HEALING.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        ENCHANTED_ALE.applyAttributes(
                modifier(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, effectsConfig.value.drinks_crit_rate_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        WIZARDS_ELIXIR.applyAttributes(
                modifier(SpellSchools.GENERIC.attributeEntry, effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(SpellPowerMechanics.HASTE.attributeEntry, effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        MERLINS_FLASK.applyAttributes(
                modifier(SpellSchools.GENERIC.attributeEntry, effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(SpellPowerMechanics.HASTE.attributeEntry, effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, effectsConfig.value.drinks_crit_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        CRUSADERS_REST.applyAttributes(
                modifier(SpellSchools.HEALING.attributeEntry, effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(EntityAttributes.GENERIC_MAX_HEALTH, effectsConfig.value.drinks_health_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        CompatEffectEntry.registerAll(entries);
    }

    public static void applyMonarchsFrostStaffPower() {
        LNEEvents.get().modifyItemComponents(context ->
                context.modify(CommonItems.MONARCHS_FROST_STAFF.item(), builder -> builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS,
                        AttributeModifiersComponent.builder()
                                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 4.0F, EntityAttributeModifier.Operation.ADD_VALUE),
                                        AttributeModifierSlot.MAINHAND)
                                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -3.0F, EntityAttributeModifier.Operation.ADD_VALUE),
                                        AttributeModifierSlot.MAINHAND)
                                .add(SpellSchools.FROST.attributeEntry,
                                        new EntityAttributeModifier(Identifier.of(MOD_ID, "monarchs_frost_staff_power"), 8.0F, EntityAttributeModifier.Operation.ADD_VALUE),
                                        AttributeModifierSlot.MAINHAND)
                                .build())));
    }
}

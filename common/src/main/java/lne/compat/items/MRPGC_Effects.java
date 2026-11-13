package lne.compat.items;

import lne.effects.CustomStatusEffect;
import lne.effects.SpecialStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import com.mrpg_lib.compat.spell_power.MoreSpellSchools;
import com.mrpg_lib.entity.attribute.MRPGCEntityAttributes;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static lne.LneMod.effectsConfig;
import static lne.config.EffectAttributeConfigs.modifier;

public class MRPGC_Effects {
    private static final List<CompatEffectEntry> entries = new ArrayList<>();

    public static List<CompatEffectEntry> getEntries() {
        return entries;
    }

    private static CompatEffectEntry entry(String name, String title, String description, StatusEffect effect) {
        return CompatEffectEntry.add(entries, name, title, description, effect);
    }

    public static final CompatEffectEntry WATERMELON_DRINK = entry("watermelon_drink",
            "Watermelon Drink", "Increases Water Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry BLUE_BERRY_PUNCH = entry("blue_berry_punch",
            "Aether Blueberry Punch", "Increases Air Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry GREEN_CHILLI = entry("green_chilli",
            "Green Chilli", "Increases Earth Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry HONEY_MET = entry("honey_met",
            "Honey Met", "Increases Rage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry CACTUS_JUICE = entry("cactus_juice",
            "Cactus Juice", "Increases Damage Reflect.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry MOSSFLOWER_TEA = entry("mossflower_tea",
            "Mossflower Tea", "Increases Nature Spell Power",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry DETTLAFFS_BLOOD = entry("dettlaffs_blood",
            "Dettlaff's Blood", "Increases Attack Damage and Melee Lifesteal.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry SCARLET_ESSENCE = entry("scarlet_essence",
            "Essence of the Scarlet Runes", "Increases Spell Power and Spell Vampire.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final CompatEffectEntry SVABLODS_BREW = entry("svablods_brew",
            "Svablod's Brew", "Increases Rage, Attack Damage and Attack Speed.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register() {
        WATERMELON_DRINK.applyAttributes(
                modifier(MoreSpellSchools.WATER.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        BLUE_BERRY_PUNCH.applyAttributes(
                modifier(MoreSpellSchools.AIR.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        GREEN_CHILLI.applyAttributes(
                modifier(MoreSpellSchools.EARTH.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        HONEY_MET.applyAttributes(
                modifier(MRPGCEntityAttributes.RAGE_MODIFIER, effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        CACTUS_JUICE.applyAttributes(
                modifier(MRPGCEntityAttributes.DAMAGE_REFLECT_MODIFIER, effectsConfig.value.drinks_reflect_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        MOSSFLOWER_TEA.applyAttributes(
                modifier(MoreSpellSchools.NATURE.attributeEntry, effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        DETTLAFFS_BLOOD.applyAttributes(
                modifier(MRPGCEntityAttributes.LIFESTEAL_MODIFIER, effectsConfig.value.drinks_lifesteal_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        SCARLET_ESSENCE.applyAttributes(
                modifier(MRPGCEntityAttributes.SPELL_VAMPIRE, effectsConfig.value.drinks_lifesteal_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(SpellSchools.GENERIC.attributeEntry, effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        SVABLODS_BREW.applyAttributes(
                modifier(EntityAttributes.GENERIC_ATTACK_SPEED, effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                modifier(MRPGCEntityAttributes.RAGE_MODIFIER, effectsConfig.value.drinks_special_attribute_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));

        CompatEffectEntry.registerAll(entries);
    }
}

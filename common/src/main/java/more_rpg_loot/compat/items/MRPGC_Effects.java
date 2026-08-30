package more_rpg_loot.compat.items;

import more_rpg_loot.effects.CustomStatusEffect;
import more_rpg_loot.effects.SpecialStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.effectsConfig;

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
        WATERMELON_DRINK.effect.addAttributeModifier(
                MoreSpellSchools.WATER.attributeEntry, WATERMELON_DRINK.modifierId(),
                effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        BLUE_BERRY_PUNCH.effect.addAttributeModifier(
                MoreSpellSchools.AIR.attributeEntry, BLUE_BERRY_PUNCH.modifierId(),
                effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        GREEN_CHILLI.effect.addAttributeModifier(
                MoreSpellSchools.EARTH.attributeEntry, GREEN_CHILLI.modifierId(),
                effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        HONEY_MET.effect.addAttributeModifier(
                MRPGCEntityAttributes.RAGE_MODIFIER, HONEY_MET.modifierId(),
                effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        CACTUS_JUICE.effect.addAttributeModifier(
                MRPGCEntityAttributes.DAMAGE_REFLECT_MODIFIER, CACTUS_JUICE.modifierId(),
                effectsConfig.value.drinks_reflect_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        MOSSFLOWER_TEA.effect.addAttributeModifier(
                MoreSpellSchools.NATURE.attributeEntry, MOSSFLOWER_TEA.modifierId(),
                effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        DETTLAFFS_BLOOD.effect.addAttributeModifier(
                        MRPGCEntityAttributes.LIFESTEAL_MODIFIER, DETTLAFFS_BLOOD.modifierId(),
                        effectsConfig.value.drinks_lifesteal_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, DETTLAFFS_BLOOD.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SCARLET_ESSENCE.effect.addAttributeModifier(
                        MRPGCEntityAttributes.SPELL_VAMPIRE, SCARLET_ESSENCE.modifierId(),
                        effectsConfig.value.drinks_lifesteal_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        SpellSchools.GENERIC.attributeEntry, SCARLET_ESSENCE.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SVABLODS_BREW.effect.addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, SVABLODS_BREW.modifierId(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, SVABLODS_BREW.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        MRPGCEntityAttributes.RAGE_MODIFIER, SVABLODS_BREW.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        CompatEffectEntry.registerAll("More RPG Library", entries);
    }
}

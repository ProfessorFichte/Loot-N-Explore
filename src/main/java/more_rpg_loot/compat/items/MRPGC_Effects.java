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
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.entity.attribute.MRPGCEntityAttributes;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class MRPGC_Effects {
    private static final ArrayList<MRPGCEntry> entries = new ArrayList<MRPGCEntry>();

    // Entry class for More RPG Classes compatible effects with datagen support
    public static class MRPGCEntry {
        public final Identifier id;
        public final String title;
        public final String description;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public MRPGCEntry(String name, String title, String description, StatusEffect effect) {
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

    public static ArrayList<MRPGCEntry> getEntries() {
        return entries;
    }

    /// T1 BUFF EFFECTS
    public static final MRPGCEntry WATERMELON_DRINK =  new MRPGCEntry("watermelon_drink",
            "Watermelon Drink",
            "Increases Water Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final MRPGCEntry BLUE_BERRY_PUNCH =  new MRPGCEntry("blue_berry_punch",
            "Aether Blueberry Punch",
            "Increases Air Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final MRPGCEntry GREEN_CHILLI =  new MRPGCEntry("green_chilli",
            "Green Chilli",
            "Increases Earth Spell Power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final MRPGCEntry HONEY_MET =  new MRPGCEntry("honey_met",
            "Honey Met",
            "Increases Rage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final MRPGCEntry CACTUS_JUICE =  new MRPGCEntry("cactus_juice",
            "Cactus Juice",
            "Increases Damage Reflect.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final MRPGCEntry DETTLAFFS_BLOOD =  new MRPGCEntry("dettlaffs_blood",
            "Dettlaff's Blood",
            "Increases Attack Damage and Melee Lifesteal.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final MRPGCEntry SCARLET_ESSENCE =  new MRPGCEntry("scarlet_essence",
            "Essence of the Scarlet Runes",
            "Increases Spell Power and Spell Vampire.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final MRPGCEntry SVABLODS_BREW =  new MRPGCEntry("svablods_brew",
            "Svablod's Brew",
            "Increases Rage, Attack Damage and Attack Speed.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));


    public static void register(){
        RPGLoot.LOGGER.info("Registering More RPG Library Compat Effects for " + MOD_ID);
        /// T1 BUFF EFFECTS
        WATERMELON_DRINK.effect
                .addAttributeModifier(
                        MoreSpellSchools.WATER.attributeEntry, WATERMELON_DRINK.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        BLUE_BERRY_PUNCH.effect
                .addAttributeModifier(
                        MoreSpellSchools.AIR.attributeEntry, BLUE_BERRY_PUNCH.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        GREEN_CHILLI.effect
                .addAttributeModifier(
                        MoreSpellSchools.EARTH.attributeEntry, GREEN_CHILLI.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        HONEY_MET.effect
                .addAttributeModifier(
                        MRPGCEntityAttributes.RAGE_MODIFIER, HONEY_MET.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        CACTUS_JUICE.effect
                .addAttributeModifier(
                        MRPGCEntityAttributes.DAMAGE_REFLECT_MODIFIER, CACTUS_JUICE.modifierId(),
                        effectsConfig.value.drinks_reflect_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T3 BUFF EFFECTS
        DETTLAFFS_BLOOD.effect
                .addAttributeModifier(
                        MRPGCEntityAttributes.LIFESTEAL_MODIFIER, DETTLAFFS_BLOOD.modifierId(),
                        effectsConfig.value.drinks_lifesteal_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, DETTLAFFS_BLOOD.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SCARLET_ESSENCE.effect
                .addAttributeModifier(
                        MRPGCEntityAttributes.SPELL_VAMPIRE, SCARLET_ESSENCE.modifierId(),
                        effectsConfig.value.drinks_lifesteal_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        SpellSchools.GENERIC.attributeEntry, SCARLET_ESSENCE.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SVABLODS_BREW.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, SVABLODS_BREW.modifierId(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, SVABLODS_BREW.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        MRPGCEntityAttributes.RAGE_MODIFIER, SVABLODS_BREW.modifierId(),
                        effectsConfig.value.drinks_special_attribute_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        for (MRPGCEntry entry: entries) {
            entry.register();
        }
    }
}

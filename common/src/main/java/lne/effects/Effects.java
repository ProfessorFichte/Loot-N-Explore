package lne.effects;
import lne.platform.LNEPlatform;

import com.mrpg_lib.config.AttributeModifier;
import com.mrpg_lib.config.EffectConfig;
import lne.config.EffectAttributeConfigs;
import lne.config.EffectsConfig;
import lne.effects.frozen_depths.FrostResistanceEffect;
import lne.effects.generic.EnderDragonScalesEffect;
import lne.effects.generic.RainbowCoralEffect;
import lne.effects.generic.WithersCurseEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static lne.LneMod.MOD_ID;
import static lne.LneMod.effectsConfig;
import static lne.config.EffectAttributeConfigs.modifier;

public class Effects {
    private static final ArrayList<Entry> entries = new ArrayList<Entry>();

    public static class Entry {
        public final Identifier id;
        public final String title;
        public final String description;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, String title, String description, StatusEffect effect) {
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

        public void applyAttributes(@Nullable Map<String, EffectConfig> configs, AttributeModifier... defaults) {
            EffectAttributeConfigs.apply(configs, id, modifierId(), effect, List.of(defaults));
        }
    }

    private static final String SPELL_ENGINE_DAMAGE_TAKEN = "spell_engine:damage_taken";

    public static ArrayList<Entry> getEntries() {
        return entries;
    }

    public static final Effects.Entry FROST_RESISTANCE =  new Effects.Entry("frost_resistance",
            "Frost Resistance",
            "Removes all Frozen Ticks as long as this effect is active.",
            new FrostResistanceEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff));
    public static final Effects.Entry FREEZING =  new Effects.Entry("freezing",
            "Freezing",
            "Adds Frozenticks every tick.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff));
    public static final Effects.Entry ELDER_GUARDIANS_CURSE =  new Effects.Entry("elder_guardians_curse",
            "Elder Guardians Curse",
            "Lowers Attack Damage.",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff));
    public static final Effects.Entry ENDER_DRAGON_SCALES =  new Effects.Entry("ender_dragon_scales",
            "Ender Dragon's Regeneration",
            "Regenerates Health and reduces incoming damage.",
            new EnderDragonScalesEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff));
    public static final Effects.Entry WITHERS_CURSE =  new Effects.Entry("withers_curse",
            "Wither's Curse",
            "Increases Incoming Damage, the amplifier increases with each harmful status effect.",
            new WithersCurseEffect(StatusEffectCategory.HARMFUL, 0x2a1b01));
    public static final Effects.Entry RAINBOW_CORAL =  new Effects.Entry("rainbow_coral",
            "Rainbow Coral",
            "Grants absorption hearts worth a share of your max health. The absorption is removed when the effect ends.",
            new RainbowCoralEffect(StatusEffectCategory.BENEFICIAL, 0xff7fd4));
    public static final int RENDING_FANG_MAX_STACKS = 10;
    public static final Effects.Entry RENDING_FANG =  new Effects.Entry("rending_fang",
            "Rending Fang",
            "Increases Attack Damage for every stack.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xB050FF));
    public static final Effects.Entry HOT_CHOCOLATE =  new Effects.Entry("hot_chocolate",
            "Hot Chocolate",
            "Increases Attack Speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry POTATO_SOUP =  new Effects.Entry("potato_soup",
            "Potato Soup",
            "Increases Attack Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry SWEET_BERRY_PUNCH =  new Effects.Entry("sweet_berry_punch",
            "Sweet Berry Punch",
            "Increases Armor Toughness.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry BEET_ROOTBEER =  new Effects.Entry("beet_rootbeer",
            "Beet Rootbeer",
            "Increases Max Health.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry MALT_EXTRACT =  new Effects.Entry("malt_extract",
            "Malt Extract",
            "Increases Attack Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry VITAL_DRINK =  new Effects.Entry("vital_drink",
            "Vital Drink",
            "Increases Armor Toughness and Max Health.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry ESPRESSO =  new Effects.Entry("espresso",
            "Strong Espresso",
            "Increases Attack Speed and Movement Speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry KNIGHTS_FAVOURITE =  new Effects.Entry("knights_favourite",
            "Knight's Favourite Stew",
            "Increases Attack Damage, Attack Speed and Knockback Resistance.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry THE_UNSHAKABLE =  new Effects.Entry("the_unshakable",
            "The Unshakable",
            "Increasing Knockback Resistance, Armor Toughness and Max Health.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        EffectsConfig cfg = effectsConfig != null ? effectsConfig.value : new EffectsConfig();
        var curseConfigs = effectsConfig != null ? effectsConfig.value.curse_effects : null;
        ELDER_GUARDIANS_CURSE.applyAttributes(curseConfigs,
                modifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, -0.25F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        ENDER_DRAGON_SCALES.applyAttributes(curseConfigs,
                modifier(SPELL_ENGINE_DAMAGE_TAKEN, -0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        WITHERS_CURSE.applyAttributes(curseConfigs,
                modifier(SPELL_ENGINE_DAMAGE_TAKEN, 0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        RENDING_FANG.effect
                .addAttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE, RENDING_FANG.modifierId(),
                    0.01, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        HOT_CHOCOLATE.effect
                .addAttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_SPEED, HOT_CHOCOLATE.modifierId(),
                    cfg.drinks_haste_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        POTATO_SOUP.effect
                .addAttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE, POTATO_SOUP.modifierId(),
                    cfg.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SWEET_BERRY_PUNCH.effect
                .addAttributeModifier(
                    EntityAttributes.GENERIC_ARMOR_TOUGHNESS, SWEET_BERRY_PUNCH.modifierId(),
                    cfg.drinks_armor_toughness_t0_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        BEET_ROOTBEER.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, BEET_ROOTBEER.modifierId(),
                        cfg.drinks_health_t1_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        MALT_EXTRACT.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, MALT_EXTRACT.modifierId(),
                        cfg.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        VITAL_DRINK.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR_TOUGHNESS, VITAL_DRINK.modifierId(),
                        cfg.drinks_armor_toughness_t2_boost, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, VITAL_DRINK.modifierId(),
                        cfg.drinks_health_t2_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        ESPRESSO.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, ESPRESSO.modifierId(),
                        cfg.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED, ESPRESSO.modifierId(),
                        cfg.drinks_speed_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        KNIGHTS_FAVOURITE.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, KNIGHTS_FAVOURITE.modifierId(),
                        cfg.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, KNIGHTS_FAVOURITE.modifierId(),
                        cfg.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNIGHTS_FAVOURITE.modifierId(),
                        cfg.drinks_knockback_resistance_t3_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        THE_UNSHAKABLE.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, THE_UNSHAKABLE.modifierId(),
                        cfg.drinks_knockback_resistance_t3_boost, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR_TOUGHNESS, THE_UNSHAKABLE.modifierId(),
                        cfg.drinks_armor_toughness_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, THE_UNSHAKABLE.modifierId(),
                        cfg.drinks_health_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);



        if (LNEPlatform.isModLoaded("spell_engine")) {
            Synchronized.configure(FROST_RESISTANCE.effect,true);
            Synchronized.configure(FREEZING.effect,true);
            Synchronized.configure(ELDER_GUARDIANS_CURSE.effect,true);
            Synchronized.configure(ENDER_DRAGON_SCALES.effect,true);
        }

        for (Entry entry: entries) {
            entry.register();
        }
    }
}

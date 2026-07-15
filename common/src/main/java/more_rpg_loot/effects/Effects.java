package more_rpg_loot.effects;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.config.EffectsConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;
import net.spell_engine.api.entity.SpellEngineAttributes;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

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
    }

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
    /// T0 BUFF EFFECTS
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
    /// T1 BUFF EFFECTS
    public static final Effects.Entry BEET_ROOTBEER =  new Effects.Entry("beet_rootbeer",
            "Beet Rootbeer",
            "Increases Max Health.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry MALT_EXTRACT =  new Effects.Entry("malt_extract",
            "Malt Extract",
            "Increases Attack Damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final Effects.Entry VITAL_DRINK =  new Effects.Entry("vital_drink",
            "Vital Drink",
            "Increases Armor Toughness and Max Health.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry ESPRESSO =  new Effects.Entry("espresso",
            "Strong Espresso",
            "Increases Attack Speed and Movement Speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final Effects.Entry KNIGHTS_FAVOURITE =  new Effects.Entry("knights_favourite",
            "Knight's Favourite Stew",
            "Increases Attack Damage, Attack Speed and Knockback Resistance.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry THE_UNSHAKABLE =  new Effects.Entry("the_unshakable",
            "The Unshakable",
            "Increasing Knockback Resistance, Armor Toughness and Max Health.",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Status Effects for " + MOD_ID);
        EffectsConfig cfg;
        try {
            cfg = effectsConfig != null ? effectsConfig.value : new EffectsConfig();
        } catch (NoClassDefFoundError e) {
            cfg = new EffectsConfig();
        }
        ELDER_GUARDIANS_CURSE.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, ELDER_GUARDIANS_CURSE.modifierId(),
                        -0.25, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            ENDER_DRAGON_SCALES.effect
                    .addAttributeModifier(
                    SpellEngineAttributes.DAMAGE_TAKEN.entry, ENDER_DRAGON_SCALES.modifierId(),
                    -0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            WITHERS_CURSE.effect
                    .addAttributeModifier(
                            SpellEngineAttributes.DAMAGE_TAKEN.entry, WITHERS_CURSE.modifierId(),
                            0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }
        /// T0 BUFF EFFECTS
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
        /// T1 BUFF EFFECTS
        BEET_ROOTBEER.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, BEET_ROOTBEER.modifierId(),
                        cfg.drinks_health_t1_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        MALT_EXTRACT.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, MALT_EXTRACT.modifierId(),
                        cfg.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T2 BUFF EFFECTS
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
        /// T3 BUFF EFFECTS
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



        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
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

package more_rpg_loot.effects;

import more_rpg_loot.RPGLoot;
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
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
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

    public static final Effects.Entry FROST_RESISTANCE =  new Effects.Entry("frost_resistance",
            new FrostResistanceEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff));
    public static final Effects.Entry FREEZING =  new Effects.Entry("freezing",
            new FreezingEffect(StatusEffectCategory.HARMFUL, 0x99ccff));
    public static final Effects.Entry ELDER_GUARDIANS_CURSE =  new Effects.Entry("elder_guardians_curse",
            new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x99ccff));
    public static final Effects.Entry ENDER_DRAGON_SCALES =  new Effects.Entry("ender_dragon_scales",
            new EnderDragonScalesEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff));
    public static final Effects.Entry WITHERS_CURSE =  new Effects.Entry("withers_curse",
            new WithersCurseEffect(StatusEffectCategory.HARMFUL, 0x2a1b01));
    /// T0 BUFF EFFECTS
    public static final Effects.Entry HOT_CHOCOLATE =  new Effects.Entry("hot_chocolate",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry POTATO_SOUP =  new Effects.Entry("potato_soup",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry SWEET_BERRY_PUNCH =  new Effects.Entry("sweet_berry_punch",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T1 BUFF EFFECTS
    public static final Effects.Entry BEET_ROOTBEER =  new Effects.Entry("beet_rootbeer",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry MALT_EXTRACT =  new Effects.Entry("malt_extract",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T2 BUFF EFFECTS
    public static final Effects.Entry VITAL_DRINK =  new Effects.Entry("vital_drink",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry ESPRESSO =  new Effects.Entry("espresso",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    /// T3 BUFF EFFECTS
    public static final Effects.Entry KNIGHTS_FAVOURITE =  new Effects.Entry("knights_favourite",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry THE_UNSHAKABLE =  new Effects.Entry("the_unshakable",
            new SpecialStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Status Effects for " + MOD_ID);
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
                    effectsConfig.value.drinks_haste_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        POTATO_SOUP.effect
                .addAttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE, POTATO_SOUP.modifierId(),
                    effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        SWEET_BERRY_PUNCH.effect
                .addAttributeModifier(
                    EntityAttributes.GENERIC_ARMOR_TOUGHNESS, SWEET_BERRY_PUNCH.modifierId(),
                    effectsConfig.value.drinks_armor_toughness_t0_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        /// T1 BUFF EFFECTS
        BEET_ROOTBEER.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, BEET_ROOTBEER.modifierId(),
                        effectsConfig.value.drinks_health_t1_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        MALT_EXTRACT.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, MALT_EXTRACT.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T2 BUFF EFFECTS
        VITAL_DRINK.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR_TOUGHNESS, VITAL_DRINK.modifierId(),
                        effectsConfig.value.drinks_armor_toughness_t2_boost, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, VITAL_DRINK.modifierId(),
                        effectsConfig.value.drinks_health_t2_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        ESPRESSO.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, ESPRESSO.modifierId(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED, ESPRESSO.modifierId(),
                        effectsConfig.value.drinks_speed_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        /// T3 BUFF EFFECTS
        KNIGHTS_FAVOURITE.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, KNIGHTS_FAVOURITE.modifierId(),
                        effectsConfig.value.drinks_haste_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, KNIGHTS_FAVOURITE.modifierId(),
                        effectsConfig.value.drinks_damage_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNIGHTS_FAVOURITE.modifierId(),
                        effectsConfig.value.drinks_knockback_resistance_t3_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        THE_UNSHAKABLE.effect
                .addAttributeModifier(
                        EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, THE_UNSHAKABLE.modifierId(),
                        effectsConfig.value.drinks_knockback_resistance_t3_boost, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR_TOUGHNESS, THE_UNSHAKABLE.modifierId(),
                        effectsConfig.value.drinks_armor_toughness_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, THE_UNSHAKABLE.modifierId(),
                        effectsConfig.value.drinks_health_t3_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);



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

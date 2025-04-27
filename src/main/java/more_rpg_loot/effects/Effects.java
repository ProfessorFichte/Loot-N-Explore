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

    public static final Effects.Entry HOT_CHOCOLATE =  new Effects.Entry("hot_chocolate",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry POTATO_SOUP =  new Effects.Entry("potato_soup",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry SWEET_BERRY_PUNCH =  new Effects.Entry("sweet_berry_punch",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry BEET_ROOTBEER =  new Effects.Entry("beet_rootbeer",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry MALT_EXTRACT =  new Effects.Entry("malt_extract",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry VITAL_DRINK =  new Effects.Entry("vital_drink",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry ESPRESSO =  new Effects.Entry("espresso",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Status Effects for " + MOD_ID);
        HOT_CHOCOLATE.effect.addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_SPEED, HOT_CHOCOLATE.modifierId(),
                effectsConfig.value.drinks_haste_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        POTATO_SOUP.effect.addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, POTATO_SOUP.modifierId(),
                effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        SWEET_BERRY_PUNCH.effect.addAttributeModifier(
                EntityAttributes.GENERIC_ARMOR, SWEET_BERRY_PUNCH.modifierId(),
                effectsConfig.value.drinks_armor_t0_boost, EntityAttributeModifier.Operation.ADD_VALUE);

        BEET_ROOTBEER.effect.addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, BEET_ROOTBEER.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR, BEET_ROOTBEER.modifierId(),
                        effectsConfig.value.drinks_armor_t1_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        MALT_EXTRACT.effect.addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_SPEED, MALT_EXTRACT.modifierId(),
                        effectsConfig.value.drinks_haste_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, MALT_EXTRACT.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        VITAL_DRINK.effect.addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, VITAL_DRINK.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ARMOR, VITAL_DRINK.modifierId(),
                        effectsConfig.value.drinks_armor_t2_boost, EntityAttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MAX_HEALTH, VITAL_DRINK.modifierId(),
                        effectsConfig.value.drinks_health_t2_boost, EntityAttributeModifier.Operation.ADD_VALUE);
        ESPRESSO.effect.addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, ESPRESSO.modifierId(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, ESPRESSO.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED, ESPRESSO.modifierId(),
                        effectsConfig.value.drinks_speed_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);



        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            Synchronized.configure(FROST_RESISTANCE.effect,true);
            Synchronized.configure(FREEZING.effect,true);
        }

        for (Entry entry: entries) {
            entry.register();
        }
    }
}

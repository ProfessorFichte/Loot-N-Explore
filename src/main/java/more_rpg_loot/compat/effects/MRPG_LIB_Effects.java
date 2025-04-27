package more_rpg_loot.compat.effects;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.effects.CustomStatusEffect;
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
import net.spell_power.api.SpellPowerMechanics;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class MRPG_LIB_Effects {
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

    public static final Entry WATERMELON_DRINK =  new Entry("watermelon_drink",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry BLUE_BERRY_PUNCH =  new Entry("blue_berry_punch",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry GREEN_CHILLI =  new Entry("green_chilli",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry HONEY_MET =  new Entry("honey_met",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering More RPG Library Compat Effects for " + MOD_ID);
        WATERMELON_DRINK.effect.addAttributeModifier(
                        SpellPowerMechanics.HASTE.attributeEntry, WATERMELON_DRINK.modifierId(),
                        effectsConfig.value.drinks_haste_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        MoreSpellSchools.WATER.attributeEntry, WATERMELON_DRINK.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        BLUE_BERRY_PUNCH.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, BLUE_BERRY_PUNCH.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        MoreSpellSchools.AIR.attributeEntry, BLUE_BERRY_PUNCH.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        GREEN_CHILLI.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, GREEN_CHILLI.modifierId(),
                        effectsConfig.value.drinks_crit_rate_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        MoreSpellSchools.EARTH.attributeEntry, GREEN_CHILLI.modifierId(),
                        effectsConfig.value.drinks_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        HONEY_MET.effect.addAttributeModifier(
                        MRPGCEntityAttributes.RAGE_MODIFIER, HONEY_MET.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_SPEED, HONEY_MET.modifierId(),
                        effectsConfig.value.drinks_haste_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE, HONEY_MET.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


        for (Entry entry: entries) {
            entry.register();
        }
    }
}

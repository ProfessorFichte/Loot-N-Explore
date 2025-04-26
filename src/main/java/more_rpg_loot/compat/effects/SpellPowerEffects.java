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
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import net.witcher_rpg.WitcherClassMod;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class SpellPowerEffects {
    private static final ArrayList<Entry> entries = new ArrayList<Entry>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(WitcherClassMod.MOD_ID, name);
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

    public static final Entry ORANGE_JUICE =  new Entry("orange_juice",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry SWEET_CHILLI =  new Entry("sweet_chilli",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry FRUIT_ICEWATER =  new Entry("fruit_icewater",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry CHORUS_EXTRACT =  new Entry("chorus_extract",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry HOT_CHILLI =  new Entry("hot_chilli",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry HOLY_WATER =  new Entry("holy_water",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Entry ENCHANTED_ALE =  new Entry("enchanted_ale",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry WIZARDS_ELIXIR =  new Entry("wizards_elixir",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));


    public static void register(){
        RPGLoot.LOGGER.info("Registering Spell Power Compat Effects for " + MOD_ID);
        ORANGE_JUICE.effect.addAttributeModifier(
                SpellPowerMechanics.HASTE.attributeEntry, ORANGE_JUICE.modifierId(),
                0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        SWEET_CHILLI.effect.addAttributeModifier(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, SWEET_CHILLI.modifierId(),
                0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellSchools.HEALING.attributeEntry, SWEET_CHILLI.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        FRUIT_ICEWATER.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, FRUIT_ICEWATER.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellSchools.FROST.attributeEntry, FRUIT_ICEWATER.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        CHORUS_EXTRACT.effect.addAttributeModifier(
                        SpellPowerMechanics.HASTE.attributeEntry, CHORUS_EXTRACT.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellSchools.ARCANE.attributeEntry, CHORUS_EXTRACT.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        HOT_CHILLI.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, HOT_CHILLI.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellSchools.FIRE.attributeEntry, HOT_CHILLI.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        HOLY_WATER.effect.addAttributeModifier(
                        SpellPowerMechanics.HASTE.attributeEntry, HOLY_WATER.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellSchools.HEALING.attributeEntry, HOLY_WATER.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        ENCHANTED_ALE.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, ENCHANTED_ALE.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, ENCHANTED_ALE.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        WIZARDS_ELIXIR.effect.addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, WIZARDS_ELIXIR.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellPowerMechanics.CRITICAL_DAMAGE.attributeEntry, WIZARDS_ELIXIR.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        SpellSchools.GENERIC.attributeEntry, WIZARDS_ELIXIR.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        for (SpellPowerEffects.Entry entry: entries) {
            entry.register();
        }
    }
}

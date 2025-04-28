package more_rpg_loot.compat.effects;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.effects.CustomStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.entity.attribute.WitcherAttributes;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.effectsConfig;

public class Witcher_Effects {
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

    public static final Entry BEAUCLAIR_WHITE =  new Entry("beauclair_white",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry RIVIAN_KRIEK =  new Entry("rivian_kriek",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static final Entry BUTCHER_OF_BLAVIKEN =  new Entry("butcher_of_blaviken",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));

    public static void register(){
        RPGLoot.LOGGER.info("Registering Witcher Compat Effects for " + MOD_ID);
        BEAUCLAIR_WHITE.effect
                .addAttributeModifier(
                    WitcherAttributes.SIGN_INTENSITY, BEAUCLAIR_WHITE.modifierId(),
                    effectsConfig.value.drinks_damage_t0_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        RIVIAN_KRIEK.effect
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, RIVIAN_KRIEK.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t1_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        BUTCHER_OF_BLAVIKEN.effect
                .addAttributeModifier(
                        WitcherAttributes.SIGN_INTENSITY, BUTCHER_OF_BLAVIKEN.modifierId(),
                        effectsConfig.value.drinks_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(
                        WitcherAttributes.ADRENALINE_MODIFIER, BUTCHER_OF_BLAVIKEN.modifierId(),
                        effectsConfig.value.drinks_crit_damage_t2_boost, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


        for (Entry entry: entries) {
            entry.register();
        }
    }
}

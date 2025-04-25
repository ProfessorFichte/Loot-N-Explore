package more_rpg_loot.effects;

import more_rpg_loot.RPGLoot;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;
import net.witcher_rpg.WitcherClassMod;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Effects {
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
            return Identifier.of(WitcherClassMod.MOD_ID, "effect." + id.getPath());
        }
    }

    public static final Effects.Entry INNKEEPERS_PROVIANT =  new Effects.Entry("innkeepers_proviant",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final Effects.Entry FROST_RESISTANCE =  new Effects.Entry("frost_resistance",
            new FrostResistanceEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff));
    public static final Effects.Entry FREEZING =  new Effects.Entry("freezing",
            new FreezingEffect(StatusEffectCategory.HARMFUL, 0x99ccff));


    public static void register(){
        RPGLoot.LOGGER.info("Registering Status Effects for " + MOD_ID);
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            Synchronized.configure(INNKEEPERS_PROVIANT.effect,true);
            Synchronized.configure(FROST_RESISTANCE.effect,true);
            Synchronized.configure(FREEZING.effect,true);
        }

        for (Entry entry: entries) {
            entry.register();
        }
    }
}

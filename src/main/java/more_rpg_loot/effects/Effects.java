package more_rpg_loot.effects;

import more_rpg_loot.RPGLoot;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Effects {
    public static StatusEffect INNKEEPERS_PROVIANT = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff);
    public static StatusEffect FROST_RESISTANCE= new FrostResistanceEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff);
    public static StatusEffect FREEZING = new FreezingEffect(StatusEffectCategory.HARMFUL, 0x99ccff);


    public static void register(){
        RPGLoot.LOGGER.info("Registering Status Effects for " + MOD_ID);
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            Synchronized.configure(INNKEEPERS_PROVIANT,true);
            Synchronized.configure(FROST_RESISTANCE,true);
            Synchronized.configure(FREEZING,true);
        }


        int lne_effectid = 20000;
        Registry.register(Registries.STATUS_EFFECT, lne_effectid++, new Identifier(MOD_ID, "innkeepers_proviant").toString(), INNKEEPERS_PROVIANT);
        Registry.register(Registries.STATUS_EFFECT, lne_effectid++, new Identifier(MOD_ID, "frost_resistance").toString(), FROST_RESISTANCE);
        Registry.register(Registries.STATUS_EFFECT, lne_effectid++, new Identifier(MOD_ID, "freezing").toString(), FREEZING);
    }
}

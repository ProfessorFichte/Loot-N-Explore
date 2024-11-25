package more_rpg_loot.compat;

import more_rpg_loot.compat.items.MRPGCItems;
import more_rpg_loot.compat.items.RangedWeaponAPIItems;
import more_rpg_loot.compat.items.SpellPowerItems;
import more_rpg_loot.compat.spell_engine.RPGSeriesLootInjectionLNE;
import net.fabricmc.loader.api.FabricLoader;

public class CompatRegistry {

    public static void registerModCompat(){
        if(FabricLoader.getInstance().isModLoaded("spell_power")){
            SpellPowerItems.registerSpellPowerItems();
        }
        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            MRPGCItems.registerMRPGCItems();
        }
        if(FabricLoader.getInstance().isModLoaded("ranged_weapon_api")){
            RangedWeaponAPIItems.registerRangedWeaponAPIItems();
        }
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            RPGSeriesLootInjectionLNE.initialize();
        }
    }
}

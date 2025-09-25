package more_rpg_loot.compat;

import more_rpg_loot.compat.items.*;
import more_rpg_loot.compat.spell_engine.SpellEngine_LNE;
import more_rpg_loot.compat.spell_engine.trinket_compat.TrinketsCompat;
import net.fabricmc.loader.api.FabricLoader;


public class CompatRegistry {


    public static void registerModCompat(){
        TrinketsCompat.init();
        if(FabricLoader.getInstance().isModLoaded("spell_power")){
            SpellPower_Effects.register();
            SpellPower_Items.registerSpellPowerItems();
        }
        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            MRPGC_Effects.register();
            MRPGC_Items.registerMRPGCItems();
        }
        if(FabricLoader.getInstance().isModLoaded("ranged_weapon_api")){
            RWA_Effects.register();
            RWA_Items.registerRangedWeaponAPIItems();
        }
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            SpellEngine_LNE.initialize();
        }
        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
            Witcher_Effects.register();
            Witcher_Items.registerWitcherItems();
        }
    }
}

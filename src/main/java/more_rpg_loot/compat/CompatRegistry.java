package more_rpg_loot.compat;

import more_rpg_loot.compat.items.*;
import more_rpg_loot.compat.spell_engine.SpellEngine_LNE;
import more_rpg_loot.compat.spell_engine.trinket_compat.TrinketsCompat;
import net.fabricmc.loader.api.FabricLoader;


public class CompatRegistry {
    public static void registerModCompat(){
        TrinketsCompat.init();

        // Register effects for loaded mods
        if(FabricLoader.getInstance().isModLoaded("spell_power")){
            SpellPower_Effects.register();
        }
        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            MRPGC_Effects.register();
        }
        if(FabricLoader.getInstance().isModLoaded("ranged_weapon_api")){
            RWA_Effects.register();
        }
        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
            Witcher_Effects.register();
        }

        // Register all compatibility items (automatically checks which mods are loaded)
        CompatItems.registerAll();

        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            SpellEngine_LNE.initialize();
        }
    }
    public static void registerCompatItems(){

    }
    public static void registerCompatEffects(){

    }
}

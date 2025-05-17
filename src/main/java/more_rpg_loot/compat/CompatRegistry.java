package more_rpg_loot.compat;

import more_rpg_loot.compat.items.MRPGC_Effects;
import more_rpg_loot.compat.items.RWA_Effects;
import more_rpg_loot.compat.items.SpellPower_Effects;
import more_rpg_loot.compat.items.Witcher_Effects;
import more_rpg_loot.compat.items.MRPGC_Items;
import more_rpg_loot.compat.items.RWA_Items;
import more_rpg_loot.compat.items.SpellPower_Items;
import more_rpg_loot.compat.items.Witcher_Items;
import more_rpg_loot.compat.spell_engine.SpellEngine_LNE;
import net.fabricmc.loader.api.FabricLoader;

public class CompatRegistry {


    public static void registerModCompat(){
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

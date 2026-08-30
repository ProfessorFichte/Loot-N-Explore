package more_rpg_loot.compat.spell_engine.trinket_compat;
import more_rpg_loot.platform.LNEPlatform;


public class TrinketsCompat {
    public static void init() {
        if (LNEPlatform.isModLoaded("trinkets")) {
            TrinketsHelper.register();
        }
    }
}

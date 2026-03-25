package more_rpg_loot.compat.spell_engine.trinket_compat;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketsCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets") && FabricLoader.getInstance().isModLoaded("spell_engine")) {
            TrinketsHelper.register();
        }
    }
}

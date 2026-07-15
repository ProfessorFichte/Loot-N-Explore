package more_rpg_loot.compat.accessories;

import net.fabricmc.loader.api.FabricLoader;

public class AccessoriesCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            AccessoriesHelper.registerFactory();
        }
    }
}

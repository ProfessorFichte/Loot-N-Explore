package com.lne.fabric.compat;

import more_rpg_loot.compat.accessories.AccessoriesCompat;
import more_rpg_loot.compat.spell_engine.trinket_compat.TrinketsCompat;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

public class CompatFeatures {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            var id = FabricCompatFeatures.initSlotCompat();
            if ("trinkets".equals(id)) {
                TrinketsCompat.init();
            } else if ("accessories".equals(id)) {
                AccessoriesCompat.init();
            }
        } else {
            TrinketsCompat.init();
        }
    }
}

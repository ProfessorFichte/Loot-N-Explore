package com.lne.fabric.compat;

import more_rpg_loot.compat.spell_engine.trinket_compat.TrinketsCompat;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

public class CompatFeatures {
    public static void init() {
        if (!FabricLoader.getInstance().isModLoaded("more_rpg_classes")
                || "trinkets".equals(FabricCompatFeatures.initSlotCompat())) {
            TrinketsCompat.init();
        }
    }
}

package com.lne.fabric.compat;

import more_rpg_loot.compat.spell_engine.trinket_compat.TrinketsCompat;
import more_rpg_loot.platform.LNEPlatform;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

public class CompatFeatures {
    public static void init() {
        if (!LNEPlatform.isModLoaded("spell_engine")
                || "trinkets".equals(FabricCompatFeatures.initSlotCompat())) {
            TrinketsCompat.init();
        }
    }
}

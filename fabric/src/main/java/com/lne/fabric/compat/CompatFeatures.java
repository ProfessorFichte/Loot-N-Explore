package com.lne.fabric.compat;

import lne.compat.trinkets.TrinketsCompat;
import lne.platform.LNEPlatform;
import net.spell_engine.fabric.compat.FabricCompatFeatures;

public class CompatFeatures {
    public static void init() {
        if (!LNEPlatform.isModLoaded("spell_engine")
                || "trinkets".equals(FabricCompatFeatures.initSlotCompat())) {
            TrinketsCompat.init();
        }
    }
}

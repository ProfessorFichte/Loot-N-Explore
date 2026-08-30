package com.lne.neoforge.platform;

import more_rpg_loot.platform.LNEPlatform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;

public final class NeoForgePlatformImpl implements LNEPlatform.Impl {
    @Override
    public boolean isModLoaded(String modId) {
        if (ModList.get() != null) {
            return ModList.get().isLoaded(modId);
        }
        return LoadingModList.get().getModFileById(modId) != null;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}

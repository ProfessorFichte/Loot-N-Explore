package com.lne.fabric.platform;

import more_rpg_loot.platform.LNEPlatform;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatformImpl implements LNEPlatform.Impl {
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}

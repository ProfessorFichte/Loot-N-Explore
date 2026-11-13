package com.lne.fabric.platform;

import com.lne.fabric.compat.TrinketsAccess;
import lne.platform.LNEPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class FabricPlatformImpl implements LNEPlatform.Impl {
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public List<ItemStack> equippedAccessories(LivingEntity entity) {
        return isModLoaded("trinkets") ? TrinketsAccess.equipped(entity) : List.of();
    }
}

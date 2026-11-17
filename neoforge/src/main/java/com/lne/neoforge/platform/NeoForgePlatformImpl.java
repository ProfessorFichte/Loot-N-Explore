package com.lne.neoforge.platform;

import com.lne.neoforge.compat.curios.CuriosAccess;
import lne.internal.platform.LNEPlatform;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;

import java.util.List;

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

    @Override
    public List<ItemStack> equippedAccessories(LivingEntity entity) {
        return isModLoaded("curios") ? CuriosAccess.equipped(entity) : List.of();
    }
}

package lne.platform;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class LNEPlatform {
    public interface Impl {
        boolean isModLoaded(String modId);

        boolean isDevelopmentEnvironment();

        default List<ItemStack> equippedAccessories(LivingEntity entity) {
            return List.of();
        }
    }

    private static Impl impl;

    private LNEPlatform() {
    }

    public static void set(Impl newImpl) {
        impl = newImpl;
    }

    public static boolean isModLoaded(String modId) {
        return impl != null && impl.isModLoaded(modId);
    }

    public static boolean isDevelopmentEnvironment() {
        return impl != null && impl.isDevelopmentEnvironment();
    }

    public static boolean hasAccessorySlots() {
        return isModLoaded("trinkets") || isModLoaded("curios");
    }

    public static List<ItemStack> equippedAccessories(LivingEntity entity) {
        return impl == null ? List.of() : impl.equippedAccessories(entity);
    }
}

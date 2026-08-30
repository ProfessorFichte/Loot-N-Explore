package more_rpg_loot.platform;

public final class LNEPlatform {
    public interface Impl {
        boolean isModLoaded(String modId);

        boolean isDevelopmentEnvironment();
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
}

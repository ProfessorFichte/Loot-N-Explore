package lne.compat.trinkets;
import lne.internal.platform.LNEPlatform;


public class TrinketsCompat {
    public static void init() {
        if (LNEPlatform.isModLoaded("trinkets")) {
            TrinketsHelper.register();
        }
    }
}

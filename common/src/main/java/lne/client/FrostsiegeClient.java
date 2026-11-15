package lne.client;

import lne.mixin.BossBarHudAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

@Environment(EnvType.CLIENT)
public final class FrostsiegeClient {
    private static final String BAR_KEY = "event.loot_n_explore.frostsiege";
    private static long checkedAt = -1;
    private static boolean active;

    private FrostsiegeClient() {
    }

    public static boolean isSiegeBar(Text name) {
        return hasKey(name, BAR_KEY);
    }

    public static boolean hasKey(Text name, String key) {
        if (name.getContent() instanceof TranslatableTextContent translatable && translatable.getKey().equals(key)) {
            return true;
        }
        for (Text sibling : name.getSiblings()) {
            if (hasKey(sibling, key)) return true;
        }
        return false;
    }

    public static boolean isActive() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return false;
        }
        long time = client.world.getTime();
        if (time != checkedAt) {
            checkedAt = time;
            active = ((BossBarHudAccessor) client.inGameHud.getBossBarHud()).lne$getBossBars().values().stream()
                    .anyMatch(bar -> isSiegeBar(bar.getName()));
        }
        return active;
    }
}

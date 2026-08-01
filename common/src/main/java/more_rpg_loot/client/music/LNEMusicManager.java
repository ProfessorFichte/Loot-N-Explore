package more_rpg_loot.client.music;

import more_rpg_loot.client.music.frozen_depths.FrostMonarchBattleMusic;
import more_rpg_loot.client.music.frozen_depths.FrozenDepthsAmbientMusic;
import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LNEMusicManager {
    @Nullable private static FrostMonarchBattleMusic bossMusic;
    @Nullable private static FrozenDepthsAmbientMusic ambientMusic;

    public static void startBossMusic(FrostMonarchEntity entity) {
        if (bossMusic != null && !bossMusic.isDone()) return;
        bossMusic = new FrostMonarchBattleMusic(entity);
        MinecraftClient.getInstance().getSoundManager().play(bossMusic);
    }

    public static void startAmbientMusic() {
        if (ambientMusic != null && !ambientMusic.isDone()) return;
        ambientMusic = new FrozenDepthsAmbientMusic();
        MinecraftClient.getInstance().getSoundManager().play(ambientMusic);
    }

    public static void stopAmbientMusic() {
        if (ambientMusic != null && !ambientMusic.isDone()) {
            ambientMusic.startFade();
        }
    }
}

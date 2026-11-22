package lne.client.music;

import lne.client.music.frozen_depths.FrostMonarchBattleMusic;
import lne.config.TweaksConfig;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.sounds.StructureMusicTrack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class LNEMusicManager {
    @Nullable private static FrostMonarchBattleMusic bossMusic;
    private static final Map<StructureMusicTrack, StructureAmbientMusic> AMBIENT_MUSIC = new EnumMap<>(StructureMusicTrack.class);

    public static boolean isPlaying() {
        return isActive(bossMusic) || AMBIENT_MUSIC.values().stream().anyMatch(LNEMusicManager::isActive);
    }

    private static boolean isActive(@Nullable MovingSoundInstance sound) {
        return sound != null && !sound.isDone() && MinecraftClient.getInstance().getSoundManager().isPlaying(sound);
    }

    public static void silenceVanillaMusic() {
        MinecraftClient.getInstance().getMusicTracker().stop();
    }

    public static void startBossMusic(FrostMonarchEntity entity) {
        if (!TweaksConfig.customMusic() || isActive(bossMusic)) return;
        MinecraftClient.getInstance().getMusicTracker().stop();
        fadeAllAmbient();
        bossMusic = new FrostMonarchBattleMusic(entity);
        MinecraftClient.getInstance().getSoundManager().play(bossMusic);
    }

    public static void startAmbientMusic(StructureMusicTrack track) {
        if (!TweaksConfig.customMusic()) return;
        if (isActive(bossMusic)) {
            fadeAllAmbient();
            return;
        }
        AMBIENT_MUSIC.forEach((other, music) -> {
            if (other != track && !music.isDone()) {
                music.startFade();
            }
        });
        StructureAmbientMusic current = AMBIENT_MUSIC.get(track);
        if (isActive(current)) {
            current.cancelFade();
            return;
        }
        MinecraftClient.getInstance().getMusicTracker().stop();
        StructureAmbientMusic music = new StructureAmbientMusic(track.sound().soundEvent());
        AMBIENT_MUSIC.put(track, music);
        MinecraftClient.getInstance().getSoundManager().play(music);
    }

    private static void fadeAllAmbient() {
        AMBIENT_MUSIC.values().forEach(music -> {
            if (!music.isDone()) {
                music.startFade();
            }
        });
    }

    public static void stopAmbientMusic(StructureMusicTrack track) {
        StructureAmbientMusic music = AMBIENT_MUSIC.get(track);
        if (music != null && !music.isDone()) {
            music.startFade();
        }
    }
}

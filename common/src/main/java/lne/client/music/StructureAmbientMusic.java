package lne.client.music;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

@Environment(EnvType.CLIENT)
public class StructureAmbientMusic extends MovingSoundInstance {
    private static final float TARGET_VOLUME = 0.5f;
    private static final float FADE_PER_TICK = TARGET_VOLUME / 80f;
    private static final float FADE_IN_PER_TICK = TARGET_VOLUME / 100f;
    private boolean fading = false;

    public StructureAmbientMusic(SoundEvent sound) {
        super(sound, SoundCategory.MUSIC, SoundInstance.createRandom());
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.relative = true;
        this.repeat = true;
        this.volume = 0.0f;
    }

    public void startFade() {
        this.fading = true;
        this.repeat = false;
    }

    public void cancelFade() {
        this.fading = false;
        this.repeat = true;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public void tick() {
        LNEMusicManager.silenceVanillaMusic();
        if (fading) {
            this.volume = Math.max(0, this.volume - FADE_PER_TICK);
            if (this.volume <= 0) {
                setDone();
            }
        } else if (this.volume < TARGET_VOLUME) {
            this.volume = Math.min(TARGET_VOLUME, this.volume + FADE_IN_PER_TICK);
        }
    }
}

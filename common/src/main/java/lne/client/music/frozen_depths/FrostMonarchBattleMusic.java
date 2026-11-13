package lne.client.music.frozen_depths;

import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.client.music.LNEMusicManager;
import lne.sounds.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class FrostMonarchBattleMusic extends MovingSoundInstance {
    private static final float TARGET_VOLUME = 0.6f;
    private static final float FADE_PER_TICK = TARGET_VOLUME / 100f;
    private static final float FADE_IN_PER_TICK = TARGET_VOLUME / 40f;
    private final FrostMonarchEntity entity;
    private boolean fading = false;

    public FrostMonarchBattleMusic(FrostMonarchEntity entity) {
        super(ModSounds.MUSIC_FROST_MONARCH_BATTLE.soundEvent(), SoundCategory.MUSIC, SoundInstance.createRandom());
        this.entity = entity;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.relative = true;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.0f;
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public void tick() {
        LNEMusicManager.silenceVanillaMusic();
        if (!fading && (entity.isRemoved() || entity.isFakeDeath())) {
            fading = true;
        }
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

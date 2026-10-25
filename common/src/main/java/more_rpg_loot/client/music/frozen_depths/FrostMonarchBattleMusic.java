package more_rpg_loot.client.music.frozen_depths;

import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import more_rpg_loot.client.music.LNEMusicManager;
import more_rpg_loot.sounds.ModSounds;
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
        super(ModSounds.MUSIC_FROST_MONARCH_BATTLE.soundEvent(), SoundCategory.RECORDS, SoundInstance.createRandom());
        this.entity = entity;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.relative = true;
        this.repeat = false;
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

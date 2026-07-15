package more_rpg_loot.client.music;

import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class FrostMonarchBattleMusic extends MovingSoundInstance {
    private static final float FADE_PER_TICK = 1.0f / 100f;
    private final FrostMonarchEntity entity;
    private boolean fading = false;

    public FrostMonarchBattleMusic(FrostMonarchEntity entity) {
        super(ModSounds.MUSIC_FROST_MONARCH_BATTLE.soundEvent(), SoundCategory.RECORDS, SoundInstance.createRandom());
        this.entity = entity;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.relative = true;
        this.repeat = false;
        this.volume = 1.0f;
    }

    @Override
    public void tick() {
        if (!fading && (entity.isRemoved() || entity.isFakeDeath())) {
            fading = true;
        }
        if (fading) {
            this.volume = Math.max(0, this.volume - FADE_PER_TICK);
            if (this.volume <= 0) {
                setDone();
            }
        }
    }
}

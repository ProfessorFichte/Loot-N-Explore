package more_rpg_loot.client.music.frozen_depths;

import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class FrozenDepthsAmbientMusic extends MovingSoundInstance {
    private static final float FADE_PER_TICK = 1.0f / 80f;
    private boolean fading = false;

    public FrozenDepthsAmbientMusic() {
        super(ModSounds.MUSIC_FROZEN_DEPTHS.soundEvent(), SoundCategory.RECORDS, SoundInstance.createRandom());
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.relative = true;
        this.repeat = true;
        this.volume = 1.0f;
    }

    public void startFade() {
        this.fading = true;
        this.repeat = false;
    }

    @Override
    public void tick() {
        if (fading) {
            this.volume = Math.max(0, this.volume - FADE_PER_TICK);
            if (this.volume <= 0) {
                setDone();
            }
        }
    }
}

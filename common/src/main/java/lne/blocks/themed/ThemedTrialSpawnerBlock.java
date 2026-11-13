package lne.blocks.themed;

import net.minecraft.block.TrialSpawnerBlock;

public class ThemedTrialSpawnerBlock extends TrialSpawnerBlock implements ThemedBlock {
    private final ParticleTheme theme;

    public ThemedTrialSpawnerBlock(Settings settings, ParticleTheme theme) {
        super(settings);
        this.theme = theme;
    }

    @Override
    public ParticleTheme theme() {
        return theme;
    }
}

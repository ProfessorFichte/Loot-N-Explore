package lne.blocks.themed;

import net.minecraft.block.VaultBlock;

public class ThemedVaultBlock extends VaultBlock implements ThemedBlock {
    private final ParticleTheme theme;

    public ThemedVaultBlock(Settings settings, ParticleTheme theme) {
        super(settings);
        this.theme = theme;
    }

    @Override
    public ParticleTheme theme() {
        return theme;
    }
}

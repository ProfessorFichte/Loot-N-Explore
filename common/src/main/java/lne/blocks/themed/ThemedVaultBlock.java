package lne.blocks.themed;

import lne.mixin.VaultServerDataAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.VaultBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ThemedVaultBlock extends VaultBlock implements ThemedBlock {
    private static final long MAX_RESUME_DELAY = 200;
    private final ParticleTheme theme;

    public ThemedVaultBlock(Settings settings, ParticleTheme theme) {
        super(settings);
        this.theme = theme;
    }

    @Override
    public ParticleTheme theme() {
        return theme;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        BlockEntityTicker<T> ticker = super.getTicker(world, state, type);
        if (ticker == null || world.isClient) return ticker;
        return (tickWorld, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof VaultBlockEntity vault) {
                clearStaleResumeTime(vault, tickWorld.getTime());
            }
            ticker.tick(tickWorld, pos, tickState, blockEntity);
        };
    }

    // Vaults saved into a structure keep the resume time of the world they were built in.
    // In a younger world that time lies hours ahead and the vault never activates.
    private static void clearStaleResumeTime(VaultBlockEntity vault, long now) {
        var data = (VaultServerDataAccessor) vault.getServerData();
        if (data.lne$getStateUpdatingResumeTime() > now + MAX_RESUME_DELAY) {
            data.lne$setStateUpdatingResumeTime(now);
        }
    }
}

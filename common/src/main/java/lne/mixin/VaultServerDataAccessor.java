package lne.mixin;

import net.minecraft.block.vault.VaultServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VaultServerData.class)
public interface VaultServerDataAccessor {
    @Invoker("getStateUpdatingResumeTime")
    long lne$getStateUpdatingResumeTime();

    @Invoker("setStateUpdatingResumeTime")
    void lne$setStateUpdatingResumeTime(long time);
}

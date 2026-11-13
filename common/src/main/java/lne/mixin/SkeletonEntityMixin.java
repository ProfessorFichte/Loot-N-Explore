package lne.mixin;

import lne.LneMod;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin {
    @Inject(method = "setConversionTime", at = @At("HEAD"), cancellable = true)
    private void lne$neverConvertToStray(int time, CallbackInfo ci) {
        SkeletonEntity self = (SkeletonEntity) (Object) this;
        if (Registries.ENTITY_TYPE.getId(self.getType()).getNamespace().equals(LneMod.MOD_ID)) {
            ci.cancel();
        }
    }
}

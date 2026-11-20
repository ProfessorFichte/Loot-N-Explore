package lne.mixin;

import lne.item.CommonItems;
import lne.server.frostsiege.Frostsiege;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void lne$cryoEssenceRitual(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (!self.getWorld().isClient && self.getStack().isOf(CommonItems.CRYO_ESSENCE.item())) {
            Frostsiege.tryCryoRitual(self);
        }
    }
}

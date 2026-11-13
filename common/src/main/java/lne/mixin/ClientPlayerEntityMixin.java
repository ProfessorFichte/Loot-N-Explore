package lne.mixin;

import lne.client.hud.WeaponReadyCue;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void lne$weaponReadyCue(CallbackInfo ci) {
        WeaponReadyCue.tick((ClientPlayerEntity) (Object) this);
    }
}

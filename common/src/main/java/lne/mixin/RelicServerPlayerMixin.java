package lne.mixin;

import lne.internal.ability.RelicTriggers;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class RelicServerPlayerMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void lne$relicOnPlayerDeath(DamageSource source, CallbackInfo ci) {
        RelicTriggers.onDeath((ServerPlayerEntity) (Object) this, source);
    }
}

package lne.mixin;

import lne.ability.RelicTriggers;
import lne.effects.generic.RainbowCoralEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class RelicLivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void lne$relicBeforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerPlayerEntity player && RelicTriggers.beforeDamage(player, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void lne$relicAfterDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            RelicTriggers.onDamageApplied((LivingEntity) (Object) this, source, amount);
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void lne$relicOnDeath(DamageSource source, CallbackInfo ci) {
        RelicTriggers.onDeath((LivingEntity) (Object) this, source);
    }

    @Inject(method = "heal", at = @At("HEAD"))
    private void lne$relicOnHeal(float amount, CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayerEntity player) {
            RelicTriggers.onHeal(player, amount);
        }
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    private void lne$relicEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;
        if (!self.getWorld().isClient && effect.getEffectType().value() instanceof RainbowCoralEffect) {
            RainbowCoralEffect.revoke(self);
        }
    }
}

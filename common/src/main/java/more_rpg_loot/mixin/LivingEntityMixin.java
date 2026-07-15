package more_rpg_loot.mixin;

import more_rpg_loot.effects.Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Unique
    LivingEntity livingEntity = (LivingEntity) (Object) (this);
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void frostResistanceDamageMixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.hasStatusEffect(Effects.FROST_RESISTANCE.registryEntry) && source.isIn(DamageTypeTags.IS_FREEZING)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    public void baseTickPowderSnowFreezingEffect(CallbackInfo ci) {
        var entity = (LivingEntity) ((Object)this);
        entity.inPowderSnow = entity.inPowderSnow || hasStatusEffect(Effects.FREEZING.registryEntry);
    }

}

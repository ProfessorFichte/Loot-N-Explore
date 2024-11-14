package more_rpg_loot.mixin;

import more_rpg_loot.effects.Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    LivingEntity livingEntity = (LivingEntity) (Object) (this);
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void frostResistanceDamageMixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.hasStatusEffect(Effects.FROST_RESISTANCE) && source.isIn(DamageTypeTags.IS_FREEZING)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

}

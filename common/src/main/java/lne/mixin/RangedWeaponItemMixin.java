package lne.mixin;

import lne.ability.RelicTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RangedWeaponItem.class)
public abstract class RangedWeaponItemMixin {

    @Inject(method = "shootAll", at = @At("HEAD"))
    private void lne$relicArrowShot(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles,
                                    float speed, float divergence, boolean critical, LivingEntity target, CallbackInfo ci) {
        RelicTriggers.onArrowShot(shooter);
    }
}

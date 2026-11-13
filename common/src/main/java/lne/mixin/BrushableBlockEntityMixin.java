package lne.mixin;

import lne.blocks.generic.SuspiciousBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin {

    @ModifyVariable(method = "brush", at = @At("HEAD"), argsOnly = true)
    private Direction lne$suspiciousEjectDirection(Direction hitDirection, long worldTime, PlayerEntity player) {
        if (hitDirection != null && ((BlockEntity) (Object) this).getCachedState().getBlock() instanceof SuspiciousBlock suspicious) {
            return suspicious.ejectDirection(hitDirection, player);
        }
        return hitDirection;
    }
}

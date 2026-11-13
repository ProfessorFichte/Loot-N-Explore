package lne.mixin;

import lne.blocks.FrozenDepthsBlocks;
import lne.blocks.frozen_depths.FrostflameBlock;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @Inject(method = "getState", at = @At("HEAD"), cancellable = true, require = 0)
    private static void lne$frostflameOnCryoticBlock(BlockView world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (FrostflameBlock.isFrostflameBase(world.getBlockState(pos.down()))) {
            cir.setReturnValue(FrozenDepthsBlocks.FROSTFLAME.getDefaultState());
        }
    }
}

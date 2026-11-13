package lne.mixin;

import lne.blocks.frozen_depths.FrozenDecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
    @Unique
    private DecoratedPotBlockEntity lne$frozenPot;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void lne$renderFrozenPot(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                     int light, int overlay, CallbackInfo ci) {
        if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof FrozenDecoratedPotBlock pot)) {
            return;
        }
        if (lne$frozenPot == null || lne$frozenPot.getCachedState().getBlock() != pot) {
            lne$frozenPot = new DecoratedPotBlockEntity(BlockPos.ORIGIN, pot.getDefaultState());
        }
        lne$frozenPot.readFrom(stack);
        MinecraftClient.getInstance().getBlockEntityRenderDispatcher().renderEntity(lne$frozenPot, matrices, vertexConsumers, light, overlay);
        ci.cancel();
    }
}

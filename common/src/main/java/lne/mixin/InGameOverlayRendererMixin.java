package lne.mixin;

import lne.client.hud.FrostflameOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Inject(method = "renderOverlays", at = @At("TAIL"), require = 0)
    private static void lne$renderFrostflameOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (client.player != null && !client.player.isSpectator() && FrostflameOverlay.isInFrostflame(client.player)) {
            FrostflameOverlay.render(client, matrices);
        }
    }
}

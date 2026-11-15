package lne.mixin;

import lne.client.FrostsiegeClient;
import lne.entity.ModEntities;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lne.LneMod.MOD_ID;

// Texture is 208x64: top 32 rows background, bottom 32 rows fill.
@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    private static final Identifier FROST_MONARCH_BAR =
            Identifier.of(MOD_ID, "textures/gui/boss_bar/frozen_depths/frost_monarch.png");
    private static final Identifier FROSTSIEGE_BAR =
            Identifier.of(MOD_ID, "textures/gui/boss_bar/frozen_depths/frostsiege.png");

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"), cancellable = true)
    private void lne_renderFrostMonarchBar(DrawContext context, int x, int y,
                                           BossBar bossBar, CallbackInfo ci) {
        Identifier texture;
        if (FrostsiegeClient.hasKey(bossBar.getName(), ModEntities.FROST_MONARCH.getTranslationKey())) {
            texture = FROST_MONARCH_BAR;
        } else if (FrostsiegeClient.isSiegeBar(bossBar.getName())) {
            texture = FROSTSIEGE_BAR;
        } else {
            return;
        }

        int barWidth = 208;
        int barHeight = 32;
        int barX = x - (barWidth - 182) / 2;

        context.drawTexture(texture, barX, y, 0, 0, barWidth, barHeight, barWidth, 64);

        int fillWidth = Math.round(barWidth * bossBar.getPercent());
        if (fillWidth > 0) {
            context.drawTexture(texture, barX, y, 0, 32, fillWidth, barHeight, barWidth, 64);
        }

        ci.cancel();
    }
}

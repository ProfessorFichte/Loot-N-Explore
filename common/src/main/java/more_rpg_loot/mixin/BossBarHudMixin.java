package more_rpg_loot.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static more_rpg_loot.RPGLoot.MOD_ID;

/**
 * Replaces the vanilla boss bar texture for the Frost Monarch boss.
 * The custom texture (assets/loot_n_explore/textures/gui/boss_bar/frozen_depths/frost_monarch.png)
 * must be 208x32: top 16 rows = background, bottom 16 rows = progress fill.
 *
 * NOTE: If this mixin fails to apply, verify that "renderBossBar" matches the
 * Yarn-mapped name of the private per-bar render helper in BossBarHud. Use a
 * decompiler with Yarn mappings to confirm.
 */
@Environment(EnvType.CLIENT)
@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    private static final Identifier FROST_MONARCH_BAR =
            Identifier.of(MOD_ID, "textures/gui/boss_bar/frozen_depths/frost_monarch.png");

    @Inject(method = "renderBossBar", at = @At("HEAD"), cancellable = true)
    private void lne_renderFrostMonarchBar(DrawContext context, int x, int y,
                                           BossBar bossBar, CallbackInfo ci) {
        if (!bossBar.getName().getString().contains("Frost Monarch")) return;

        // Custom texture is 208x32. Center the wider bar over the vanilla 182px position.
        int barWidth  = 208;
        int barHeight = 32; // each half-strip is 16px tall
        int barX = x - (barWidth - 182) / 2;

        // Background strip (top 16 rows)
        context.drawTexture(FROST_MONARCH_BAR, barX, y, 0, 0, barWidth, barHeight, barWidth, 64);

        // Progress fill strip (bottom 16 rows), width scaled by health percent
        int fillWidth = Math.round(barWidth * bossBar.getPercent());
        if (fillWidth > 0) {
            context.drawTexture(FROST_MONARCH_BAR, barX, y, 0, 32, fillWidth, barHeight, barWidth, 64);
        }

        // Re-draw the name text centered above the bar (vanilla normally does this outside renderBossBar)
        var tr = MinecraftClient.getInstance().textRenderer;
        int nameX = x + 91 - tr.getWidth(bossBar.getName()) / 2;
        context.drawText(tr, bossBar.getName(), nameX, y - 9, 0xFFFFFF, true);

        ci.cancel();
    }
}

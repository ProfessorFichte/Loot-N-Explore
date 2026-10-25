package more_rpg_loot.mixin;

import more_rpg_loot.entity.ModEntities;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static more_rpg_loot.RPGLoot.MOD_ID;

// Texture is 208x64: top 32 rows background, bottom 32 rows fill.
@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    private static final Identifier FROST_MONARCH_BAR =
            Identifier.of(MOD_ID, "textures/gui/boss_bar/frozen_depths/frost_monarch.png");

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"), cancellable = true)
    private void lne_renderFrostMonarchBar(DrawContext context, int x, int y,
                                           BossBar bossBar, CallbackInfo ci) {
        if (!lne_isFrostMonarchName(bossBar.getName())) return;

        int barWidth = 208;
        int barHeight = 32;
        int barX = x - (barWidth - 182) / 2;

        context.drawTexture(FROST_MONARCH_BAR, barX, y, 0, 0, barWidth, barHeight, barWidth, 64);

        int fillWidth = Math.round(barWidth * bossBar.getPercent());
        if (fillWidth > 0) {
            context.drawTexture(FROST_MONARCH_BAR, barX, y, 0, 32, fillWidth, barHeight, barWidth, 64);
        }

        ci.cancel();
    }

    private static boolean lne_isFrostMonarchName(Text name) {
        if (name.getContent() instanceof TranslatableTextContent translatable
                && translatable.getKey().equals(ModEntities.FROST_MONARCH.getTranslationKey())) {
            return true;
        }
        for (Text sibling : name.getSiblings()) {
            if (lne_isFrostMonarchName(sibling)) return true;
        }
        return false;
    }
}

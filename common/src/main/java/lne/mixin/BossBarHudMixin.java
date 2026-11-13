package lne.mixin;

import lne.entity.ModEntities;
import net.minecraft.client.MinecraftClient;
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

import static lne.LneMod.MOD_ID;

// Texture is 208x64: top 32 rows background, bottom 32 rows fill.
@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    private static final Identifier FROST_MONARCH_BAR =
            Identifier.of(MOD_ID, "textures/gui/boss_bar/frozen_depths/frost_monarch.png");
    private static final Identifier FROSTSIEGE_BAR =
            Identifier.of(MOD_ID, "textures/gui/boss_bar/frozen_depths/frostsiege.png");
    private static final String FROSTSIEGE_KEY = "event.loot_n_explore.frostsiege";

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"), cancellable = true)
    private void lne_renderFrostMonarchBar(DrawContext context, int x, int y,
                                           BossBar bossBar, CallbackInfo ci) {
        Identifier texture;
        if (lne_hasKey(bossBar.getName(), ModEntities.FROST_MONARCH.getTranslationKey())) {
            texture = FROST_MONARCH_BAR;
        } else if (lne_hasKey(bossBar.getName(), FROSTSIEGE_KEY)) {
            // The raid falls back to the Monarch's bar as long as it has no texture of its own, will be removed later.
            texture = MinecraftClient.getInstance().getResourceManager().getResource(FROSTSIEGE_BAR).isPresent()
                    ? FROSTSIEGE_BAR : FROST_MONARCH_BAR;
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

    private static boolean lne_hasKey(Text name, String key) {
        if (name.getContent() instanceof TranslatableTextContent translatable && translatable.getKey().equals(key)) {
            return true;
        }
        for (Text sibling : name.getSiblings()) {
            if (lne_hasKey(sibling, key)) return true;
        }
        return false;
    }
}

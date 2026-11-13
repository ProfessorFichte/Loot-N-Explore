package lne.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import lne.config.TweaksConfig;
import lne.hud_text.EntityHudTextStyle;
import lne.network.EntityHudTextPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class EntityHudTextOverlay {
    private static final int REFERENCE_HEIGHT = 240;
    private static final float GLYPH_FADE_IN_TICKS = 3.0F;
    private static final float GLYPH_DROP_IN = 2.0F;
    private static final int ICON_SIZE = 64;
    private static final int ICON_SPACING = 4;
    private static final int ICON_FADE_IN_TICKS = 10;
    private static final double BANNER_SHAKE = 2.0;
    private static final int WOBBLE_TICKS = 6;
    private static final float FALL_GRAVITY = 0.14F;
    private static final float OVERLAY_Z = 200.0F;

    private static final List<Line> LINES = new ArrayList<>();
    @Nullable private static ClientWorld linesWorld;

    private EntityHudTextOverlay() {}

    private record Line(EntityHudTextPayload spec, String[] glyphs, float[] offsets, int[] fallOrder,
                        int fallCount, int width, long startTick) {
        float totalTicks() {
            int base = spec.typeTicks() + spec.holdTicks();
            if (spec.style() == EntityHudTextStyle.TYPEWRITER_ICICLE_FALL) {
                return base + Math.max(0, fallCount - 1) * EntityHudTextStyle.ICICLE_FALL_STAGGER_TICKS
                        + EntityHudTextStyle.ICICLE_FALL_TICKS;
            }
            return base + spec.style().fadeOutTicks();
        }
    }

    public static void show(EntityHudTextPayload payload) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!TweaksConfig.entityHudText() || client.world == null) {
            return;
        }
        if (client.world != linesWorld) {
            LINES.clear();
            linesWorld = client.world;
        }
        LINES.removeIf(line -> line.spec().style() == payload.style());
        LINES.add(layout(client.textRenderer, payload, client.world.getTime()));
    }

    private static Line layout(TextRenderer textRenderer, EntityHudTextPayload payload, long startTick) {
        String text = Text.translatable(payload.translationKey()).getString();
        int[] codePoints = text.codePoints().toArray();
        String[] glyphs = new String[codePoints.length];
        float[] offsets = new float[codePoints.length];
        int[] fallOrder = new int[codePoints.length];
        StringBuilder prefix = new StringBuilder();
        int fallCount = 0;
        for (int i = 0; i < codePoints.length; i++) {
            offsets[i] = textRenderer.getWidth(prefix.toString());
            glyphs[i] = new String(Character.toChars(codePoints[i]));
            fallOrder[i] = Character.isWhitespace(codePoints[i]) ? -1 : fallCount++;
            prefix.appendCodePoint(codePoints[i]);
        }
        return new Line(payload, glyphs, offsets, fallOrder, fallCount, textRenderer.getWidth(text), startTick);
    }

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (LINES.isEmpty()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.world != linesWorld || !TweaksConfig.entityHudText()) {
            LINES.clear();
            return;
        }
        float now = client.world.getTime() + tickCounter.getTickDelta(false);
        Iterator<Line> iterator = LINES.iterator();
        while (iterator.hasNext()) {
            Line line = iterator.next();
            float elapsed = now - line.startTick();
            if (elapsed >= line.totalTicks()) {
                iterator.remove();
                continue;
            }
            switch (line.spec().style()) {
                case BANNER -> renderBanner(context, client.textRenderer, line, Math.max(0, elapsed));
                case TYPEWRITER -> renderCentered(context, client.textRenderer, line, Math.max(0, elapsed), 0.3F, 1.5F, 1.0F, 3.0F);
                case TYPEWRITER_ICICLE_FALL -> renderCentered(context, client.textRenderer, line, Math.max(0, elapsed), 0.38F, 2.5F, 1.5F, 4.0F);
            }
        }
    }

    private static float fitScale(DrawContext context, Line line, float base, float min, float max) {
        float heightScale = base * (context.getScaledWindowHeight() / (float) REFERENCE_HEIGHT);
        float widthScale = (context.getScaledWindowWidth() * 0.8F) / Math.max(1, line.width());
        return MathHelper.clamp(Math.min(heightScale, widthScale), min, max);
    }

    private static float fadeOut(Line line, float elapsed) {
        int fade = line.spec().style().fadeOutTicks();
        if (fade <= 0) {
            return 1.0F;
        }
        return MathHelper.clamp((line.totalTicks() - elapsed) / fade, 0.0F, 1.0F);
    }

    private static void renderBanner(DrawContext context, TextRenderer textRenderer, Line line, float elapsed) {
        float scale = fitScale(context, line, 2.0F, 1.5F, 4.0F);
        float alpha = fadeOut(line, elapsed);
        Identifier icon = line.spec().icon().orElse(null);
        float iconBlock = icon != null ? ICON_SIZE + ICON_SPACING : 0;
        float totalHeight = (iconBlock + textRenderer.fontHeight) * scale;
        float startY = (context.getScaledWindowHeight() - totalHeight) / 2.0F;
        double shakeX = (Math.random() - 0.5) * BANNER_SHAKE;
        double shakeY = (Math.random() - 0.5) * BANNER_SHAKE;

        context.getMatrices().push();
        context.getMatrices().translate(context.getScaledWindowWidth() / 2.0 + shakeX, startY + shakeY, OVERLAY_Z);
        context.getMatrices().scale(scale, scale, 1.0F);
        if (icon != null) {
            float iconAlpha = alpha * MathHelper.clamp(elapsed / ICON_FADE_IN_TICKS, 0.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            context.setShaderColor(1.0F, 1.0F, 1.0F, iconAlpha);
            context.drawTexture(icon, -ICON_SIZE / 2, 0, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
        drawGlyphs(context, textRenderer, line, elapsed, -line.width() / 2.0F, iconBlock, alpha);
        context.getMatrices().pop();
    }

    private static void renderCentered(DrawContext context, TextRenderer textRenderer, Line line, float elapsed,
                                       float heightFraction, float base, float min, float max) {
        float scale = fitScale(context, line, base, min, max);
        float centerY = context.getScaledWindowHeight() * heightFraction;
        context.getMatrices().push();
        context.getMatrices().translate(context.getScaledWindowWidth() / 2.0, centerY - textRenderer.fontHeight * scale / 2.0F, OVERLAY_Z);
        context.getMatrices().scale(scale, scale, 1.0F);
        drawGlyphs(context, textRenderer, line, elapsed, -line.width() / 2.0F, 0, fadeOut(line, elapsed));
        context.getMatrices().pop();
    }

    private static void drawGlyphs(DrawContext context, TextRenderer textRenderer, Line line, float elapsed,
                                   float originX, float originY, float alpha) {
        int count = line.glyphs().length;
        if (count == 0) {
            return;
        }
        EntityHudTextStyle style = line.spec().style();
        float typeTicks = Math.max(0, line.spec().typeTicks());
        int fallStart = line.spec().typeTicks() + line.spec().holdTicks();
        for (int i = 0; i < count; i++) {
            float appearAt = typeTicks * i / count;
            float typed = typeTicks <= 0 ? 1.0F : MathHelper.clamp((elapsed - appearAt) / GLYPH_FADE_IN_TICKS, 0.0F, 1.0F);
            if (typed <= 0.0F) {
                break;
            }
            float glyphAlpha = alpha * typed;
            float x = originX + line.offsets()[i];
            float y = originY - (1.0F - typed) * GLYPH_DROP_IN;
            if (style == EntityHudTextStyle.TYPEWRITER_ICICLE_FALL && line.fallOrder()[i] >= 0) {
                float t = elapsed - (fallStart + line.fallOrder()[i] * EntityHudTextStyle.ICICLE_FALL_STAGGER_TICKS);
                if (t >= 0) {
                    y += FALL_GRAVITY * t * t;
                    glyphAlpha *= MathHelper.clamp(1.0F - t / EntityHudTextStyle.ICICLE_FALL_TICKS, 0.0F, 1.0F);
                } else if (t > -WOBBLE_TICKS) {
                    float strength = 1.0F + t / WOBBLE_TICKS;
                    x += MathHelper.sin(elapsed * 2.6F + i) * 0.6F * strength;
                }
            }
            drawGlyph(context, textRenderer, line.glyphs()[i], x, y, line.spec().color(), glyphAlpha);
        }
    }

    private static void drawGlyph(DrawContext context, TextRenderer textRenderer, String glyph, float x, float y, int rgb, float alpha) {
        int a = Math.round(MathHelper.clamp(alpha, 0.0F, 1.0F) * 255.0F);
        if (a < 5) {
            return;
        }
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.drawText(textRenderer, glyph, 0, 0, (a << 24) | (rgb & 0xFFFFFF), true);
        context.getMatrices().pop();
    }
}

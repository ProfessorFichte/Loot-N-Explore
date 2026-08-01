package more_rpg_loot.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrostMonarchSpawnOverlay {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/gui/frostmonarch_spawn_overlay.png");
    private static final int DURATION_TICKS = 120;
    private static final int TEXTURE_SIZE = 64;
    private static final float BASE_SCALE = 2.0F;
    private static final float MIN_SCALE = 1.5F;
    private static final float MAX_SCALE = 4.0F;
    private static final int REFERENCE_HEIGHT = 240;
    private static final int SPACING = 4;
    private static final double SHAKE_AMPLITUDE = 2.0;

    private static int ticksRemaining = 0;

    public static void start() {
        ticksRemaining = DURATION_TICKS;
    }

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (ticksRemaining <= 0) {
            return;
        }
        ticksRemaining--;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        Text message = Text.translatable("entity.loot_n_explore.frost_monarch.spawn_message");

        float heightScale = BASE_SCALE * (screenHeight / (float) REFERENCE_HEIGHT);
        float widthScale = (screenWidth * 0.8F) / client.textRenderer.getWidth(message);
        float scale = MathHelper.clamp(Math.min(heightScale, widthScale), MIN_SCALE, MAX_SCALE);

        float textureHeight = TEXTURE_SIZE * scale;
        float textHeight = client.textRenderer.fontHeight * scale;
        float totalHeight = textureHeight + SPACING * scale + textHeight;
        float startY = (screenHeight - totalHeight) / 2.0F;

        double shakeX = (Math.random() - 0.5) * SHAKE_AMPLITUDE;
        double shakeY = (Math.random() - 0.5) * SHAKE_AMPLITUDE;

        context.getMatrices().push();
        context.getMatrices().translate(screenWidth / 2.0 + shakeX, startY + shakeY, 0);
        context.getMatrices().scale(scale, scale, 1.0F);
        context.drawTexture(TEXTURE, -TEXTURE_SIZE / 2, 0, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
        context.drawCenteredTextWithShadow(client.textRenderer, message, 0, TEXTURE_SIZE + SPACING, 0xFFFFFF);
        context.getMatrices().pop();
    }
}

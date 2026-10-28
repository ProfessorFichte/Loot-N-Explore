package more_rpg_loot.client.hud;

import more_rpg_loot.blocks.FrozenDepthsBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class FrostflameOverlay {
    private static final Identifier SPRITE = Identifier.of(MOD_ID, "block/frostflame_1");

    public static boolean isInFrostflame(PlayerEntity player) {
        BlockPos feet = player.getBlockPos();
        return player.getWorld().getBlockState(feet).isOf(FrozenDepthsBlocks.FROSTFLAME)
                || player.getWorld().getBlockState(BlockPos.ofFloored(player.getEyePos())).isOf(FrozenDepthsBlocks.FROSTFLAME);
    }

    public static void render(MinecraftClient client, MatrixStack matrices) {
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(SPRITE);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, sprite.getAtlasId());

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float midU = (minU + maxU) / 2.0F;
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();
        float midV = (minV + maxV) / 2.0F;
        float shrink = sprite.getAnimationFrameDelta();
        float u0 = MathHelper.lerp(shrink, minU, midU);
        float u1 = MathHelper.lerp(shrink, maxU, midU);
        float v0 = MathHelper.lerp(shrink, minV, midV);
        float v1 = MathHelper.lerp(shrink, maxV, midV);

        for (int i = 0; i < 2; i++) {
            matrices.push();
            matrices.translate(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((i * 2 - 1) * 10.0F));
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            buffer.vertex(matrix, -0.5F, -0.5F, -0.5F).texture(u1, v1).color(1.0F, 1.0F, 1.0F, 0.9F);
            buffer.vertex(matrix, 0.5F, -0.5F, -0.5F).texture(u0, v1).color(1.0F, 1.0F, 1.0F, 0.9F);
            buffer.vertex(matrix, 0.5F, 0.5F, -0.5F).texture(u0, v0).color(1.0F, 1.0F, 1.0F, 0.9F);
            buffer.vertex(matrix, -0.5F, 0.5F, -0.5F).texture(u1, v0).color(1.0F, 1.0F, 1.0F, 0.9F);
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            matrices.pop();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
}

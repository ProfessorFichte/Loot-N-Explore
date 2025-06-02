package more_rpg_loot.client.entity.renderers;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.FrostMonarchEntityModel;
import more_rpg_loot.client.entity.renderers.feature.FrostmonarchEyesFeatureRenderer;
import more_rpg_loot.client.entity.renderers.feature.FrostmonarchOverlayFeatureRenderer;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrostmonarchEntityRenderer extends MobEntityRenderer<FrostMonarchEntity, FrostMonarchEntityModel> {
    private static final Identifier TEXTURE = Identifier.of("loot_n_explore", "textures/entity/mobs/frostmonarch.png");
    private static final Identifier EYES = Identifier.of("loot_n_explore", "textures/entity/mobs/frostmonarch_eyes.png");
    private static final Identifier OVERLAY = Identifier.of("loot_n_explore", "textures/entity/mobs/frostmonarch_overlay.png");

    public FrostmonarchEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FrostMonarchEntityModel(context.getPart(EntityModelLayers.FROST_MONARCH)), 0.6f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new FrostmonarchEyesFeatureRenderer(this));
        this.addFeature(new FrostmonarchOverlayFeatureRenderer<>(this, context.getModelLoader(), EntityModelLayers.FROST_MONARCH_OUTER, OVERLAY));
    }

    @Override
    public Identifier getTexture(FrostMonarchEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(FrostMonarchEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(1.4F, 1.4F, 1.4F);
    }
    @Override
    public void render(FrostMonarchEntity entity, float entityYaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        if (entity.getInvulnerableTimer() > 0) {
            renderSinkingIceBlockOnSpawn(entity, tickDelta, matrices, vertexConsumers, light);
            float shakingIntensity = 0.1f;
            float shakeX = (entity.getWorld().random.nextFloat() - 0.5f) * shakingIntensity;
            float shakeY = (entity.getWorld().random.nextFloat() - 0.5f) * shakingIntensity;
            float shakeZ = (entity.getWorld().random.nextFloat() - 0.5f) * shakingIntensity;
            matrices.translate(shakeX, shakeY, shakeZ);
        }
        super.render(entity, entityYaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
    private void renderSinkingIceBlockOnSpawn(FrostMonarchEntity entity, float tickDelta, MatrixStack matrices,
                                VertexConsumerProvider vertexConsumers, int light) {
        int invulTime = entity.getInvulnerableTimer();
        if (invulTime <= 0) return;
        float progress = (invulTime - tickDelta) / 220.0f;
        float sinkOffset = entity.getHeight() * (1.0f - progress);

        float scaleFactor = 2.5f;
        float width = entity.getWidth() * scaleFactor;
        float height = entity.getHeight() + 1.0F;

        matrices.push();

        matrices.translate(-width / 2.0, -sinkOffset, -width / 2.0);
        matrices.scale(width, height, width);

        float shakingIntensity = 0.05f;
        float shakeX = (entity.getWorld().random.nextFloat() - 0.5f) * shakingIntensity;
        float shakeY = (entity.getWorld().random.nextFloat() - 0.5f) * shakingIntensity;
        float shakeZ = (entity.getWorld().random.nextFloat() - 0.5f) * shakingIntensity;
        matrices.translate(shakeX, shakeY, shakeZ);

        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                Blocks.ICE.getDefaultState(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }

}
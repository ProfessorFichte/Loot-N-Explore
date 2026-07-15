package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import more_rpg_loot.entity.frozen_depths.projectile.BarrierIcicleEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Renderer for BarrierIcicleEntity that animates the icicle rising from the ground
 * and retracting back down.
 */
public class BarrierIcicleRenderer extends EntityRenderer<BarrierIcicleEntity> {
    private static final Identifier TEXTURE = Identifier.of("loot_n_explore", "textures/block/sharp_icicles.png");
    private static final Identifier MODEL_ID = Identifier.of("loot_n_explore", "block/icicle_straight");

    // Model dimensions (based on the JSON model - icicle extends from y=-16 to y=28, total 44 pixels = 2.75 blocks)
    private static final float MODEL_HEIGHT = 2.75F;

    public BarrierIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(BarrierIcicleEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float animationProgress = entity.getAnimationProgress(tickDelta);

        // Early exit if fully retracted (invisible)
        if (animationProgress <= 0.01F) {
            return;
        }

        matrices.push();

        // Calculate emergence progress based on animation state
        // animationProgress: 0.0 = fully retracted, 1.0 = fully emerged
        float emergeProgress = animationProgress;

        // Translate based on emergence (start below ground, rise up)
        // The model extends from y=-16 to y=28 (in pixels/16 = -1 to 1.75 blocks)
        // We want it to emerge from below, so we translate it down and gradually bring it up
        float yOffset = -MODEL_HEIGHT + (MODEL_HEIGHT * emergeProgress);
        matrices.translate(0.0, yOffset, 0.0);

        // Get the baked model from the model manager
        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(MODEL_ID);

        if (model != null) {
            // Scale the model to make it bigger (1.5x size)
            matrices.scale(1.5F, 1.5F, 1.5F);
            matrices.translate(-0.5, -0.5, -0.5);

            // Get the sprite/texture from the model to determine the correct render layer
            var particleSprite = model.getParticleSprite();
            var renderLayer = RenderLayer.getEntityCutoutNoCull(particleSprite.getAtlasId());
            VertexConsumer buffer = vertexConsumers.getBuffer(renderLayer);

            // Render the model with its textures
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                matrices.peek(),
                buffer,
                null,
                model,
                1.0F, 1.0F, 1.0F,
                light,
                OverlayTexture.DEFAULT_UV
            );
        }

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BarrierIcicleEntity entity) {
        return TEXTURE;
    }
}

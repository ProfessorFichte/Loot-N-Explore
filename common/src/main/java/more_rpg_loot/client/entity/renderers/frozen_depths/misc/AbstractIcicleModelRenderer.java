package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

/**
 * Shared rendering for the frozen_depths icicle entities: both rise out of the ground using the
 * same baked block model, texture, and scale - only the emergence curve (and, for the thrown
 * variant, a yaw rotation) differs.
 */
public abstract class AbstractIcicleModelRenderer<T extends Entity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/block/frozen_depths/sharp_icicles.png");
    private static final Identifier MODEL_ID = Identifier.of(MOD_ID, "block/frozen_depths/icicle_straight");

    // Model extends from y=-16 to y=28 (44px = 2.75 blocks)
    private static final float MODEL_HEIGHT = 2.75F;

    protected AbstractIcicleModelRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    /** 0.0 = fully retracted below ground, 1.0 = fully emerged. */
    protected abstract float getEmergeProgress(T entity, float tickDelta);

    protected float getEmergeThreshold() {
        return 0.0F;
    }

    protected void applyExtraTransform(MatrixStack matrices, T entity) {
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float emergeProgress = getEmergeProgress(entity, tickDelta);
        if (emergeProgress <= getEmergeThreshold()) {
            return;
        }

        matrices.push();

        applyExtraTransform(matrices, entity);

        float yOffset = -MODEL_HEIGHT + (MODEL_HEIGHT * emergeProgress);
        matrices.translate(0.0, yOffset, 0.0);

        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(MODEL_ID);
        if (model != null) {
            matrices.scale(1.5F, 1.5F, 1.5F);
            matrices.translate(-0.5, -0.5, -0.5);

            var particleSprite = model.getParticleSprite();
            var renderLayer = RenderLayer.getEntityCutoutNoCull(particleSprite.getAtlasId());
            VertexConsumer buffer = vertexConsumers.getBuffer(renderLayer);

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
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}

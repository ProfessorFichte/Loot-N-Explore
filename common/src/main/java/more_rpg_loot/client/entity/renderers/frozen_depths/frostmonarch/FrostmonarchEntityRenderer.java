package more_rpg_loot.client.entity.renderers.frozen_depths.frostmonarch;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frozen_depths.frostmonarch.FrostMonarchModel;
import more_rpg_loot.client.entity.renderers.generic.GenericEyesFeatureRenderer;
import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrostmonarchEntityRenderer extends MobEntityRenderer<FrostMonarchEntity, FrostMonarchModel> {
    private static final Identifier TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frostmonarch.png");
    private static final Identifier EYES_TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frostmonarch_eyes.png");

    public FrostmonarchEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FrostMonarchModel(context.getPart(EntityModelLayers.FROST_MONARCH)), 0.6f);
        this.addFeature(new FrostMonarchHeldItemFeatureRenderer(this, context.getHeldItemRenderer()));
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
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
    protected void setupTransforms(FrostMonarchEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) {
        if (entity.deathTime > 0 || entity.isFakeDeath()) {
            float floatAmount = this.getAnimationProgress(entity, tickDelta);
            this.scale(entity, matrices, tickDelta);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - entity.bodyYaw));
        } else {
            super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta, scale);
        }

        // Push entity down initially during spawn to match animation start position
        int invulTime = entity.getInvulnerableTimer();
        if (invulTime > 0) {
            float animationTicks = Math.min(40.0f, 220 - invulTime + tickDelta);
            if (animationTicks < 1.0f) {
                matrices.translate(0.0, -42.0 * 1.4, 0.0);
            }
        }
    }

    @Override
    public void render(FrostMonarchEntity entity, float entityYaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        if (entity.deathTime > 0 || entity.isFakeDeath()) {
            entity.hurtTime = 0;
        }

        super.render(entity, entityYaw, tickDelta, matrices, vertexConsumers, light);
    }

}
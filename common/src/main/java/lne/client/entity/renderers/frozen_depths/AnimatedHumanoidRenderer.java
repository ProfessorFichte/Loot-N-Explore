package lne.client.entity.renderers.frozen_depths;

import com.mrpg_lib.compat.player_animator.api.MobAnimations;
import lne.compat.player_animator.LneMobAnimationCompat;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public abstract class AnimatedHumanoidRenderer<T extends MobEntity, M extends EntityModel<T>> extends MobEntityRenderer<T, M> {
    private static final double PLAYER_HIP_PIXELS = 12.0;
    private static final double MOVING_THRESHOLD_SQUARED = 1.0E-4;

    protected AnimatedHumanoidRenderer(EntityRendererFactory.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) {
        boolean animations = MobAnimations.isAvailable();
        // these models never turn the head on its own, so while an attack or cast animation plays on a standing
        // mob the whole model follows the synced head yaw instead of the lagging client body yaw
        if (animations && !isMoving(entity) && LneMobAnimationCompat.isAnimating(entity)) {
            bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevHeadYaw, entity.headYaw);
        }
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta, scale);
        if (animations) {
            float renderScale = animationRenderScale();
            double pivot = LneMobAnimationCompat.PLAYER_BODY_PIVOT * hipHeightPixels() / PLAYER_HIP_PIXELS * renderScale;
            LneMobAnimationCompat.replaceBodyTransform(entity, matrices, tickDelta, pivot, renderScale);
        }
    }

    private static boolean isMoving(MobEntity entity) {
        double dx = entity.getX() - entity.prevX;
        double dz = entity.getZ() - entity.prevZ;
        return dx * dx + dz * dz > MOVING_THRESHOLD_SQUARED;
    }

    protected float hipHeightPixels() {
        return 12.0F;
    }

    protected float animationRenderScale() {
        return 1.0F;
    }
}

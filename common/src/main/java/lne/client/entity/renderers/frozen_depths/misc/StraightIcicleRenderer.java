package lne.client.entity.renderers.frozen_depths.misc;

import lne.entity.frozen_depths.projectile.StraightIcicleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class StraightIcicleRenderer extends AbstractIcicleModelRenderer<StraightIcicleEntity> {
    private static final float TELEGRAPH_TIP_PROGRESS = 0.4F;

    public StraightIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getMinBlockLight() {
        return ICICLE_GLOW;
    }

    @Override
    protected void applyExtraTransform(MatrixStack matrices, StraightIcicleEntity entity, float tickDelta) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getYaw()));
    }

    @Override
    protected float getEmergeProgress(StraightIcicleEntity entity, float tickDelta) {
        if (entity.isTelegraphing()) {
            return TELEGRAPH_TIP_PROGRESS;
        }
        float animationProgress = entity.getAnimationProgress(tickDelta);
        if (animationProgress < 0.5F) {
            float rise = animationProgress * 2.0F;
            return entity.hasTelegraph() ? Math.max(TELEGRAPH_TIP_PROGRESS, rise) : rise;
        }
        return 2.0F - (animationProgress * 2.0F);
    }
}

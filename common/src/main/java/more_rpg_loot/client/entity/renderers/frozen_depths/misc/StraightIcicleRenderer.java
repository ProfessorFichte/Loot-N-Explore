package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import more_rpg_loot.entity.frozen_depths.projectile.StraightIcicleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class StraightIcicleRenderer extends AbstractIcicleModelRenderer<StraightIcicleEntity> {
    public StraightIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected void applyExtraTransform(MatrixStack matrices, StraightIcicleEntity entity) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getYaw()));
    }

    @Override
    protected float getEmergeProgress(StraightIcicleEntity entity, float tickDelta) {
        float animationProgress = entity.getAnimationProgress(tickDelta);
        // rises for the first half of the animation, sinks back down for the second half
        return animationProgress < 0.5F ? animationProgress * 2.0F : 2.0F - (animationProgress * 2.0F);
    }
}

package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import more_rpg_loot.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class TrackingIcicleRenderer extends AbstractIcicleModelRenderer<TrackingIcicleEntity> {
    public TrackingIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected void applyExtraTransform(MatrixStack matrices, TrackingIcicleEntity entity) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));
    }

    @Override
    protected float getEmergeProgress(TrackingIcicleEntity entity, float tickDelta) {
        return 1.0F;
    }
}

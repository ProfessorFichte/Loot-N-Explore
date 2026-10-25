package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import more_rpg_loot.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class TrackingIcicleRenderer extends AbstractIcicleModelRenderer<TrackingIcicleEntity> {
    private static final Identifier MODEL_ID = Identifier.of(MOD_ID, "entity/frozen_depths/icicle_projectile");
    private static final float MODEL_YAW_OFFSET = 180.0F;

    public TrackingIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getModelId() {
        return MODEL_ID;
    }

    @Override
    protected void applyExtraTransform(MatrixStack matrices, TrackingIcicleEntity entity, float tickDelta) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getYaw()) + MODEL_YAW_OFFSET));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
    }

    @Override
    protected void applyModelTransform(MatrixStack matrices) {
        matrices.translate(-0.4375, -0.4375, -0.5);
    }

    @Override
    protected float getEmergeProgress(TrackingIcicleEntity entity, float tickDelta) {
        return 1.0F;
    }
}

package lne.client.entity.renderers.frozen_depths.misc;

import lne.entity.frozen_depths.conjured.IceWallEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import static lne.LneMod.MOD_ID;

public class IceWallRenderer extends AbstractIcicleModelRenderer<IceWallEntity> {
    private static final Identifier MODEL_ID = Identifier.of(MOD_ID, "block/frozen_depths/ice_wall");
    private static final float SCALE = 1.5F;
    private static final float MODEL_HEIGHT = 29.0F / 16.0F * SCALE;

    public IceWallRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getModelId(IceWallEntity entity) {
        return MODEL_ID;
    }

    @Override
    protected float getModelHeight() {
        return MODEL_HEIGHT;
    }

    @Override
    protected float getEmergeThreshold() {
        return 0.01F;
    }

    @Override
    protected void applyExtraTransform(MatrixStack matrices, IceWallEntity entity, float tickDelta) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getYaw()));
    }

    @Override
    protected void applyModelTransform(MatrixStack matrices) {
        matrices.scale(SCALE, SCALE, SCALE);
        matrices.translate(-0.5, 0.0, -0.5);
    }

    @Override
    protected float getEmergeProgress(IceWallEntity entity, float tickDelta) {
        return entity.getAnimationProgress(tickDelta);
    }
}

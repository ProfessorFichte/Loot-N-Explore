package lne.client.entity.renderers.frozen_depths.frozen_ranger;

import lne.client.entity.models.frozen_depths.frozen_ranger.FrozenRangerModel;
import lne.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import lne.entity.frozen_depths.mob.frozen_ranger.FrozenRangerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class FrozenRangerHeldItemFeatureRenderer<T extends FrozenRangerEntity> extends AbstractHeldItemFeatureRenderer<T, FrozenRangerModel<T>> {
    public FrozenRangerHeldItemFeatureRenderer(FeatureRendererContext<T, FrozenRangerModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }

    @Override
    protected void applyGripOffset(MatrixStack matrices, boolean leftHand) {
        matrices.translate((float) (leftHand ? -0.1 : 0.1) / 16.0F, 0.1125F, -0.015F);
    }
}

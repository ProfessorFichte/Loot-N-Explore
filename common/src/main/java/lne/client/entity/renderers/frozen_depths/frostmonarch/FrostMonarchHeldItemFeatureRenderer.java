package lne.client.entity.renderers.frozen_depths.frostmonarch;

import lne.client.entity.models.frozen_depths.frostmonarch.FrostMonarchModel;
import lne.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class FrostMonarchHeldItemFeatureRenderer extends AbstractHeldItemFeatureRenderer<FrostMonarchEntity, FrostMonarchModel> {
    public FrostMonarchHeldItemFeatureRenderer(FeatureRendererContext<FrostMonarchEntity, FrostMonarchModel> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }

    @Override
    protected boolean shouldRender(FrostMonarchEntity entity) {
        return entity.deathTime <= 0 && !entity.isFakeDeath();
    }
}

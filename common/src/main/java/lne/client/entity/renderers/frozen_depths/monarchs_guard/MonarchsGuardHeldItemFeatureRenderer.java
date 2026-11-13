package lne.client.entity.renderers.frozen_depths.monarchs_guard;

import lne.client.entity.models.frozen_depths.monarchs_guard.MonarchsGuardModel;
import lne.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import lne.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class MonarchsGuardHeldItemFeatureRenderer<T extends MonarchsGuardEntity> extends AbstractHeldItemFeatureRenderer<T, MonarchsGuardModel<T>> {
    public MonarchsGuardHeldItemFeatureRenderer(FeatureRendererContext<T, MonarchsGuardModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

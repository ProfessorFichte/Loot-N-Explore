package lne.client.entity.renderers.frozen_depths.monarchs_soldier;

import lne.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import lne.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import lne.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class MonarchsSoldierHeldItemFeatureRenderer<T extends MonarchsSoldierEntity> extends AbstractHeldItemFeatureRenderer<T, MonarchsSoldierModel<T>> {
    public MonarchsSoldierHeldItemFeatureRenderer(FeatureRendererContext<T, MonarchsSoldierModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

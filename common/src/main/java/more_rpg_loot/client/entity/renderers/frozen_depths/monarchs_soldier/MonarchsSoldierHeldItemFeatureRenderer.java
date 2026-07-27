package more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_soldier;

import more_rpg_loot.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import more_rpg_loot.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class MonarchsSoldierHeldItemFeatureRenderer<T extends MonarchsSoldierEntity> extends AbstractHeldItemFeatureRenderer<T, MonarchsSoldierModel<T>> {
    public MonarchsSoldierHeldItemFeatureRenderer(FeatureRendererContext<T, MonarchsSoldierModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

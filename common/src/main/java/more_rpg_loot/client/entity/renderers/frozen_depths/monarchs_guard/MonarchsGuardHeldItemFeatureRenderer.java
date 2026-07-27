package more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_guard;

import more_rpg_loot.client.entity.models.frozen_depths.monarchs_guard.MonarchsGuardModel;
import more_rpg_loot.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class MonarchsGuardHeldItemFeatureRenderer extends AbstractHeldItemFeatureRenderer<MonarchsGuardEntity, MonarchsGuardModel> {
    public MonarchsGuardHeldItemFeatureRenderer(FeatureRendererContext<MonarchsGuardEntity, MonarchsGuardModel> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

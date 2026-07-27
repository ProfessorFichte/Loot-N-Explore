package more_rpg_loot.client.entity.renderers.frozen_depths.frosthaunt.features;

import more_rpg_loot.client.entity.models.frozen_depths.frosthaunt.FrosthauntModel;
import more_rpg_loot.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import more_rpg_loot.entity.frozen_depths.mob.frosthaunt.FrosthauntEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class FrosthauntHeldItemFeatureRenderer extends AbstractHeldItemFeatureRenderer<FrosthauntEntity, FrosthauntModel> {
    public FrosthauntHeldItemFeatureRenderer(FeatureRendererContext<FrosthauntEntity, FrosthauntModel> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

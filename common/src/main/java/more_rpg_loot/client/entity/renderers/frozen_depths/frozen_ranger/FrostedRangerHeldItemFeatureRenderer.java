package more_rpg_loot.client.entity.renderers.frozen_depths.frozen_ranger;

import more_rpg_loot.client.entity.models.frozen_depths.frozen_ranger.FrostedRangerModel;
import more_rpg_loot.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.FrostedRangerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class FrostedRangerHeldItemFeatureRenderer<T extends FrostedRangerEntity> extends AbstractHeldItemFeatureRenderer<T, FrostedRangerModel<T>> {
    public FrostedRangerHeldItemFeatureRenderer(FeatureRendererContext<T, FrostedRangerModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

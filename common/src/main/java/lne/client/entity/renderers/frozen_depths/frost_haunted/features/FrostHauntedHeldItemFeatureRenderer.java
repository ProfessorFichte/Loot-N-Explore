package lne.client.entity.renderers.frozen_depths.frost_haunted.features;

import lne.client.entity.models.frozen_depths.frost_haunted.FrostHauntedModel;
import lne.client.entity.renderers.frozen_depths.AbstractHeldItemFeatureRenderer;
import lne.entity.frozen_depths.mob.frost_haunted.FrostHauntedEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;

public class FrostHauntedHeldItemFeatureRenderer extends AbstractHeldItemFeatureRenderer<FrostHauntedEntity, FrostHauntedModel> {
    public FrostHauntedHeldItemFeatureRenderer(FeatureRendererContext<FrostHauntedEntity, FrostHauntedModel> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
    }
}

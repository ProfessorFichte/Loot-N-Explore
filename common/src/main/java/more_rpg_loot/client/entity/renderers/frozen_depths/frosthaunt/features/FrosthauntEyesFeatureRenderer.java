package more_rpg_loot.client.entity.renderers.frozen_depths.frosthaunt.features;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class FrosthauntEyesFeatureRenderer<T extends MobEntity, M extends EntityModel<T>> extends EyesFeatureRenderer<T, M> {
    private static final RenderLayer SKIN = RenderLayer.getEyes(Identifier.of("loot_n_explore:textures/entity/mobs/frosthaunt_eyes.png"));

    public FrosthauntEyesFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}

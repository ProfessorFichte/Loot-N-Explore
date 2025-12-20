package more_rpg_loot.client.entity.renderers.frosthaunt;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frosthaunt.FrosthauntModel;
import more_rpg_loot.client.entity.renderers.frosthaunt.features.FrosthauntEyesFeatureRenderer;
import more_rpg_loot.client.entity.renderers.frosthaunt.features.FrosthauntHeldItemFeatureRenderer;
import more_rpg_loot.entity.mob.FrosthauntEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class FrosthauntEntityRenderer extends MobEntityRenderer<FrosthauntEntity, FrosthauntModel> {
    private static final Identifier TEXTURE = Identifier.of("loot_n_explore", "textures/entity/frosthaunt.png");

    public FrosthauntEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FrosthauntModel(context.getPart(EntityModelLayers.FROSTHAUNT)), 0.5f);
        this.addFeature(new FrosthauntHeldItemFeatureRenderer(this, context.getHeldItemRenderer()));
        this.addFeature(new FrosthauntEyesFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(FrosthauntEntity entity) {
        return TEXTURE;
    }
}

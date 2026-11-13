package lne.client.entity.renderers.frozen_depths.frost_hound;

import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.frost_hound.FrostHoundModel;
import lne.entity.frozen_depths.mob.frost_hound.FrostHoundEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class FrostHoundRenderer extends MobEntityRenderer<FrostHoundEntity, FrostHoundModel> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/frozen_depths/mobs/frost_hound.png");

    private static final Identifier EYES_TEXTURE = Identifier.of(MOD_ID, "textures/entity/frozen_depths/mobs/frost_hound_eyes.png");

    public FrostHoundRenderer(EntityRendererFactory.Context context) {
        super(context, new FrostHoundModel(context.getPart(EntityModelLayers.FROST_HOUND)), 0.5f);
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
    }

    @Override
    public Identifier getTexture(FrostHoundEntity entity) {
        return TEXTURE;
    }
}

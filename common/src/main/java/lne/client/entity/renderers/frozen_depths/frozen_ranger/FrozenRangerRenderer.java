package lne.client.entity.renderers.frozen_depths.frozen_ranger;

import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.frozen_ranger.FrozenRangerModel;
import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.entity.frozen_depths.mob.frozen_ranger.FrozenRangerEntity;
import lne.entity.frozen_depths.mob.frozen_ranger.GeneralFrozenRangerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import lne.client.entity.renderers.frozen_depths.AnimatedHumanoidRenderer;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class FrozenRangerRenderer<T extends FrozenRangerEntity> extends AnimatedHumanoidRenderer<T, FrozenRangerModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frozen_ranger.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/general_frozen_ranger.png");
    private static final Identifier EYES_TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frozen_ranger_eyes.png");

    public FrozenRangerRenderer(EntityRendererFactory.Context context) {
        super(context, new FrozenRangerModel<>(context.getPart(EntityModelLayers.FROZEN_RANGER)), 0.5f);
        this.addFeature(new FrozenRangerHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralFrozenRangerEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

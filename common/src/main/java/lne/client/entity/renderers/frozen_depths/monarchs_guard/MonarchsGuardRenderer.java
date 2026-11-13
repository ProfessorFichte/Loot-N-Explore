package lne.client.entity.renderers.frozen_depths.monarchs_guard;

import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.monarchs_guard.MonarchsGuardModel;
import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.entity.frozen_depths.mob.monarchs_guard.GeneralMonarchsGuardEntity;
import lne.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import lne.client.entity.renderers.frozen_depths.AnimatedHumanoidRenderer;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class MonarchsGuardRenderer<T extends MonarchsGuardEntity> extends AnimatedHumanoidRenderer<T, MonarchsGuardModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/monarchs_guard.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/general_monarchs_guard.png");
    private static final Identifier EYES_TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/monarchs_guard_eyes.png");

    public MonarchsGuardRenderer(EntityRendererFactory.Context context) {
        super(context, new MonarchsGuardModel<>(context.getPart(EntityModelLayers.MONARCHS_GUARD)), 0.5f);
        this.addFeature(new MonarchsGuardHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralMonarchsGuardEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

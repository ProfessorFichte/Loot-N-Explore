package lne.client.entity.renderers.frozen_depths.monarchs_soldier;

import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.entity.frozen_depths.mob.monarchs_soldier.GeneralMonarchsSoldierEntity;
import lne.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import lne.client.entity.renderers.frozen_depths.AnimatedHumanoidRenderer;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class MonarchsSoldierRenderer<T extends MonarchsSoldierEntity> extends AnimatedHumanoidRenderer<T, MonarchsSoldierModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frozen_soldier.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/general_frozen_soldier.png");
    private static final Identifier EYES_TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frozen_soldier_eyes.png");

    public MonarchsSoldierRenderer(EntityRendererFactory.Context context) {
        super(context, new MonarchsSoldierModel<>(context.getPart(EntityModelLayers.MONARCHS_SOLDIER)), 0.5f);
        this.addFeature(new MonarchsSoldierHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralMonarchsSoldierEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

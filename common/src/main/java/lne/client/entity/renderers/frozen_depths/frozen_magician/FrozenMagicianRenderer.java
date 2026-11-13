package lne.client.entity.renderers.frozen_depths.frozen_magician;

import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.frozen_magician.FrozenMagicianModel;
import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.entity.frozen_depths.mob.frozen_magician.GeneralFrozenMagicianEntity;
import lne.entity.frozen_depths.mob.frozen_magician.FrozenMagicianEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import lne.client.entity.renderers.frozen_depths.AnimatedHumanoidRenderer;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class FrozenMagicianRenderer<T extends FrozenMagicianEntity> extends AnimatedHumanoidRenderer<T, FrozenMagicianModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frozen_magician.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/general_frozen_magician.png");
    private static final Identifier EYES_TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frozen_magician_eyes.png");

    public FrozenMagicianRenderer(EntityRendererFactory.Context context) {
        super(context, new FrozenMagicianModel<>(context.getPart(EntityModelLayers.FROZEN_MAGICIAN)), 0.5f);
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralFrozenMagicianEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

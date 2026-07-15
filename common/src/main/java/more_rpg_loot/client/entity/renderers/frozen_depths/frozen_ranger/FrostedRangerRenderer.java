package more_rpg_loot.client.entity.renderers.frozen_depths.frozen_ranger;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frozen_depths.frozen_ranger.FrostedRangerModel;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.FrostedRangerEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.GeneralFrostedRangerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrostedRangerRenderer<T extends FrostedRangerEntity> extends MobEntityRenderer<T, FrostedRangerModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/mobs/frosted_ranger.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(MOD_ID, "textures/entity/mobs/general_frosted_ranger.png");

    public FrostedRangerRenderer(EntityRendererFactory.Context context) {
        super(context, new FrostedRangerModel<>(context.getPart(EntityModelLayers.FROSTED_RANGER)), 0.5f);
        this.addFeature(new FrostedRangerHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralFrostedRangerEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

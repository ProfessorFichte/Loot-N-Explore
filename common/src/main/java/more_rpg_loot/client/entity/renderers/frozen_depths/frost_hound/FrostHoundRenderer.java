package more_rpg_loot.client.entity.renderers.frozen_depths.frost_hound;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frozen_depths.frost_hound.FrostHoundModel;
import more_rpg_loot.entity.frozen_depths.mob.frost_hound.FrostHoundEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrostHoundRenderer extends MobEntityRenderer<FrostHoundEntity, FrostHoundModel> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/mobs/frost_hound.png");

    public FrostHoundRenderer(EntityRendererFactory.Context context) {
        super(context, new FrostHoundModel(context.getPart(EntityModelLayers.FROST_HOUND)), 0.5f);
    }

    @Override
    public Identifier getTexture(FrostHoundEntity entity) {
        return TEXTURE;
    }
}

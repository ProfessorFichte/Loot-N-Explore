package more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_guard;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frozen_depths.monarchs_guard.MonarchsGuardModel;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class MonarchsGuardRenderer extends MobEntityRenderer<MonarchsGuardEntity, MonarchsGuardModel> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/mobs/monarchs_guard.png");

    public MonarchsGuardRenderer(EntityRendererFactory.Context context) {
        super(context, new MonarchsGuardModel(context.getPart(EntityModelLayers.MONARCHS_GUARD)), 0.5f);
        this.addFeature(new MonarchsGuardHeldItemFeatureRenderer(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(MonarchsGuardEntity entity) {
        return TEXTURE;
    }
}

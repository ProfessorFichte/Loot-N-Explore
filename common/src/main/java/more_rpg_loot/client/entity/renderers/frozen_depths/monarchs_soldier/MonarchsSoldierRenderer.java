package more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_soldier;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.GeneralMonarchsSoldierEntity;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class MonarchsSoldierRenderer<T extends MonarchsSoldierEntity> extends MobEntityRenderer<T, MonarchsSoldierModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/mobs/monarchs_soldier.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(MOD_ID, "textures/entity/mobs/general_monarchs_soldier.png");

    public MonarchsSoldierRenderer(EntityRendererFactory.Context context) {
        super(context, new MonarchsSoldierModel<>(context.getPart(EntityModelLayers.MONARCHS_SOLDIER)), 0.5f);
        this.addFeature(new MonarchsSoldierHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralMonarchsSoldierEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

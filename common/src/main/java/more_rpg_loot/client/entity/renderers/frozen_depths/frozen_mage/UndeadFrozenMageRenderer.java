package more_rpg_loot.client.entity.renderers.frozen_depths.frozen_mage;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.models.frozen_depths.frozen_mage.UndeadFrozenMageModel;
import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.GeneralUndeadFrozenMageEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.UndeadFrozenMageEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class UndeadFrozenMageRenderer<T extends UndeadFrozenMageEntity> extends MobEntityRenderer<T, UndeadFrozenMageModel<T>> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/mobs/undead_frozen_mage.png");
    private static final Identifier TEXTURE_GENERAL = Identifier.of(MOD_ID, "textures/entity/mobs/general_undead_frozen_mage.png");

    public UndeadFrozenMageRenderer(EntityRendererFactory.Context context) {
        super(context, new UndeadFrozenMageModel<>(context.getPart(EntityModelLayers.UNDEAD_FROZEN_MAGE)), 0.5f);
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity instanceof GeneralUndeadFrozenMageEntity ? TEXTURE_GENERAL : TEXTURE;
    }
}

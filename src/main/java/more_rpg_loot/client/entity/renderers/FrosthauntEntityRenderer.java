package more_rpg_loot.client.entity.renderers;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class FrosthauntEntityRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE =
            Identifier.of(MOD_ID, "textures/entity/mobs/frosthaunt.png");

    public FrosthauntEntityRenderer(EntityRendererFactory.Context context) {
        super(context, EntityModelLayers.SKELETON, EntityModelLayers.SKELETON_INNER_ARMOR, EntityModelLayers.SKELETON_OUTER_ARMOR);
    }

    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
}

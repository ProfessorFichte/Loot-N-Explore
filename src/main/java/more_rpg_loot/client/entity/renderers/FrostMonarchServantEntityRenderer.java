package more_rpg_loot.client.entity.renderers;


import more_rpg_loot.client.entity.renderers.feature.FrostMonarchServantOverlayFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class FrostMonarchServantEntityRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE =
            Identifier.of(MOD_ID, "textures/entity/mobs/frostmonarch_servant.png");

    public FrostMonarchServantEntityRenderer(EntityRendererFactory.Context context) {
        super(context, EntityModelLayers.SKELETON, EntityModelLayers.STRAY_INNER_ARMOR, EntityModelLayers.STRAY_OUTER_ARMOR);
        this.addFeature(new FrostMonarchServantOverlayFeatureRenderer<>(this, context.getModelLoader()));
    }

    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
}

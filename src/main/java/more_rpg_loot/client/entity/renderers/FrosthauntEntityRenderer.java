package more_rpg_loot.client.entity.renderers;


import more_rpg_loot.client.entity.renderers.feature.FrosthauntEyesFeatureRenderer;
import more_rpg_loot.client.entity.renderers.feature.FrosthauntOverlayFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class FrosthauntEntityRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE =
            Identifier.of(MOD_ID, "textures/entity/mobs/frosthaunt.png");

    public FrosthauntEntityRenderer(EntityRendererFactory.Context context) {
        super(context, EntityModelLayers.SKELETON, EntityModelLayers.STRAY_INNER_ARMOR, EntityModelLayers.STRAY_OUTER_ARMOR);
        this.addFeature(new FrosthauntEyesFeatureRenderer<>(this));
        this.addFeature(new FrosthauntOverlayFeatureRenderer<>(this, context.getModelLoader()));
    }

    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
    protected void scale(AbstractSkeletonEntity abstractSkeletonEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
    }
}

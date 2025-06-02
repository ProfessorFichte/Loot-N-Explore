package more_rpg_loot.client.entity.renderers.feature;

import more_rpg_loot.client.entity.models.EntityModelLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class FrostmonarchOverlayFeatureRenderer<T extends MobEntity & RangedAttackMob, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier TEXTURE =
            Identifier.of(MOD_ID, "textures/entity/mobs/frostmonarch_overlay.png");
    private final SkeletonEntityModel<T> model;
    private final Identifier texture;

    public FrostmonarchOverlayFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader, EntityModelLayer layer, Identifier texture) {
        super(context);
        this.model = new SkeletonEntityModel<>(loader.getModelPart(EntityModelLayers.FROST_MONARCH_OUTER));
        this.texture = texture;
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T mobEntity, float f, float g, float h, float j, float k, float l) {
        render(this.getContextModel(), this.model, this.texture, matrixStack, vertexConsumerProvider, i, mobEntity, f, g, j, k, l, h, -1);
    }
}

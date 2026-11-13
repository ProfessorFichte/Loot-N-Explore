package lne.client.entity.renderers.frozen_depths.glaze;

import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.glaze.GlazeModel;
import lne.entity.frozen_depths.mob.glaze.GlazeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static lne.LneMod.MOD_ID;

public class GlazeEntityRenderer extends MobEntityRenderer<GlazeEntity, GlazeModel> {
    private static final Identifier TEXTURE =
            Identifier.of(MOD_ID, "textures/entity/frozen_depths/mobs/glaze.png");

    public GlazeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GlazeModel(context.getPart(EntityModelLayers.GLAZE)), 0.5F);
    }

    protected int getBlockLight(GlazeEntity glazeEntity, BlockPos blockPos) {
        return 15;
    }

    public Identifier getTexture(GlazeEntity glazeEntity) {
        return TEXTURE;
    }


}



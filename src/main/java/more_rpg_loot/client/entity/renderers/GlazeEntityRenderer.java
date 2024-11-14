package more_rpg_loot.client.entity.renderers;

import more_rpg_loot.client.entity.models.GlazeEntityModel;
import more_rpg_loot.entity.mob.GlazeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class GlazeEntityRenderer extends MobEntityRenderer<GlazeEntity, GlazeEntityModel<GlazeEntity>> {
    private static final Identifier TEXTURE =
            Identifier.of(MOD_ID, "textures/entity/mobs/glaze.png");

    public GlazeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GlazeEntityModel(context.getPart(EntityModelLayers.BLAZE)), 0.5F);
    }

    protected int getBlockLight(GlazeEntity glazeEntity, BlockPos blockPos) {
        return 15;
    }

    public Identifier getTexture(GlazeEntity glazeEntity) {
        return TEXTURE;
    }
}


package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import more_rpg_loot.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class TrackingIcicleRenderer extends EntityRenderer<TrackingIcicleEntity> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/projectiles/tracking_icicle.png");

    public TrackingIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(TrackingIcicleEntity entity) {
        return TEXTURE;
    }
}

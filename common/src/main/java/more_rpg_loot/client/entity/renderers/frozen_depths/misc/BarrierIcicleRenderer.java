package more_rpg_loot.client.entity.renderers.frozen_depths.misc;

import more_rpg_loot.entity.frozen_depths.projectile.BarrierIcicleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class BarrierIcicleRenderer extends AbstractIcicleModelRenderer<BarrierIcicleEntity> {
    public BarrierIcicleRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected float getEmergeThreshold() {
        return 0.01F;
    }

    @Override
    protected float getEmergeProgress(BarrierIcicleEntity entity, float tickDelta) {
        return entity.getAnimationProgress(tickDelta);
    }
}

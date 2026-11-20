package lne.client.entity.renderers.frozen_depths.misc;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.util.math.BlockPos;

public class GlowingFlyingItemRenderer<T extends Entity & FlyingItemEntity> extends FlyingItemEntityRenderer<T> {
    private static final int GLOW = 10;

    public GlowingFlyingItemRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return Math.max(super.getBlockLight(entity, pos), GLOW);
    }
}

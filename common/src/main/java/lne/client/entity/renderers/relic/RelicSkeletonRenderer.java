package lne.client.entity.renderers.relic;

import org.jetbrains.annotations.Nullable;
import net.minecraft.client.render.RenderLayer;
import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.entity.relic.EmergingMinion;
import lne.entity.relic.RelicSkeletonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class RelicSkeletonRenderer extends SkeletonEntityRenderer<RelicSkeletonEntity> {
    private static final Identifier EYES_TEXTURE = Identifier.of(MOD_ID, "textures/entity/relic/relic_skeleton_eyes.png");
    private static final OptionalTexture TEXTURE = new OptionalTexture(
            Identifier.of(MOD_ID, "textures/entity/relic/relic_skeleton.png"),
            Identifier.ofVanilla("textures/entity/skeleton/skeleton.png"));

    public RelicSkeletonRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.addFeature(new SpectralBodyFeatureRenderer<>(this, entity -> TEXTURE.get(), entity -> true, null,
                Identifier.of(MOD_ID, "textures/entity/relic/relic_skeleton_glow.png")));
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE));
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(RelicSkeletonEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return null;
    }

    @Override
    public Identifier getTexture(RelicSkeletonEntity entity) {
        return TEXTURE.get();
    }

    @Override
    protected void setupTransforms(RelicSkeletonEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta, scale);
        if (EmergingMinion.isEmerging(entity)) {
            matrices.translate(0.0F, -(1.0F - EmergingMinion.progress(entity, tickDelta)) * 2.0F, 0.0F);
        }
    }
}

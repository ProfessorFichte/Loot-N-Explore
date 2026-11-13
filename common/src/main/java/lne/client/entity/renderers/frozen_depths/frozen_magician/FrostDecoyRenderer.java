package lne.client.entity.renderers.frozen_depths.frozen_magician;

import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.frozen_magician.FrozenMagicianAnimations;
import lne.client.entity.models.frozen_depths.frozen_magician.FrozenMagicianModel;
import lne.entity.frozen_depths.conjured.FrostDecoyEntity;
import lne.entity.frozen_depths.mob.frozen_magician.FrozenMagicianEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

import static lne.LneMod.MOD_ID;

public class FrostDecoyRenderer extends EntityRenderer<FrostDecoyEntity> {
    private static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/frozen_depths/mobs/frost_decoy.png");
    private static final long POSE_TIME_MS = 600L;
    private static final int BODY_ALPHA = 0xD8;
    private static final int GLOW = 0xFF183048;
    private static final int FLASH_TICKS = 16;

    private final FrozenMagicianModel<FrozenMagicianEntity> model;
    private final Vector3f scratch = new Vector3f();

    public FrostDecoyRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new FrozenMagicianModel<>(context.getPart(EntityModelLayers.FROZEN_MAGICIAN));
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(FrostDecoyEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        float scale = entity.getScale();
        matrices.scale(scale, scale, scale);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - entity.getYaw()));
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.translate(0.0F, -1.501F, 0.0F);

        model.getPart().traverse().forEach(ModelPart::resetTransform);
        AnimationHelper.animate(model, FrozenMagicianAnimations.cast, POSE_TIME_MS, 1.0F, scratch);

        int remaining = entity.getFuse() - entity.age;
        boolean flash = remaining <= FLASH_TICKS && (entity.age / 2) % 2 == 0;
        int overlay = flash ? OverlayTexture.packUv(OverlayTexture.getU(0.6F), 10) : OverlayTexture.DEFAULT_UV;
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay, (BODY_ALPHA << 24) | 0xFFFFFF);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(TEXTURE)), light, OverlayTexture.DEFAULT_UV, GLOW);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(FrostDecoyEntity entity) {
        return TEXTURE;
    }
}

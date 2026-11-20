package lne.client.entity.renderers.frozen_depths.frost_haunted;

import org.jetbrains.annotations.Nullable;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.OverlayTexture;
import lne.client.entity.renderers.relic.SpectralBodyFeatureRenderer;
import lne.client.entity.models.EntityModelLayers;
import lne.client.entity.models.frozen_depths.frost_haunted.FrostHauntedModel;
import lne.client.entity.renderers.frozen_depths.frost_haunted.features.FrostHauntedHeldItemFeatureRenderer;
import com.mrpg_lib.client.render.GenericEyesFeatureRenderer;
import lne.entity.frozen_depths.mob.frost_haunted.FrostHauntedEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import lne.client.entity.renderers.frozen_depths.AnimatedHumanoidRenderer;
import net.minecraft.client.util.math.MatrixStack;
import lne.client.entity.renderers.relic.OptionalTexture;
import lne.entity.relic.EmergingMinion;
import lne.entity.relic.RelicFrostHauntedEntity;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class FrostHauntedEntityRenderer extends AnimatedHumanoidRenderer<FrostHauntedEntity, FrostHauntedModel> {
    private static final Identifier TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frost_haunted.png");
    private static final Identifier EYES_TEXTURE = Identifier.of(
            MOD_ID, "textures/entity/frozen_depths/mobs/frost_haunted_eyes.png");
    private static final OptionalTexture RELIC_TEXTURE = new OptionalTexture(
            Identifier.of(MOD_ID, "textures/entity/relic/relic_frost_haunted.png"), TEXTURE);
    private static final OptionalTexture RELIC_EYES_TEXTURE = new OptionalTexture(
            Identifier.of(MOD_ID, "textures/entity/relic/relic_frost_haunted_eyes.png"), EYES_TEXTURE);
    private static final Identifier RELIC_CLOTHING_TEXTURE = Identifier.of(MOD_ID, "textures/entity/relic/relic_frost_haunted_clothing.png");

    public FrostHauntedEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FrostHauntedModel(context.getPart(EntityModelLayers.FROST_HAUNTED)), 0.5f);
        this.addFeature(new FrostHauntedHeldItemFeatureRenderer(this, context.getHeldItemRenderer()));
        this.addFeature(new SpectralBodyFeatureRenderer<>(this, entity -> RELIC_TEXTURE.get(),
                entity -> entity instanceof RelicFrostHauntedEntity, RELIC_CLOTHING_TEXTURE,
                Identifier.of(MOD_ID, "textures/entity/relic/relic_frost_haunted_glow.png")));
        this.addFeature(new GenericEyesFeatureRenderer<>(this, EYES_TEXTURE) {
            @Override
            public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, FrostHauntedEntity entity,
                               float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
                if (entity instanceof RelicFrostHauntedEntity) {
                    var buffer = vertexConsumers.getBuffer(RenderLayer.getEyes(RELIC_EYES_TEXTURE.get()));
                    getContextModel().render(matrices, buffer, 0xF00000, OverlayTexture.DEFAULT_UV);
                } else {
                    super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
                }
            }
        });
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(FrostHauntedEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return entity instanceof RelicFrostHauntedEntity ? null : super.getRenderLayer(entity, showBody, translucent, showOutline);
    }

    @Override
    protected void setupTransforms(FrostHauntedEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta, scale);
        if (entity instanceof EmergingMinion && EmergingMinion.isEmerging(entity)) {
            matrices.translate(0.0F, -(1.0F - EmergingMinion.progress(entity, tickDelta)) * 2.0F, 0.0F);
        } else if (entity.isEmergingFromGround()) {
            matrices.translate(0.0F, -(1.0F - entity.groundEmergeProgress(tickDelta)) * 2.0F, 0.0F);
        }
    }

    @Override
    public Identifier getTexture(FrostHauntedEntity entity) {
        return entity instanceof RelicFrostHauntedEntity ? RELIC_TEXTURE.get() : TEXTURE;
    }
}

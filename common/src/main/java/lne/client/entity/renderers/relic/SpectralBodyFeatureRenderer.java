package lne.client.entity.renderers.relic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public class SpectralBodyFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public static final int BODY_ALPHA = 0x73;
    public static final int GLOW_STRENGTH = 0x2A;

    private final Function<T, Identifier> texture;
    private final Predicate<T> applies;
    @Nullable private final Identifier solidOverlay;
    @Nullable private final Identifier glowMap;

    public SpectralBodyFeatureRenderer(FeatureRendererContext<T, M> context, Function<T, Identifier> texture, Predicate<T> applies,
                                       @Nullable Identifier solidOverlay, @Nullable Identifier glowMap) {
        super(context);
        this.texture = texture;
        this.applies = applies;
        this.solidOverlay = solidOverlay;
        this.glowMap = glowMap;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!applies.test(entity) || entity.isInvisible()) return;
        var model = getContextModel();
        int overlay = LivingEntityRenderer.getOverlay(entity, 0.0F);
        Identifier body = texture.apply(entity);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(body)), light, overlay, (BODY_ALPHA << 24) | 0xFFFFFF);
        int glow = GLOW_STRENGTH;
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(body)), light, overlay, 0xFF000000 | (glow << 16) | (glow << 8) | glow);
        if (exists(solidOverlay)) {
            model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(solidOverlay)), light, overlay, -1);
        }
        if (exists(glowMap)) {
            model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(glowMap)), LightmapTextureManager.MAX_LIGHT_COORDINATE,
                    OverlayTexture.DEFAULT_UV, -1);
        }
    }

    private static boolean exists(@Nullable Identifier id) {
        return id != null && MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent();
    }
}

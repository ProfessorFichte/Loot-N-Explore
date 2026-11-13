package lne.client.entity.renderers.relic;

import lne.entity.relic.GuardianBeamEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class GuardianBeamRenderer extends EntityRenderer<GuardianBeamEntity> {
    private static final Identifier BEAM_TEXTURE = Identifier.ofVanilla("textures/entity/guardian_beam.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(BEAM_TEXTURE);
    private static final float WARMUP_TICKS = 100.0F;

    public GuardianBeamRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(GuardianBeamEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public void render(GuardianBeamEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Vec3d origin = entity.getLerpedPos(tickDelta);
        Vec3d start = origin;
        if (entity.owner() instanceof Entity owner) {
            Vec3d look = owner.getRotationVec(tickDelta);
            start = owner.getLerpedPos(tickDelta).add(0, owner.getStandingEyeHeight() - 0.25, 0).add(look.multiply(0.6));
        }
        Entity target = entity.target();
        Vec3d end = target != null ? target.getLerpedPos(tickDelta).add(0, target.getHeight() * 0.5, 0) : entity.end();
        Vec3d delta = end.subtract(start);
        float length = (float) delta.length();
        if (length < 0.05F) return;
        Vec3d direction = delta.normalize();
        float pitch = (float) Math.acos(MathHelper.clamp(direction.y, -1.0, 1.0));
        float heading = (float) Math.atan2(direction.z, direction.x);

        matrices.push();
        matrices.translate(start.x - origin.x, start.y - origin.y, start.z - origin.z);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - heading) * 57.295776F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch * 57.295776F));

        float time = entity.age + tickDelta;
        float scroll = time * 0.5F % 1.0F;
        float spin = time * 0.05F * -1.5F;
        float progress = MathHelper.clamp(time / WARMUP_TICKS, 0.0F, 1.0F);
        float squared = progress * progress;
        int red = 64 + (int) (squared * 191.0F);
        int green = 32 + (int) (squared * 191.0F);
        int blue = 128 - (int) (squared * 64.0F);

        float outerX1 = MathHelper.cos(spin + 2.3561945F) * 0.282F;
        float outerZ1 = MathHelper.sin(spin + 2.3561945F) * 0.282F;
        float outerX2 = MathHelper.cos(spin + 0.7853982F) * 0.282F;
        float outerZ2 = MathHelper.sin(spin + 0.7853982F) * 0.282F;
        float outerX3 = MathHelper.cos(spin + 3.926991F) * 0.282F;
        float outerZ3 = MathHelper.sin(spin + 3.926991F) * 0.282F;
        float outerX4 = MathHelper.cos(spin + 5.4977875F) * 0.282F;
        float outerZ4 = MathHelper.sin(spin + 5.4977875F) * 0.282F;
        float innerX1 = MathHelper.cos(spin + 3.1415927F) * 0.2F;
        float innerZ1 = MathHelper.sin(spin + 3.1415927F) * 0.2F;
        float innerX2 = MathHelper.cos(spin) * 0.2F;
        float innerZ2 = MathHelper.sin(spin) * 0.2F;
        float innerX3 = MathHelper.cos(spin + 1.5707964F) * 0.2F;
        float innerZ3 = MathHelper.sin(spin + 1.5707964F) * 0.2F;
        float innerX4 = MathHelper.cos(spin + 4.712389F) * 0.2F;
        float innerZ4 = MathHelper.sin(spin + 4.712389F) * 0.2F;
        float vStart = -1.0F + scroll;
        float vEnd = length * 2.5F + vStart;

        VertexConsumer buffer = vertexConsumers.getBuffer(LAYER);
        MatrixStack.Entry entry = matrices.peek();
        vertex(buffer, entry, innerX1, length, innerZ1, red, green, blue, 0.4999F, vEnd);
        vertex(buffer, entry, innerX1, 0.0F, innerZ1, red, green, blue, 0.4999F, vStart);
        vertex(buffer, entry, innerX2, 0.0F, innerZ2, red, green, blue, 0.0F, vStart);
        vertex(buffer, entry, innerX2, length, innerZ2, red, green, blue, 0.0F, vEnd);
        vertex(buffer, entry, innerX3, length, innerZ3, red, green, blue, 0.4999F, vEnd);
        vertex(buffer, entry, innerX3, 0.0F, innerZ3, red, green, blue, 0.4999F, vStart);
        vertex(buffer, entry, innerX4, 0.0F, innerZ4, red, green, blue, 0.0F, vStart);
        vertex(buffer, entry, innerX4, length, innerZ4, red, green, blue, 0.0F, vEnd);
        float capV = entity.age % 2 == 0 ? 0.5F : 0.0F;
        vertex(buffer, entry, outerX1, length, outerZ1, red, green, blue, 0.5F, capV + 0.5F);
        vertex(buffer, entry, outerX2, length, outerZ2, red, green, blue, 1.0F, capV + 0.5F);
        vertex(buffer, entry, outerX4, length, outerZ4, red, green, blue, 1.0F, capV);
        vertex(buffer, entry, outerX3, length, outerZ3, red, green, blue, 0.5F, capV);
        matrices.pop();
    }

    private static void vertex(VertexConsumer buffer, MatrixStack.Entry entry, float x, float y, float z, int red, int green, int blue, float u, float v) {
        buffer.vertex(entry, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV)
                .light(15728880).normal(entry, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public Identifier getTexture(GuardianBeamEntity entity) {
        return BEAM_TEXTURE;
    }
}

package more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_guard;

import more_rpg_loot.entity.frozen_depths.projectile.ThrownLanceEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class ThrownLanceEntityRenderer extends EntityRenderer<ThrownLanceEntity> {
    private final ItemRenderer itemRenderer;

    public ThrownLanceEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownLanceEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Render the raw model (ModelTransformationMode.NONE - no baked "held in hand" rotation)
        // and orient it purely from travel direction so the tip points cleanly along the flight
        // path - stacking our rotation on top of THIRD_PERSON_RIGHT_HAND's baked angle (previous
        // attempt) fought against it instead.
        Vec3d velocity = entity.getVelocity().normalize();
        float directionYaw = (float) (Math.toDegrees(Math.atan2(velocity.x, velocity.z)) + 180.0);
        float directionPitch = (float) Math.toDegrees(Math.asin(velocity.y));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(directionYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(directionPitch));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        matrices.scale(1.7F, 1.7F, 1.7F);

        this.itemRenderer.renderItem(entity.getStack(), ModelTransformationMode.NONE,
                light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), entity.getId());

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(ThrownLanceEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

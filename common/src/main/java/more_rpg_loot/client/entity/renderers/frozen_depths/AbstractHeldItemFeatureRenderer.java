package more_rpg_loot.client.entity.renderers.frozen_depths;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

/**
 * Shared held-item rendering for the frozen_depths mob rigs. Main hand renders on the model's
 * left arm and off hand on the right - that's where each rig's weapon socket sits, swapped from
 * the naive getMainArm()-based vanilla mapping.
 */
public abstract class AbstractHeldItemFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M> {
    private final HeldItemRenderer heldItemRenderer;

    protected AbstractHeldItemFeatureRenderer(FeatureRendererContext<T, M> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    protected boolean shouldRender(T entity) {
        return true;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!shouldRender(entity)) {
            return;
        }

        ItemStack mainHandStack = entity.getMainHandStack();
        ItemStack offHandStack = entity.getOffHandStack();

        if (!mainHandStack.isEmpty() || !offHandStack.isEmpty()) {
            matrices.push();

            this.renderItem(entity, mainHandStack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrices, vertexConsumers, light);
            this.renderItem(entity, offHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrices, vertexConsumers, light);

            matrices.pop();
        }
    }

    private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (stack.isEmpty()) {
            return;
        }

        matrices.push();

        this.getContextModel().setArmAngle(arm, matrices);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

        boolean leftHand = arm == Arm.LEFT;
        matrices.translate((float) (leftHand ? -0.1 : 0.1) / 16.0F, 0.1125F, -0.015F);

        this.heldItemRenderer.renderItem(entity, stack, transformationMode, leftHand, matrices, vertexConsumers, light);

        matrices.pop();
    }
}

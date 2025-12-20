package more_rpg_loot.client.entity.renderers.frostmonarch.features;

import more_rpg_loot.client.entity.models.frostmonarch.FrostMonarchModel;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

public class FrostMonarchHeldItemFeatureRenderer extends FeatureRenderer<FrostMonarchEntity, FrostMonarchModel> {
    private final HeldItemRenderer heldItemRenderer;

    public FrostMonarchHeldItemFeatureRenderer(FeatureRendererContext<FrostMonarchEntity, FrostMonarchModel> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, FrostMonarchEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        // Don't render held items during death animation (including fake death)
        if (entity.deathTime > 0 || entity.isFakeDeath()) {
            return;
        }

        boolean rightHanded = entity.getMainArm() == Arm.RIGHT;
        ItemStack mainHandStack = rightHanded ? entity.getMainHandStack() : entity.getOffHandStack();
        ItemStack offHandStack = rightHanded ? entity.getOffHandStack() : entity.getMainHandStack();

        if (!mainHandStack.isEmpty() || !offHandStack.isEmpty()) {
            matrices.push();

            // Render right hand item
            this.renderItem(entity, mainHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrices, vertexConsumers, light);
            // Render left hand item
            this.renderItem(entity, offHandStack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrices, vertexConsumers, light);

            matrices.pop();
        }
    }

    private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!stack.isEmpty()) {
            matrices.push();

            // Use the model's setArmAngle method to properly position at the hand
            // This handles all parent transformations and animations correctly
            this.getContextModel().setArmAngle(arm, matrices);

            // Standard vanilla item rotation (makes it look like it's being held)
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            // Custom item position offset for FrostMonarch model
            boolean leftHand = arm == Arm.LEFT;
            // Using the same values as Frosthaunt that the user found to work correctly
            matrices.translate((float)(leftHand ? -0.1 : 0.1) / 16.0F, 0.1125F, -0.015F);

            this.heldItemRenderer.renderItem(entity, stack, transformationMode, leftHand, matrices, vertexConsumers, light);

            matrices.pop();
        }
    }
}

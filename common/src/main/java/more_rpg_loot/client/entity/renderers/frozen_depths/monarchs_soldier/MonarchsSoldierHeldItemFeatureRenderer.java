package more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_soldier;

import more_rpg_loot.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
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

public class MonarchsSoldierHeldItemFeatureRenderer<T extends MonarchsSoldierEntity> extends FeatureRenderer<T, MonarchsSoldierModel<T>> {
    private final HeldItemRenderer heldItemRenderer;

    public MonarchsSoldierHeldItemFeatureRenderer(FeatureRendererContext<T, MonarchsSoldierModel<T>> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        boolean rightHanded = entity.getMainArm() == Arm.RIGHT;
        ItemStack mainHandStack = rightHanded ? entity.getMainHandStack() : entity.getOffHandStack();
        ItemStack offHandStack = rightHanded ? entity.getOffHandStack() : entity.getMainHandStack();

        if (!mainHandStack.isEmpty() || !offHandStack.isEmpty()) {
            matrices.push();

            this.renderItem(entity, mainHandStack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrices, vertexConsumers, light);
            this.renderItem(entity, offHandStack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrices, vertexConsumers, light);

            matrices.pop();
        }
    }

    private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!stack.isEmpty()) {
            matrices.push();

            this.getContextModel().setArmAngle(arm, matrices);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            boolean leftHand = arm == Arm.LEFT;
            matrices.translate((float)(leftHand ? -0.1 : 0.1) / 16.0F, 0.1125F, -0.015F);

            this.heldItemRenderer.renderItem(entity, stack, transformationMode, leftHand, matrices, vertexConsumers, light);

            matrices.pop();
        }
    }
}

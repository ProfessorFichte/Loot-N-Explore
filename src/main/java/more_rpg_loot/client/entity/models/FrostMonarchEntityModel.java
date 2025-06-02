package more_rpg_loot.client.entity.models;

import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class FrostMonarchEntityModel extends BipedEntityModel<FrostMonarchEntity> {
    public FrostMonarchEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getInnerArmorModelData() {
        return createBaseModelData();
    }

    public static TexturedModelData getOuterArmorModelData() {
        return createBaseModelData();
    }

    public static TexturedModelData getOverlayModelData() {
        return createBaseModelData();
    }

    public static TexturedModelData createBaseModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        addLimbs(modelPartData);
        return TexturedModelData.of(modelData, 64, 32);
    }
    protected static void addLimbs(ModelPartData data) {
        data.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        data.addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
        data.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));
        data.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), ModelTransform.pivot(2.0F, 12.0F, 0.0F));
    }

    @Override
    public void animateModel(FrostMonarchEntity mobEntity, float f, float g, float h) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;

        ItemStack mainHandStack = mobEntity.getStackInHand(Hand.MAIN_HAND);
        if (mainHandStack.isOf(Items.BOW) && mobEntity.isAttacking()) {
            if (mobEntity.getMainArm() == Arm.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }

        if (!mobEntity.getOffHandStack().isEmpty() && mobEntity.canHeal()) {
            this.leftArmPose = ArmPose.ITEM;
        }
        super.animateModel(mobEntity, f, g, h);
    }

    private void animateWildSlash() {
        float k = MathHelper.sin(this.handSwingProgress * (float)Math.PI);
        this.rightArm.yaw = (float)Math.toRadians(90) - k * (float)Math.toRadians(180) + MathHelper.sin(this.handSwingProgress * 10) * 0.1F;
        this.rightArm.pitch = -((float)Math.PI / 6) - k * 0.7F + MathHelper.cos(this.handSwingProgress * 15) * 0.1F;
        this.rightArm.roll = MathHelper.sin(this.handSwingProgress * (float)Math.PI * 3) * 0.4F;

        this.leftArm.yaw = MathHelper.sin(this.handSwingProgress * (float)Math.PI) * 0.2F;
        this.leftArm.pitch = MathHelper.cos(this.handSwingProgress * (float)Math.PI) * 0.1F;
        this.leftArm.roll = MathHelper.sin(this.handSwingProgress * (float)Math.PI * 2) * 0.15F;
    }
    private void animateOverheadSmash() {
        float k = MathHelper.sin(this.handSwingProgress * (float)Math.PI);
        float l = MathHelper.sin((1.0F - (1.0F - this.handSwingProgress) * (1.0F - this.handSwingProgress)) * (float)Math.PI);
        this.rightArm.pitch = -((float)Math.PI / 2) - k * 1.5F;
        this.rightArm.yaw = 0.0F;
        this.rightArm.roll = 0.0F;
        this.leftArm.pitch = -((float)Math.PI / 3) - k * 0.8F;
        this.leftArm.yaw = 0.1F;
        this.leftArm.roll = 0.1F;
    }
    private void animateSideSwipe() {
        float k = MathHelper.sin(this.handSwingProgress * (float)Math.PI);
        float l = MathHelper.sin((1.0F - (1.0F - this.handSwingProgress) * (1.0F - this.handSwingProgress)) * (float)Math.PI);
        this.rightArm.pitch = -((float)Math.PI / 3) + k * 1.0F;
        this.rightArm.yaw = -k * (float)Math.PI / 1.5F;
        this.rightArm.roll = MathHelper.cos(this.handSwingProgress * (float)Math.PI * 4) * 0.2F;
        this.leftArm.pitch = -((float)Math.PI / 6);
        this.leftArm.yaw = 0.1F;
        this.leftArm.roll = 0.1F;
    }

    @Override
    public void setAngles(FrostMonarchEntity mobEntity, float f, float g, float h, float i, float j) {
        super.setAngles(mobEntity, f, g, h, i, j);
        ItemStack itemStack = mobEntity.getMainHandStack();
        if (mobEntity.isAttacking() && (itemStack.isEmpty() || !itemStack.isOf(Items.BOW))) {
            float k = MathHelper.sin(this.handSwingProgress * (float)Math.PI);
            float l = MathHelper.sin((1.0F - (1.0F - this.handSwingProgress) * (1.0F - this.handSwingProgress)) * (float)Math.PI);
            this.rightArm.roll = 0.0F;
            this.leftArm.roll = 0.0F;
            this.rightArm.yaw = -(0.1F - k * 0.6F);
            this.leftArm.yaw = 0.1F - k * 0.6F;
            this.rightArm.pitch = -((float)Math.PI / 2);
            this.leftArm.pitch = -((float)Math.PI / 2);
            this.rightArm.pitch -= k * 1.2F - l * 0.4F;
            this.leftArm.pitch -= k * 1.2F - l * 0.4F;
            CrossbowPosing.swingArms(this.rightArm, this.leftArm, k);
        }

        else if (mobEntity.canHeal()) {
            this.leftArm.pitch = -1.8F;
            this.leftArm.yaw = 0.0F;
            float angle = (mobEntity.age + h) * 0.1f;
            this.leftArm.roll = MathHelper.cos(angle) * 0.5f;
        }

        if (mobEntity.isScreeching()) {
            float raisedAngle = -1.5708F;
            this.rightArm.pitch = raisedAngle;
            this.leftArm.pitch = raisedAngle;
            this.rightArm.yaw = 0.1F;
            this.leftArm.yaw = -0.1F;

            float shakeIntensity = 0.5f;
            this.head.yaw += (mobEntity.getRandom().nextFloat() - 0.5f) * 2.0f * shakeIntensity;
            this.head.pitch += (mobEntity.getRandom().nextFloat() - 0.5f) * 2.0f * shakeIntensity;
        }
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        float f = arm == Arm.RIGHT ? 1.0F : -1.0F;
        ModelPart modelPart = this.getArm(arm);
        modelPart.pivotX += f;
        modelPart.rotate(matrices);
        modelPart.pivotX -= f;
    }
}
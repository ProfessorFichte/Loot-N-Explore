package more_rpg_loot.client.entity.models.frozen_depths.frozen_ranger;

import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.FrostedRangerEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class FrostedRangerModel<T extends FrostedRangerEntity> extends SinglePartEntityModel<T> implements ModelWithArms {
    private final ModelPart group;
    private final ModelPart waist;
    private final ModelPart Body;
    private final ModelPart cape;
    private final ModelPart Head;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart leftItem;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

    public FrostedRangerModel(ModelPart root) {
        this.group = root.getChild("group");
        this.waist = this.group.getChild("waist");
        this.Body = this.waist.getChild("Body");
        this.cape = this.Body.getChild("cape");
        this.Head = this.Body.getChild("Head");
        this.RightArm = this.Body.getChild("RightArm");
        this.LeftArm = this.Body.getChild("LeftArm");
        this.leftItem = this.LeftArm.getChild("leftItem");
        this.RightLeg = this.group.getChild("RightLeg");
        this.LeftLeg = this.group.getChild("LeftLeg");
    }

    @Override
    public ModelPart getPart() {
        return this.group;
    }

    // Getters for held item rendering
    public ModelPart getRightArm() {
        return this.RightArm;
    }

    public ModelPart getLeftArm() {
        return this.LeftArm;
    }

    public ModelPart getLeftItem() {
        return this.leftItem;
    }

    public ModelPart getWaist() {
        return this.waist;
    }

    public ModelPart getGroup() {
        return this.group;
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData group = modelPartData.addChild("group", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData waist = group.addChild("waist", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
        .uv(0, 33).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cape = Body.addChild("cape", ModelPartBuilder.create().uv(42, 1).cuboid(-5.0F, 0.0F, 0.0F, 9.0F, 15.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -12.425F, 0.75F, 0.0873F, 0.0F, 0.0F));

        ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData Head_r1 = Head.addChild("Head_r1", ModelPartBuilder.create().uv(32, 24).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.425F)), ModelTransform.of(0.0F, 0.0F, 0.3F, 0.0873F, 0.0F, 0.0F));

        ModelPartData RightArm = Body.addChild("RightArm", ModelPartBuilder.create().uv(8, 16).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

        ModelPartData RightArm_r1 = RightArm.addChild("RightArm_r1", ModelPartBuilder.create().uv(1, 49).cuboid(-3.0F, -1.0F, -2.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.075F)), ModelTransform.of(-0.075F, -1.0F, 0.5F, 0.0F, 0.0F, -0.0873F));

        ModelPartData LeftArm = Body.addChild("LeftArm", ModelPartBuilder.create().uv(8, 16).mirrored().cuboid(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

        ModelPartData LeftArm_r1 = LeftArm.addChild("LeftArm_r1", ModelPartBuilder.create().uv(14, 49).mirrored().cuboid(0.0F, -1.0F, -2.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.075F)).mirrored(false), ModelTransform.of(0.075F, -1.0F, 0.5F, 0.0F, 0.0F, 0.0873F));

        ModelPartData leftItem = LeftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 8.0F, 1.0F));

        ModelPartData RightLeg = group.addChild("RightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));

        ModelPartData LeftLeg = group.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, -12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        // Play animations based on entity state
        this.animateMovement(FrostedRangerAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, FrostedRangerAnimations.idle, ageInTicks);
        this.updateAnimation(entity.normalShootAnimationState, FrostedRangerAnimations.attackFast, ageInTicks);
        this.updateAnimation(entity.salvoAnimationState, FrostedRangerAnimations.attack, ageInTicks);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        group.render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        // Get the appropriate arm based on which side
        ModelPart armPart = arm == Arm.RIGHT ? this.RightArm : this.LeftArm;
        ModelPart handPart = arm == Arm.LEFT ? this.leftItem : null; // Only left hand has the item part defined

        // Apply transformations through the model hierarchy
        // group -> waist -> Body -> arm -> hand position
        this.applyPartTransform(matrices, this.group);
        this.applyPartTransform(matrices, this.waist);
        this.applyPartTransform(matrices, this.Body);
        this.applyPartTransform(matrices, armPart);

        // If there's a hand part (leftItem), use it. Otherwise calculate hand position
        if (handPart != null) {
            this.applyPartTransform(matrices, handPart);
        } else {
            // For right arm, manually position at hand location (end of arm)
            // LeftArm hand is at (2.0, 8, 1), RightArm hand is mirrored at (-2.0, 8, 1)
            matrices.translate(-0.125F, 0.5F, 0.0625F); // (-2/16, 8/16, 1/16)
        }
    }

    private void applyPartTransform(MatrixStack matrices, ModelPart part) {
        // ModelPart.rotate() handles both pivot translation and rotations
        part.rotate(matrices);
    }
}

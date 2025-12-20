package more_rpg_loot.client.entity.models.frosthaunt;

import more_rpg_loot.entity.mob.FrosthauntEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

// Made with Blockbench 5.0.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class FrosthauntModel extends SinglePartEntityModel<FrosthauntEntity> implements ModelWithArms {
	private final ModelPart bone;
	private final ModelPart waist;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart RightArm;
	private final ModelPart rightitem;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public FrosthauntModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.waist = this.bone.getChild("waist");
		this.Body = this.waist.getChild("Body");
		this.Head = this.Body.getChild("Head");
		this.RightArm = this.waist.getChild("RightArm");
		this.rightitem = this.RightArm.getChild("rightitem");
		this.LeftArm = this.waist.getChild("LeftArm");
		this.RightLeg = this.bone.getChild("RightLeg");
		this.LeftLeg = this.bone.getChild("LeftLeg");
	}

	@Override
	public ModelPart getPart() {
		return this.bone;
	}

	// Getters for held item rendering
	public ModelPart getRightArm() {
		return this.RightArm;
	}

	public ModelPart getLeftArm() {
		return this.LeftArm;
	}

	public ModelPart getRightItem() {
		return this.rightitem;
	}

	public ModelPart getWaist() {
		return this.waist;
	}

	public ModelPart getBone() {
		return this.bone;
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData waist = bone.addChild("waist", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(0, 16).cuboid(-4.5F, -12.0F, -4.0F, 9.0F, 11.0F, 4.0F, new Dilation(0.0F))
		.uv(26, 16).cuboid(-4.5F, -1.0F, -4.0F, 9.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(38, 32).cuboid(-1.0F, -2.025F, -4.0F, 5.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -10.0F, -2.0F));

		ModelPartData Head_r1 = Head.addChild("Head_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.025F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData RightArm = waist.addChild("RightArm", ModelPartBuilder.create().uv(22, 32).cuboid(-2.5F, 1.0F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 24).cuboid(-3.5F, -0.5F, -2.0F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

		ModelPartData rightitem = RightArm.addChild("rightitem", ModelPartBuilder.create(), ModelTransform.pivot(-1.5F, 12.0F, 0.0F));

		ModelPartData LeftArm = waist.addChild("LeftArm", ModelPartBuilder.create().uv(30, 32).cuboid(0.5F, 1.0F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 31).cuboid(0.5F, -0.5F, -2.0F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

		ModelPartData RightLeg = bone.addChild("RightLeg", ModelPartBuilder.create().uv(14, 31).cuboid(-0.8F, -1.0F, 0.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.7F, 2.0F, 0.0F));

		ModelPartData LeftLeg = bone.addChild("LeftLeg", ModelPartBuilder.create().uv(32, 0).cuboid(-1.2F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.7F, 2.0F, 1.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(FrosthauntEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		// Play animations based on entity state
		this.animateMovement(FrosthauntAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.updateAnimation(entity.idleAnimationState, FrosthauntAnimations.idle, ageInTicks);
		this.updateAnimation(entity.attackAnimationState, FrosthauntAnimations.attack, ageInTicks);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		bone.render(matrices, vertices, light, overlay, color);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		// Get the appropriate arm based on which side
		ModelPart armPart = arm == Arm.RIGHT ? this.RightArm : this.LeftArm;
		ModelPart handPart = arm == Arm.RIGHT ? this.rightitem : null; // Only right hand has the item part defined

		// Apply transformations through the model hierarchy
		// bone -> waist -> arm -> hand position
		this.applyPartTransform(matrices, this.bone);
		this.applyPartTransform(matrices, this.waist);
		this.applyPartTransform(matrices, armPart);

		// If there's a hand part (rightitem), use it. Otherwise calculate hand position
		if (handPart != null) {
			this.applyPartTransform(matrices, handPart);
		} else {
			// For left arm, manually position at hand location (end of arm)
			// RightArm hand is at (-1.5, 12, 0), LeftArm hand is mirrored at (+1.5, 12, 0)
			matrices.translate(0.09375F, 0.75F, 0.0F); // (1.5/16, 12/16, 0)
		}
	}

	private void applyPartTransform(MatrixStack matrices, ModelPart part) {
		// ModelPart.rotate() handles both pivot translation and rotations
		part.rotate(matrices);
	}
}

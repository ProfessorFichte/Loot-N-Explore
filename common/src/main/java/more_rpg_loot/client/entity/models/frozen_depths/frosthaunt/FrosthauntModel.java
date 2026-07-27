package more_rpg_loot.client.entity.models.frozen_depths.frosthaunt;

import more_rpg_loot.entity.frozen_depths.mob.frosthaunt.FrosthauntEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class FrosthauntModel extends SinglePartEntityModel<FrosthauntEntity> implements ModelWithArms {
	private final ModelPart group;
	private final ModelPart waist;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart leftItem;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public FrosthauntModel(ModelPart root) {
		this.group = root.getChild("group");
		this.waist = this.group.getChild("waist");
		this.Body = this.waist.getChild("Body");
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

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData group = modelPartData.addChild("group", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData waist = group.addChild("waist", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

		ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 32).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

		ModelPartData RightArm = Body.addChild("RightArm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

		ModelPartData RightArm_r1 = RightArm.addChild("RightArm_r1", ModelPartBuilder.create().uv(26, 33).cuboid(-2.5F, -1.75F, -2.0F, 5.0F, 4.0F, 4.0F, new Dilation(-0.025F)), ModelTransform.of(-1.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		ModelPartData LeftArm = Body.addChild("LeftArm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

		ModelPartData leftItem = LeftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 8.0F, 1.0F));

		ModelPartData RightLeg = group.addChild("RightLeg", ModelPartBuilder.create().uv(0, 17).cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));

		ModelPartData LeftLeg = group.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 17).mirrored().cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
		.uv(25, 42).mirrored().cuboid(-3.0F, 1.2F, -2.0F, 5.0F, 12.0F, 4.0F, new Dilation(0.225F)).mirrored(false), ModelTransform.pivot(2.0F, -12.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(FrosthauntEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		// Play animations based on entity state
		this.animateMovement(FrosthauntAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.updateAnimation(entity.idleAnimationState, FrosthauntAnimations.idle, ageInTicks);
		this.updateAnimation(entity.attackAnimationState, FrosthauntAnimations.attack1, ageInTicks);
		this.updateAnimation(entity.attackAnimationState2, FrosthauntAnimations.attack2, ageInTicks);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		group.render(matrices, vertices, light, overlay, color);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		ModelPart armPart = arm == Arm.RIGHT ? this.RightArm : this.LeftArm;
		ModelPart handPart = arm == Arm.LEFT ? this.leftItem : null; // only the left arm has an item part defined

		this.applyPartTransform(matrices, this.group);
		this.applyPartTransform(matrices, this.waist);
		this.applyPartTransform(matrices, this.Body);
		this.applyPartTransform(matrices, armPart);

		if (handPart != null) {
			this.applyPartTransform(matrices, handPart);
		} else {
			// right arm has no item part, so the hand offset is applied manually: mirror of leftItem's pivot (2.0, 8, 1)
			matrices.translate(-0.125F, 0.5F, 0.0625F);
		}
	}

	// rotate() also applies the part's pivot translation, so this is the whole parent-chain transform
	private void applyPartTransform(MatrixStack matrices, ModelPart part) {
		part.rotate(matrices);
	}
}

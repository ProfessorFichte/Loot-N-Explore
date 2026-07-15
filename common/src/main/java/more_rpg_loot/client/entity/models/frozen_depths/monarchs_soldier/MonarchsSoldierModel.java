package more_rpg_loot.client.entity.models.frozen_depths.monarchs_soldier;

import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class MonarchsSoldierModel<T extends MonarchsSoldierEntity> extends SinglePartEntityModel<T> implements ModelWithArms {
	private final ModelPart group;
	private final ModelPart waist;
	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart RightArm;
	private final ModelPart rightItem;
	private final ModelPart LeftArm;
	private final ModelPart leftItem;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public MonarchsSoldierModel(ModelPart root) {
		this.group = root.getChild("group");
		this.waist = this.group.getChild("waist");
		this.Body = this.waist.getChild("Body");
		this.Head = this.Body.getChild("Head");
		this.RightArm = this.Body.getChild("RightArm");
		this.rightItem = this.RightArm.getChild("rightItem");
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

	public ModelPart getRightItem() {
		return this.rightItem;
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

		ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
		.uv(36, 33).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(28, 16).cuboid(-4.5F, -8.5F, -4.5F, 9.0F, 8.0F, 9.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(-4.5F, -8.5F, -4.5F, 9.0F, 8.0F, 9.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

		ModelPartData RightArm = Body.addChild("RightArm", ModelPartBuilder.create().uv(8, 16).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

		ModelPartData RightArm_r1 = RightArm.addChild("RightArm_r1", ModelPartBuilder.create().uv(0, 48).cuboid(-3.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, -0.25F, 0.5F, 0.0F, 0.0F, -0.0873F));

		ModelPartData rightItem = RightArm.addChild("rightItem", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 9.0F, 1.0F));

		ModelPartData LeftArm = Body.addChild("LeftArm", ModelPartBuilder.create().uv(8, 16).mirrored().cuboid(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

		ModelPartData LeftArm_r1 = LeftArm.addChild("LeftArm_r1", ModelPartBuilder.create().uv(0, 48).mirrored().cuboid(0.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.of(0.0F, -0.25F, 0.5F, 0.0F, 0.0F, 0.0873F));

		ModelPartData leftItem = LeftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 9.0F, 0.0F));

		ModelPartData RightLeg = group.addChild("RightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
		.uv(16, 17).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.25F))
		.uv(24, 16).cuboid(-1.5F, 9.025F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));

		ModelPartData LeftLeg = group.addChild("LeftLeg", ModelPartBuilder.create().uv(16, 17).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.25F)).mirrored(false)
		.uv(0, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
		.uv(24, 16).mirrored().cuboid(-1.5F, 9.025F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, -12.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		this.updateAnimation(entity.idleAnimationState, MonarchsSoldierAnimations.idle, ageInTicks);
		this.updateAnimation(entity.walkAnimationState, MonarchsSoldierAnimations.walk, ageInTicks);
		this.updateAnimation(entity.blockWalkAnimationState, MonarchsSoldierAnimations.walk_shield, ageInTicks);
		this.updateAnimation(entity.normalHitAnimationState, MonarchsSoldierAnimations.attack, ageInTicks);
		this.updateAnimation(entity.aoeSwingAnimationState, MonarchsSoldierAnimations.attack_aoe, ageInTicks);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		group.render(matrices, vertices, light, overlay, color);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		ModelPart armPart = arm == Arm.RIGHT ? this.RightArm : this.LeftArm;
		ModelPart handPart = arm == Arm.RIGHT ? this.rightItem : this.leftItem;

		// group -> waist -> Body -> arm -> hand position
		this.applyPartTransform(matrices, this.group);
		this.applyPartTransform(matrices, this.waist);
		this.applyPartTransform(matrices, this.Body);
		this.applyPartTransform(matrices, armPart);
		this.applyPartTransform(matrices, handPart);
	}

	private void applyPartTransform(MatrixStack matrices, ModelPart part) {
		part.rotate(matrices);
	}
}

package more_rpg_loot.client.entity.models.frozen_depths.glaze;

import more_rpg_loot.entity.frozen_depths.mob.glaze.GlazeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class GlazeModel extends SinglePartEntityModel<GlazeEntity> {
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart core;
	private final ModelPart rods;
	private final ModelPart rod_1;
	private final ModelPart rod_2;
	private final ModelPart snow;

	public GlazeModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.core = this.body.getChild("core");
		this.rods = this.body.getChild("rods");
		this.rod_1 = this.rods.getChild("rod_1");
		this.rod_2 = this.rods.getChild("rod_2");
		this.snow = this.body.getChild("snow");
	}

	@Override
	public ModelPart getPart() {
		return this.body;
	}

	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 12).cuboid(-3.25F, -1.25F, -3.25F, 7.0F, 5.0F, 7.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-4.25F, -4.25F, -4.25F, 9.0F, 3.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.25F, -21.0F, -0.25F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(30, 1).mirrored().cuboid(-3.0F, -1.5F, -1.0F, 5.0F, 3.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.0F, 0.0F, -2.5F, 0.0F, 0.0F, -0.0873F));

		ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(30, 1).cuboid(-2.0F, -2.5F, -1.0F, 5.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 1.0F, -2.5F, 0.0F, 0.0F, 0.0873F));

		ModelPartData core = body.addChild("core", ModelPartBuilder.create().uv(0, 24).cuboid(-2.0F, -5.1786F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 24).cuboid(-1.5F, -0.1786F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -10.8214F, 0.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r3 = core.addChild("cube_r3", ModelPartBuilder.create().uv(28, 12).mirrored().cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.6214F, -3.0F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r4 = core.addChild("cube_r4", ModelPartBuilder.create().uv(16, 30).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.6214F, 0.0F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r5 = core.addChild("cube_r5", ModelPartBuilder.create().uv(28, 22).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.6214F, 3.0F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r6 = core.addChild("cube_r6", ModelPartBuilder.create().uv(24, 32).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 0.6214F, 0.0F, 0.0F, 0.0F, -0.3927F));

		ModelPartData rods = body.addChild("rods", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -14.0F, -0.25F));

		ModelPartData rod_1 = rods.addChild("rod_1", ModelPartBuilder.create().uv(32, 32).cuboid(-1.0F, -5.25F, -0.75F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-7.0F, 1.25F, 0.25F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r7 = rod_1.addChild("cube_r7", ModelPartBuilder.create().uv(40, 24).cuboid(0.0F, -1.25F, -1.5F, 0.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.25F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r8 = rod_1.addChild("cube_r8", ModelPartBuilder.create().uv(40, 24).cuboid(0.0F, -1.25F, -1.5F, 0.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.25F, 0.0F, -0.7854F, 0.0F));

		ModelPartData rod_2 = rods.addChild("rod_2", ModelPartBuilder.create().uv(32, 32).mirrored().cuboid(-1.0F, -5.25F, -0.75F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(7.0F, 1.25F, 0.25F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r9 = rod_2.addChild("cube_r9", ModelPartBuilder.create().uv(40, 24).mirrored().cuboid(0.0F, -3.0F, -1.5F, 0.0F, 6.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.75F, 0.25F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r10 = rod_2.addChild("cube_r10", ModelPartBuilder.create().uv(40, 24).mirrored().cuboid(0.0F, -3.0F, -1.5F, 0.0F, 6.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.75F, 0.25F, 0.0F, 0.7854F, 0.0F));

		ModelPartData snow = body.addChild("snow", ModelPartBuilder.create(), ModelTransform.pivot(0.0858F, -11.75F, -0.0429F));

		ModelPartData cube_r11 = snow.addChild("cube_r11", ModelPartBuilder.create().uv(1, 45).cuboid(-5.0F, -4.5F, -5.0F, 10.0F, 9.0F, 10.0F, new Dilation(-0.35F)), ModelTransform.of(0.0F, 0.25F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(GlazeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		this.updateAnimation(entity.idleAnimationState, GlazeAnimations.idle, ageInTicks);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		body.render(matrices, vertices, light, overlay, color);
	}
}

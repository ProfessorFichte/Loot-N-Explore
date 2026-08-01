package more_rpg_loot.client.entity.models.frozen_depths.frost_hound;

import more_rpg_loot.entity.frozen_depths.mob.frost_hound.FrostHoundEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class FrostHoundModel extends SinglePartEntityModel<FrostHoundEntity> {
    private final ModelPart bone;
    private final ModelPart L_leg_1;
    private final ModelPart R_leg_1;
    private final ModelPart L_leg_2;
    private final ModelPart R_leg_2;
    private final ModelPart Body;
    private final ModelPart Lower_body;
    private final ModelPart tail;
    private final ModelPart Upper_Body;
    private final ModelPart spikes;
    private final ModelPart Head;
    private final ModelPart l_ear;
    private final ModelPart r_ear;

    public FrostHoundModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.L_leg_1 = this.bone.getChild("L_leg_1");
        this.R_leg_1 = this.bone.getChild("R_leg_1");
        this.L_leg_2 = this.bone.getChild("L_leg_2");
        this.R_leg_2 = this.bone.getChild("R_leg_2");
        this.Body = this.bone.getChild("Body");
        this.Lower_body = this.Body.getChild("Lower_body");
        this.tail = this.Body.getChild("tail");
        this.Upper_Body = this.Body.getChild("Upper_Body");
        this.spikes = this.Upper_Body.getChild("spikes");
        this.Head = this.Upper_Body.getChild("Head");
        this.l_ear = this.Head.getChild("l_ear");
        this.r_ear = this.Head.getChild("r_ear");
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // export has no single wrapping root part; wrap all top-level parts under "bone"
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.NONE);

        ModelPartData L_leg_1 = bone.addChild("L_leg_1", ModelPartBuilder.create().uv(36, 0).cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 15.5F, -6.25F));

        ModelPartData R_leg_1 = bone.addChild("R_leg_1", ModelPartBuilder.create().uv(36, 0).mirrored().cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.0F, 15.5F, -6.25F));

        ModelPartData L_leg_2 = bone.addChild("L_leg_2", ModelPartBuilder.create().uv(16, 36).cuboid(-1.5F, -1.5F, -1.75F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F))
        .uv(48, 0).cuboid(-1.5F, 3.5F, -0.75F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 13.5F, 6.0F));

        ModelPartData R_leg_2 = bone.addChild("R_leg_2", ModelPartBuilder.create().uv(16, 36).mirrored().cuboid(-1.5F, -1.5F, -1.75F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
        .uv(48, 0).mirrored().cuboid(-1.5F, 3.5F, -0.75F, 3.0F, 7.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.5F, 13.5F, 6.0F));

        ModelPartData Body = bone.addChild("Body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.75F, 4.25F));

        ModelPartData Lower_body = Body.addChild("Lower_body", ModelPartBuilder.create().uv(0, 18).cuboid(-3.5F, -3.75F, 5.0F, 7.0F, 8.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -9.0F));

        ModelPartData tail = Body.addChild("tail", ModelPartBuilder.create().uv(40, 10).cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 9.0F, new Dilation(0.0F))
        .uv(24, 19).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
        .uv(40, 52).cuboid(-1.5F, 0.0F, 0.5F, 3.0F, 3.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, -2.75F, 6.0F, -0.6109F, 0.0F, 0.0F));

        ModelPartData Upper_Body = Body.addChild("Upper_Body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F))
        .uv(0, 46).cuboid(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -0.25F, -8.5F));

        ModelPartData spikes = Upper_Body.addChild("spikes", ModelPartBuilder.create().uv(34, 35).cuboid(-2.5F, -7.5F, 0.0F, 5.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -5.5F, -2.5F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r1 = spikes.addChild("cube_r1", ModelPartBuilder.create().uv(44, 35).mirrored().cuboid(3.018F, -5.0814F, -1.7114F, 5.0F, 9.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, -0.3927F, 0.5236F));

        ModelPartData cube_r2 = spikes.addChild("cube_r2", ModelPartBuilder.create().uv(44, 35).cuboid(-8.018F, -5.0814F, -1.7114F, 5.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, 0.3927F, -0.5236F));

        ModelPartData Head = Upper_Body.addChild("Head", ModelPartBuilder.create().uv(36, 22).cuboid(-3.5F, -3.875F, -4.25F, 7.0F, 7.0F, 5.0F, new Dilation(0.0F))
        .uv(0, 36).cuboid(-2.0F, -0.875F, -8.25F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.125F, -4.25F));

        ModelPartData l_ear = Head.addChild("l_ear", ModelPartBuilder.create().uv(36, 12).cuboid(-1.5F, -3.0F, -0.5F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, -3.375F, -1.75F));

        ModelPartData r_ear = Head.addChild("r_ear", ModelPartBuilder.create().uv(36, 12).mirrored().cuboid(-1.5F, -3.0F, -0.5F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.5F, -3.375F, -1.75F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.bone;
    }

    @Override
    public void setAngles(FrostHoundEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.updateAnimation(entity.idleAnimationState, FrostHoundAnimations.idle, ageInTicks);
        this.updateAnimation(entity.walkAnimationState, FrostHoundAnimations.walk, ageInTicks);
        this.updateAnimation(entity.runAnimationState, FrostHoundAnimations.running, ageInTicks);
        this.updateAnimation(entity.biteAnimationState, FrostHoundAnimations.bite, ageInTicks);
        this.updateAnimation(entity.clawAnimationState, FrostHoundAnimations.claw, ageInTicks);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        bone.render(matrices, vertices, light, overlay, color);
    }
}

package more_rpg_loot.client.entity.models.frostmonarch;

import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;


public class FrostMonarchModel extends SinglePartEntityModel<FrostMonarchEntity> implements ModelWithArms {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Identifier.of(MOD_ID, "frost_monarch"), "main");
    private final ModelPart bone2;
    private final ModelPart bone;
    private final ModelPart Body;
    private final ModelPart Body2;
    private final ModelPart Head;
    private final ModelPart Head3;
    private final ModelPart Head2;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

	public FrostMonarchModel(ModelPart root) {
        this.bone2 = root.getChild("bone2");
        this.bone = this.bone2.getChild("bone");
        this.Body = this.bone.getChild("Body");
        this.Body2 = this.Body.getChild("Body2");
        this.Head = this.Body2.getChild("Head");
        this.Head3 = this.Head.getChild("Head3");
        this.Head2 = this.Head.getChild("Head2");
        this.LeftArm = this.Body2.getChild("LeftArm");
        this.RightArm = this.Body2.getChild("RightArm");
        this.RightLeg = this.bone.getChild("RightLeg");
        this.LeftLeg = this.bone.getChild("LeftLeg");
    }

    @Override
    public ModelPart getPart() {
        return this.bone2;
    }

    // Getters for held item rendering
    public ModelPart getRightArm() {
        return this.RightArm;
    }

    public ModelPart getLeftArm() {
        return this.LeftArm;
    }

    public ModelPart getBody2() {
        return this.Body2;
    }

    public ModelPart getBody() {
        return this.Body;
    }

    public ModelPart getBone() {
        return this.bone;
    }

    public ModelPart getBone2() {
        return this.bone2;
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bone2 = modelPartData.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData bone = bone2.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Body = bone.addChild("Body", ModelPartBuilder.create().uv(0, 32).cuboid(-4.0F, -2.0F, -2.0F, 8.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Body2 = Body.addChild("Body2", ModelPartBuilder.create().uv(32, 14).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 7.0F, 4.0F, new Dilation(0.0F))
                .uv(42, 42).cuboid(-4.0F, -3.0F, -4.0F, 8.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 2.0F));

        ModelPartData Head = Body2.addChild("Head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -10.25F, -2.0F));

        ModelPartData Head3 = Head.addChild("Head3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.25F, 0.0F));

        ModelPartData Head_r1 = Head3.addChild("Head_r1", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData Head2 = Head.addChild("Head2", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -6.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

        ModelPartData LeftArm = Body2.addChild("LeftArm", ModelPartBuilder.create().uv(1, 65).mirrored().cuboid(0.0F, -1.5F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(8, 46).cuboid(0.0F, -1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -8.0F, -2.0F));

        ModelPartData RightArm = Body2.addChild("RightArm", ModelPartBuilder.create().uv(1, 65).cuboid(-4.0F, -1.5F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 46).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -8.0F, -2.0F));

        ModelPartData RightLeg = bone.addChild("RightLeg", ModelPartBuilder.create().uv(16, 46).cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 2.0F, 0.0F));

        ModelPartData LeftLeg = bone.addChild("LeftLeg", ModelPartBuilder.create().uv(24, 49).cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 2.0F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(FrostMonarchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        // Play idle animation by default
        this.animateMovement(FrostMonarchAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, FrostMonarchAnimations.idle, ageInTicks);
        this.updateAnimation(entity.screechAnimationState, FrostMonarchAnimations.screech, ageInTicks);
        this.updateAnimation(entity.summonAnimationState, FrostMonarchAnimations.summon, ageInTicks);
        this.updateAnimation(entity.deathAnimationState, FrostMonarchAnimations.death, ageInTicks);
        this.updateAnimation(entity.spawnAnimationState, FrostMonarchAnimations.spawn, ageInTicks);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        bone2.render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        // Get the appropriate arm based on which side
        ModelPart armPart = arm == Arm.RIGHT ? this.RightArm : this.LeftArm;

        // Apply transformations through the model hierarchy
        // bone2 -> bone -> Body -> Body2 -> arm
        this.applyPartTransform(matrices, this.bone2);
        this.applyPartTransform(matrices, this.bone);
        this.applyPartTransform(matrices, this.Body);
        this.applyPartTransform(matrices, this.Body2);
        this.applyPartTransform(matrices, armPart);

        // Position at hand location (end of arm)
        // The arm cuboid goes from Y=-1 to Y=13 (14 pixels tall)
        // Hand is at Y=13, so translate to end of arm
        boolean rightArm = arm == Arm.RIGHT;
        float xOffset = rightArm ? -0.0625F : 0.0625F; // Small X offset to center
        matrices.translate(xOffset, 0.875F, 0.0F); // (offset, 14/16, 0)
    }

    private void applyPartTransform(MatrixStack matrices, ModelPart part) {
        // ModelPart.rotate() handles both pivot translation and rotations
        part.rotate(matrices);
    }
}

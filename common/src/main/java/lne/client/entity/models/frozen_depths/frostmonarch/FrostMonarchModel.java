package lne.client.entity.models.frozen_depths.frostmonarch;

import com.mrpg_lib.client.animation.AnimationBlender;
import lne.client.entity.models.HumanoidBoneModel;
import lne.client.entity.models.HumanoidBones;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchAnimationTimes;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrostMonarchModel extends SinglePartEntityModel<FrostMonarchEntity> implements ModelWithArms, HumanoidBoneModel {
    private static final Animation QUICK_CAST = cut(FrostMonarchAnimations.summon, FrostMonarchAnimationTimes.QUICK_CAST_LENGTH_TICKS, 1.21F);
    private static final Animation WALL_CAST = cut(FrostMonarchAnimations.summon, FrostMonarchAnimationTimes.WALL_CAST_LENGTH_TICKS, 0.38F);
    private final AnimationBlender blender = new AnimationBlender();
    private final ModelPart group;
    private final ModelPart frostmonarch;
    private final ModelPart waist;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart eyes_off3;
    private final ModelPart crown;
    private final ModelPart eyes_off2;
    private final ModelPart eyes_off;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart leftItem;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final HumanoidBones bones;

    public FrostMonarchModel(ModelPart root) {
        this.group = root.getChild("group");
        this.frostmonarch = this.group.getChild("frostmonarch");
        this.waist = this.frostmonarch.getChild("waist");
        this.Body = this.waist.getChild("Body");
        this.Head = this.Body.getChild("Head");
        this.eyes_off3 = this.Head.getChild("eyes_off3");
        this.crown = this.Head.getChild("crown");
        this.eyes_off2 = this.Head.getChild("eyes_off2");
        this.eyes_off = this.Head.getChild("eyes_off");
        this.RightArm = this.Body.getChild("RightArm");
        this.LeftArm = this.Body.getChild("LeftArm");
        this.leftItem = this.LeftArm.getChild("leftItem");
        this.RightLeg = this.frostmonarch.getChild("RightLeg");
        this.LeftLeg = this.frostmonarch.getChild("LeftLeg");
        this.bones = new HumanoidBones(this.Body, this.Head, this.RightArm, this.LeftArm, this.RightLeg, this.LeftLeg);
    }

    @Override
    public HumanoidBones bones() {
        return this.bones;
    }

    @Override
    public ModelPart getPart() {
        return this.group;
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData group = modelPartData.addChild("group", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData frostmonarch = group.addChild("frostmonarch", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData waist = frostmonarch.addChild("waist", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 18).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData eyes_off3 = Head.addChild("eyes_off3", ModelPartBuilder.create().uv(0, 2).cuboid(-3.0F, -0.5F, -0.475F, 2.0F, 1.0F, 1.0F, new Dilation(0.025F))
                .uv(0, 2).cuboid(1.0F, -0.5F, -0.475F, 2.0F, 1.0F, 1.0F, new Dilation(0.025F)), ModelTransform.pivot(0.0F, -2.5F, -3.475F));

        ModelPartData crown = Head.addChild("crown", ModelPartBuilder.create().uv(0, 51).cuboid(-4.0F, -4.5F, -4.0F, 8.0F, 5.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -5.5F, 0.0F));

        ModelPartData eyes_off2 = Head.addChild("eyes_off2", ModelPartBuilder.create().uv(0, 4).cuboid(-3.0F, -0.5F, -0.475F, 2.0F, 1.0F, 1.0F, new Dilation(0.025F))
                .uv(0, 4).cuboid(1.0F, -0.5F, -0.475F, 2.0F, 1.0F, 1.0F, new Dilation(0.025F)), ModelTransform.pivot(0.0F, -2.5F, -3.475F));

        ModelPartData eyes_off = Head.addChild("eyes_off", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -0.5F, -0.475F, 2.0F, 1.0F, 1.0F, new Dilation(0.025F))
                .uv(0, 0).cuboid(1.0F, -0.5F, -0.475F, 2.0F, 1.0F, 1.0F, new Dilation(0.025F)), ModelTransform.pivot(0.0F, -2.5F, -3.475F));

        ModelPartData RightArm = Body.addChild("RightArm", ModelPartBuilder.create().uv(8, 18).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

        ModelPartData RightArm_r1 = RightArm.addChild("RightArm_r1", ModelPartBuilder.create().uv(32, 56).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.05F)), ModelTransform.of(-1.65F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData LeftArm = Body.addChild("LeftArm", ModelPartBuilder.create().uv(8, 18).mirrored().cuboid(0.0F, -1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

        ModelPartData LeftArm_r1 = LeftArm.addChild("LeftArm_r1", ModelPartBuilder.create().uv(32, 56).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.05F)).mirrored(false), ModelTransform.of(1.65F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData leftItem = LeftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 13.0F, -1.0F));

        ModelPartData RightLeg = frostmonarch.addChild("RightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-2.0F, 0.5F, -2.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-2.0F, -16.0F, 0.0F));

        ModelPartData LeftLeg = frostmonarch.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(40, 16).mirrored().cuboid(-2.0F, 0.5F, -2.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(2.0F, -16.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(FrostMonarchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.bones.track(entity);

        AnimationBlender.Session session = blender.begin(entity, ageInTicks);
        session.action(entity.screechAnimationState, FrostMonarchAnimations.screech);
        session.action(entity.summonAnimationState, FrostMonarchAnimations.summon);
        session.action(entity.quickCastAnimationState, QUICK_CAST);
        session.action(entity.wallCastAnimationState, WALL_CAST);
        session.action(entity.spawnAnimationState, FrostMonarchAnimations.spawn);
        session.hold(entity.deathAnimationState, FrostMonarchAnimations.death);
        session.apply(this, FrostMonarchAnimations.idle);
        this.animateMovement(FrostMonarchAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.attackAnimationState1, FrostMonarchAnimations.attack1, ageInTicks);
        this.updateAnimation(entity.attackAnimationState2, FrostMonarchAnimations.attack2, ageInTicks);
        this.updateAnimation(entity.attackAnimationState3, FrostMonarchAnimations.attack3, ageInTicks);
    }

    private static Animation cut(Animation animation, int lengthTicks, float holdFromSeconds) {
        Map<String, List<Transformation>> bones = new HashMap<>();
        animation.boneAnimations().forEach((bone, transformations) -> bones.put(bone, transformations.stream()
                .map(transformation -> new Transformation(transformation.target(), keyframesUntil(transformation.keyframes(), holdFromSeconds)))
                .toList()));
        return new Animation(lengthTicks / 20.0F, false, bones);
    }

    private static Keyframe[] keyframesUntil(Keyframe[] keyframes, float seconds) {
        int count = 1;
        while (count < keyframes.length && keyframes[count].timestamp() <= seconds) {
            count++;
        }
        return Arrays.copyOf(keyframes, count);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        group.render(matrices, vertices, light, overlay, color);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        ModelPart armPart = arm == Arm.RIGHT ? this.RightArm : this.LeftArm;
        ModelPart handPart = arm == Arm.LEFT ? this.leftItem : null;

        this.applyPartTransform(matrices, this.group);
        this.applyPartTransform(matrices, this.frostmonarch);
        this.applyPartTransform(matrices, this.waist);
        this.applyPartTransform(matrices, this.Body);
        this.applyPartTransform(matrices, armPart);

        if (handPart != null) {
            this.applyPartTransform(matrices, handPart);
        } else {
            // no item part on the right arm, so mirror leftItem's pivot (1, 13, -1)
            matrices.translate(-0.0625F, 0.8125F, -0.0625F);
        }
    }

    // rotate() already applies the pivot translation
    private void applyPartTransform(MatrixStack matrices, ModelPart part) {
        part.rotate(matrices);
    }
}

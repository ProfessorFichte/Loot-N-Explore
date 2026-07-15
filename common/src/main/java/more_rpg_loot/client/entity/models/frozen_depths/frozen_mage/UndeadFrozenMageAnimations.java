package more_rpg_loot.client.entity.models.frozen_depths.frozen_mage;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 5.1.4
 * Exported for Minecraft version 1.17 or later with Mojang mappings
 * Converted for Fabric 1.21.1 with Yarn mappings
 */
public class UndeadFrozenMageAnimations {
    public static final Animation idle = Animation.Builder.create(5.4546F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5081F, 5.4976F, -0.1095F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3636F, AnimationHelper.createRotationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.7273F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0909F, AnimationHelper.createRotationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createRotationalVector(-2.5081F, 5.4976F, -0.1095F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(6.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3636F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.7652F, AnimationHelper.createRotationalVector(6.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0909F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createRotationalVector(6.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.3636F, AnimationHelper.createTranslationalVector(0.0F, -0.0002F, -0.25F), Transformation.Interpolations.LINEAR),
            new Keyframe(2.7652F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(4.0909F, AnimationHelper.createTranslationalVector(0.0F, -0.0002F, -0.25F), Transformation.Interpolations.LINEAR),
            new Keyframe(5.4546F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.7273F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.7273F, AnimationHelper.createRotationalVector(-19.5F, 0.0F, -6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createRotationalVector(-12.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("cloak", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(3.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3636F, AnimationHelper.createRotationalVector(3.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.7273F, AnimationHelper.createRotationalVector(-2.0F, -1.0F, -1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.053F, AnimationHelper.createRotationalVector(3.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createRotationalVector(3.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("ice_orb", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createRotationalVector(0.0F, -360.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("ice_orb", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3636F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.7273F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0909F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(5.4546F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation walk = Animation.Builder.create(0.9259F).looping()
        .addBoneAnimation("waist", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2315F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.463F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2315F, AnimationHelper.createRotationalVector(-1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.463F, AnimationHelper.createRotationalVector(1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createRotationalVector(-1.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.463F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(13.485F, -0.6418F, 8.6742F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1852F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5093F, AnimationHelper.createRotationalVector(-13.5F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(13.485F, -0.6418F, 8.6742F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2315F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3704F, AnimationHelper.createRotationalVector(-7.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5093F, AnimationHelper.createRotationalVector(-7.4538F, -4.0958F, -11.4731F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(-12.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5093F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(10.31F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0463F, AnimationHelper.createRotationalVector(12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0926F, AnimationHelper.createRotationalVector(12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2315F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.463F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.787F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(10.31F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.03F, 1.34F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0463F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0926F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.463F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, -1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6018F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.8796F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createTranslationalVector(0.0F, -0.03F, 1.34F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3241F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5093F, AnimationHelper.createRotationalVector(13.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5556F, AnimationHelper.createRotationalVector(13.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, -1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1389F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2315F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5093F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5556F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, -1.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("cloak", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 2.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2315F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.463F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6944F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9259F, AnimationHelper.createRotationalVector(5.0F, 2.0F, 1.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation cast = Animation.Builder.create(1.2083F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createRotationalVector(2.5F, -12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(2.5F, -15.68F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(2.5F, -15.68F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.875F, AnimationHelper.createRotationalVector(0.0F, -10.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-0.16F, 0.95F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(6.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(18.5F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(16.5F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(16.5F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createRotationalVector(6.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-54.9186F, -32.0565F, -14.8641F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.625F, AnimationHelper.createRotationalVector(-64.3296F, -27.3179F, -13.0264F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-64.4857F, -36.5964F, -17.4028F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-2.8F, 0.0F, 24.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 6.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-0.8236F, -0.0489F, -1.3409F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.625F, AnimationHelper.createTranslationalVector(-0.8236F, -0.0489F, -1.3409F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(-0.8236F, -0.0489F, -1.3409F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-15.4355F, 11.864F, 5.3796F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-17.9355F, 11.864F, 5.3796F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-12.0F, 0.0F, -11.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0724F, 0.4773F, 0.1302F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0724F, 0.4773F, 0.1302F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("cloak", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(3.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(8.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createRotationalVector(3.3906F, 1.5182F, -3.9743F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(15.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(17.712F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createRotationalVector(17.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.8333F, AnimationHelper.createRotationalVector(15.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createRotationalVector(1.308F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createRotationalVector(3.5F, 1.0F, 1.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("ice_orb", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-360.0F, -720.0F, 360.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("ice_orb", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.375F, AnimationHelper.createTranslationalVector(-0.2304F, 0.2304F, -0.3404F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-0.2304F, 0.2304F, -0.3404F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC)
        ))
        .build();
}

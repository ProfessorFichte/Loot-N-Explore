package more_rpg_loot.client.entity.models.frozen_depths.frozen_ranger;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 5.1.4
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * Converted for Fabric 1.21.1 with Yarn mappings
 */
public class FrostedRangerAnimations {
    public static final Animation idle = Animation.Builder.create(3.0F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-4.0344F, 7.4818F, -0.5261F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 7.4989F, -0.1316F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(-4.0344F, 7.4818F, -0.5261F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(2.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(-5.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(4.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("cape", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.4784F, -0.0242F, 0.3275F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(6.4526F, -0.0445F, 0.7174F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(2.4784F, -0.0242F, 0.3275F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation walk = Animation.Builder.create(1.0F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.4999F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(2.4999F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-2.4999F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5472F, 0.0F, 0.3313F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-10.0472F, 0.0289F, 0.3301F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-2.5472F, 0.0F, 0.3313F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(8.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.1F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.1F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.1F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(8.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(8.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.1F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.1F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.1F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(16.72F, 0.03F, -0.57F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(21.4562F, 0.0371F, -0.8086F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(16.72F, 0.03F, -0.57F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.14F, 0.2F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.75F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -1.75F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -0.14F, 0.2F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(21.4562F, -0.0371F, 0.8086F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -1.75F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.75F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -1.75F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("cape", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(11.4522F, -0.1068F, 0.7108F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-0.0216F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(11.4522F, -0.1068F, 0.7108F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation attack = Animation.Builder.create(1.6667F)
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-4.0344F, 7.4818F, -0.5261F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-17.9053F, 76.8853F, -17.4668F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-7.4151F, 57.2815F, -6.2487F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-7.4151F, 57.2815F, -6.2487F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-7.4151F, 57.2815F, -6.2487F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-16.4244F, 56.4089F, -13.7962F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.625F, AnimationHelper.createRotationalVector(-4.0344F, 7.4818F, -0.5261F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-5.5877F, -60.0061F, 2.9386F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-3.7292F, -57.5293F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-4.3467F, -50.0429F, 1.43F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createRotationalVector(32.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.375F, AnimationHelper.createRotationalVector(-87.1728F, -44.9651F, 5.5012F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.75F, AnimationHelper.createRotationalVector(-49.5001F, -53.0436F, -40.8568F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.1657F, AnimationHelper.createRotationalVector(-49.5001F, -53.0436F, -40.8568F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-49.5001F, -53.0436F, -40.8568F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.2073F, AnimationHelper.createRotationalVector(-49.5001F, -53.0436F, -40.8568F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-49.5001F, -53.0436F, -40.8568F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-85.6F, -62.4F, 3.6F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(9.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createRotationalVector(2.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(1.5752F, -0.2485F, -1.1681F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.75F, AnimationHelper.createTranslationalVector(1.5752F, -0.2485F, -1.1681F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.6978F, -0.1168F, -0.7067F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-0.1795F, 0.015F, -0.2452F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(-0.93F, 0.13F, 0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(-0.93F, 0.13F, 0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0614F, -0.0005F, 0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-80.149F, -1.728F, 4.8511F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-84.3297F, -28.1311F, 0.6281F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.75F, AnimationHelper.createRotationalVector(-77.5698F, -42.4651F, -7.113F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-77.5698F, -42.4651F, -7.113F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.4167F, AnimationHelper.createRotationalVector(-55.894F, -25.0569F, -16.6421F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createRotationalVector(11.579F, 1.0949F, 1.1139F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(-0.4681F, 0.1039F, 0.8776F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.75F, AnimationHelper.createTranslationalVector(-1.0073F, 0.1F, 0.0366F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(-1.0073F, 0.1F, 0.0366F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(4.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(9.4096F, 22.4097F, 2.0675F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createRotationalVector(9.4096F, 22.4097F, 2.0675F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createRotationalVector(4.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.7635F, 0.2093F, 3.0908F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.7635F, 0.2093F, 3.0908F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-15.2843F, 43.8828F, -6.8285F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-15.2843F, 43.8828F, -6.8285F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-1.4123F, 0.1047F, -1.9123F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(-1.4123F, 0.1047F, -1.9123F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("cape", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.4784F, -0.0242F, 0.3275F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(31.6235F, 3.6372F, 13.9345F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(27.6269F, 18.0052F, -6.9599F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(12.6645F, 13.5494F, 2.3467F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.5417F, AnimationHelper.createRotationalVector(22.6645F, 13.5494F, 2.3467F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.6667F, AnimationHelper.createRotationalVector(2.4784F, -0.0242F, 0.3275F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation attackFast = Animation.Builder.create(1.0417F)
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-4.0344F, 7.4818F, -0.5261F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-7.4151F, 57.2815F, -6.2487F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-7.4151F, 57.2815F, -6.2487F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-7.4151F, 57.2815F, -6.2487F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-4.0344F, 7.4818F, -0.5261F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-5.5877F, -60.0061F, 2.9386F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-3.7292F, -57.5293F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-4.3467F, -50.0429F, 1.43F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-5.0F, -7.5114F, 0.0006F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(32.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-89.7093F, -54.8754F, 2.5349F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-56.4907F, -54.5803F, -39.0684F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.7073F, AnimationHelper.createRotationalVector(-56.4907F, -54.5803F, -39.0684F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-56.4907F, -54.5803F, -39.0684F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-24.2466F, -9.6751F, 15.4315F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(2.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(1.9677F, -0.2402F, -1.3616F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(1.9677F, -0.2402F, -1.3616F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.6978F, -0.1168F, -0.7067F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.625F, AnimationHelper.createTranslationalVector(-0.1795F, 0.015F, -0.2452F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(-0.93F, 0.13F, 0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-80.149F, -1.728F, 4.8511F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-53.6909F, -0.3434F, -67.6217F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-53.6909F, -0.3434F, -67.6217F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.875F, AnimationHelper.createRotationalVector(11.579F, 1.0949F, 1.1139F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(-0.5852F, -1.641F, 0.9399F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(-0.5852F, -1.641F, 0.9399F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(4.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(9.4096F, 22.4097F, 2.0675F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(9.4096F, 22.4097F, 2.0675F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(4.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.7635F, 0.2093F, 3.0908F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.7635F, 0.2093F, 3.0908F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-15.2843F, 43.8828F, -6.8285F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-15.2843F, 43.8828F, -6.8285F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-1.4123F, 0.1047F, -1.9123F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(-1.4123F, 0.1047F, -1.9123F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("cape", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.4784F, -0.0242F, 0.3275F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(31.6235F, 3.6372F, 13.9345F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createRotationalVector(10.1269F, 18.0052F, -6.9599F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createRotationalVector(16.5529F, 15.7626F, 3.578F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9167F, AnimationHelper.createRotationalVector(17.6269F, 18.0052F, -6.9599F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(2.4784F, -0.0242F, 0.3275F), Transformation.Interpolations.CUBIC)
        ))
        .build();
}

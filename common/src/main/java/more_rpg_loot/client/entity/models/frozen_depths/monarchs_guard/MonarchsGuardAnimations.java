package more_rpg_loot.client.entity.models.frozen_depths.monarchs_guard;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 5.1.4
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * Converted for Fabric 1.21.1 with Yarn mappings
 */
public class MonarchsGuardAnimations {
    public static final Animation idle = Animation.Builder.create(4.0F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(3.0F, AnimationHelper.createRotationalVector(5.0607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(7.5F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(-116.9597F, -47.5267F, 95.6921F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC),
            new Keyframe(2.0F, AnimationHelper.createRotationalVector(-3.2697F, 30.7893F, -11.4939F), Transformation.Interpolations.CUBIC),
            new Keyframe(4.0F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.0218F, -0.7505F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation walk = Animation.Builder.create(1.0F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.4393F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(4.9452F, 2.4556F, 0.7553F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-2.4393F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(10.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-7.5F, -2.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(10.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-100.8548F, -55.4656F, 65.8607F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-100.8548F, -55.4656F, 65.8607F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.4847F, 36.2798F, -11.8825F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-3.4847F, 36.2798F, -11.8825F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(20.33F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0833F, AnimationHelper.createRotationalVector(22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(20.33F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.24F, 2.13F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.93F, 1.82F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, -1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.42F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.24F, 2.13F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, -1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.42F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.93F, 1.82F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, -1.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation stab = Animation.Builder.create(0.5F)
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(7.3585F, 4.8673F, 1.7067F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createRotationalVector(7.3585F, 4.8673F, 1.7067F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.3585F, 4.8673F, 1.7067F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-2.5605F, -12.4879F, 0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(16.4961F, 1.4037F, -4.9152F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(16.4961F, 1.4037F, -4.9152F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.375F, AnimationHelper.createRotationalVector(-12.5F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.125F, AnimationHelper.createTranslationalVector(-0.0848F, -0.1276F, 0.9882F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(-0.0848F, -0.1276F, 0.9882F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-103.8029F, -31.5618F, 72.6586F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-103.8029F, -31.5618F, 72.6586F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(1.5082F, -3.3533F, -2.8836F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(1.5082F, -3.3533F, -2.8836F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-15.949F, 4.8577F, -3.7491F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-15.949F, 4.8577F, -3.7491F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(-1.0949F, -1.0876F, -2.4338F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(-1.0949F, -1.0876F, -2.4338F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.0218F, -0.7505F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -0.52F, -2.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, -0.52F, -2.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -0.0218F, -0.7505F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
        ))
        .build();

    public static final Animation throwSpear = Animation.Builder.create(1.0833F)
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-8.0657F, -29.9158F, 1.1577F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-8.0657F, -29.9158F, 1.1577F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(17.2506F, 6.6136F, 5.4089F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(17.2506F, 6.6136F, 5.4089F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(19.7506F, 6.6136F, 5.4089F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createRotationalVector(2.5607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.7448F, 0.1955F, -1.2873F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5F, AnimationHelper.createRotationalVector(-10.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-9.5941F, -3.778F, -7.4016F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(2.9059F, -3.778F, -7.4016F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(0.0F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-44.9547F, -34.6409F, 99.8843F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-44.9547F, -34.6409F, 99.8843F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createRotationalVector(39.9997F, 11.3764F, 26.7939F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-9.8305F, -21.5503F, 8.6046F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createRotationalVector(-126.0947F, -49.5679F, 97.8731F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(-0.6964F, -1.5731F, 0.6546F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-0.6964F, -1.5731F, 0.6546F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(-0.17F, -1.92F, 2.46F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(-0.17F, -1.92F, 2.46F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(-0.1681F, -1.9234F, -0.5427F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(10.0F, -5.0F, -175.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(10.0F, -5.0F, -175.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-35.0F, -5.0F, -175.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-35.0F, -5.0F, -175.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createRotationalVector(31.4895F, 17.3974F, 12.9178F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createRotationalVector(1.7949F, 28.7925F, -11.3638F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(3.0F, -2.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.375F, AnimationHelper.createTranslationalVector(3.0F, -2.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.625F, AnimationHelper.createTranslationalVector(2.6757F, -0.845F, -2.8434F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(2.6368F, 0.0F, -1.6972F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(2.6368F, 0.0F, -1.6972F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0937F, 0.8182F, 1.5428F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("leftItem", new Transformation(Transformation.Targets.SCALE,
            new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.499F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.5F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.6657F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.6667F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.9573F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(0.9583F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.0407F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
            new Keyframe(1.0417F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.0218F, -0.7505F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, -0.02F, -2.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(0.0F, -0.02F, -2.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -0.02F, -0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -0.02F, -0.25F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, -0.0218F, -0.7505F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, -0.0218F, -0.7505F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .build();

    public static final Animation charge = Animation.Builder.create(0.5833F).looping()
        .addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createRotationalVector(9.7818F, -0.2153F, 1.8237F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(15.0607F, -12.4879F, -0.554F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(12.5F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createRotationalVector(19.7132F, 2.7305F, -2.1564F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(12.5F, 12.5F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-104.8573F, -45.8762F, 61.356F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-105.5628F, -48.2887F, 62.3191F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-104.8573F, -45.8762F, 61.356F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(1.1674F, -2.0415F, -0.9927F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.4684F, -1.7214F, -1.3028F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(1.1674F, -2.0415F, -0.9927F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.5148F, 21.3035F, -10.9149F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-13.0148F, 21.3035F, -10.9149F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-15.5148F, 21.3035F, -10.9149F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.555F, 0.6936F, -1.3835F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(-0.5935F, 1.1723F, -1.2554F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(-0.555F, 0.6936F, -1.3835F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(44.19F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0417F, AnimationHelper.createRotationalVector(47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(44.19F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.93F, 1.82F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, -1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.32F, -0.52F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE,
            new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.3333F, AnimationHelper.createRotationalVector(47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
        ))
        .addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE,
            new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, -1.5F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.32F, -0.52F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.93F, 1.82F), Transformation.Interpolations.CUBIC),
            new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 2.0F, -1.5F), Transformation.Interpolations.CUBIC)
        ))
        .build();
}

package more_rpg_loot.client.entity.models.frozen_depths.frostmonarch;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class FrostMonarchAnimations {
	public static final Animation spawn = Animation.Builder.create(3.0F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.25F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5417F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-19.1431F, -5.9031F, -16.5038F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(-19.1431F, 5.9031F, 16.5038F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 57.5F, 27.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(57.1013F, 8.4214F, 112.0878F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0417F, AnimationHelper.createRotationalVector(-23.9759F, 7.301F, 15.9476F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createRotationalVector(-23.9759F, 7.301F, 15.9476F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2917F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, -57.5F, -27.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(57.1013F, -8.4214F, -112.0878F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-23.9759F, -7.301F, -15.9476F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(-23.9759F, -7.301F, -15.9476F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(4.9575F, -0.6518F, 7.4718F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-23.9759F, -7.301F, -15.9476F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("frostmonarch", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -42.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 7.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("group", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2917F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.625F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7083F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.8333F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.875F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0417F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2917F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4583F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5417F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.625F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.6667F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation idle = Animation.Builder.create(3.0F).looping()
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(3.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-11.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation screech = Animation.Builder.create(1.25F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-22.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-22.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(15.5F, -4.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(15.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(15.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(15.5F, 4.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(15.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(15.5F, -4.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-25.0F, 10.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-25.0F, -10.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-25.0F, 10.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-42.6105F, -10.6715F, -50.6179F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-42.6105F, -10.6715F, -50.6179F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(68.6443F, 49.3406F, 99.2461F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(64.479F, 39.9185F, 103.9756F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(47.1393F, 60.9364F, 81.9622F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-50.4342F, 20.4741F, 58.6826F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-50.4342F, 20.4741F, 58.6826F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(68.6443F, -49.3406F, -99.2461F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(64.479F, -39.9185F, -103.9756F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(47.1393F, -60.9364F, -81.9622F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0833F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.7F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.25F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.25F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation summon = Animation.Builder.create(3.75F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(20.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-21.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-21.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7083F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0417F, AnimationHelper.createRotationalVector(-21.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7083F, AnimationHelper.createRotationalVector(-21.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.125F, AnimationHelper.createRotationalVector(7.6146F, -0.0013F, 0.0201F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.25F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5417F, AnimationHelper.createRotationalVector(-27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9583F, AnimationHelper.createRotationalVector(22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.375F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-66.4217F, 23.3464F, -3.9551F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(22.4398F, 46.6571F, 146.0536F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(44.0003F, 28.5154F, 164.4659F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createRotationalVector(23.9428F, 32.6797F, 153.2227F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(22.7514F, 39.6105F, 129.9975F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(44.2125F, 28.1918F, 163.5962F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.8333F, AnimationHelper.createRotationalVector(23.9428F, 32.6797F, 153.2227F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(22.7514F, 39.6105F, 129.9975F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createRotationalVector(44.2125F, 28.1918F, 163.5962F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(23.9428F, 32.6797F, 153.2227F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.8333F, AnimationHelper.createRotationalVector(22.7514F, 39.6105F, 129.9975F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0417F, AnimationHelper.createRotationalVector(19.8592F, -2.2495F, 7.1565F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(15.7406F, -6.8866F, 3.9987F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5417F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.1941F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, -0.0152F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5833F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.8333F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, -0.0152F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, -0.0152F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.8333F, AnimationHelper.createTranslationalVector(-2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-66.4217F, -23.3464F, 3.9551F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(22.4398F, -46.6571F, -146.0536F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(44.0003F, -28.5154F, -164.4659F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createRotationalVector(23.9428F, -32.6797F, -153.2227F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(22.7514F, -39.6105F, -129.9975F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(44.2125F, -28.1918F, -163.5962F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.8333F, AnimationHelper.createRotationalVector(23.9428F, -32.6797F, -153.2227F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(22.7514F, -39.6105F, -129.9975F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createRotationalVector(44.2125F, -28.1918F, -163.5962F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(23.9428F, -32.6797F, -153.2227F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.8333F, AnimationHelper.createRotationalVector(22.7514F, -39.6105F, -129.9975F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0417F, AnimationHelper.createRotationalVector(19.8592F, 2.2495F, -7.1565F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(6.5436F, 6.4141F, -6.65F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5417F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.1941F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.8333F, AnimationHelper.createTranslationalVector(2.0F, 0.3636F, 0.9848F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(22.4735F, -0.9255F, 2.8176F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(17.4828F, -0.6947F, 2.6778F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(12.4828F, -0.6947F, 2.6778F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(17.4828F, -0.6947F, 2.6778F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5F, AnimationHelper.createRotationalVector(14.9828F, -0.6947F, 2.6778F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createRotationalVector(17.4828F, -0.6947F, 2.6778F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.125F, AnimationHelper.createRotationalVector(4.68F, -0.35F, 2.59F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5417F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(9.9859F, 0.5155F, -2.1449F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(14.9784F, 0.7F, -1.8815F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(19.9784F, 0.7F, -1.8815F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(14.9784F, 0.7F, -1.8815F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5F, AnimationHelper.createRotationalVector(7.4784F, 0.7F, -1.8815F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createRotationalVector(14.9784F, 0.7F, -1.8815F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.125F, AnimationHelper.createRotationalVector(-3.45F, 0.35F, -2.19F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5417F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("frostmonarch", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(2.5F, 2.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0417F, AnimationHelper.createRotationalVector(-2.5F, -2.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7917F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("frostmonarch", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 10.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 9.6F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 10.4F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.625F, AnimationHelper.createTranslationalVector(0.0F, 9.6F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0417F, AnimationHelper.createTranslationalVector(0.0F, 9.6F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4583F, AnimationHelper.createTranslationalVector(0.0F, 10.4F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createTranslationalVector(0.0F, 9.6F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation death = Animation.Builder.create(10.5417F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-9.99F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createRotationalVector(20.18F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.7083F, AnimationHelper.createRotationalVector(17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.25F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(9.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.375F, AnimationHelper.createTranslationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(10.3333F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.4157F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.4167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-30.14F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4583F, AnimationHelper.createRotationalVector(19.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createRotationalVector(19.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4167F, AnimationHelper.createRotationalVector(4.0F, 35.0F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5F, AnimationHelper.createRotationalVector(4.0F, 35.0F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(4.0F, 35.0F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0417F, AnimationHelper.createRotationalVector(19.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(3.433F, -17.405F, -5.3248F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.7917F, AnimationHelper.createRotationalVector(3.433F, -17.405F, -5.3248F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.1667F, AnimationHelper.createRotationalVector(19.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createRotationalVector(23.14F, 1.27F, 0.39F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.0833F, AnimationHelper.createRotationalVector(19.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.3333F, AnimationHelper.createRotationalVector(-48.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.4583F, AnimationHelper.createRotationalVector(-48.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.7917F, AnimationHelper.createRotationalVector(-48.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.125F, AnimationHelper.createRotationalVector(-31.2703F, 39.173F, 46.1262F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.4583F, AnimationHelper.createRotationalVector(-6.1294F, 12.2878F, 93.1528F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.25F, AnimationHelper.createTranslationalVector(11.0F, -13.352F, -4.2099F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.75F, AnimationHelper.createTranslationalVector(11.3F, -14.5059F, -4.6234F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5F, AnimationHelper.createTranslationalVector(11.0F, -14.3027F, -4.514F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.9167F, AnimationHelper.createTranslationalVector(11.0F, -20.1665F, -5.8021F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(9.8333F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(9.9157F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(9.9167F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(15.3862F, 1.096F, 9.8644F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-24.6656F, 4.2085F, 16.5794F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.375F, AnimationHelper.createRotationalVector(-24.67F, 4.21F, 16.58F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.625F, AnimationHelper.createRotationalVector(-96.9865F, -10.3119F, -21.937F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9167F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.25F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2917F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4167F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4583F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.625F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.6667F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.75F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7917F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.8333F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9583F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0833F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.125F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.1667F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.2917F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.3333F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4583F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5833F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.625F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.6667F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.7917F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.8333F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9167F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9583F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.0F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.0833F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.125F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.1667F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.25F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.2917F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.3333F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.4167F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.4583F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.5F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.5833F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.625F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.6667F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.75F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.7917F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.8333F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.9167F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.9583F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0833F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.125F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.1667F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.25F, AnimationHelper.createTranslationalVector(0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createTranslationalVector(-0.1F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.625F, AnimationHelper.createTranslationalVector(-4.21F, -15.96F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createTranslationalVector(-4.21F, -15.96F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.5F, AnimationHelper.createTranslationalVector(-4.21F, -21.6823F, -0.8042F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(10.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.0823F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.0833F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-98.6728F, 1.2736F, -2.818F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.6667F, AnimationHelper.createRotationalVector(-102.2836F, 0.9217F, -8.636F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.7917F, AnimationHelper.createRotationalVector(-98.6728F, 1.2736F, -2.818F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.0833F, AnimationHelper.createRotationalVector(-43.1459F, 8.7594F, 102.344F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.2917F, AnimationHelper.createRotationalVector(-45.8064F, 19.4955F, 91.522F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, -2.8153F, 1.2535F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createTranslationalVector(0.0F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.6667F, AnimationHelper.createTranslationalVector(0.0F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.75F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7917F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.8333F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9583F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0833F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.125F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.1667F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.2917F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.3333F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4583F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.5833F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.625F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.6667F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.7917F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.8333F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9167F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9583F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.0F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.0833F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.125F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.1667F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.25F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.2917F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.3333F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.4167F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.4583F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.5F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.5833F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.625F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.6667F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.75F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.7917F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.8333F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.9167F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.9583F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0833F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.125F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.1667F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.25F, AnimationHelper.createTranslationalVector(-0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createTranslationalVector(0.1F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.3333F, AnimationHelper.createTranslationalVector(0.0F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.875F, AnimationHelper.createTranslationalVector(0.0F, -3.7785F, 0.9847F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.1667F, AnimationHelper.createTranslationalVector(2.0F, -13.1457F, -7.0611F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.8333F, AnimationHelper.createTranslationalVector(2.0F, -13.15F, -7.06F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.5417F, AnimationHelper.createTranslationalVector(1.4041F, -17.9607F, -8.2855F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(9.875F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(9.9573F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(9.9583F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-4.9223F, 14.8282F, 2.7131F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createRotationalVector(-4.9223F, 14.8282F, 2.7131F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createRotationalVector(-4.92F, 14.83F, 2.71F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.9583F, AnimationHelper.createRotationalVector(27.58F, 14.83F, 2.71F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 6.0F, -5.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createTranslationalVector(0.0F, 6.0F, -5.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createTranslationalVector(0.0F, 6.0F, -5.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.8333F, AnimationHelper.createTranslationalVector(0.0F, -3.37F, -3.3F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.2917F, AnimationHelper.createTranslationalVector(0.0F, -9.0F, -1.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(10.125F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.2073F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.2083F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(63.1082F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createRotationalVector(63.11F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.0F, AnimationHelper.createRotationalVector(78.11F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -0.82F, -2.07F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createTranslationalVector(0.0F, -0.82F, -2.07F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.2917F, AnimationHelper.createTranslationalVector(0.0F, -9.82F, -2.07F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(10.2083F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.2907F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.2917F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("frostmonarch", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(3.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("frostmonarch", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.6667F, AnimationHelper.createTranslationalVector(0.0F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.7917F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.8333F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9167F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9583F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.0F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.4167F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.4583F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.5F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.5833F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.625F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.6667F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.75F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0833F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.125F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.1667F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.25F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.2917F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.3333F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.4167F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.4583F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.5F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.5833F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.625F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.6667F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.75F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.7917F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.8333F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.9167F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.9583F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.0F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.0833F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.125F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.1667F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.25F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.2917F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.3333F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.4167F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.4583F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.5F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.5833F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.625F, AnimationHelper.createTranslationalVector(0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.6667F, AnimationHelper.createTranslationalVector(-0.25F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.75F, AnimationHelper.createTranslationalVector(0.35F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.7917F, AnimationHelper.createTranslationalVector(-0.35F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.8333F, AnimationHelper.createTranslationalVector(0.35F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.9167F, AnimationHelper.createTranslationalVector(-0.35F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(6.9583F, AnimationHelper.createTranslationalVector(0.35F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.0F, AnimationHelper.createTranslationalVector(-0.35F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.0833F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.125F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.1667F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.25F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.2917F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.3333F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.4167F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.4583F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.5F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.5833F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.625F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.6667F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.75F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.7917F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.8333F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.9167F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(7.9583F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.0F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.0833F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.125F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.1667F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.25F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.2917F, AnimationHelper.createTranslationalVector(0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.3333F, AnimationHelper.createTranslationalVector(-0.4F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.4583F, AnimationHelper.createTranslationalVector(0.0F, -8.8F, 2.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("eyes_off3", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(7.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.749F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.03F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.9573F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.03F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("crown", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(7.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.5833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 67.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 67.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("crown", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.749F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.8323F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.0833F, AnimationHelper.createTranslationalVector(3.023F, 3.5749F, 0.05F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.2917F, AnimationHelper.createTranslationalVector(2.1167F, 6.2603F, 0.18F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.4583F, AnimationHelper.createTranslationalVector(-0.9445F, 8.7676F, -0.0038F), Transformation.Interpolations.CUBIC),
			new Keyframe(8.5833F, AnimationHelper.createTranslationalVector(-1.0609F, 11.1261F, -0.1565F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.1667F, AnimationHelper.createTranslationalVector(-1.0609F, 11.1261F, -0.1565F), Transformation.Interpolations.CUBIC),
			new Keyframe(9.5417F, AnimationHelper.createTranslationalVector(5.9076F, 11.4362F, -0.7593F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("crown", new Transformation(Transformation.Targets.SCALE, 
			new Keyframe(9.2917F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(9.374F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(9.375F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("eyes_off2", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(7.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.9573F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(7.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.03F), Transformation.Interpolations.LINEAR),
			new Keyframe(8.2073F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.03F), Transformation.Interpolations.LINEAR),
			new Keyframe(8.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("eyes_off", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(8.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(8.2073F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(8.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.03F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation walk = Animation.Builder.create(1.0F).looping()
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-3.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(10.0F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(10.0F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(14.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(14.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.03F, 0.43F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.4F, 0.26F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -0.7F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.69F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -0.03F, 0.43F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -0.7F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.69F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.4F, 0.26F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -0.7F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation attack1 = Animation.Builder.create(0.875F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-10.0975F, -19.7143F, 3.4378F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(7.9692F, 9.9939F, -0.3525F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(7.9692F, 9.9939F, -0.3525F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(7.9692F, 9.9939F, -0.3525F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-27.8609F, -8.8605F, 4.6546F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(13.9917F, -14.3246F, -0.5346F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-16.3758F, -9.332F, -0.7072F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-151.5679F, -38.3295F, 33.2388F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-92.0981F, -25.1469F, 6.6648F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-40.8579F, -18.2095F, -5.1282F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-42.7373F, -17.9287F, -5.3617F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-42.7373F, -17.9287F, -5.3617F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-21.3233F, -5.7479F, 9.5379F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(4.5584F, 0.3653F, -1.9181F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(2.56F, 0.37F, -1.92F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(2.56F, 0.37F, -1.92F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(2.56F, 0.37F, -1.92F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-126.4785F, -5.3996F, 27.7158F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-126.4785F, -5.3996F, 27.7158F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-34.8714F, 8.7814F, 2.48F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-32.3546F, 21.7947F, -7.3817F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-32.3546F, 21.7947F, -7.3817F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-7.7616F, 14.8687F, 0.4969F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(-0.486F, 0.0057F, -1.8828F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(9.7992F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(10.4148F, 19.6961F, 1.0447F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(10.4148F, 19.6961F, 1.0447F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(10.4148F, 19.6961F, 1.0447F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation attack2 = Animation.Builder.create(1.75F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-12.3711F, -32.4777F, 1.2737F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-12.3711F, -32.4777F, 1.2737F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-12.3711F, -32.4777F, 1.2737F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-12.511F, 37.4764F, -1.354F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(-12.511F, 37.4764F, -1.354F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(13.133F, 27.3867F, 2.5977F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(13.133F, 27.3867F, 2.5977F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(13.133F, 27.3867F, 2.5977F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(27.9976F, -28.1344F, -2.3501F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4167F, AnimationHelper.createRotationalVector(27.9976F, -28.1344F, -2.3501F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-72.9292F, -53.7274F, 17.3765F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(11.1366F, -75.6951F, -62.6572F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(32.5014F, -87.5005F, -82.5014F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(32.5014F, -87.5005F, -82.5014F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-64.1159F, -21.9388F, 11.0695F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-69.2554F, -2.826F, 39.2486F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(9.392F, 24.3855F, 88.2965F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(71.5245F, 52.7199F, 133.4557F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2917F, AnimationHelper.createRotationalVector(71.5245F, 52.7199F, 133.4557F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-7.3241F, 1.6189F, 19.8963F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(7.1871F, -0.5553F, -2.087F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(7.1871F, -0.5553F, -2.087F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(2.4295F, -0.035F, 0.0482F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.537F, -0.1807F, -0.824F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2917F, AnimationHelper.createTranslationalVector(0.537F, -0.1807F, -0.824F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-177.007F, -54.2746F, 117.3475F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-190.4967F, -29.2969F, 118.7973F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-54.8258F, -15.0801F, 86.1139F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(73.5778F, 19.3866F, 99.9204F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(73.5778F, 19.3866F, 99.9204F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(-71.5024F, 31.0748F, -6.0473F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(-3.5543F, -1.3779F, -1.8516F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(-7.7449F, -1.325F, -2.3179F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2917F, AnimationHelper.createTranslationalVector(-7.7449F, -1.325F, -2.3179F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(13.2746F, 19.5065F, 7.0044F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(15.1437F, 34.0544F, 11.1179F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(2.7583F, -24.9746F, -3.6652F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(4.6111F, -43.0181F, -5.1601F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(4.6111F, -43.0181F, -5.1601F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(-0.0019F, 0.0436F, 0.999F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(-0.0019F, 0.0436F, 0.999F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation attack3 = Animation.Builder.create(1.5F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-19.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(70.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(68.16F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(70.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-40.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-40.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createRotationalVector(-40.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, -0.8264F, 1.8213F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, -0.225F, -0.0862F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createTranslationalVector(0.0F, -0.614F, 1.4706F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, -0.614F, 1.4706F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-253.8264F, 21.6938F, 13.6191F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-241.6878F, 16.8201F, 32.2477F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-185.5165F, -24.2541F, 27.3765F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-103.569F, -18.7393F, 10.8248F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-68.6429F, -8.0902F, -5.118F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createRotationalVector(-76.0273F, -10.4883F, -5.8338F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-39.5686F, 6.4087F, 15.1926F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 3.8293F, 1.8265F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 3.8293F, 1.8265F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(1.0F, -2.2158F, -0.2753F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createTranslationalVector(1.0F, -2.3869F, -0.7513F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-240.7477F, -20.6533F, -7.5886F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-228.3125F, -12.7515F, -10.5445F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-159.1294F, 0.2845F, -10.3564F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-89.4375F, 11.3094F, 2.1873F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-48.3934F, 20.7049F, 14.7075F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createRotationalVector(-53.3934F, 20.7049F, 14.7075F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-31.6066F, -7.9936F, -20.2342F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 3.6658F, 1.7806F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 3.6658F, 1.7806F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(0.0F, -1.8731F, -1.6221F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.125F, AnimationHelper.createTranslationalVector(0.0F, -1.8731F, -1.6221F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(1.7892F, -0.4456F, 10.1185F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(1.7892F, -0.4456F, 10.1185F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-7.6177F, 19.9426F, 2.4843F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-7.6177F, 19.9426F, 2.4843F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-3.8511F, -0.5066F, -9.983F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-3.8511F, -0.5066F, -9.983F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(17.4375F, 1.5017F, -7.2696F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(17.4375F, 1.5017F, -7.2696F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, -2.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.25F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("frostmonarch", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -0.1F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, -2.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

}
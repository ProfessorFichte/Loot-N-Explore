package more_rpg_loot.client.entity.models.frozen_depths.monarchs_soldier;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 5.1.4
 * Exported for Minecraft version 1.17 or later with Mojang mappings
 * Converted for Fabric 1.21.1 with Yarn mappings
 */
public class MonarchsSoldierAnimations {
	public static final Animation idle = Animation.Builder.create(2.0F).looping()
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(4.065F, 0.0354F, -0.4987F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(4.0467F, -0.0397F, 0.561F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-3.9065F, 0.9977F, -0.0681F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-3.7311F, -1.1226F, 0.0736F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-7.9121F, 5.1384F, 8.9943F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-6.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(1.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(5.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
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
		.build();

	public static final Animation walk = Animation.Builder.create(0.9583F).looping()
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-1.7142F, 10.7632F, 12.1302F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-9.7752F, -5.4294F, -10.4731F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-14.0748F, -5.6128F, -10.4342F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-10.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(5.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(9.5F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(5.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(14.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0833F, AnimationHelper.createRotationalVector(16.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-10.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(14.09F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.18F, 1.44F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.26F, 1.28F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.18F, 1.44F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0833F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(16.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-10.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.26F, 1.28F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -2.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation walk_shield = Animation.Builder.create(0.9583F).looping()
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.4778F, -5.1229F, 0.662F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-67.4778F, -5.1229F, 0.662F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-57.4778F, -5.1229F, 0.662F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-52.4778F, -5.1229F, 0.662F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-57.4778F, -5.1229F, 0.662F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-0.0691F, 0.5385F, 0.8398F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(-0.1305F, 0.2688F, 1.4906F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(-0.0691F, 0.5385F, 0.8398F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-9.7752F, -5.4294F, -10.4731F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-14.0748F, -5.6128F, -10.4342F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-10.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(5.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(9.5F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(5.0F, -5.0F, -8.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(14.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0833F, AnimationHelper.createRotationalVector(16.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-10.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(14.09F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.18F, 1.44F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.26F, 1.28F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 0.18F, 1.44F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.0833F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(16.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-10.34F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-10.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.26F, 1.28F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.6F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createTranslationalVector(0.0F, 1.25F, -2.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("rightItem", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(4.8077F, -40.0044F, -89.7156F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("rightItem", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.7165F, -1.4099F, -0.2634F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation attack = Animation.Builder.create(0.75F)
		.addBoneAnimation("waist", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-2.6604F, -19.9801F, 0.9095F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-7.6604F, -19.9801F, 0.9095F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-7.6604F, -19.9801F, 0.9095F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(13.5674F, 8.7323F, 1.6898F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(13.5674F, 8.7323F, 1.6898F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(11.5F, 10.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-8.7496F, -7.447F, -1.019F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-8.6752F, -0.0332F, -2.1567F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createRotationalVector(18.2642F, -2.4025F, 16.1762F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(28.849F, -14.2576F, 12.1629F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(33.849F, -14.2576F, 12.1629F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-15.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-144.4089F, 6.7411F, 34.3688F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-144.4089F, 6.7411F, 34.3688F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-42.6732F, 13.3687F, 3.3282F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(36.7695F, -12.6759F, -17.5667F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(35.051F, -11.5581F, -10.4298F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(5.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, -0.1089F, 1.2452F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, -0.1089F, 1.2452F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(-4.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-9.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-9.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(9.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(9.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(9.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation attack_aoe = Animation.Builder.create(0.9167F)
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-6.2071F, -37.333F, -3.826F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2083F, AnimationHelper.createRotationalVector(-6.2071F, -37.333F, -3.826F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(11.6958F, 64.5364F, 10.5866F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(11.6958F, 64.5364F, 10.5866F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(7.296F, -14.84F, -1.0642F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Body", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.15F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("Head", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-10.4096F, 22.4097F, -2.0675F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-24.6576F, -58.3187F, 11.2117F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-2.0532F, -44.7824F, 4.981F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(1.6041F, -1.6543F, 5.3394F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-6.8464F, -18.2748F, 38.6117F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-22.5F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-21.7188F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-10.0F, 5.0F, 8.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(87.4267F, -37.5557F, -69.1718F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(-73.3137F, 18.728F, -65.3299F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-73.3137F, 18.728F, -65.3299F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-43.1951F, 17.0799F, -44.7567F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(57.115F, -12.9477F, -15.499F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(5.0F, -7.5F, -7.5F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftArm", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5417F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(15.9545F, 44.687F, 5.9674F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(15.9545F, 44.687F, 5.9674F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createRotationalVector(15.9545F, 44.687F, 5.9674F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createRotationalVector(15.9545F, 44.687F, 5.9674F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(15.9545F, 44.687F, 5.9674F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(6.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("RightLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.5F, 0.0F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.75F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-8.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-3.6495F, 22.2692F, -3.2993F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.addBoneAnimation("LeftLeg", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.1667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(-1.1F, -0.35F, -2.25F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.CUBIC)
		))
		.build();
}
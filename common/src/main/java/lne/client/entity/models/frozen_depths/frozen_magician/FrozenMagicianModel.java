package lne.client.entity.models.frozen_depths.frozen_magician;

import com.mrpg_lib.client.animation.AnimationBlender;
import lne.entity.frozen_depths.mob.frozen_magician.FrozenMagicianAnimationTimes;
import lne.entity.frozen_depths.mob.frozen_magician.FrozenMagicianEntity;
import lne.client.entity.models.HumanoidBoneModel;
import lne.client.entity.models.HumanoidBones;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrozenMagicianModel<T extends FrozenMagicianEntity> extends SinglePartEntityModel<T> implements HumanoidBoneModel {
    private static final float HOLD_START = 0.625F;
    private static final float SPIN_START = 0.25F;
    private static final String SPIN_BONE = "ice_orb";
    private static final AnimationState STOPPED = new AnimationState();
    private static final Map<Integer, Animation> LONG_CASTS = new HashMap<>();
    private final AnimationBlender blender = new AnimationBlender();
    private final ModelPart Main;
    private final ModelPart waist;
    private final ModelPart Body;
    private final ModelPart cloak;
    private final ModelPart Head;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart leftItem;
    private final ModelPart ice_orb;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final HumanoidBones bones;

    public FrozenMagicianModel(ModelPart root) {
        this.Main = root.getChild("Main");
        this.waist = this.Main.getChild("waist");
        this.Body = this.waist.getChild("Body");
        this.cloak = this.Body.getChild("cloak");
        this.Head = this.Body.getChild("Head");
        this.RightArm = this.Body.getChild("RightArm");
        this.LeftArm = this.Body.getChild("LeftArm");
        this.leftItem = this.LeftArm.getChild("leftItem");
        this.ice_orb = this.leftItem.getChild("ice_orb");
        this.RightLeg = this.Main.getChild("RightLeg");
        this.LeftLeg = this.Main.getChild("LeftLeg");
        this.bones = new HumanoidBones(this.Body, this.Head, this.RightArm, this.LeftArm, this.RightLeg, this.LeftLeg);
    }

    @Override
    public HumanoidBones bones() {
        return this.bones;
    }

    @Override
    public ModelPart getPart() {
        return this.Main;
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData Main = modelPartData.addChild("Main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData waist = Main.addChild("waist", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
        .uv(0, 30).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 14.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cloak = Body.addChild("cloak", ModelPartBuilder.create(), ModelTransform.of(-0.5F, -13.0F, 0.5F, 0.0436F, 0.0F, 0.0F));

        ModelPartData Body_r1 = cloak.addChild("Body_r1", ModelPartBuilder.create().uv(26, 32).cuboid(-6.0F, 0.3731F, -1.18F, 13.0F, 20.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.025F))
        .uv(32, 16).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData RightArm = Body.addChild("RightArm", ModelPartBuilder.create().uv(56, 2).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
        .uv(8, 16).cuboid(-3.0F, -1.5F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.05F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

        ModelPartData LeftArm = Body.addChild("LeftArm", ModelPartBuilder.create().uv(56, 2).mirrored().cuboid(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
        .uv(8, 16).mirrored().cuboid(0.0F, -1.5F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.05F)).mirrored(false), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

        ModelPartData leftItem = LeftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 8.0F, 1.0F));

        ModelPartData ice_orb = leftItem.addChild("ice_orb", ModelPartBuilder.create().uv(22, 16).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.25F, 0.5F, -5.0F, 0.0F, -1.789F, 0.7854F));

        ModelPartData cube_r1 = ice_orb.addChild("cube_r1", ModelPartBuilder.create().uv(2, 52).cuboid(-5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r2 = ice_orb.addChild("cube_r2", ModelPartBuilder.create().uv(2, 52).cuboid(-1.7479F, -4.7929F, 1.5561F, 11.0F, 11.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.7521F, 0.7071F, -1.5561F, 0.0F, 0.0F, -3.1416F));

        ModelPartData RightLeg = Main.addChild("RightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));

        ModelPartData LeftLeg = Main.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, -12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.bones.track(entity);

        AnimationBlender.Session session = blender.begin(entity, ageInTicks);
        Animation cast = castAnimation(entity.getCastTicks());
        session.action(cast == FrozenMagicianAnimations.cast ? entity.castAnimationState : STOPPED, FrozenMagicianAnimations.cast);
        LONG_CASTS.values().forEach(animation -> session.action(animation == cast ? entity.castAnimationState : STOPPED, animation));
        float actionWeight = session.apply(this, FrozenMagicianAnimations.idle);
        AnimationBlender.movement(this, FrozenMagicianAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f, 1.0F - actionWeight);
    }

    private static Animation castAnimation(int castTicks) {
        int extraTicks = castTicks - FrozenMagicianAnimationTimes.CAST_RELEASE_TICK;
        if (extraTicks <= 0) {
            return FrozenMagicianAnimations.cast;
        }
        return LONG_CASTS.computeIfAbsent(extraTicks, ticks -> extend(FrozenMagicianAnimations.cast, ticks / 20.0F));
    }

    private static Animation extend(Animation animation, float extra) {
        Map<String, List<Transformation>> bones = new HashMap<>();
        animation.boneAnimations().forEach((bone, transformations) -> bones.put(bone, transformations.stream()
                .map(transformation -> new Transformation(transformation.target(),
                        bone.equals(SPIN_BONE) && transformation.target() == Transformation.Targets.ROTATE
                                ? spin(transformation.keyframes(), extra)
                                : hold(transformation.keyframes(), extra)))
                .toList()));
        return new Animation(animation.lengthInSeconds() + extra, false, bones);
    }

    private static Keyframe[] hold(Keyframe[] keyframes, float extra) {
        List<Keyframe> result = new ArrayList<>();
        boolean hasHoldKey = false;
        for (Keyframe keyframe : keyframes) {
            if (keyframe.timestamp() <= HOLD_START) {
                result.add(keyframe);
                hasHoldKey |= keyframe.timestamp() == HOLD_START;
            }
        }
        Vector3f pose = sample(keyframes, HOLD_START);
        if (!hasHoldKey) {
            result.add(new Keyframe(HOLD_START, pose, Transformation.Interpolations.CUBIC));
        }
        result.add(new Keyframe(HOLD_START + extra, pose, Transformation.Interpolations.LINEAR));
        for (Keyframe keyframe : keyframes) {
            if (keyframe.timestamp() > HOLD_START) {
                result.add(new Keyframe(keyframe.timestamp() + extra, keyframe.target(), keyframe.interpolation()));
            }
        }
        return result.toArray(Keyframe[]::new);
    }

    private static Keyframe[] spin(Keyframe[] keyframes, float extra) {
        float spinLength = keyframes[keyframes.length - 1].timestamp() - SPIN_START;
        float turns = Math.max(1, Math.round((spinLength + extra) / spinLength));
        Keyframe[] result = new Keyframe[keyframes.length];
        for (int i = 0; i < keyframes.length; i++) {
            Keyframe keyframe = keyframes[i];
            result[i] = keyframe.timestamp() > SPIN_START
                    ? new Keyframe(keyframe.timestamp() + extra, new Vector3f(keyframe.target()).mul(turns), keyframe.interpolation())
                    : keyframe;
        }
        return result;
    }

    private static Vector3f sample(Keyframe[] keyframes, float seconds) {
        int start = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> seconds <= keyframes[index].timestamp()) - 1);
        int end = Math.min(keyframes.length - 1, start + 1);
        Keyframe from = keyframes[start];
        Keyframe to = keyframes[end];
        float delta = end != start ? MathHelper.clamp((seconds - from.timestamp()) / (to.timestamp() - from.timestamp()), 0.0F, 1.0F) : 0.0F;
        return to.interpolation().apply(new Vector3f(), delta, keyframes, start, end, 1.0F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        Main.render(matrices, vertices, light, overlay, color);
    }
}

package lne.compat.player_animator;

import com.mrpg_lib.compat.player_animator.client.MobModelAdapter;
import com.mrpg_lib.compat.player_animator.client.MobModelParts;
import lne.client.entity.models.HumanoidBoneModel;
import lne.client.entity.models.HumanoidBones;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.Arm;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

// The proxies stand in for the independent parts of a player model, while these models nest head and arms
// under the body. prepare() loads each bone's pose relative to the body's parent into its proxy (mirrored into
// right-handed player space for right-handed mobs) so playerAnimator blends over it exactly like over a
// player's vanilla pose, and finish() converts the result back into the bone hierarchy.
@Environment(EnvType.CLIENT)
public final class LneMobModelAdapter implements MobModelAdapter {
    private static final ModelTransform HEAD = ModelTransform.pivot(0.0F, 0.0F, 0.0F);
    private static final ModelTransform TORSO = ModelTransform.pivot(0.0F, 0.0F, 0.0F);
    private static final ModelTransform RIGHT_ARM = ModelTransform.pivot(-5.0F, 2.0F, 0.0F);
    private static final ModelTransform LEFT_ARM = ModelTransform.pivot(5.0F, 2.0F, 0.0F);
    private static final ModelTransform RIGHT_LEG = ModelTransform.pivot(-1.9F, 12.0F, 0.0F);
    private static final ModelTransform LEFT_LEG = ModelTransform.pivot(1.9F, 12.0F, 0.0F);

    private final Map<EntityModel<?>, MobModelParts> proxies = new WeakHashMap<>();

    @Override
    @Nullable
    public MobModelParts parts(EntityModel<?> model) {
        if (!(model instanceof HumanoidBoneModel)) return null;
        return proxies.computeIfAbsent(model, key -> new MobModelParts(proxy(HEAD), proxy(TORSO),
                proxy(LEFT_ARM), proxy(RIGHT_ARM), proxy(LEFT_LEG), proxy(RIGHT_LEG)));
    }

    @Override
    public void prepare(EntityModel<?> model) {
        if (!(model instanceof HumanoidBoneModel boneModel)) return;
        MobModelParts parts = proxies.get(model);
        if (parts == null) return;
        HumanoidBones bones = boneModel.bones();
        boolean mirror = bones.mainArm() == Arm.RIGHT;
        Quaternionf body = rotation(bones.body());
        load(parts.torso(), TORSO, bones.body(), body, mirror);
        load(parts.head(), HEAD, bones.head(), new Quaternionf(body).mul(rotation(bones.head())), mirror);
        load(playerArm(parts, Arm.RIGHT, mirror), playerArmRest(Arm.RIGHT, mirror), bones.rightArm(),
                new Quaternionf(body).mul(rotation(bones.rightArm())), mirror);
        load(playerArm(parts, Arm.LEFT, mirror), playerArmRest(Arm.LEFT, mirror), bones.leftArm(),
                new Quaternionf(body).mul(rotation(bones.leftArm())), mirror);
        load(playerLeg(parts, Arm.RIGHT, mirror), playerLegRest(Arm.RIGHT, mirror), bones.rightLeg(), rotation(bones.rightLeg()), mirror);
        load(playerLeg(parts, Arm.LEFT, mirror), playerLegRest(Arm.LEFT, mirror), bones.leftLeg(), rotation(bones.leftLeg()), mirror);
    }

    @Override
    public void finish(EntityModel<?> model) {
        if (!(model instanceof HumanoidBoneModel boneModel)) return;
        MobModelParts parts = proxies.get(model);
        if (parts == null) return;
        HumanoidBones bones = boneModel.bones();
        boolean mirror = bones.mainArm() == Arm.RIGHT;
        Quaternionf body = read(parts.torso(), mirror);
        Quaternionf inverseBody = new Quaternionf(body).conjugate();
        store(bones.body(), parts.torso(), TORSO, body, mirror);
        store(bones.head(), parts.head(), HEAD, new Quaternionf(inverseBody).mul(read(parts.head(), mirror)), mirror);
        for (Arm side : Arm.values()) {
            ModelPart arm = playerArm(parts, side, mirror);
            store(side == Arm.RIGHT ? bones.rightArm() : bones.leftArm(), arm, playerArmRest(side, mirror),
                    new Quaternionf(inverseBody).mul(read(arm, mirror)), mirror);
            ModelPart leg = playerLeg(parts, side, mirror);
            store(side == Arm.RIGHT ? bones.rightLeg() : bones.leftLeg(), leg, playerLegRest(side, mirror), read(leg, mirror), mirror);
        }
    }

    private static ModelPart playerArm(MobModelParts parts, Arm modelSide, boolean mirror) {
        return (modelSide == Arm.RIGHT) != mirror ? parts.rightArm() : parts.leftArm();
    }

    private static ModelTransform playerArmRest(Arm modelSide, boolean mirror) {
        return (modelSide == Arm.RIGHT) != mirror ? RIGHT_ARM : LEFT_ARM;
    }

    private static ModelPart playerLeg(MobModelParts parts, Arm modelSide, boolean mirror) {
        return (modelSide == Arm.RIGHT) != mirror ? parts.rightLeg() : parts.leftLeg();
    }

    private static ModelTransform playerLegRest(Arm modelSide, boolean mirror) {
        return (modelSide == Arm.RIGHT) != mirror ? RIGHT_LEG : LEFT_LEG;
    }

    private static void load(ModelPart proxy, ModelTransform proxyRest, ModelPart bone, Quaternionf rotation, boolean mirror) {
        ModelTransform rest = bone.getDefaultTransform();
        float sign = mirror ? -1.0F : 1.0F;
        proxy.pivotX = proxyRest.pivotX + sign * (bone.pivotX - rest.pivotX);
        proxy.pivotY = proxyRest.pivotY + (bone.pivotY - rest.pivotY);
        proxy.pivotZ = proxyRest.pivotZ + (bone.pivotZ - rest.pivotZ);
        Vector3f angles = mirrored(rotation, mirror).getEulerAnglesZYX(new Vector3f());
        proxy.pitch = angles.x;
        proxy.yaw = angles.y;
        proxy.roll = angles.z;
    }

    private static Quaternionf read(ModelPart proxy, boolean mirror) {
        return mirrored(rotation(proxy), mirror);
    }

    private static void store(ModelPart bone, ModelPart proxy, ModelTransform proxyRest, Quaternionf rotation, boolean mirror) {
        ModelTransform rest = bone.getDefaultTransform();
        float sign = mirror ? -1.0F : 1.0F;
        bone.pivotX = rest.pivotX + sign * (proxy.pivotX - proxyRest.pivotX);
        bone.pivotY = rest.pivotY + (proxy.pivotY - proxyRest.pivotY);
        bone.pivotZ = rest.pivotZ + (proxy.pivotZ - proxyRest.pivotZ);
        Vector3f angles = rotation.getEulerAnglesZYX(new Vector3f());
        bone.pitch = angles.x;
        bone.yaw = angles.y;
        bone.roll = angles.z;
    }

    private static Quaternionf mirrored(Quaternionf rotation, boolean mirror) {
        return mirror ? new Quaternionf(rotation.x, -rotation.y, -rotation.z, rotation.w) : rotation;
    }

    private static Quaternionf rotation(ModelPart part) {
        return new Quaternionf().rotationZYX(part.roll, part.yaw, part.pitch);
    }

    private static ModelPart proxy(ModelTransform transform) {
        ModelPart part = new ModelPart(List.of(), Map.of());
        part.setTransform(transform);
        part.setDefaultTransform(transform);
        return part;
    }
}

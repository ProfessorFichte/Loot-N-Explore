package lne.compat.player_animator;

import com.mrpg_lib.compat.player_animator.client.MobAnimatable;
import com.mrpg_lib.compat.player_animator.client.MobAnimationState;
import com.mrpg_lib.compat.player_animator.client.MobModelAdapters;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class LneMobAnimationCompat {
    public static final double PLAYER_BODY_PIVOT = 0.7;
    private static final float ITEM_POSITION_SCALE = 1.0F / 16.0F;
    // the animations are authored for 4 px thick player arms, these models have 2 px arms
    private static final float ARM_THICKNESS_SCALE = 0.5F;

    private LneMobAnimationCompat() {
    }

    public static void register() {
        MobModelAdapters.register(new LneMobModelAdapter());
    }

    public static void applyHeldItem(LivingEntity entity, Arm modelArm, MatrixStack matrices) {
        AnimationApplier applier = activeApplier(entity);
        if (applier == null) {
            return;
        }
        boolean mirror = entity.getMainArm() == Arm.RIGHT;
        if (mirror) {
            matrices.scale(-1.0F, 1.0F, 1.0F);
        }
        String part = (mirror ? modelArm.getOpposite() : modelArm) == Arm.LEFT ? "leftItem" : "rightItem";
        Vec3f scale = applier.get3DTransform(part, TransformType.SCALE, new Vec3f(1.0F, 1.0F, 1.0F));
        Vec3f rotation = applier.get3DTransform(part, TransformType.ROTATION, Vec3f.ZERO);
        Vec3f position = applier.get3DTransform(part, TransformType.POSITION, Vec3f.ZERO);
        matrices.scale(scale.getX(), scale.getY(), scale.getZ());
        matrices.translate(position.getX() * ITEM_POSITION_SCALE * ARM_THICKNESS_SCALE,
                position.getY() * ITEM_POSITION_SCALE * ARM_THICKNESS_SCALE,
                position.getZ() * ITEM_POSITION_SCALE);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(rotation.getZ()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(rotation.getY()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(rotation.getX()));
        if (mirror) {
            matrices.scale(-1.0F, 1.0F, 1.0F);
        }
    }

    // The library applies the whole-body transform around a player-sized pivot and never mirrors it, while
    // LneMobModelAdapter mirrors the bones of right-handed mobs. Undo it and apply it again mirrored and
    // around the model's own hip height so body twist, limbs and head stay in sync.
    public static void replaceBodyTransform(LivingEntity entity, MatrixStack matrices, float tickDelta, double pivot, float offsetScale) {
        AnimationApplier applier = activeApplier(entity);
        if (applier == null) {
            return;
        }
        applier.setTickDelta(tickDelta);
        Vec3f position = applier.get3DTransform("body", TransformType.POSITION, Vec3f.ZERO);
        Vec3f rotation = applier.get3DTransform("body", TransformType.ROTATION, Vec3f.ZERO);
        if (isZero(position) && isZero(rotation)) {
            return;
        }
        matrices.translate(0.0, PLAYER_BODY_PIVOT, 0.0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(-rotation.getX()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-rotation.getY()));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(-rotation.getZ()));
        matrices.translate(-position.getX(), -position.getY() - PLAYER_BODY_PIVOT, -position.getZ());

        float sign = entity.getMainArm() == Arm.RIGHT ? -1.0F : 1.0F;
        matrices.translate(sign * position.getX() * offsetScale, position.getY() * offsetScale + pivot, position.getZ() * offsetScale);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(sign * rotation.getZ()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(sign * rotation.getY()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(rotation.getX()));
        matrices.translate(0.0, -pivot, 0.0);
    }

    public static boolean isAnimating(LivingEntity entity) {
        return activeApplier(entity) != null;
    }

    private static boolean isZero(Vec3f vector) {
        return vector.getX() == 0.0F && vector.getY() == 0.0F && vector.getZ() == 0.0F;
    }

    @Nullable
    private static AnimationApplier activeApplier(LivingEntity entity) {
        if (!(entity instanceof MobAnimatable animatable)) {
            return null;
        }
        MobAnimationState state = animatable.mrpgLib$animationStateIfPresent();
        if (state == null || !state.needsRender() || !state.isActive()) {
            return null;
        }
        return state.applier();
    }
}

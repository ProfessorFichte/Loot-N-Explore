package more_rpg_loot.entity.frozen_depths.mob.frozen_ranger;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.entity.frozen_depths.projectile.FrozenArrowEntity;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FrostedRangerEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> PERFORMING_SALVO = DataTracker.registerData(FrostedRangerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState normalShootAnimationState = new AnimationState();
    public final AnimationState salvoAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    int salvoCooldown = 0;

    public FrostedRangerEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.moveControl = new SmoothMoveControl(this);
        this.experiencePoints += 5;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 28.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PERFORMING_SALVO, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FrostSalvoGoal(this));
        this.goalSelector.add(2, new BowAttackGoal<>(this, 1.0, 20, 15.0F));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        if (FabricLoader.getInstance().isModLoaded("thermoo")) {
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0);
        return data;
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(LNE_WeaponItems.FROZEN_BOW.item()));
        this.updateDropChances(EquipmentSlot.MAINHAND);
    }

    @Override
    public void updateDropChances(EquipmentSlot slot) {
        if (slot.getType() == EquipmentSlot.Type.HAND) {
            this.handDropChances[slot.getEntitySlotId()] = 0.0F;
        } else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            this.armorDropChances[slot.getEntitySlotId()] = 0.0F;
        }
    }

    public boolean isPerformingSalvo() {
        return this.dataTracker.get(PERFORMING_SALVO);
    }

    public void setPerformingSalvo(boolean performing) {
        this.dataTracker.set(PERFORMING_SALVO, performing);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient && salvoCooldown > 0) {
            salvoCooldown--;
        }

        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.isPerformingSalvo()) {
            this.salvoAnimationState.startIfNotRunning(this.age);
            this.normalShootAnimationState.stop();
            this.idleAnimationState.stop();
        } else if (this.isUsingItem()) {
            // Bow draw begins here; vanilla BowAttackGoal fires the arrow at itemUseTime == 20,
            // which lands near the end of the ~20.8-tick attackFast clip (its release/relax tail).
            this.normalShootAnimationState.startIfNotRunning(this.age);
            this.salvoAnimationState.stop();
            this.idleAnimationState.stop();
        } else {
            this.normalShootAnimationState.stop();
            this.salvoAnimationState.stop();
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --idleAnimationTimeout;
            }
        }
    }

    protected int getSalvoArrowCount() {
        return 5;
    }

    private class FrostSalvoGoal extends Goal {
        private final FrostedRangerEntity ranger;
        private LivingEntity target;
        private int phase = 0;
        private int windupTimer = 0;
        private int arrowsFired = 0;
        private int fireTimer = 0;

        FrostSalvoGoal(FrostedRangerEntity ranger) {
            this.ranger = ranger;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = ranger.getTarget();
            return target != null && target.isAlive()
                    && ranger.squaredDistanceTo(target) <= 20.0 * 20.0
                    && ranger.salvoCooldown <= 0;
        }

        @Override
        public boolean shouldContinue() {
            return phase != 0 && (target == null || target.isAlive());
        }

        @Override
        public void start() {
            phase = 1;
            windupTimer = 15;
            arrowsFired = 0;
            fireTimer = 0;
            ranger.setPerformingSalvo(true);
            ranger.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) ranger.getLookControl().lookAt(target, 30.0F, 30.0F);

            if (phase == 1) {
                if (--windupTimer <= 0) {
                    jumpBack();
                    phase = 2;
                    fireTimer = 3;
                }
            } else if (phase == 2) {
                if (--fireTimer <= 0 && arrowsFired < ranger.getSalvoArrowCount()) {
                    fireArrow(arrowsFired);
                    arrowsFired++;
                    fireTimer = 4;
                }
                if (arrowsFired >= ranger.getSalvoArrowCount()) stop();
            }
        }

        private void jumpBack() {
            if (target == null) return;
            Vec3d awayDir = ranger.getPos().subtract(target.getPos()).normalize();
            ranger.addVelocity(awayDir.x * 0.5, 0.3, awayDir.z * 0.5);
            ranger.velocityModified = true;
        }

        private void fireArrow(int index) {
            if (target == null) return;
            World world = ranger.getWorld();

            double spread = (Math.PI / (ranger.getSalvoArrowCount() - 1));
            double baseAngle = Math.atan2(target.getZ() - ranger.getZ(), target.getX() - ranger.getX());
            double angle = baseAngle + (index - ranger.getSalvoArrowCount() / 2.0) * spread * 0.4;

            FrozenArrowEntity arrow = new FrozenArrowEntity(world, ranger);
            double dx = Math.cos(angle);
            double dy = (target.getBodyY(0.5) - arrow.getY()) / ranger.distanceTo(target) + ranger.random.nextGaussian() * 0.05;
            double dz = Math.sin(angle);

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
            arrow.setVelocity(dx / length, dy / length, dz / length, 1.6F, 5.0F);
            arrow.setOwner(ranger);
            arrow.setDamage(ranger.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5);
            world.spawnEntity(arrow);
        }

        @Override
        public void stop() {
            phase = 0;
            ranger.setPerformingSalvo(false);
            ranger.salvoCooldown = 100 + ranger.random.nextInt(60);
        }
    }

    static class SmoothMoveControl extends MoveControl {
        SmoothMoveControl(FrostedRangerEntity entity) {
            super(entity);
        }

        @Override
        protected float wrapDegrees(float from, float to, float max) {
            float f = MathHelper.wrapDegrees(to - from);
            if (f > 30.0F) f = 30.0F;
            if (f < -30.0F) f = -30.0F;
            float g = from + f;
            if (g < 0.0F) g += 360.0F;
            else if (g > 360.0F) g -= 360.0F;
            return g;
        }
    }
}

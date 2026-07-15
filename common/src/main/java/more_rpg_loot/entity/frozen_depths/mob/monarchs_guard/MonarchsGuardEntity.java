package more_rpg_loot.entity.frozen_depths.mob.monarchs_guard;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.entity.frozen_depths.projectile.StraightIcicleEntity;
import more_rpg_loot.item.CommonItems;
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

public class MonarchsGuardEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> DASHING = DataTracker.registerData(MonarchsGuardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> THROWING = DataTracker.registerData(MonarchsGuardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState stabAnimationState = new AnimationState();
    public final AnimationState spearThrowAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private int dashCooldown = 0;
    private int throwCooldown = 0;
    private int stabCooldown = 0;

    public MonarchsGuardEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.moveControl = new SmoothMoveControl(this);
        this.experiencePoints += 8;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DASHING, false);
        builder.add(THROWING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DashAttackGoal(this));
        this.goalSelector.add(2, new SpearThrowGoal(this));
        this.goalSelector.add(3, new LongRangeStabGoal(this));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

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
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0);
        return data;
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(CommonItems.GUARDS_FROST_LANCE.item()));
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

    public boolean isDashing() { return this.dataTracker.get(DASHING); }
    public void setDashing(boolean dashing) { this.dataTracker.set(DASHING, dashing); }

    public boolean isThrowing() { return this.dataTracker.get(THROWING); }
    public void setThrowing(boolean throwing) { this.dataTracker.set(THROWING, throwing); }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (dashCooldown > 0) dashCooldown--;
            if (throwCooldown > 0) throwCooldown--;
            if (stabCooldown > 0) stabCooldown--;
        }
        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        boolean moving = this.getVelocity().horizontalLengthSquared() > 0.01;

        if (this.isDashing()) {
            this.chargeAnimationState.startIfNotRunning(this.age);
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
        } else if (this.isThrowing()) {
            this.spearThrowAnimationState.startIfNotRunning(this.age);
            this.idleAnimationState.stop();
        } else if (this.handSwinging && this.handSwingTicks == 0) {
            this.stabAnimationState.start(this.age);
        } else if (moving) {
            this.walkAnimationState.startIfNotRunning(this.age);
            this.chargeAnimationState.stop();
            this.idleAnimationState.stop();
        } else {
            this.chargeAnimationState.stop();
            this.walkAnimationState.stop();
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --idleAnimationTimeout;
            }
        }
    }

    private class DashAttackGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int windupTimer = 0;
        private boolean active = false;

        DashAttackGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.dashCooldown > 0) return false;
            double dist = guard.squaredDistanceTo(target);
            return dist > 4.0 * 4.0 && dist <= 12.0 * 12.0;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 10;
            guard.setDashing(true);
            guard.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (--windupTimer <= 0) {
                if (target != null) {
                    Vec3d dir = target.getPos().subtract(guard.getPos()).normalize();
                    guard.setVelocity(dir.x * 1.5, 0.1, dir.z * 1.5);
                    guard.velocityModified = true;

                    guard.getWorld().getNonSpectatingEntities(LivingEntity.class, guard.getBoundingBox().expand(1.5).offset(dir.multiply(2))).forEach(entity -> {
                        if (entity != guard && !entity.isTeammate(guard)) {
                            guard.tryAttack(entity);
                        }
                    });
                }
                stop();
            }
        }

        @Override
        public void stop() {
            active = false;
            guard.setDashing(false);
            guard.dashCooldown = 60 + guard.random.nextInt(40);
        }
    }

    private class SpearThrowGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int windupTimer = 0;
        private boolean active = false;

        SpearThrowGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.throwCooldown > 0) return false;
            double dist = guard.squaredDistanceTo(target);
            return dist > 6.0 * 6.0 && dist <= 20.0 * 20.0;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 20;
            guard.setThrowing(true);
            guard.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (--windupTimer <= 0) {
                throwSpear();
                stop();
            }
        }

        private void throwSpear() {
            if (target == null) return;
            World world = guard.getWorld();
            Vec3d spawnPos = guard.getPos().add(0, guard.getHeight() * 0.5, 0);
            Vec3d toTarget = target.getPos().add(0, target.getHeight() * 0.5, 0).subtract(spawnPos).normalize();
            float yaw = (float) (MathHelper.atan2(-toTarget.x, toTarget.z) * (180.0 / Math.PI));
            world.spawnEntity(new StraightIcicleEntity(world, spawnPos.x, spawnPos.y, spawnPos.z, yaw, 5, guard));
        }

        @Override
        public void stop() {
            active = false;
            guard.setThrowing(false);
            guard.throwCooldown = 120 + guard.random.nextInt(60);
        }
    }

    private class LongRangeStabGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int windupTimer = 0;
        private boolean active = false;

        LongRangeStabGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.stabCooldown > 0) return false;
            double dist = guard.squaredDistanceTo(target);
            return dist > 1.5 * 1.5 && dist <= 4.5 * 4.5;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 8;
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (--windupTimer <= 0) {
                if (target != null && guard.squaredDistanceTo(target) <= 5.0 * 5.0) {
                    guard.tryAttack(target);
                    if (guard.getWorld().isClient) guard.stabAnimationState.start(guard.age);
                }
                stop();
            }
        }

        @Override
        public void stop() {
            active = false;
            guard.stabCooldown = 30 + guard.random.nextInt(20);
        }
    }

    static class SmoothMoveControl extends MoveControl {
        SmoothMoveControl(MonarchsGuardEntity entity) {
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

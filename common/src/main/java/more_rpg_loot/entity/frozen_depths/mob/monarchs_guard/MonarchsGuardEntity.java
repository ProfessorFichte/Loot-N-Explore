package more_rpg_loot.entity.frozen_depths.mob.monarchs_guard;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.frozen_depths.projectile.ThrownLanceEntity;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.util.LongReachMeleeAttackGoal;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
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
    private int stabAnimTicksRemaining = 0;

    private int dashCooldown = 0;
    private int throwCooldown = 0;
    private int stabCooldown = 0;
    private int postActionCooldown = 0;

    // Server-side only - lets the three special-attack goals below check each other's state so a
    // higher-priority goal can't interrupt one that's already mid-windup/mid-action
    private boolean stabbingActive = false;

    public MonarchsGuardEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
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
        this.goalSelector.add(4, new LongReachMeleeAttackGoal(this, 1.0, false, 4.5));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        // Defensive: strip any duplicate melee goal that slipped in before updateAttackType() was
        // neutralized below (excludes our own LongReachMeleeAttackGoal subclass).
        this.goalSelector.getGoals().stream()
                .filter(g -> g.getGoal().getClass() == MeleeAttackGoal.class)
                .toList()
                .forEach(g -> this.goalSelector.remove(g.getGoal()));
    }

    @Override
    public void updateAttackType() {
        // AbstractSkeletonEntity re-adds its own internal meleeAttackGoal here whenever equipment
        // changes, since getMainHandStack().isOf(Items.BOW) is always false for the lance - that
        // runs after initGoals() (during equip), so a one-time goal filter can't catch it. Stop the
        // vanilla swap entirely; this entity's own LongReachMeleeAttackGoal is already in initGoals().
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
            if (postActionCooldown > 0) postActionCooldown--;
        }
        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        // getVelocity() isn't reliably synced to the client during normal AI pathfinding movement
        // (only knockback sends a velocity packet), so detect movement from the actual position delta instead
        double dx = this.getX() - this.prevX;
        double dz = this.getZ() - this.prevZ;
        boolean moving = (dx * dx + dz * dz) > 0.0001;

        if (this.isDashing()) {
            this.chargeAnimationState.startIfNotRunning(this.age);
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
            this.spearThrowAnimationState.stop();
        } else if (this.isThrowing()) {
            this.spearThrowAnimationState.startIfNotRunning(this.age);
            this.idleAnimationState.stop();
            this.chargeAnimationState.stop();
        } else if (this.handSwinging && this.handSwingTicks == 0) {
            this.stabAnimationState.start(this.age);
            this.stabAnimTicksRemaining = 10;
            this.chargeAnimationState.stop();
            this.spearThrowAnimationState.stop();
        } else if (moving) {
            this.walkAnimationState.startIfNotRunning(this.age);
            this.chargeAnimationState.stop();
            this.idleAnimationState.stop();
            this.spearThrowAnimationState.stop();
        } else {
            this.chargeAnimationState.stop();
            this.walkAnimationState.stop();
            this.spearThrowAnimationState.stop();
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --idleAnimationTimeout;
            }
        }

        if (this.stabAnimTicksRemaining > 0 && --this.stabAnimTicksRemaining <= 0) {
            this.stabAnimationState.stop();
        }
    }

    private class DashAttackGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int windupTimer = 0;
        private int chargeTicks = 0;
        private Vec3d chargeDir = Vec3d.ZERO;
        private boolean active = false;

        DashAttackGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.dashCooldown > 0 || guard.postActionCooldown > 0) return false;
            if (guard.isThrowing() || guard.stabbingActive) return false;
            double dist = guard.squaredDistanceTo(target);
            return dist > 4.0 * 4.0 && dist <= 12.0 * 12.0;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 10 + guard.random.nextInt(11);
            chargeTicks = 0;
            guard.setDashing(true);
            guard.getNavigation().stop();
            RPGLoot.LOGGER.info("[MonarchsGuard] DashAttackGoal started against {}, windup={}", target, windupTimer);
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);

            if (windupTimer > 0) {
                if (--windupTimer <= 0 && target != null) {
                    chargeDir = target.getPos().subtract(guard.getPos()).normalize();
                    chargeTicks = 8;

                    // Force-face the charge direction exactly at launch rather than trusting
                    // look-control/body-control to have fully caught up - in rare cases (target
                    // was near directly behind at windup start) body yaw can still be lagging,
                    // making the model appear to charge backward even though velocity is correct.
                    float launchYaw = (float) (Math.toDegrees(Math.atan2(-chargeDir.x, chargeDir.z)));
                    guard.setYaw(launchYaw);
                    guard.setBodyYaw(launchYaw);
                    guard.setHeadYaw(launchYaw);
                    guard.prevYaw = launchYaw;
                    guard.prevBodyYaw = launchYaw;
                    guard.prevHeadYaw = launchYaw;

                    var hits = guard.getWorld().getNonSpectatingEntities(LivingEntity.class, guard.getBoundingBox().expand(1.5).offset(chargeDir.multiply(2)));
                    int hitCount = 0;
                    for (LivingEntity entity : hits) {
                        if (entity != guard && !entity.isTeammate(guard)) {
                            guard.tryAttack(entity);
                            hitCount++;
                        }
                    }
                    RPGLoot.LOGGER.info("[MonarchsGuard] Dash launched toward {}, hit {} entities", target, hitCount);
                }
                return;
            }

            if (chargeTicks > 0) {
                guard.setVelocity(chargeDir.x * 0.9, guard.getVelocity().y, chargeDir.z * 0.9);
                guard.velocityModified = true;
                if (--chargeTicks <= 0) stop();
            } else {
                stop();
            }
        }

        @Override
        public void stop() {
            active = false;
            guard.setDashing(false);
            guard.dashCooldown = 60 + guard.random.nextInt(40);
            guard.postActionCooldown = 10;
        }
    }

    private class SpearThrowGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int windupTimer = 0;
        private int recoverTimer = 0;
        private boolean hasThrown = false;
        private boolean active = false;

        SpearThrowGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.throwCooldown > 0 || guard.postActionCooldown > 0) return false;
            if (guard.isDashing() || guard.stabbingActive) return false;
            double dist = guard.squaredDistanceTo(target);
            return dist > 6.0 * 6.0 && dist <= 32.0 * 32.0;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            // throwSpear animation (21.67 ticks total) hides the held lance model between tick 10
            // and ~19 (leftItem scale keyframes) - the throw must fire exactly when it vanishes.
            windupTimer = 10;
            hasThrown = false;
            guard.setThrowing(true);
            guard.getNavigation().stop();
            RPGLoot.LOGGER.info("[MonarchsGuard] SpearThrowGoal started against {}", target);
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (!hasThrown) {
                if (--windupTimer <= 0) {
                    throwSpear();
                    hasThrown = true;
                    recoverTimer = 12;
                }
            } else if (--recoverTimer <= 0) {
                stop();
            }
        }

        private void throwSpear() {
            if (target == null) {
                RPGLoot.LOGGER.warn("[MonarchsGuard] SpearThrowGoal fired with a null target - no lance spawned");
                return;
            }
            World world = guard.getWorld();
            if (world.isClient) return;

            Vec3d spawnPos = guard.getPos().add(0, guard.getStandingEyeHeight() * 0.8, 0);
            Vec3d toTarget = target.getPos().add(0, target.getHeight() * 0.5, 0).subtract(spawnPos);

            ThrownLanceEntity lance = new ThrownLanceEntity(world, guard);
            lance.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
            lance.setVelocity(toTarget.x, toTarget.y, toTarget.z, 2.5F, 0.3F);
            world.spawnEntity(lance);
            RPGLoot.LOGGER.info("[MonarchsGuard] Lance thrown at {}", target);
        }

        @Override
        public void stop() {
            active = false;
            guard.setThrowing(false);
            guard.throwCooldown = 120 + guard.random.nextInt(60);
            guard.postActionCooldown = 10;
        }
    }

    private class LongRangeStabGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int windupTimer = 0;
        private boolean active = false;

        LongRangeStabGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.stabCooldown > 0 || guard.postActionCooldown > 0) return false;
            if (guard.isDashing() || guard.isThrowing()) return false;
            double dist = guard.squaredDistanceTo(target);
            return dist > 1.5 * 1.5 && dist <= 4.5 * 4.5;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 8;
            guard.stabbingActive = true;
            RPGLoot.LOGGER.info("[MonarchsGuard] LongRangeStabGoal started against {}", target);
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (--windupTimer <= 0) {
                if (target != null && guard.squaredDistanceTo(target) <= 5.0 * 5.0) {
                    guard.tryAttack(target);
                    if (guard.getWorld().isClient) guard.stabAnimationState.start(guard.age);
                } else {
                    RPGLoot.LOGGER.info("[MonarchsGuard] LongRangeStabGoal whiffed - target {} out of range or gone at resolve time", target);
                }
                stop();
            }
        }

        @Override
        public void stop() {
            active = false;
            guard.stabbingActive = false;
            guard.stabCooldown = 30 + guard.random.nextInt(20);
            guard.postActionCooldown = 6;
        }
    }

}

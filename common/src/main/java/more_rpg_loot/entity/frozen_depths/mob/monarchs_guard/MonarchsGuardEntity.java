package more_rpg_loot.entity.frozen_depths.mob.monarchs_guard;

import com.mrpg_lib.util.AllyHelper;
import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
import more_rpg_loot.entity.frozen_depths.projectile.ThrownLanceEntity;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
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
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class MonarchsGuardEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> DASHING = DataTracker.registerData(MonarchsGuardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> THROWING = DataTracker.registerData(MonarchsGuardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState stabAnimationState = new AnimationState();
    public final AnimationState spearThrowAnimationState = new AnimationState();
    public final AnimationState chargeAnimationState = new AnimationState();
    private int movingGraceTicks = 0;
    private int stabAnimTicksRemaining = 0;

    private int dashCooldown = 0;
    private int throwCooldown = 0;
    private int stabCooldown = 0;
    private int postActionCooldown = 0;
    private EntityAttributeConfig.Entry tuningFallback;

    // Server-side only - lets the three special-attack goals below check each other's state so a
    // higher-priority goal can't interrupt one that's already mid-windup/mid-action
    private boolean stabbingActive = false;

    public MonarchsGuardEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.experiencePoints += 8;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2);
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
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
        this.goalSelector.add(4, new ChaseTargetGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        this.goalSelector.getGoals().stream()
                .filter(g -> g.getGoal() instanceof MeleeAttackGoal)
                .toList()
                .forEach(g -> this.goalSelector.remove(g.getGoal()));
    }

    @Override
    public void updateAttackType() {
        // Vanilla re-adds its own melee goal on equip (isOf(BOW) is false for our weapon); block it.
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        EntityAttributeApplier.apply(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, attributeFallback());
        return data;
    }

    protected EntityAttributeConfig.Entry attributeFallback() {
        EntityAttributeConfig.Entry fallback = new EntityAttributeConfig.Entry()
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 35.0)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 6.0);
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.monarchsGuard(fallback);
    }

    protected CustomValues tuning() {
        if (tuningFallback == null) tuningFallback = attributeFallback();
        return EntityAttributeApplier.custom(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, tuningFallback);
    }

    private boolean isWithin(LivingEntity target, double minDistance, String maxDistanceKey, double maxDistanceDefault) {
        double dist = this.squaredDistanceTo(target);
        double max = tuning().getDouble(maxDistanceKey, maxDistanceDefault);
        return dist > minDistance * minDistance && dist <= max * max;
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
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
        if (dx * dx + dz * dz > 0.0001) {
            this.movingGraceTicks = 6;
        } else if (this.movingGraceTicks > 0) {
            this.movingGraceTicks--;
        }
        boolean moving = this.movingGraceTicks > 0;

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
            this.idleAnimationState.startIfNotRunning(this.age);
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
        private final Set<LivingEntity> dashHits = new HashSet<>();
        private boolean active = false;

        DashAttackGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.dashCooldown > 0 || guard.postActionCooldown > 0) return false;
            if (guard.isThrowing() || guard.stabbingActive) return false;
            return guard.isWithin(target, 4.0, FrozenDepthsTuning.DASH_RANGE, 12.0);
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 10 + guard.random.nextInt(11);
            chargeTicks = 0;
            dashHits.clear();
            guard.setDashing(true);
            guard.getNavigation().stop();
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

                    strikeAlongCharge();
                }
                return;
            }

            if (chargeTicks > 0) {
                guard.setVelocity(chargeDir.x * 0.9, guard.getVelocity().y, chargeDir.z * 0.9);
                guard.velocityModified = true;
                strikeAlongCharge();
                if (--chargeTicks <= 0) stop();
            } else {
                stop();
            }
        }

        private int strikeAlongCharge() {
            Box sweep = guard.getBoundingBox().stretch(chargeDir.multiply(1.0)).expand(0.6, 0.3, 0.6);
            int hitCount = 0;
            for (LivingEntity entity : guard.getWorld().getNonSpectatingEntities(LivingEntity.class, sweep)) {
                if (entity == guard || entity instanceof net.minecraft.entity.decoration.ArmorStandEntity || !AllyHelper.canHurt(guard, entity)) continue;
                if (entity instanceof net.minecraft.entity.mob.MobEntity && entity != guard.getTarget()) continue;
                if (!dashHits.add(entity)) continue;
                guard.tryAttack(entity);
                hitCount++;
            }
            return hitCount;
        }

        @Override
        public void stop() {
            active = false;
            dashHits.clear();
            guard.setDashing(false);
            guard.dashCooldown = guard.tuning().getInt(FrozenDepthsTuning.DASH_COOLDOWN, 60) + guard.random.nextInt(40);
            guard.postActionCooldown = 10;
        }
    }

    private class SpearThrowGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;
        private int throwTimer = 0;
        private boolean active = false;

        SpearThrowGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.throwCooldown > 0 || guard.postActionCooldown > 0) return false;
            if (guard.isDashing() || guard.stabbingActive) return false;
            return guard.isWithin(target, 6.0, FrozenDepthsTuning.SPEAR_THROW_RANGE, 32.0);
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            throwTimer = 0;
            guard.setThrowing(true);
            guard.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (throwTimer == MonarchsGuardAnimationTimes.THROW_RELEASE_TICK) {
                throwSpear();
            }
            if (++throwTimer >= MonarchsGuardAnimationTimes.THROW_LENGTH_TICKS) {
                stop();
            }
        }

        private void throwSpear() {
            if (target == null) {
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
        }

        @Override
        public void stop() {
            active = false;
            guard.setThrowing(false);
            guard.throwCooldown = guard.tuning().getInt(FrozenDepthsTuning.SPEAR_THROW_COOLDOWN, 120) + guard.random.nextInt(60);
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
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            if (target == null || !target.isAlive() || guard.stabCooldown > 0 || guard.postActionCooldown > 0) return false;
            if (guard.isDashing() || guard.isThrowing()) return false;
            return guard.squaredDistanceTo(target) <= 4.5 * 4.5;
        }

        @Override
        public boolean shouldContinue() { return active; }

        @Override
        public void start() {
            active = true;
            windupTimer = 0;
            guard.stabbingActive = true;
            guard.getNavigation().stop();
            guard.swingHand(Hand.MAIN_HAND);
        }

        @Override
        public void tick() {
            if (target != null) guard.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (windupTimer == MonarchsGuardAnimationTimes.STAB_IMPACT_TICK) {
                if (target != null && target.isAlive() && guard.squaredDistanceTo(target) <= 5.0 * 5.0) {
                    guard.tryAttack(target);
                } else {
                }
            }
            if (++windupTimer >= MonarchsGuardAnimationTimes.STAB_LENGTH_TICKS) {
                stop();
            }
        }

        @Override
        public void stop() {
            active = false;
            guard.stabbingActive = false;
            guard.stabCooldown = guard.tuning().getInt(FrozenDepthsTuning.STAB_COOLDOWN, 30) + guard.random.nextInt(20);
            guard.postActionCooldown = 6;
        }
    }

    private class ChaseTargetGoal extends Goal {
        private final MonarchsGuardEntity guard;
        private LivingEntity target;

        ChaseTargetGoal(MonarchsGuardEntity guard) {
            this.guard = guard;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = guard.getTarget();
            return target != null && target.isAlive() && !guard.isDashing() && !guard.isThrowing();
        }

        @Override
        public boolean shouldContinue() {
            return canStart();
        }

        @Override
        public void tick() {
            if (guard.age % 5 == 0) {
                guard.getNavigation().startMovingTo(target, 1.0);
            }
        }

        @Override
        public void stop() {
            guard.getNavigation().stop();
        }
    }

}

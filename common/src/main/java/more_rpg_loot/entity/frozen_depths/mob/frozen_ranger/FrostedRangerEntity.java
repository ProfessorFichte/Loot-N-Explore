package more_rpg_loot.entity.frozen_depths.mob.frozen_ranger;

import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.compat.items.RWA_Effects;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
import more_rpg_loot.entity.frozen_depths.projectile.FrozenArrowEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FrostedRangerEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> PERFORMING_SALVO = DataTracker.registerData(FrostedRangerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> DRAWING_BOW = DataTracker.registerData(FrostedRangerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState normalShootAnimationState = new AnimationState();
    public final AnimationState salvoAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    int salvoCooldown = 0;
    private EntityAttributeConfig.Entry tuningFallback;

    public FrostedRangerEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.experiencePoints += 5;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 28.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
        if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
            builder = RWA_Effects.addDamageAttribute(builder);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
    }

    protected double rangedWeaponDamageMultiplier() {
        return LNEPlatform.isModLoaded("ranged_weapon_api") ? RWA_Effects.getDamageMultiplier(this) : 1.0;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PERFORMING_SALVO, false);
        builder.add(DRAWING_BOW, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FrostSalvoGoal(this));
        this.goalSelector.add(2, new NormalShotGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));

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
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 28.0)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 2.0);
        if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
            fallback.set(RWA_Effects.damageAttributeId(), 1.0);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.frostedRanger(fallback);
    }

    protected CustomValues tuning() {
        if (tuningFallback == null) tuningFallback = attributeFallback();
        return EntityAttributeApplier.custom(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, tuningFallback);
    }

    private boolean isWithinAttackRange(LivingEntity target, double extra) {
        double range = tuning().getDouble(FrozenDepthsTuning.ATTACK_RANGE, 20.0) + extra;
        return this.squaredDistanceTo(target) <= range * range;
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

    public boolean isPerformingSalvo() {
        return this.dataTracker.get(PERFORMING_SALVO);
    }

    public void setPerformingSalvo(boolean performing) {
        this.dataTracker.set(PERFORMING_SALVO, performing);
    }

    public boolean isDrawingBow() {
        return this.dataTracker.get(DRAWING_BOW);
    }

    public void setDrawingBow(boolean drawing) {
        this.dataTracker.set(DRAWING_BOW, drawing);
    }

    private void startBowPull() {
        if (!this.isUsingItem()) {
            this.setCurrentHand(Hand.MAIN_HAND);
        }
    }

    private void stopBowPull() {
        if (this.isUsingItem()) {
            this.clearActiveItem();
        }
    }

    private void faceTarget(LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        float desiredYaw = (float) (MathHelper.atan2(dz, dx) * 57.2957763671875) - 90.0F;
        float yaw = this.getYaw() + MathHelper.clamp(MathHelper.wrapDegrees(desiredYaw - this.getYaw()), -30.0F, 30.0F);
        this.setYaw(yaw);
        this.setBodyYaw(yaw);
        this.setHeadYaw(yaw);
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
        } else if (this.isDrawingBow()) {
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
        return Math.max(1, tuning().getInt(FrozenDepthsTuning.SALVO_ARROW_COUNT, 5));
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        FrozenArrowEntity arrow = new FrozenArrowEntity(this.getWorld(), this);
        double dx = target.getX() - this.getX();
        double dy = target.getBodyY(0.3333333333333333) - arrow.getY();
        double dz = target.getZ() - this.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        arrow.setVelocity(dx, dy + horizontalDistance * 0.20000000298023224, dz, 1.6F,
                (float) (14 - this.getWorld().getDifficulty().getId() * 4));
        arrow.setDamage(this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5 * rangedWeaponDamageMultiplier());
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(arrow);
    }

    private class NormalShotGoal extends Goal {
        private final FrostedRangerEntity ranger;
        private LivingEntity target;
        private int drawTimer = 0;
        private int shotCooldown = 0;
        private boolean released = false;

        NormalShotGoal(FrostedRangerEntity ranger) {
            this.ranger = ranger;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            LivingEntity t = ranger.getTarget();
            return t != null && t.isAlive() && ranger.isWithinAttackRange(t, 0.0);
        }

        @Override
        public boolean shouldContinue() {
            return target != null && target.isAlive() && ranger.isWithinAttackRange(target, 2.0);
        }

        @Override
        public void start() {
            target = ranger.getTarget();
            drawTimer = 0;
            shotCooldown = 0;
            released = false;
        }

        @Override
        public void stop() {
            target = null;
            drawTimer = 0;
            released = false;
            ranger.setDrawingBow(false);
            ranger.stopBowPull();
            ranger.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target == null) return;
            ranger.getLookControl().lookAt(target, 30.0F, 30.0F);

            double distSq = ranger.squaredDistanceTo(target);
            boolean canSee = ranger.getVisibilityCache().canSee(target);

            boolean drawing = ranger.isDrawingBow();

            if (!drawing && (distSq > 14.0 * 14.0 || !canSee)) {
                ranger.getNavigation().startMovingTo(target, 1.0);
            } else {
                ranger.getNavigation().stop();
            }

            if (shotCooldown > 0) {
                shotCooldown--;
                return;
            }

            if (!drawing) {
                if (!canSee) return;
                ranger.setDrawingBow(true);
                drawTimer = 0;
                released = false;
            } else if (!released && !canSee) {
                drawTimer = 0;
                ranger.setDrawingBow(false);
                ranger.stopBowPull();
                return;
            }

            ranger.faceTarget(target);
            if (drawTimer == FrostedRangerAnimationTimes.ATTACK_PULL_START_TICK) {
                ranger.startBowPull();
            }
            if (!released && drawTimer >= FrostedRangerAnimationTimes.ATTACK_RELEASE_TICK) {
                ranger.shootAt(target, 1.0F);
                ranger.stopBowPull();
                released = true;
            }
            drawTimer++;
            if (drawTimer >= FrostedRangerAnimationTimes.ATTACK_LENGTH_TICKS) {
                drawTimer = 0;
                released = false;
                shotCooldown = ranger.tuning().getInt(FrozenDepthsTuning.SHOT_COOLDOWN, 20);
                ranger.setDrawingBow(false);
            }
        }
    }

    private class FrostSalvoGoal extends Goal {
        private final FrostedRangerEntity ranger;
        private LivingEntity target;
        private int phase = 0;
        private int windupTimer = 0;

        FrostSalvoGoal(FrostedRangerEntity ranger) {
            this.ranger = ranger;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = ranger.getTarget();
            return target != null && target.isAlive()
                    && ranger.salvoCooldown <= 0
                    && ranger.isWithinAttackRange(target, 0.0);
        }

        @Override
        public boolean shouldContinue() {
            return phase != 0 && (target == null || target.isAlive());
        }

        @Override
        public void start() {
            phase = 1;
            windupTimer = 0;
            ranger.setPerformingSalvo(true);
            ranger.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) {
                ranger.getLookControl().lookAt(target, 30.0F, 30.0F);
                ranger.faceTarget(target);
            }

            if (windupTimer == FrostedRangerAnimationTimes.SALVO_PULL_START_TICK) {
                ranger.startBowPull();
            }
            if (windupTimer == FrostedRangerAnimationTimes.SALVO_RELEASE_TICK) {
                ranger.stopBowPull();
                if (target != null) {
                    int arrowCount = ranger.getSalvoArrowCount();
                    for (int i = 0; i < arrowCount; i++) {
                        fireArrow(i, arrowCount);
                    }
                }
                jumpBack();
            }
            if (++windupTimer >= FrostedRangerAnimationTimes.SALVO_LENGTH_TICKS) {
                stop();
            }
        }

        private void jumpBack() {
            if (target == null) {
                return;
            }
            Vec3d awayDir = new Vec3d(ranger.getX() - target.getX(), 0.0, ranger.getZ() - target.getZ()).normalize();
            if (!isSafeLandingSpot(awayDir)) {
                return;
            }
            ranger.addVelocity(awayDir.x * 1.1, 0.4, awayDir.z * 1.1);
            ranger.velocityModified = true;
        }

        private boolean isSafeLandingSpot(Vec3d awayDir) {
            World world = ranger.getWorld();
            for (double dist = 1.0; dist <= 6.0; dist += 1.0) {
                BlockPos column = BlockPos.ofFloored(ranger.getX() + awayDir.x * dist, ranger.getY(), ranger.getZ() + awayDir.z * dist);
                if (!hasSafeFloor(world, column)) {
                    return false;
                }
            }
            return true;
        }

        private boolean hasSafeFloor(World world, BlockPos column) {
            if (world.getFluidState(column).isIn(FluidTags.LAVA)) {
                return false;
            }
            for (int dy = 0; dy >= -3; dy--) {
                BlockPos check = column.add(0, dy, 0);
                BlockState state = world.getBlockState(check);
                if (state.getFluidState().isIn(FluidTags.LAVA) || state.isIn(BlockTags.FIRE)
                        || state.isOf(Blocks.MAGMA_BLOCK) || state.isOf(Blocks.CACTUS) || state.isOf(Blocks.SWEET_BERRY_BUSH)) {
                    return false;
                }
                if (!state.getCollisionShape(world, check).isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        private void fireArrow(int index, int arrowCount) {
            if (target == null) return;
            World world = ranger.getWorld();

            double spread = arrowCount > 1 ? Math.PI / (arrowCount - 1) : 0.0;
            double baseAngle = Math.atan2(target.getZ() - ranger.getZ(), target.getX() - ranger.getX());
            double angle = baseAngle + (index - arrowCount / 2.0) * spread * 0.4;

            FrozenArrowEntity arrow = new FrozenArrowEntity(world, ranger);
            double dx = Math.cos(angle);
            double dy = (target.getBodyY(0.5) - arrow.getY()) / ranger.distanceTo(target) + ranger.random.nextGaussian() * 0.05;
            double dz = Math.sin(angle);

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
            arrow.setVelocity(dx / length, dy / length, dz / length, 1.6F, 5.0F);
            arrow.setOwner(ranger);
            arrow.setDamage(ranger.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5 * ranger.rangedWeaponDamageMultiplier());
            world.spawnEntity(arrow);
        }

        @Override
        public void stop() {
            phase = 0;
            ranger.stopBowPull();
            ranger.setPerformingSalvo(false);
            ranger.salvoCooldown = ranger.tuning().getInt(FrozenDepthsTuning.SALVO_COOLDOWN, 100) + ranger.random.nextInt(60);
        }
    }
}

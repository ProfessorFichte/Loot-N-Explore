package more_rpg_loot.entity.frozen_depths.mob.frozen_mage;

import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import net.minecraft.util.Identifier;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
import more_rpg_loot.entity.frozen_depths.projectile.StraightIcicleEntity;
import more_rpg_loot.entity.frozen_depths.projectile.IcicleFormation;
import more_rpg_loot.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class UndeadFrozenMageEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> CASTING = DataTracker.registerData(UndeadFrozenMageEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState castAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    int closeCooldown = 0;
    int rangeCooldown = 0;
    int postActionCooldown = 0;
    int singleCooldown = 0;
    private EntityAttributeConfig.Entry tuningFallback;
    private boolean retreatedThisTick = false;

    private static final double APPROACH_DISTANCE = 18.0;
    private static final double APPROACH_STOP_DISTANCE = 15.0;
    private static final double RETREAT_DISTANCE = 8.0;
    private static final double RETREAT_STOP_DISTANCE = 11.0;
    private static final double FLEE_SPEED = 1.35;
    private static final double FLEE_BOOST = 4.4;
    private static final Identifier FLEE_BOOST_ID = Identifier.of("loot_n_explore", "mage_retreat_boost");
    private static final double FLEE_STEP = 10.0;
    private static final int MODE_HOLD = 0;
    private static final int MODE_RETREAT = 1;
    private static final int MODE_APPROACH = 2;

    public UndeadFrozenMageEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.experiencePoints += 5;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
        if (LNEPlatform.isModLoaded("spell_power")) {
            builder = builder.add(SpellSchools.FROST.attributeEntry, 3.0);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(CASTING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new CloseRangeIcicleGoal(this));
        this.goalSelector.add(2, new LongRangeIcicleGoal(this));
        this.goalSelector.add(3, new SingleIcicleGoal(this));
        this.goalSelector.add(4, new KeepDistanceGoal(this));
        this.goalSelector.add(5, new WanderAroundGoal(this, 0.8, 40));
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
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 30.0)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 3.0);
        if (LNEPlatform.isModLoaded("spell_power")) {
            fallback.set(SpellSchools.FROST.attributeEntry.getIdAsString(), 3.0);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.undeadFrozenMage(fallback);
    }

    protected CustomValues tuning() {
        if (tuningFallback == null) tuningFallback = attributeFallback();
        return EntityAttributeApplier.custom(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, tuningFallback);
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

    public boolean isCasting() {
        return this.dataTracker.get(CASTING);
    }

    public void setCasting(boolean casting) {
        this.dataTracker.set(CASTING, casting);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            if (!retreatedThisTick) clearFleeBoost();
            retreatedThisTick = false;
            if (closeCooldown > 0) closeCooldown--;
            if (rangeCooldown > 0) rangeCooldown--;
            if (singleCooldown > 0) singleCooldown--;
            if (postActionCooldown > 0) postActionCooldown--;
        }

        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.isCasting()) {
            this.castAnimationState.startIfNotRunning(this.age);
            this.idleAnimationState.stop();
        } else {
            this.castAnimationState.stop();
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --idleAnimationTimeout;
            }
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

    private void retreatFrom(LivingEntity threat) {
        if (this.getVisibilityCache().canSee(threat)) {
            this.faceTarget(threat);
            if (!this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).hasModifier(FLEE_BOOST_ID)) {
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(
                        new EntityAttributeModifier(FLEE_BOOST_ID, FLEE_BOOST, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            this.getNavigation().stop();
            this.getMoveControl().strafeTo(-1.0F, 0.0F);
            this.retreatedThisTick = true;
        } else if (this.getNavigation().isIdle() || this.age % 15 == 0) {
            this.fleeFrom(threat, FLEE_SPEED);
        }
    }

    private void clearFleeBoost() {
        var speedAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedAttribute != null && speedAttribute.hasModifier(FLEE_BOOST_ID)) {
            speedAttribute.removeModifier(FLEE_BOOST_ID);
        }
    }

    private boolean fleeFrom(LivingEntity threat, double speed) {
        Vec3d away = this.getPos().subtract(threat.getPos()).multiply(1.0, 0.0, 1.0);
        if (away.lengthSquared() < 1.0E-4) {
            away = new Vec3d(this.random.nextDouble() - 0.5, 0.0, this.random.nextDouble() - 0.5);
        }
        Vec3d direction = away.normalize();
        for (float turn : new float[]{0.0F, 0.6F, -0.6F, 1.2F, -1.2F}) {
            Vec3d point = this.getPos().add(direction.rotateY(turn).multiply(FLEE_STEP));
            Path path = this.getNavigation().findPathTo(point.x, point.y, point.z, 0);
            if (path != null && path.reachesTarget()) {
                return this.getNavigation().startMovingAlong(path, speed);
            }
        }
        Vec3d fallback = NoPenaltyTargeting.findFrom(this, 8, 4, threat.getPos());
        return fallback != null && this.getNavigation().startMovingTo(fallback.x, fallback.y, fallback.z, speed);
    }

    protected int getGroundIcicleCount() {
        return Math.max(1, tuning().getInt(FrozenDepthsTuning.GROUND_ICICLE_COUNT, 5));
    }

    protected int getAirIcicleCount() {
        return Math.max(1, tuning().getInt(FrozenDepthsTuning.AIR_ICICLE_COUNT, 5));
    }

    private class CloseRangeIcicleGoal extends Goal {
        private final UndeadFrozenMageEntity mage;
        private LivingEntity target;
        private int castTicks = 0;
        private boolean casting = false;

        CloseRangeIcicleGoal(UndeadFrozenMageEntity mage) {
            this.mage = mage;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = mage.getTarget();
            return target != null && target.isAlive()
                    && mage.squaredDistanceTo(target) <= 8.0 * 8.0
                    && mage.closeCooldown <= 0
                    && mage.postActionCooldown <= 0;
        }

        @Override
        public boolean shouldContinue() {
            return casting && (target == null || target.isAlive());
        }

        @Override
        public boolean canStop() {
            return !casting;
        }

        @Override
        public void start() {
            casting = true;
            castTicks = 0;
            mage.setCasting(true);
            mage.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) {
                mage.getLookControl().lookAt(target, 30.0F, 30.0F);
                mage.faceTarget(target);
            }
            castTicks++;
            if (castTicks == UndeadFrozenMageAnimationTimes.CAST_RELEASE_TICK) {
                spawnIcicles();
            }
            if (castTicks >= UndeadFrozenMageAnimationTimes.CAST_RELEASE_TICK + UndeadFrozenMageAnimationTimes.CAST_RECOVERY_TICKS) {
                stop();
            }
        }

        private void spawnIcicles() {
            if (target == null) return;
            World world = mage.getWorld();
            double targetAngle = Math.atan2(target.getZ() - mage.getZ(), target.getX() - mage.getX());
            int count = mage.getGroundIcicleCount();
            double minY = mage.getY() - 3.0;
            double maxY = mage.getY() + 2.0;

            for (int i = 0; i < count; i++) {
                double angleOffset = (i - count / 2.0) * (Math.PI / (count * 1.5));
                double angle = targetAngle + angleOffset;
                double dist = 1.5 + i * 1.0;
                double x = mage.getX() + Math.cos(angle) * dist;
                double z = mage.getZ() + Math.sin(angle) * dist;
                float yawRad = (float) MathHelper.atan2(-(float)(x - mage.getX()), (float)(z - mage.getZ()));
                spawnIcicleOnGround(world, x, z, minY, maxY, yawRad, i * 4);
            }
        }

        private void spawnIcicleOnGround(World world, double x, double z, double minY, double maxY, float yaw, int warmup) {
            BlockPos blockPos = BlockPos.ofFloored(x, maxY, z);
            boolean foundGround = false;
            double yOffset = 0.0;

            do {
                BlockPos belowPos = blockPos.down();
                BlockState blockState = world.getBlockState(belowPos);

                if (blockState.isSideSolidFullSquare(world, belowPos, Direction.UP)) {
                    if (!world.isAir(blockPos)) {
                        BlockState aboveState = world.getBlockState(blockPos);
                        var voxelShape = aboveState.getCollisionShape(world, blockPos);
                        if (!voxelShape.isEmpty()) {
                            yOffset = voxelShape.getMax(Direction.Axis.Y);
                        }
                    }
                    foundGround = true;
                    break;
                }

                blockPos = blockPos.down();
            } while (blockPos.getY() >= MathHelper.floor(minY) - 1);

            if (foundGround) {
                world.spawnEntity(new StraightIcicleEntity(world, x, (double) blockPos.getY() + yOffset, z, yaw, warmup, mage));
            } else {
            }
        }

        @Override
        public void stop() {
            casting = false;
            mage.setCasting(false);
            mage.closeCooldown = mage.tuning().getInt(FrozenDepthsTuning.CLOSE_ICICLE_COOLDOWN, 60) + mage.random.nextInt(40);
            mage.postActionCooldown = 10;
        }
    }

    private class LongRangeIcicleGoal extends Goal {
        private static final int ICICLE_HOVER_TICKS = 12;
        private static final int ICICLE_LAUNCH_INTERVAL_MIN = 4;
        private static final int ICICLE_LAUNCH_INTERVAL_MAX = 6;

        private final UndeadFrozenMageEntity mage;
        private LivingEntity target;
        private int phase = 0;
        private int castTicks = 0;
        private int fireTimer = 0;
        private int recoveryTicks = 0;
        private int launched = 0;
        private final List<TrackingIcicleEntity> icicles = new ArrayList<>();

        LongRangeIcicleGoal(UndeadFrozenMageEntity mage) {
            this.mage = mage;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = mage.getTarget();
            if (target == null || !target.isAlive() || mage.rangeCooldown > 0 || mage.postActionCooldown > 0) return false;
            double distSq = mage.squaredDistanceTo(target);
            return distSq > 5.0 * 5.0 && distSq <= 22.0 * 22.0;
        }

        @Override
        public boolean shouldContinue() {
            return phase != 0 && (target == null || target.isAlive());
        }

        @Override
        public boolean canStop() {
            return phase == 0;
        }

        @Override
        public void start() {
            phase = 1;
            castTicks = 0;
            launched = 0;
            recoveryTicks = 0;
            icicles.clear();
            mage.setCasting(true);
            mage.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) mage.getLookControl().lookAt(target, 30.0F, 30.0F);

            castTicks++;

            if (phase == 1) {
                if (castTicks >= UndeadFrozenMageAnimationTimes.CAST_RELEASE_TICK) {
                    phase = 2;
                    fireTimer = ICICLE_HOVER_TICKS;
                    spawnFormation();
                }
            } else if (phase == 2) {
                if (--fireTimer <= 0 && launched < icicles.size()) {
                    icicles.get(launched++).launch(target);
                    fireTimer = mage.random.nextBetween(ICICLE_LAUNCH_INTERVAL_MIN, ICICLE_LAUNCH_INTERVAL_MAX);
                }
                if (launched >= icicles.size() && ++recoveryTicks >= UndeadFrozenMageAnimationTimes.CAST_RECOVERY_TICKS) stop();
            }

            if (target != null) {
                if (castTicks >= UndeadFrozenMageAnimationTimes.CAST_LENGTH_TICKS
                        && mage.squaredDistanceTo(target) < RETREAT_DISTANCE * RETREAT_DISTANCE) {
                    mage.retreatFrom(target);
                } else {
                    mage.faceTarget(target);
                }
            }
        }

        private void spawnFormation() {
            if (target == null) return;
            World world = mage.getWorld();
            for (float[] slot : IcicleFormation.slots(mage.getAirIcicleCount())) {
                TrackingIcicleEntity icicle = new TrackingIcicleEntity(world, mage, target, slot[0], slot[1]);
                world.spawnEntity(icicle);
                icicles.add(icicle);
            }
        }

        @Override
        public void stop() {
            for (int i = launched; i < icicles.size(); i++) {
                icicles.get(i).discard();
            }
            icicles.clear();
            phase = 0;
            mage.setCasting(false);
            mage.rangeCooldown = mage.tuning().getInt(FrozenDepthsTuning.ICICLE_FORMATION_COOLDOWN, 80) + mage.random.nextInt(40);
            mage.postActionCooldown = 10;
        }
    }

    private class SingleIcicleGoal extends Goal {
        private static final double MAX_DISTANCE = 22.0;

        private final UndeadFrozenMageEntity mage;
        private LivingEntity target;
        private int castTicks = 0;
        private boolean casting = false;

        SingleIcicleGoal(UndeadFrozenMageEntity mage) {
            this.mage = mage;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = mage.getTarget();
            return target != null && target.isAlive()
                    && mage.singleCooldown <= 0
                    && mage.postActionCooldown <= 0
                    && mage.squaredDistanceTo(target) <= MAX_DISTANCE * MAX_DISTANCE
                    && mage.getVisibilityCache().canSee(target);
        }

        @Override
        public boolean shouldContinue() {
            return casting && target != null && target.isAlive();
        }

        @Override
        public boolean canStop() {
            return !casting;
        }

        @Override
        public void start() {
            casting = true;
            castTicks = 0;
            mage.setCasting(true);
            mage.getNavigation().stop();
        }

        @Override
        public void tick() {
            mage.getLookControl().lookAt(target, 30.0F, 30.0F);
            mage.faceTarget(target);
            castTicks++;
            if (castTicks == UndeadFrozenMageAnimationTimes.CAST_RELEASE_TICK) {
                shoot();
            }
            if (castTicks >= UndeadFrozenMageAnimationTimes.CAST_RELEASE_TICK + UndeadFrozenMageAnimationTimes.CAST_RECOVERY_TICKS) {
                stop();
            }
        }

        private void shoot() {
            float scale = mage.getHeight() / 1.99F;
            float yawRad = mage.getBodyYaw() * ((float) Math.PI / 180.0F);
            Vec3d forward = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
            Vec3d right = new Vec3d(-MathHelper.cos(yawRad), 0.0, -MathHelper.sin(yawRad));
            Vec3d origin = mage.getPos()
                    .add(forward.multiply(0.8 * scale))
                    .add(right.multiply(0.4 * scale))
                    .add(0.0, 1.6 * scale, 0.0);
            mage.getWorld().spawnEntity(new TrackingIcicleEntity(mage.getWorld(), mage, target, origin.x, origin.y, origin.z));
        }

        @Override
        public void stop() {
            casting = false;
            mage.setCasting(false);
            mage.singleCooldown = mage.tuning().getInt(FrozenDepthsTuning.SINGLE_ICICLE_COOLDOWN, 25) + mage.random.nextInt(20);
            mage.postActionCooldown = 4;
        }
    }

    private class KeepDistanceGoal extends Goal {
        private final UndeadFrozenMageEntity mage;
        private LivingEntity target;
        private int strollTimer = 0;
        private int mode = MODE_HOLD;

        KeepDistanceGoal(UndeadFrozenMageEntity mage) {
            this.mage = mage;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            this.target = mage.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean shouldContinue() {
            return target != null && target.isAlive() && mage.getTarget() == target;
        }

        @Override
        public void start() {
            strollTimer = 20 + mage.random.nextInt(40);
            mode = MODE_HOLD;
        }

        @Override
        public void tick() {
            double distSq = mage.squaredDistanceTo(target);
            int previousMode = mode;
            if (distSq < RETREAT_DISTANCE * RETREAT_DISTANCE) {
                mode = MODE_RETREAT;
            } else if (mode == MODE_RETREAT && distSq < RETREAT_STOP_DISTANCE * RETREAT_STOP_DISTANCE) {
                mode = MODE_RETREAT;
            } else if (distSq > APPROACH_DISTANCE * APPROACH_DISTANCE) {
                mode = MODE_APPROACH;
            } else if (mode == MODE_APPROACH && distSq > APPROACH_STOP_DISTANCE * APPROACH_STOP_DISTANCE) {
                mode = MODE_APPROACH;
            } else {
                mode = MODE_HOLD;
            }

            if (mode == MODE_RETREAT) {
                mage.retreatFrom(target);
            } else if (mode == MODE_APPROACH) {
                if (mage.getNavigation().isIdle() || mage.age % 10 == 0) {
                    mage.getNavigation().startMovingTo(target, 1.0);
                }
            } else if (previousMode != MODE_HOLD) {
                mage.getNavigation().stop();
            } else if (mage.getNavigation().isIdle() && --strollTimer <= 0) {
                strollTimer = 40 + mage.random.nextInt(60);
                Vec3d spot = NoPenaltyTargeting.find(mage, 6, 3);
                if (spot != null) {
                    double spotDistSq = target.squaredDistanceTo(spot);
                    if (spotDistSq > RETREAT_STOP_DISTANCE * RETREAT_STOP_DISTANCE && spotDistSq < APPROACH_DISTANCE * APPROACH_DISTANCE) {
                        mage.getNavigation().startMovingTo(spot.x, spot.y, spot.z, 0.9);
                    }
                }
            }
        }

        @Override
        public void stop() {
            mage.getNavigation().stop();
        }
    }

}

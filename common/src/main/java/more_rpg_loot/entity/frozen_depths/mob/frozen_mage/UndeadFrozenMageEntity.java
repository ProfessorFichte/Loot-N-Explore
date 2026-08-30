package more_rpg_loot.entity.frozen_depths.mob.frozen_mage;

import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.frozen_depths.projectile.StraightIcicleEntity;
import more_rpg_loot.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.block.BlockState;
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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class UndeadFrozenMageEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> CASTING = DataTracker.registerData(UndeadFrozenMageEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState castAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    int closeCooldown = 0;
    int rangeCooldown = 0;
    int postActionCooldown = 0;

    public UndeadFrozenMageEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.experiencePoints += 5;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
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
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(5, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        // Defensive: strip any melee goal that slipped in before updateAttackType() was neutralized.
        this.goalSelector.getGoals().stream()
                .filter(g -> g.getGoal() instanceof MeleeAttackGoal)
                .toList()
                .forEach(g -> this.goalSelector.remove(g.getGoal()));
    }

    @Override
    public void updateAttackType() {
        // AbstractSkeletonEntity would otherwise re-add its own internal meleeAttackGoal here
        // whenever equipment changes, since getMainHandStack().isOf(Items.BOW) is always false
        // for the held snowball; this entity's own icicle goals are already in initGoals().
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        if (LNEPlatform.isModLoaded("thermoo")) {
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(3.0);
        return data;
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.SNOWBALL));
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
            if (closeCooldown > 0) closeCooldown--;
            if (rangeCooldown > 0) rangeCooldown--;
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

    protected int getGroundIcicleCount() {
        return 5;
    }

    protected int getAirIcicleCount() {
        return 5;
    }

    private class CloseRangeIcicleGoal extends Goal {
        private final UndeadFrozenMageEntity mage;
        private LivingEntity target;
        private int castTimer = 0;
        private boolean casting = false;

        CloseRangeIcicleGoal(UndeadFrozenMageEntity mage) {
            this.mage = mage;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
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
            castTimer = 20;
            mage.setCasting(true);
            mage.getNavigation().stop();
            RPGLoot.LOGGER.info("[UndeadFrozenMage] CloseRangeIcicleGoal started, target={}", target);
        }

        @Override
        public void tick() {
            if (target != null) mage.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (--castTimer <= 0) {
                spawnIcicles();
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
                RPGLoot.LOGGER.warn("[UndeadFrozenMage] CloseRangeIcicleGoal found no ground for icicle at x={}, z={} (minY={}, maxY={}) - icicle skipped", x, z, minY, maxY);
            }
        }

        @Override
        public void stop() {
            casting = false;
            mage.setCasting(false);
            mage.closeCooldown = 60 + mage.random.nextInt(40);
            mage.postActionCooldown = 10;
        }
    }

    private class LongRangeIcicleGoal extends Goal {
        private final UndeadFrozenMageEntity mage;
        private LivingEntity target;
        private int phase = 0;
        private int castTimer = 0;
        private int fireTimer = 0;
        private int iciclesFired = 0;

        LongRangeIcicleGoal(UndeadFrozenMageEntity mage) {
            this.mage = mage;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
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
            castTimer = 20;
            iciclesFired = 0;
            mage.setCasting(true);
            mage.getNavigation().stop();
            RPGLoot.LOGGER.info("[UndeadFrozenMage] LongRangeIcicleGoal started, target={}", target);
        }

        @Override
        public void tick() {
            if (target != null) mage.getLookControl().lookAt(target, 30.0F, 30.0F);

            if (phase == 1) {
                if (--castTimer <= 0) {
                    phase = 2;
                    fireTimer = 0;
                }
            } else if (phase == 2) {
                if (--fireTimer <= 0 && iciclesFired < mage.getAirIcicleCount()) {
                    if (target != null && target.isAlive()) spawnTrackingIcicle();
                    iciclesFired++;
                    fireTimer = mage.random.nextBetween(3, 5);
                }
                if (iciclesFired >= mage.getAirIcicleCount()) stop();
            }
        }

        private void spawnTrackingIcicle() {
            World world = mage.getWorld();
            double spawnX = mage.getX() + (mage.random.nextDouble() - 0.5) * 2.0;
            double spawnY = mage.getY() + 3.0 + mage.random.nextDouble() * 2.0;
            double spawnZ = mage.getZ() + (mage.random.nextDouble() - 0.5) * 2.0;
            world.spawnEntity(new TrackingIcicleEntity(world, mage, target, spawnX, spawnY, spawnZ));
        }

        @Override
        public void stop() {
            phase = 0;
            mage.setCasting(false);
            mage.rangeCooldown = 80 + mage.random.nextInt(40);
            mage.postActionCooldown = 10;
        }
    }

}

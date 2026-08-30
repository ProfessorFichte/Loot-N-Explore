package more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier;

import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class MonarchsSoldierEntity extends SkeletonEntity {
    private static final TrackedData<Boolean> BLOCKING = DataTracker.registerData(MonarchsSoldierEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> PERFORMING_AOE = DataTracker.registerData(MonarchsSoldierEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState blockWalkAnimationState = new AnimationState();
    public final AnimationState normalHitAnimationState = new AnimationState();
    public final AnimationState aoeSwingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int normalHitAnimTicksRemaining = 0;

    private int aoeCooldown = 0;
    private int blockTimer = 0;
    private int blockCooldown = 0;
    private int postActionCooldown = 0;

    public MonarchsSoldierEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.experiencePoints += 8;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BLOCKING, false);
        builder.add(PERFORMING_AOE, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AoeSwordSwingGoal(this));
        this.goalSelector.add(2, new ShieldBlockGoal(this));
        MeleeAttackGoal ownMeleeGoal = new MeleeAttackGoal(this, 1.0, false);
        this.goalSelector.add(3, ownMeleeGoal);
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FocusAttackerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        // Defensive: strip any duplicate melee goal that slipped in before updateAttackType() was
        // neutralized below (excludes our own tracked instance).
        this.goalSelector.getGoals().stream()
                .filter(g -> g.getGoal().getClass() == MeleeAttackGoal.class && g.getGoal() != ownMeleeGoal)
                .toList()
                .forEach(g -> this.goalSelector.remove(g.getGoal()));
    }

    @Override
    public void updateAttackType() {
        // AbstractSkeletonEntity re-adds its own internal meleeAttackGoal here whenever equipment
        // changes, since getMainHandStack().isOf(Items.BOW) is always false for the sword/shield -
        // that runs after initGoals() (during equip), so a one-time goal filter can't catch it.
        // Stop the vanilla swap entirely; this entity's own MeleeAttackGoal is already in initGoals().
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        if (LNEPlatform.isModLoaded("thermoo")) {
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(5.0);
        return data;
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        this.updateDropChances(EquipmentSlot.MAINHAND);
        this.updateDropChances(EquipmentSlot.OFFHAND);
    }

    @Override
    public void updateDropChances(EquipmentSlot slot) {
        if (slot.getType() == EquipmentSlot.Type.HAND) {
            this.handDropChances[slot.getEntitySlotId()] = 0.0F;
        } else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            this.armorDropChances[slot.getEntitySlotId()] = 0.0F;
        }
    }

    public boolean isBlocking() {
        return this.dataTracker.get(BLOCKING);
    }

    public void setBlocking(boolean blocking) {
        this.dataTracker.set(BLOCKING, blocking);
    }

    public boolean isPerformingAoe() {
        return this.dataTracker.get(PERFORMING_AOE);
    }

    public void setPerformingAoe(boolean performing) {
        this.dataTracker.set(PERFORMING_AOE, performing);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (isBlocking() && !source.isIn(DamageTypeTags.BYPASSES_SHIELD)) {
            Entity origin = source.getSource() != null ? source.getSource() : source.getAttacker();
            if (origin != null) {
                Vec3d attackDir = origin.getPos().subtract(this.getPos()).normalize();
                Vec3d facing = Vec3d.fromPolar(0, this.getYaw());
                boolean fromBehind = attackDir.dotProduct(facing) < -0.3;
                boolean isProjectile = source.isIn(DamageTypeTags.IS_PROJECTILE);
                if (!(fromBehind && isProjectile)) {
                    RPGLoot.LOGGER.info("[MonarchsSoldier] Blocked {} damage from {} (fromBehind={}, isProjectile={})",
                            amount, origin, fromBehind, isProjectile);
                    return false;
                }
                RPGLoot.LOGGER.info("[MonarchsSoldier] Block bypassed by projectile from behind: {} damage from {}", amount, origin);
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            if (aoeCooldown > 0) aoeCooldown--;
            if (blockCooldown > 0) blockCooldown--;
            if (postActionCooldown > 0) postActionCooldown--;
            if (blockTimer > 0) {
                blockTimer--;
                if (blockTimer <= 0) setBlocking(false);
            }
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

        if (this.isPerformingAoe()) {
            this.aoeSwingAnimationState.startIfNotRunning(this.age);
            this.normalHitAnimationState.stop();
            this.blockWalkAnimationState.stop();
            this.idleAnimationState.stop();
        } else if (this.handSwinging && this.handSwingTicks == 0) {
            this.normalHitAnimationState.start(this.age);
            this.normalHitAnimTicksRemaining = 15;
            this.aoeSwingAnimationState.stop();
        } else if (this.isBlocking()) {
            // No dedicated stationary block pose exists - keep the shield-raised walk animation
            // showing while blocking even if standing still, rather than falling through to idle.
            this.blockWalkAnimationState.startIfNotRunning(this.age);
            this.walkAnimationState.stop();
            this.idleAnimationState.stop();
        } else if (moving) {
            this.walkAnimationState.startIfNotRunning(this.age);
            this.blockWalkAnimationState.stop();
            this.idleAnimationState.stop();
        } else {
            this.walkAnimationState.stop();
            this.blockWalkAnimationState.stop();
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --idleAnimationTimeout;
            }
        }

        if (this.normalHitAnimTicksRemaining > 0 && --this.normalHitAnimTicksRemaining <= 0) {
            this.normalHitAnimationState.stop();
        }
    }

    protected int getAoeSwingRange() {
        return 3;
    }

    private class AoeSwordSwingGoal extends Goal {
        private final MonarchsSoldierEntity soldier;
        private LivingEntity target;
        private int windupTimer = 0;
        private int recoverTimer = 0;
        private boolean hasSwung = false;
        private boolean active = false;

        AoeSwordSwingGoal(MonarchsSoldierEntity soldier) {
            this.soldier = soldier;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = soldier.getTarget();
            return target != null && target.isAlive()
                    && soldier.squaredDistanceTo(target) <= 4.0 * 4.0
                    && soldier.aoeCooldown <= 0
                    && soldier.postActionCooldown <= 0
                    && !soldier.isBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return active;
        }

        @Override
        public void start() {
            active = true;
            // attack_aoe (18.33 ticks total) swings the weapon arm through its widest sweep
            // between roughly tick 3 and tick 11 - land the hit mid-sweep, not near the end.
            windupTimer = 7;
            hasSwung = false;
            soldier.setPerformingAoe(true);
            soldier.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target != null) soldier.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (!hasSwung) {
                if (--windupTimer <= 0) {
                    performAoeSwing();
                    hasSwung = true;
                    recoverTimer = 11;
                }
            } else if (--recoverTimer <= 0) {
                stop();
            }
        }

        private void performAoeSwing() {
            float radius = soldier.getAoeSwingRange();
            Vec3d forward = Vec3d.fromPolar(0, soldier.getYaw());
            Box aoeBox = Box.of(soldier.getPos().add(forward.multiply(radius * 0.5)), radius * 2.5, 3.0, radius * 2.5);

            List<LivingEntity> targets = soldier.getWorld().getNonSpectatingEntities(LivingEntity.class, aoeBox);
            int hitCount = 0;
            for (LivingEntity entity : targets) {
                if (entity == soldier) continue;
                if (entity.isTeammate(soldier)) continue;

                Vec3d toEntity = entity.getPos().subtract(soldier.getPos());
                // atan2 gives a raw math angle; Minecraft yaw is offset by -90 from that convention
                double angle = Math.abs(MathHelper.wrapDegrees((float) (Math.atan2(toEntity.z, toEntity.x) * (180.0 / Math.PI)) - 90.0F - soldier.getYaw()));
                if (angle <= 60.0) {
                    soldier.tryAttack(entity);
                    hitCount++;
                }
            }
            if (hitCount == 0) {
                RPGLoot.LOGGER.info("[MonarchsSoldier] AoE swing hit nobody ({} candidates in box)", targets.size());
            } else {
                RPGLoot.LOGGER.info("[MonarchsSoldier] AoE swing hit {} entities", hitCount);
            }
        }

        @Override
        public void stop() {
            active = false;
            soldier.setPerformingAoe(false);
            soldier.aoeCooldown = 80 + soldier.random.nextInt(40);
            soldier.postActionCooldown = 10;
        }
    }

    private class ShieldBlockGoal extends Goal {
        private final MonarchsSoldierEntity soldier;
        private LivingEntity target;

        ShieldBlockGoal(MonarchsSoldierEntity soldier) {
            this.soldier = soldier;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            this.target = soldier.getTarget();
            if (target == null || !target.isAlive()) return false;
            double dist = soldier.squaredDistanceTo(target);
            return dist <= 7.0 * 7.0 && dist > 1.0 * 1.0
                    && !soldier.isBlocking() && !soldier.isPerformingAoe()
                    && soldier.blockCooldown <= 0
                    && soldier.random.nextFloat() < 0.08F;
        }

        @Override
        public boolean shouldContinue() {
            return soldier.isBlocking();
        }

        @Override
        public void start() {
            soldier.setBlocking(true);
            soldier.blockTimer = 40 + soldier.random.nextInt(20);
            RPGLoot.LOGGER.info("[MonarchsSoldier] ShieldBlockGoal started against {} for {} ticks", target, soldier.blockTimer);
        }

        @Override
        public void tick() {
            if (target == null || !target.isAlive()) {
                soldier.getNavigation().stop();
                return;
            }
            soldier.getLookControl().lookAt(target, 30.0F, 30.0F);
            if (soldier.squaredDistanceTo(target) > 3.0 * 3.0) {
                if (soldier.age % 10 == 0) {
                    soldier.getNavigation().startMovingTo(target, 0.8);
                }
            } else {
                soldier.getNavigation().stop();
            }
        }

        @Override
        public void stop() {
            soldier.setBlocking(false);
            soldier.blockTimer = 0;
            soldier.blockCooldown = 60 + soldier.random.nextInt(40);
            soldier.postActionCooldown = 10;
        }
    }

    private class FocusAttackerGoal extends Goal {
        private final MonarchsSoldierEntity soldier;

        FocusAttackerGoal(MonarchsSoldierEntity soldier) {
            this.soldier = soldier;
            this.setControls(EnumSet.noneOf(Control.class));
        }

        @Override
        public boolean canStart() {
            LivingEntity current = soldier.getTarget();
            if (current == null || !current.isAlive()) return false;
            boolean fleeing = soldier.squaredDistanceTo(current) > 5.0 * 5.0
                    && current.getPos().subtract(soldier.getPos()).dotProduct(current.getVelocity()) > 0;
            if (!fleeing) return false;
            return findCloserAttacker(current) != null;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            LivingEntity current = soldier.getTarget();
            PlayerEntity attacker = findCloserAttacker(current);
            if (attacker != null) {
                RPGLoot.LOGGER.info("[MonarchsSoldier] Switching target from fleeing {} to closer {}", current, attacker);
                soldier.setTarget(attacker);
            }
        }

        private PlayerEntity findCloserAttacker(LivingEntity current) {
            return soldier.getWorld().getEntitiesByClass(PlayerEntity.class, soldier.getBoundingBox().expand(8.0),
                            p -> p != current && !p.isSpectator() && soldier.canTarget(p))
                    .stream()
                    .min(Comparator.comparingDouble(soldier::squaredDistanceTo))
                    .orElse(null);
        }
    }

}

package more_rpg_loot.entity.frozen_depths.mob.frozen_ranger;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.frozen_depths.projectile.FrozenArrowEntity;
import more_rpg_loot.item.CommonItems;
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
import net.minecraft.sound.SoundEvents;
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

    public FrostedRangerEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
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

        // Defensive: strip any melee goal that slipped in before updateAttackType() was neutralized.
        this.goalSelector.getGoals().stream()
                .filter(g -> g.getGoal() instanceof MeleeAttackGoal)
                .toList()
                .forEach(g -> this.goalSelector.remove(g.getGoal()));
    }

    @Override
    public void updateAttackType() {
        // AbstractSkeletonEntity re-adds its own internal meleeAttackGoal/bowAttackGoal here
        // whenever equipment changes, deciding via getMainHandStack().isOf(Items.BOW) - which is
        // false for our custom FROZEN_BOW item, so it always falls back to melee. That call runs
        // after initGoals() (during equip), so a one-time goal filter can't catch it - stop the
        // vanilla swap entirely and let initGoals() be the only source of truth for this entity's goals.
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
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(CommonItems.FROZEN_BOW.item()));
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

    public boolean isDrawingBow() {
        return this.dataTracker.get(DRAWING_BOW);
    }

    public void setDrawingBow(boolean drawing) {
        this.dataTracker.set(DRAWING_BOW, drawing);
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
        return 5;
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
        arrow.setDamage(this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5);
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(arrow);
        RPGLoot.LOGGER.info("[FrostedRanger] Normal arrow shot at {}", target);
    }

    private class NormalShotGoal extends Goal {
        // Matches the length of FrostedRangerAnimations.attack (1.6667s), now bound to
        // normalShootAnimationState, so the arrow releases right as the draw animation finishes.
        private static final int DRAW_TICKS = 33;
        private static final int SHOT_COOLDOWN = 20;

        private final FrostedRangerEntity ranger;
        private LivingEntity target;
        private int drawTimer = 0;
        private int shotCooldown = 0;

        NormalShotGoal(FrostedRangerEntity ranger) {
            this.ranger = ranger;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity t = ranger.getTarget();
            return t != null && t.isAlive() && ranger.squaredDistanceTo(t) <= 20.0 * 20.0;
        }

        @Override
        public boolean shouldContinue() {
            return target != null && target.isAlive() && ranger.squaredDistanceTo(target) <= 22.0 * 22.0;
        }

        @Override
        public void start() {
            target = ranger.getTarget();
            drawTimer = 0;
            shotCooldown = 0;
            RPGLoot.LOGGER.info("[FrostedRanger] NormalShotGoal started against {}", target);
        }

        @Override
        public void stop() {
            target = null;
            drawTimer = 0;
            ranger.setDrawingBow(false);
            ranger.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target == null) return;
            ranger.getLookControl().lookAt(target, 30.0F, 30.0F);

            double distSq = ranger.squaredDistanceTo(target);
            boolean canSee = ranger.getVisibilityCache().canSee(target);

            if (distSq > 14.0 * 14.0 || !canSee) {
                ranger.getNavigation().startMovingTo(target, 1.0);
            } else {
                ranger.getNavigation().stop();
            }

            if (shotCooldown > 0) {
                shotCooldown--;
                return;
            }

            if (canSee) {
                drawTimer++;
                ranger.setDrawingBow(true);
                if (drawTimer >= DRAW_TICKS) {
                    ranger.shootAt(target, 1.0F);
                    drawTimer = 0;
                    shotCooldown = SHOT_COOLDOWN;
                    ranger.setDrawingBow(false);
                }
            } else {
                drawTimer = 0;
                ranger.setDrawingBow(false);
            }
        }
    }

    private class FrostSalvoGoal extends Goal {
        private final FrostedRangerEntity ranger;
        private LivingEntity target;
        private int phase = 0;
        private int windupTimer = 0;
        private int retreatTimer = 0;

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
            // Matches the length of FrostedRangerAnimations.attackFast (1.0417s), now bound to
            // salvoAnimationState, so the volley fires right as the draw animation finishes.
            windupTimer = 21;
            retreatTimer = 0;
            ranger.setPerformingSalvo(true);
            ranger.getNavigation().stop();
            RPGLoot.LOGGER.info("[FrostedRanger] FrostSalvoGoal started against {}", target);
        }

        @Override
        public void tick() {
            if (target != null) ranger.getLookControl().lookAt(target, 30.0F, 30.0F);

            if (phase == 1) {
                if (--windupTimer <= 0) {
                    if (target == null) {
                        RPGLoot.LOGGER.warn("[FrostedRanger] FrostSalvoGoal windup finished with a null target - no arrows fired");
                    } else {
                        for (int i = 0; i < ranger.getSalvoArrowCount(); i++) {
                            fireArrow(i);
                        }
                        RPGLoot.LOGGER.info("[FrostedRanger] Salvo volley fired at {}", target);
                    }
                    phase = 2;
                    retreatTimer = 3;
                }
            } else if (phase == 2) {
                if (--retreatTimer <= 0) {
                    jumpBack();
                    stop();
                }
            }
        }

        private void jumpBack() {
            if (target == null) {
                RPGLoot.LOGGER.warn("[FrostedRanger] FrostSalvoGoal tried to leap back with a null target - skipped");
                return;
            }
            Vec3d awayDir = new Vec3d(ranger.getX() - target.getX(), 0.0, ranger.getZ() - target.getZ()).normalize();
            ranger.addVelocity(awayDir.x * 1.1, 0.4, awayDir.z * 1.1);
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
}

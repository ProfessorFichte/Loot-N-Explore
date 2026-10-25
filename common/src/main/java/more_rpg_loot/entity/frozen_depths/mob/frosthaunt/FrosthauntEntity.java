package more_rpg_loot.entity.frozen_depths.mob.frosthaunt;

import more_rpg_loot.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import com.mrpg_lib.entity.goal.WindupMeleeAttackGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.mrpg_lib.util.FreezeMethods.addFrozenTicksCapped;

public class FrosthauntEntity extends SkeletonEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState attackAnimationState2 = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimationCounter = 0;
    private int attackAnimTicksRemaining = 0;
    private EntityAttributeConfig.Entry tuningFallback;

    public FrosthauntEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 8.0F);
        this.experiencePoints += 1;

    }

    public static DefaultAttributeContainer.Builder createFrosthauntSkeletonAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2505)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2f);
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new WindupMeleeAttackGoal(this, 1.2, false, 0.0, new int[]{FrosthauntAnimationTimes.ATTACK1_IMPACT_TICK, FrosthauntAnimationTimes.ATTACK2_IMPACT_TICK}));
    }

    @Override
    public void updateAttackType() {
    }

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

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData);
        EntityAttributeApplier.apply(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, attributeFallback());
        return entityData2;
    }

    protected EntityAttributeConfig.Entry attributeFallback() {
        EntityAttributeConfig.Entry fallback = new EntityAttributeConfig.Entry()
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 25.0)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 4.0);
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.frosthaunt(fallback);
    }

    protected CustomValues tuning() {
        if (tuningFallback == null) tuningFallback = attributeFallback();
        return EntityAttributeApplier.custom(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, tuningFallback);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean attacked = super.tryAttack(target);
        if (attacked && target instanceof LivingEntity entity) {
            addFrozenTicksCapped(entity, tuning().getInt(FrozenDepthsTuning.FREEZE_TICKS_ON_HIT, 20));
            this.setAttacking(true);
        }
        return attacked;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            this.getLookControl().lookAt(target, 30.0F, 30.0F);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.handSwinging && this.handSwingTicks == 0) {
            this.attackAnimationState.stop();
            this.attackAnimationState2.stop();
            int idx = attackAnimationCounter % 2;
            if (idx == 0) {
                this.attackAnimationState.start(this.age);
            } else {
                this.attackAnimationState2.start(this.age);
            }
            this.attackAnimTicksRemaining = 15;
            attackAnimationCounter++;
        } else if (this.attackAnimTicksRemaining > 0 && --this.attackAnimTicksRemaining <= 0) {
            this.attackAnimationState.stop();
            this.attackAnimationState2.stop();
        }

        if (this.attackAnimTicksRemaining > 0) {
            this.idleAnimationState.stop();
            this.idleAnimationTimeout = 0;
        } else {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --this.idleAnimationTimeout;
            }
        }
    }

}




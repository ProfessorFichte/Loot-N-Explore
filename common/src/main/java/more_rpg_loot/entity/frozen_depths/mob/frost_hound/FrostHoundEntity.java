package more_rpg_loot.entity.frozen_depths.mob.frost_hound;

import more_rpg_loot.RPGLoot;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import com.mrpg_lib.entity.goal.WindupMeleeAttackGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FrostHoundEntity extends HostileEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState runAnimationState = new AnimationState();
    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState clawAnimationState = new AnimationState();
    private int movingGraceTicks = 0;
    private boolean nextAttackIsClaw = false;
    private int biteAnimTicksRemaining = 0;
    private int clawAnimTicksRemaining = 0;

    public FrostHoundEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints += 3;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        EntityAttributeConfig.Entry fallback = new EntityAttributeConfig.Entry()
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 20.0)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), 5.0);
        EntityAttributeApplier.apply(this, RPGLoot.entityAttributeConfig != null ? RPGLoot.entityAttributeConfig.value : null, fallback);
        return data;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new WindupMeleeAttackGoal(this, 1.2, false, 0.0, new int[]{FrostHoundAnimationTimes.BITE_IMPACT_TICK, FrostHoundAnimationTimes.CLAW_IMPACT_TICK}));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        double dx = this.getX() - this.prevX;
        double dz = this.getZ() - this.prevZ;
        if (dx * dx + dz * dz > 0.0001) {
            this.movingGraceTicks = 6;
        } else if (this.movingGraceTicks > 0) {
            this.movingGraceTicks--;
        }
        boolean moving = this.movingGraceTicks > 0;
        boolean sprinting = moving && this.isAttacking();

        if (this.handSwinging && this.handSwingTicks == 0) {
            if (nextAttackIsClaw) {
                this.clawAnimationState.start(this.age);
                this.clawAnimTicksRemaining = 13;
            } else {
                this.biteAnimationState.start(this.age);
                this.biteAnimTicksRemaining = 8;
            }
            nextAttackIsClaw = !nextAttackIsClaw;
        }

        if (this.biteAnimTicksRemaining > 0 && --this.biteAnimTicksRemaining <= 0) {
            this.biteAnimationState.stop();
        }
        if (this.clawAnimTicksRemaining > 0 && --this.clawAnimTicksRemaining <= 0) {
            this.clawAnimationState.stop();
        }

        boolean attacking = this.biteAnimTicksRemaining > 0 || this.clawAnimTicksRemaining > 0;

        if (sprinting) {
            this.runAnimationState.startIfNotRunning(this.age);
            this.walkAnimationState.stop();
        } else if (moving) {
            this.walkAnimationState.startIfNotRunning(this.age);
            this.runAnimationState.stop();
        } else {
            this.runAnimationState.stop();
            this.walkAnimationState.stop();
        }

        if (moving || attacking) {
            this.idleAnimationState.stop();
        } else {
            this.idleAnimationState.startIfNotRunning(this.age);
        }
    }
}

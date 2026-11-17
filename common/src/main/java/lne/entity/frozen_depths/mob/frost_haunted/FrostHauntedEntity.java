package lne.entity.frozen_depths.mob.frost_haunted;

import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import lne.util.LneLootTables;

import lne.util.LneTags;
import lne.platform.LNEPlatform;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import lne.LneMod;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import com.mrpg_lib.entity.ISpellCasterEntity;
import lne.config.TweaksConfig;
import lne.entity.frozen_depths.goal.CastingBodyLock;
import lne.entity.frozen_depths.goal.MobCompatGoals;
import lne.entity.frozen_depths.goal.MobSpellGoals;
import lne.entity.frozen_depths.goal.ToggleableMeleeGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

public class FrostHauntedEntity extends SkeletonEntity implements ISpellCasterEntity {

    @Override
    protected RegistryKey<LootTable> getLootTableId() {
        return LneLootTables.frozenDepthsEntity(this.getType());
    }

    private static final TrackedData<Boolean> SPELL_CASTING = DataTracker.registerData(FrostHauntedEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState attackAnimationState2 = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimationCounter = 0;
    private int attackAnimTicksRemaining = 0;
    private EntityAttributeConfig.Entry tuningFallback;
    private boolean spellCasting;
    private final CastingBodyLock castingBodyLock = new CastingBodyLock(this, SPELL_CASTING);

    public FrostHauntedEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 8.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 8.0F);
        this.experiencePoints += 1;

    }

    public static DefaultAttributeContainer.Builder createFrostHauntedSkeletonAttributes() {
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
        MobSpellGoals.Factory spells = MobSpellGoals.create(this);
        if (spells != null) {
            this.goalSelector.add(3, spells.weaponSkills(MobSpellGoals.Options.of(this::usesWeaponSkills)));
        }
        Goal betterCombat = MobCompatGoals.betterCombat(this, 1.2, this::usesBetterCombat);
        if (betterCombat != null) {
            this.goalSelector.add(4, betterCombat);
        }
        this.goalSelector.add(4, new ToggleableMeleeGoal(this, () -> tuning().getBoolean(FrozenDepthsTuning.MELEE_ENABLED, true) && !usesBetterCombat(),
                1.2, false, 0.0, new int[]{FrostHauntedAnimationTimes.ATTACK1_IMPACT_TICK, FrostHauntedAnimationTimes.ATTACK2_IMPACT_TICK}, this::onMeleeImpact));
    }

    private void onMeleeImpact(int variant, @Nullable LivingEntity target) {
        this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7F, 0.9F);
        if (target != null) {
            this.tryAttack(target);
        }
    }

    private boolean usesWeaponSkills() {
        return TweaksConfig.mobSpellCasting()
                && tuning().getBoolean(FrozenDepthsTuning.WEAPON_SKILLS_ENABLED, true);
    }

    private boolean usesBetterCombat() {
        return TweaksConfig.mobBetterCombat()
                && tuning().getBoolean(FrozenDepthsTuning.MELEE_ENABLED, true)
                && tuning().getBoolean(FrozenDepthsTuning.BETTER_COMBAT_ENABLED, true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SPELL_CASTING, false);
    }

    @Override
    public void startSpellCast(int ticks) {
        this.spellCasting = true;
        this.castingBodyLock.set(true);
    }

    @Override
    public void stopSpellCast() {
        this.spellCasting = false;
        this.castingBodyLock.set(false);
    }

    @Override
    public boolean isMobCasting() {
        return this.spellCasting;
    }

    @Override
    public MobEntity asMobEntity() {
        return this;
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
        EntityAttributeApplier.apply(this, LneMod.entityAttributeConfig != null ? LneMod.entityAttributeConfig.value : null, attributeFallback());
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
        return FrozenDepthsTuning.frostHaunted(fallback);
    }

    protected CustomValues tuning() {
        if (tuningFallback == null) tuningFallback = attributeFallback();
        return EntityAttributeApplier.custom(this, LneMod.entityAttributeConfig != null ? LneMod.entityAttributeConfig.value : null, tuningFallback);
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

    protected float voicePitch() {
        return 0.85F;
    }

    @Override
    public float getSoundPitch() {
        return voicePitch() + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_STRAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_STRAY_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_STRAY_STEP, 0.15F, 1.0F);
    }


    @Override
    public void tick() {
        super.tick();
        this.castingBodyLock.tick();

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

    @Override
    protected boolean isAffectedByDaylight() {
        return super.isAffectedByDaylight() && !this.getWorld().getBiome(this.getBlockPos()).isIn(LneTags.ICY_BIOMES);
    }
}

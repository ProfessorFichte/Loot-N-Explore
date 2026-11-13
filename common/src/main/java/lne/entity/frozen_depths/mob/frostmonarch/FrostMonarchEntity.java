package lne.entity.frozen_depths.mob.frostmonarch;
import lne.platform.LNEPlatform;
import lne.LneMod;
import lne.entity.frozen_depths.FrozenDepthsTuning;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import lne.fx.LNEFx;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.effects.Effects;
import lne.entity.frozen_depths.mob.frostmonarch.goals.*;
import lne.item.CommonItems;
import lne.sounds.ModSounds;
import com.mrpg_lib.util.combat.MeleeArcStrike;
import net.minecraft.entity.*;
import com.mrpg_lib.sounds.MRPGLibSounds;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import lne.config.TweaksConfig;
import lne.entity.frozen_depths.goal.CastingBodyLock;
import lne.entity.frozen_depths.goal.MobCompatGoals;
import lne.entity.frozen_depths.goal.MobFacing;
import lne.entity.frozen_depths.goal.MobSpellGoals;
import lne.entity.frozen_depths.goal.ToggleableMeleeGoal;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import com.mrpg_lib.effect.MRPGCEffects;
import com.mrpg_lib.entity.config.CustomValues;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import com.mrpg_lib.entity.ISpellCasterEntity;
import net.minecraft.entity.mob.MobEntity;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static lne.util.HelperMethods.applyStatusEffect;
import static lne.util.HelperMethods.getFreezingEffect;

public class FrostMonarchEntity extends SkeletonEntity implements ISpellCasterEntity {
    private static final TrackedData<Integer> INVUL_TIMER;
    private static final TrackedData<Integer> BARRIER_TIMER;
    private static final TrackedData<Boolean> SCREECHING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CASTING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CHANNELING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_SPAWNED = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FAKE_DEATH = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> CAST_ANIMATION = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SPELL_CASTING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final int SPELL_RELEASE_LOCK_TICKS = 10;
    private static final double BASE_MAX_HEALTH = 300.0;
    private static final double BASE_ATTACK_DAMAGE = 6.0;
    private static final double BASE_ARMOR = 4.0;
    private static final int DEFAULT_INVUL_TIMER = 220;
    private static final int INTRO_HEALING_SOUND_DELAY = 30;
    private static final int DEATH_ANIMATION_DURATION = FrostMonarchAnimationTimes.DEATH_LENGTH_TICKS;
    public static final double CLOSE_RANGE = 8.0;
    public static final double MID_RANGE = 20.0;
    public static final double MAX_RANGE = 40.0;
    private static final double REACH_HEIGHT = 2.5;
    private static final double REACH_PATH_TOLERANCE_SQUARED = 4.0;
    private static final int REACH_CHECK_INTERVAL = 10;
    private final ServerBossBar bossBar;
    private int fakeDeathTimer = 0;
    private boolean introRequested;
    // Store the damage source that triggered death. Cannot be saved to NBT, will be null after reload.
    // Falls back to generic damage source if null.
    private DamageSource lastFatalDamageSource = null;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState screechAnimationState = new AnimationState();
    public final AnimationState summonAnimationState = new AnimationState();
    public final AnimationState quickCastAnimationState = new AnimationState();
    public final AnimationState wallCastAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public final AnimationState spawnAnimationState = new AnimationState();
    public final AnimationState attackAnimationState1 = new AnimationState();
    public final AnimationState attackAnimationState2 = new AnimationState();
    public final AnimationState attackAnimationState3 = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int attackAnimationCounter = 0;
    private int attackAnimTicksRemaining = 0;
    public int screechCooldown = 0;
    public int hailStormCooldown = 0;
    public int icicleCooldown = 0;
    public int distanceIcicleCooldown = 0;
    public int basicIcicleCooldown = 0;
    public int iceWallCooldown = 0;
    private int lastDeflectSoundAge = -100;
    private boolean targetOutOfReach;
    private int reachCheckTimer = 0;
    private int barrierUsesRemaining = -1;
    private int barrierThresholdReached = -1;
    public int globalAbilityCooldown = 0;
    private int castAnimationTicks = 0;
    private int spellReleaseLockTicks = 0;
    private static final int STORM_FX_INTERVAL = 40;
    private static final int AURA_FX_INTERVAL = 10;
    private EntityAttributeConfig.Entry tuningFallback;
    private boolean spellCasting;
    private final CastingBodyLock castingBodyLock = new CastingBodyLock(this, SPELL_CASTING);
    @Nullable
    private MobSpellGoals.Factory spellGoals;


    public FrostMonarchEntity(EntityType<? extends FrostMonarchEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.setHealth(this.getMaxHealth());
        this.experiencePoints += 50;
        this.setPersistent();
    }

    public ItemStack getWeaponForDifficulty() {
        return new ItemStack(CommonItems.MONARCHS_FROST_STAFF.item());
    }

    public int getBarrierUsesRemaining() {
        if (this.barrierUsesRemaining < 0) {
            this.barrierUsesRemaining = Math.max(0, tuning().getInt(FrozenDepthsTuning.BARRIER_USES, 3));
        }
        return this.barrierUsesRemaining;
    }

    public void consumeBarrierUse() {
        this.barrierUsesRemaining = Math.max(0, this.getBarrierUsesRemaining() - 1);
    }

    public int getBarrierThresholdReached() {
        return this.barrierThresholdReached;
    }

    public void setBarrierThresholdReached(int index) {
        this.barrierThresholdReached = index;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    public Float getHealingForDifficulty() {
        Difficulty difficulty = this.getWorld().getDifficulty();
        return switch (difficulty) {
            case PEACEFUL, EASY -> 0.25F;
            case NORMAL -> 0.35F;
            case HARD -> 0.5F;
        };
    }

    public static DefaultAttributeContainer.Builder createFrostmonarchAttributes() {
        DefaultAttributeContainer.Builder builder = HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR, BASE_ARMOR)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2705)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, BASE_MAX_HEALTH)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f);
        if (LNEPlatform.isModLoaded("spell_power")) {
            builder = builder.add(SpellSchools.FROST.attributeEntry, 5.25)
                    .add(SpellSchools.FIRE.attributeEntry, 1.0);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            builder = builder.add(ThermooAttributes.MIN_TEMPERATURE, 0.0)
                    .add(ThermooAttributes.FROST_RESISTANCE, 0.0);
        }
        return builder;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
        nbt.putBoolean("HasSpawned", this.hasSpawned());
        nbt.putBoolean("FakeDeath", this.isFakeDeath());
        nbt.putInt("FakeDeathTimer", this.fakeDeathTimer);
        nbt.putInt("BarrierUsesRemaining", this.barrierUsesRemaining);
        nbt.putInt("BarrierThresholdReached", this.barrierThresholdReached);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
        this.setBarrierTimer(0);
        this.setCasting(false);
        this.setChanneling(false);
        this.setScreeching(false);
        this.dataTracker.set(CAST_ANIMATION, FrostMonarchAnimationTimes.Cast.NONE.ordinal());
        this.castingBodyLock.set(false);
        this.setHasSpawned(nbt.getBoolean("HasSpawned"));
        // A monarch from /summon with NBT has no saved HasSpawned. It spawns normally,
        // unless the command asks for the intro with IntroAnimation:1b.
        if (nbt.getBoolean("IntroAnimation")) {
            this.introRequested = true;
        } else if (!nbt.contains("HasSpawned")) {
            this.setHasSpawned(true);
        }
        this.setFakeDeath(nbt.getBoolean("FakeDeath"));
        this.fakeDeathTimer = nbt.getInt("FakeDeathTimer");
        this.barrierUsesRemaining = nbt.contains("BarrierUsesRemaining") ? nbt.getInt("BarrierUsesRemaining") : -1;
        this.barrierThresholdReached = nbt.contains("BarrierThresholdReached") ? nbt.getInt("BarrierThresholdReached") : -1;
        this.setPersistent();
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

    }

    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new IceBarrierGoal(this));
        this.goalSelector.add(1, new ProtectiveIceWallGoal(this));
        this.goalSelector.add(2, new ScreechGoal(this));
        this.goalSelector.add(3, new HailstormGoal(this));
        this.goalSelector.add(4, new DistanceIcicleGoal(this));
        this.goalSelector.add(4, new IcicleAttackGoal(this));
        this.spellGoals = MobSpellGoals.create(this);
        if (this.spellGoals != null) {
            this.goalSelector.add(5, this.spellGoals.spells(MobSpellGoals.tag("frost_monarch", "secondary"),
                    MobSpellGoals.Options.of(this::canCastSecondarySpell)
                            .cooldown(() -> tuning().getInt(FrozenDepthsTuning.SPELL_CASTING_COOLDOWN, 240))
                            .onFinish(this::startAbilityPause)));
            this.goalSelector.add(6, this.spellGoals.weaponSkills(MobSpellGoals.Options.of(this::canCastStaffSpell)
                    .cooldown(() -> tuning().getInt(FrozenDepthsTuning.BASIC_ICICLE_COOLDOWN, 160))
                    .onFinish(() -> {
                        this.basicIcicleCooldown = tuning().getInt(FrozenDepthsTuning.BASIC_ICICLE_COOLDOWN, 160);
                        this.startAbilityPause();
                    })));
        }
        this.goalSelector.add(6, new BasicIcicleGoal(this));

        Goal betterCombat = MobCompatGoals.betterCombat(this, 1.2, () -> usesBetterCombat() && !isBusy());
        if (betterCombat != null) {
            this.goalSelector.add(7, betterCombat);
        }
        this.goalSelector.add(7, new ConditionalGoal(this, new ToggleableMeleeGoal(this,
                () -> tuning().getBoolean(FrozenDepthsTuning.MELEE_ENABLED, true) && !usesBetterCombat(),
                1.2, false, 3.5, new int[]{FrostMonarchAnimationTimes.ATTACK1_IMPACT_TICK, FrostMonarchAnimationTimes.ATTACK2_IMPACT_TICK, FrostMonarchAnimationTimes.ATTACK3_IMPACT_TICK}, this::onMeleeImpact)));

        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        this.goalSelector.getGoals().stream()
                .filter(g -> g.getGoal().getClass() == MeleeAttackGoal.class)
                .toList()
                .forEach(g -> this.goalSelector.remove(g.getGoal()));
    }

    @Override
    public void updateAttackType() {
        // Vanilla re-adds its own melee goal on equip (isOf(BOW) is false for our weapon); block it.
    }

    public boolean usesBetterCombat() {
        return TweaksConfig.mobBetterCombat()
                && tuning().getBoolean(FrozenDepthsTuning.MELEE_ENABLED, true)
                && tuning().getBoolean(FrozenDepthsTuning.BETTER_COMBAT_ENABLED, true);
    }

    public boolean usesStaffSpell() {
        return this.spellGoals != null && TweaksConfig.mobSpellCasting()
                && tuning().getBoolean(FrozenDepthsTuning.PRIMARY_SPELL_ENABLED, true)
                && this.spellGoals.hasWeaponSkills();
    }

    private boolean canCastStaffSpell() {
        LivingEntity target = this.getTarget();
        return target != null && BasicIcicleGoal.canUseBasicAttack(this, target) && usesStaffSpell();
    }

    private boolean canCastSecondarySpell() {
        LivingEntity target = this.getTarget();
        return target != null && target.isAlive() && !isPerformingAbility()
                && TweaksConfig.mobSpellCasting()
                && tuning().getBoolean(FrozenDepthsTuning.SPELL_CASTING_ENABLED, true)
                && (this.isTargetOutOfReach() || this.horizontalDistanceTo(target) > CLOSE_RANGE)
                && !BasicIcicleGoal.coreAbilityReady(this, target)
                && this.getVisibilityCache().canSee(target);
    }

    @Override
    public void startSpellCast(int ticks) {
        this.spellCasting = true;
        this.castingBodyLock.set(true);
        this.getNavigation().stop();
    }

    @Override
    public void stopSpellCast() {
        if (this.spellCasting) {
            this.spellReleaseLockTicks = SPELL_RELEASE_LOCK_TICKS;
        }
        this.spellCasting = false;
        this.castingBodyLock.set(false);
    }

    public boolean isSpellCastingSynced() {
        return this.castingBodyLock.isCasting();
    }

    @Override
    public boolean isMobCasting() {
        return this.spellCasting;
    }

    @Override
    public MobEntity asMobEntity() {
        return this;
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(INVUL_TIMER, 0);
        builder.add(BARRIER_TIMER, 0);
        builder.add(SCREECHING, false);
        builder.add(CASTING, false);
        builder.add(CHANNELING, false);
        builder.add(HAS_SPAWNED, false);
        builder.add(FAKE_DEATH, false);
        builder.add(CAST_ANIMATION, FrostMonarchAnimationTimes.Cast.NONE.ordinal());
        builder.add(SPELL_CASTING, false);
    }

    public FrostMonarchAnimationTimes.Cast getCastAnimation() {
        return FrostMonarchAnimationTimes.Cast.byId(this.dataTracker.get(CAST_ANIMATION));
    }

    public void playCastAnimation(FrostMonarchAnimationTimes.Cast cast) {
        this.dataTracker.set(CAST_ANIMATION, cast.ordinal());
        this.castAnimationTicks = cast.lengthTicks;
        this.getNavigation().stop();
    }

    public void startAbilityPause() {
        int pause = Math.max(0, tuning().getInt(FrozenDepthsTuning.ABILITY_PAUSE, 40));
        this.globalAbilityCooldown = pause + this.random.nextInt(pause / 2 + 1);
    }

    public boolean isScreeching() {
        return this.dataTracker.get(SCREECHING);
    }

    public void setScreeching(boolean value) {
        this.dataTracker.set(SCREECHING, value);
    }

    public boolean isCasting() {
        return this.dataTracker.get(CASTING);
    }

    public void setCasting(boolean value) {
        this.dataTracker.set(CASTING, value);
    }

    public boolean isChanneling() {
        return this.dataTracker.get(CHANNELING);
    }

    public void setChanneling(boolean value) {
        this.dataTracker.set(CHANNELING, value);
    }

    public boolean hasSpawned() {
        return this.dataTracker.get(HAS_SPAWNED);
    }

    private void setHasSpawned(boolean value) {
        this.dataTracker.set(HAS_SPAWNED, value);
    }

    public boolean isFakeDeath() {
        return this.dataTracker.get(FAKE_DEATH);
    }

    private void setFakeDeath(boolean value) {
        this.dataTracker.set(FAKE_DEATH, value);
    }

    public boolean isPerformingAbility() {
        return this.isBusy() || this.globalAbilityCooldown > 0;
    }

    public boolean isBusy() {
        return this.isCasting() || this.getInvulnerableTimer() > 0 || this.isMovementLocked();
    }

    public boolean isMovementLocked() {
        return this.castAnimationTicks > 0 || this.spellReleaseLockTicks > 0 || this.spellCasting
            || this.isScreeching() || this.getBarrierTimer() > 0;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isMovementLocked() && !this.getWorld().isClient) {
            this.setJumping(false);
            super.travel(Vec3d.ZERO);
            return;
        }
        super.travel(movementInput);
    }

    public double horizontalDistanceTo(Entity entity) {
        double dx = entity.getX() - this.getX();
        double dz = entity.getZ() - this.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    public boolean isTargetOutOfReach() {
        return this.targetOutOfReach;
    }

    private void updateReach() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            this.targetOutOfReach = false;
            this.reachCheckTimer = 0;
            return;
        }
        if (--this.reachCheckTimer > 0) {
            return;
        }
        this.reachCheckTimer = REACH_CHECK_INTERVAL;
        this.targetOutOfReach = computeOutOfReach(target);
    }

    private boolean computeOutOfReach(LivingEntity target) {
        double dy = target.getY() - this.getY();
        if (dy > REACH_HEIGHT) {
            return true;
        }
        if (Math.abs(dy) <= REACH_HEIGHT && this.horizontalDistanceTo(target) <= 3.0) {
            return false;
        }
        Path path = this.getNavigation().findPathTo(target, 0);
        if (path == null) {
            return true;
        }
        if (path.reachesTarget()) {
            return false;
        }
        PathNode end = path.getEnd();
        return end == null || end.getSquaredDistance(target.getBlockPos()) > REACH_PATH_TOLERANCE_SQUARED;
    }

    public float getDifficultyMultiplier() {
        Difficulty difficulty = this.getWorld().getDifficulty();
        return switch (difficulty) {
            case PEACEFUL, EASY -> 0.75F;
            case NORMAL -> 1.0F;
            case HARD -> 1.25F;
        };
    }

    public float getHealthMultiplier() {
        float healthPercent = this.getHealth() / this.getMaxHealth();

        if (healthPercent >= 0.75F) {
            return 1.0F;
        } else if (healthPercent >= 0.50F) {
            return 1.1F;
        } else if (healthPercent >= 0.25F) {
            return 1.25F;
        } else if (healthPercent >= 0.10F) {
            return 1.4F;
        } else {
            return 1.6F;
        }
    }

    public float getCombinedMultiplier() {
        return getDifficultyMultiplier() * getHealthMultiplier();
    }

    public float getScaledDamage(float baseDamage) {
        return baseDamage * getCombinedMultiplier();
    }

    public int getScaledCount(int baseCount) {
        return Math.max(1, Math.round(baseCount * getCombinedMultiplier()));
    }

    public int getScaledCooldown(int baseCooldown) {
        return Math.max(20, Math.round(baseCooldown / getHealthMultiplier()));
    }


    public void tickMovement() {
        if(this.isFakeDeath() || this.deathTime > 0 || this.getInvulnerableTimer() > 0){
            float lockedYaw = this.bodyYaw;
            this.setPitch(0.0F);
            this.setYaw(lockedYaw);
            this.setHeadYaw(lockedYaw);
            this.bodyYaw = lockedYaw;
            this.prevBodyYaw = lockedYaw;
            this.prevPitch = 0.0F;
            this.prevYaw = lockedYaw;
            this.prevHeadYaw = lockedYaw;
            this.setVelocity(Vec3d.ZERO);
            this.velocityModified = true;
        }

        if(this.isFakeDeath()) {
            return;
        }
        if (this.getWorld().isClient) {
            if (this.getInvulnerableTimer() > 0) {
                if (this.age % STORM_FX_INTERVAL == 0) {
                    float progress = 1.0F - this.getInvulnerableTimer() / 220.0F;
                    float radius = 2.0F + 2.5F * progress;
                    MrpgFx.playLocal(LNEFx.MONARCH_AWAKENING_STORM, FxParams.on(this).radius(radius));
                    MrpgFx.playLocal(LNEFx.MONARCH_AWAKENING_GUST, FxParams.on(this).radius(radius + 0.5F));
                }
            } else if (this.age % AURA_FX_INTERVAL == 0 && !this.isOnFire()) {
                MrpgFx.playLocal(this.isChanneling() ? LNEFx.MONARCH_CASTING_AURA : LNEFx.MONARCH_AURA, FxParams.on(this));
            }
        }
        super.tickMovement();
    }

    public void playCastFx() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            MrpgFx.play(serverWorld, LNEFx.MONARCH_CAST, FxParams.at(this.getPos().add(0, this.getHeight() * 0.6, 0)));
        }
    }

    protected void mobTick() {
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            this.getLookControl().lookAt(target, 30.0F, 30.0F);
        }

        int i;
        if (this.getInvulnerableTimer() > 0) {

            i = this.getInvulnerableTimer() - 1;
            float spawnProgress = 1.0F - (float)i / 220.0F;
            this.bossBar.setPercent(spawnProgress);
            if (i <= 0) {
                if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;
                this.setHasSpawned(true);

                this.equipStack(EquipmentSlot.MAINHAND, getWeaponForDifficulty());
                this.getWorld().playSound(
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        ModSounds.FROSTMONARCH_DEEP_FREEZE.soundEvent(),
                        SoundCategory.HOSTILE,
                        0.75f, 1.0f
                );
                MrpgFx.play(serverWorld, LNEFx.MONARCH_AWAKENING_SHOCKWAVE, FxParams.at(this.getPos()));
                MrpgFx.play(serverWorld, LNEFx.MONARCH_AWAKENING_BURST, FxParams.at(this.getPos().add(0, 1.0, 0)));
                double radius = 5.0;
                List<LivingEntity> livingEntities = this.getWorld().getEntitiesByClass(
                        LivingEntity.class,
                        new Box(
                                this.getPos().x - radius, this.getPos().y - radius, this.getPos().z - radius,
                                this.getPos().x + radius, this.getPos().y + radius, this.getPos().z + radius
                        ),
                        livingEntity -> livingEntity.isAlive() && livingEntity.squaredDistanceTo(this.getPos()) <= radius * radius
                );
                RegistryEntry<StatusEffect> effectEntry = Effects.FREEZING.registryEntry;
                if (LNEPlatform.isModLoaded("mrpg_lib")) {
                    effectEntry = MRPGCEffects.FROZEN_SOLID.entry;
                }
                for (LivingEntity livingEntity : livingEntities) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(
                            effectEntry, 100, 0
                    ));
                }

            }

            this.setInvulTimer(i);
            if (i == DEFAULT_INVUL_TIMER - INTRO_HEALING_SOUND_DELAY) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.FROSTMONARCH_HEALING.soundEvent(), SoundCategory.HOSTILE, 1.5F, 1.0F);
            }
            if (this.age % 10 == 0) {
                float healAmount = this.getMaxHealth() / 22.0F;
                this.heal(healAmount);
            }

        } else {
            updateReach();
            super.mobTick();
            if (this.isMovementLocked()) {
                this.getNavigation().stop();
            }
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
    }

    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, getWeaponForDifficulty());
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData);
        if (spawnReason != SpawnReason.STRUCTURE) {
            this.setHasSpawned(true);
        }

        EntityAttributeApplier.apply(this, LneMod.entityAttributeConfig != null ? LneMod.entityAttributeConfig.value : null, attributeFallback());

        this.updateAttackType();
        return entityData2;
    }

    protected EntityAttributeConfig.Entry attributeFallback() {
        EntityAttributeConfig.Entry fallback = new EntityAttributeConfig.Entry()
                .set(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), BASE_MAX_HEALTH, 1.25, 1.5)
                .set(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), BASE_ATTACK_DAMAGE, 1.25, 1.5)
                .set(EntityAttributes.GENERIC_ARMOR.getIdAsString(), BASE_ARMOR, 1.25, 1.5);
        if (LNEPlatform.isModLoaded("spell_power")) {
            fallback.set(SpellSchools.FROST.attributeEntry.getIdAsString(), 5.25, 1.25, 1.5)
                    .set(SpellSchools.FIRE.attributeEntry.getIdAsString(), 1.0, 1.25, 1.5);
        }
        if (LNEPlatform.isModLoaded("thermoo")) {
            fallback.set(ThermooAttributes.MIN_TEMPERATURE.getIdAsString(), 5.0)
                    .set(ThermooAttributes.FROST_RESISTANCE.getIdAsString(), 10.0);
        }
        return FrozenDepthsTuning.frostMonarch(fallback);
    }

    public CustomValues tuning() {
        if (tuningFallback == null) tuningFallback = attributeFallback();
        return EntityAttributeApplier.custom(this, LneMod.entityAttributeConfig != null ? LneMod.entityAttributeConfig.value : null, tuningFallback);
    }

    private void onMeleeImpact(int variant, @Nullable LivingEntity target) {
        if (variant == 1) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.2F, 0.6F);
            MeleeArcStrike.strike(this, 4.5, 0.0F, 50.0F);
        } else {
            this.playSound(MRPGLibSounds.STRONG_ATTACK.soundEvent(), 1.0F, 0.8F);
            if (target != null) {
                this.tryAttack(target);
            }
        }
    }

    private void playDeflectSound(DamageSource source) {
        if (this.getWorld().isClient || !(source.getAttacker() instanceof PlayerEntity) || this.age - this.lastDeflectSoundAge < 10) {
            return;
        }
        this.lastDeflectSoundAge = this.age;
        this.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, 1.0F, 0.6F);
        this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 0.5F, 1.5F);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (this.getBarrierTimer() > 0) {
            return false;
        }

        boolean success = super.tryAttack(target);
        if (success) {
            if (target instanceof LivingEntity entity) {
                applyStatusEffect(entity,0,6, getFreezingEffect(),2,
                        true,true,true,0);

                this.setAttacking(true);
            }
        }
        return success;
    }

    protected boolean isAffectedByDaylight() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (this.isFakeDeath()) {
            if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.lastFatalDamageSource = source;
            }
            return false;
        }
        if ((this.getInvulnerableTimer() > 0 || this.getBarrierTimer() > 0) && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            playDeflectSound(source);
            return false;
        }
        if(source.isIn(DamageTypeTags.IS_FIRE)){
            return false;
        }

        if (!this.getWorld().isClient && !this.isOnFire()) {
            if(!source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !source.isOf(DamageTypes.THORNS) && source.isDirect() && source.isIn(DamageTypeTags.IS_PLAYER_ATTACK)){
                Entity attacker = source.getSource();
                if (attacker instanceof LivingEntity livingEntity) {
                    applyStatusEffect(livingEntity,0,6, getFreezingEffect(),0,
                            false,true,true,0);
                }
            }
        }

        if (!this.getWorld().isClient && !this.isFakeDeath() && this.getHealth() - amount <= 0.0F) {
            this.setHealth(0.0F);
            this.dead = true;
            this.onDeath(source);
            return true;
        }

        return super.damage(source, amount);
    }

    private void triggerFakeDeath(DamageSource damageSource) {
        this.lastFatalDamageSource = damageSource;
        this.setVelocity(Vec3d.ZERO);
        this.velocityModified = true;
        this.velocityDirty = true;
        this.setNoGravity(true);
        float lockedYaw = this.bodyYaw;
        this.setPitch(0.0F);
        this.setYaw(lockedYaw);
        this.setHeadYaw(lockedYaw);
        this.bodyYaw = lockedYaw;
        this.prevBodyYaw = lockedYaw;
        this.prevPitch = 0.0F;
        this.prevYaw = lockedYaw;
        this.prevHeadYaw = lockedYaw;

        this.setFakeDeath(true);
        this.setChanneling(false);
        this.fakeDeathTimer = DEATH_ANIMATION_DURATION;

        this.setHealth(0.5F);

        this.dead = false;

        this.deathTime = 0;

        this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
        FrostMonarchLines.fakeDeath(this);

        this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.isFakeDeath() && !this.getWorld().isClient) {
            this.triggerFakeDeath(damageSource);
        } else if (!this.isFakeDeath()) {
            super.onDeath(damageSource);
        }
    }

    private void dropLootManually(ServerWorld world, DamageSource damageSource) {
        Entity killer = damageSource.getAttacker();

        net.minecraft.loot.context.LootContextParameterSet.Builder builder =
            new net.minecraft.loot.context.LootContextParameterSet.Builder(world)
                .add(net.minecraft.loot.context.LootContextParameters.THIS_ENTITY, this)
                .add(net.minecraft.loot.context.LootContextParameters.ORIGIN, this.getPos())
                .add(net.minecraft.loot.context.LootContextParameters.DAMAGE_SOURCE, damageSource)
                .addOptional(net.minecraft.loot.context.LootContextParameters.ATTACKING_ENTITY, killer)
                .addOptional(net.minecraft.loot.context.LootContextParameters.DIRECT_ATTACKING_ENTITY, damageSource.getSource());

        if (killer instanceof PlayerEntity player) {
            builder.add(net.minecraft.loot.context.LootContextParameters.LAST_DAMAGE_PLAYER, player)
                   .luck(player.getLuck());
        }
        net.minecraft.loot.context.LootContextParameterSet lootContextParameterSet = builder.build(net.minecraft.loot.context.LootContextTypes.ENTITY);
        net.minecraft.loot.LootTable lootTable = world.getServer().getReloadableRegistries().getLootTable(this.getLootTable());

        lootTable.generateLoot(lootContextParameterSet, this.getLootTableSeed(), stack -> {
            ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), stack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        });
    }

    private void dropExperience(ServerWorld world, @Nullable Entity attacker) {
        if (attacker instanceof PlayerEntity) {
            ExperienceOrbEntity.spawn(world, this.getPos(), this.experiencePoints);
        }
    }



    @Override
    public void tick() {
        if (this.introRequested && !this.getWorld().isClient) {
            this.introRequested = false;
            this.setHasSpawned(false);
            this.onSummoned();
        }
        super.tick();
        if (this.isFakeDeath()) {
            this.hurtTime = 0;

            this.setVelocity(Vec3d.ZERO);
            this.velocityModified = true;
            this.velocityDirty = true;
            this.setNoGravity(true);

            float lockedYaw = this.bodyYaw;
            this.setPitch(0.0F);
            this.setYaw(lockedYaw);
            this.setHeadYaw(lockedYaw);
            this.bodyYaw = lockedYaw;
            this.prevBodyYaw = lockedYaw;
            this.prevPitch = 0.0F;
            this.prevYaw = lockedYaw;
            this.prevHeadYaw = lockedYaw;

            if (!this.getWorld().isClient) {
                this.fakeDeathTimer--;

                if (this.fakeDeathTimer <= 0) {
                    if (this.getWorld() instanceof ServerWorld serverWorld) {
                        DamageSource deathSource = this.lastFatalDamageSource;
                        if (deathSource == null) {
                            deathSource = this.getDamageSources().generic();
                        }

                        this.dropLootManually(serverWorld, deathSource);

                        this.dropExperience(serverWorld, deathSource.getAttacker());
                        MrpgFx.play(serverWorld, LNEFx.MONARCH_DEATH_BURST, FxParams.at(this.getPos().add(0, 1.2, 0)));
                    }
                    this.setNoGravity(false);
                    this.setHealth(0.0F);
                    this.dead = true;
                    this.remove(RemovalReason.KILLED);
                }
            }

            if (this.getWorld().isClient) {
                setupAnimationStates();
                if (this.age % AURA_FX_INTERVAL == 0) {
                    MrpgFx.playLocal(LNEFx.MONARCH_DEATH_SNOWFALL, FxParams.at(this.getPos()));
                }
            }
            return;
        }

        if (this.deathTime > 0) {
            this.hurtTime = 0;
        }

        if (screechCooldown > 0) screechCooldown--;
        if (hailStormCooldown > 0) hailStormCooldown--;
        if (icicleCooldown > 0) icicleCooldown--;
        if (distanceIcicleCooldown > 0) distanceIcicleCooldown--;
        if (basicIcicleCooldown > 0) basicIcicleCooldown--;
        if (iceWallCooldown > 0) iceWallCooldown--;
        if (globalAbilityCooldown > 0) globalAbilityCooldown--;
        if (spellReleaseLockTicks > 0) spellReleaseLockTicks--;
        if (castAnimationTicks > 0 && --castAnimationTicks == 0 && !this.getWorld().isClient) {
            this.dataTracker.set(CAST_ANIMATION, FrostMonarchAnimationTimes.Cast.NONE.ordinal());
        }

        if (!this.getWorld().isClient && this.isMovementLocked()) {
            Vec3d velocity = this.getVelocity();
            this.setVelocity(0.0, Math.min(velocity.y, 0.0), 0.0);
        }

        if (this.isSpellCastingSynced() && !this.getWorld().isClient && this.getTarget() != null && this.getTarget().isAlive()) {
            MobFacing.face(this, this.getTarget());
        }
        this.castingBodyLock.tick();

        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.deathTime > 0 || this.isFakeDeath()) {
            this.deathAnimationState.startIfNotRunning(this.age);

            this.screechAnimationState.stop();
            this.summonAnimationState.stop();
            this.quickCastAnimationState.stop();
            this.wallCastAnimationState.stop();
            this.spawnAnimationState.stop();
            this.idleAnimationState.stop();
            this.attackAnimationState1.stop();
            this.attackAnimationState2.stop();
            this.attackAnimationState3.stop();
            return;
        }

        if (this.handSwinging && this.handSwingTicks == 0) {
            this.attackAnimationState1.stop();
            this.attackAnimationState2.stop();
            this.attackAnimationState3.stop();
            int idx = attackAnimationCounter % 3;
            switch (idx) {
                case 0 -> { this.attackAnimationState1.start(this.age); this.attackAnimTicksRemaining = 18; }
                case 1 -> { this.attackAnimationState2.start(this.age); this.attackAnimTicksRemaining = 35; }
                default -> { this.attackAnimationState3.start(this.age); this.attackAnimTicksRemaining = 30; }
            }
            attackAnimationCounter++;
        } else if (this.attackAnimTicksRemaining > 0 && --this.attackAnimTicksRemaining <= 0) {
            this.attackAnimationState1.stop();
            this.attackAnimationState2.stop();
            this.attackAnimationState3.stop();
        }

        if (getInvulnerableTimer() > 0 && !hasSpawned()) {
            this.spawnAnimationState.startIfNotRunning(this.age);
        } else {
            this.spawnAnimationState.stop();
        }

        if (isScreeching()) {
            this.screechAnimationState.startIfNotRunning(this.age);
        } else {
            this.screechAnimationState.stop();
        }

        FrostMonarchAnimationTimes.Cast cast = isScreeching() ? FrostMonarchAnimationTimes.Cast.NONE : getCastAnimation();
        toggle(this.summonAnimationState, cast == FrostMonarchAnimationTimes.Cast.SUMMON);
        toggle(this.quickCastAnimationState, cast == FrostMonarchAnimationTimes.Cast.QUICK);
        toggle(this.wallCastAnimationState, cast == FrostMonarchAnimationTimes.Cast.WALL);

        if (!isScreeching() && cast == FrostMonarchAnimationTimes.Cast.NONE && (getInvulnerableTimer() <= 0 || hasSpawned())) {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.age);
            } else {
                --this.idleAnimationTimeout;
            }
        } else {
            this.idleAnimationState.stop();
            this.idleAnimationTimeout = 0;
        }
    }

    private void toggle(AnimationState state, boolean running) {
        if (running) {
            state.startIfNotRunning(this.age);
        } else {
            state.stop();
        }
    }

    public void onSummoned() {
        if (this.hasSpawned()) {
            return;
        }

        this.setInvulTimer(DEFAULT_INVUL_TIMER);
        if (!this.getWorld().isClient) {
            FrostMonarchLines.intro(this, DEFAULT_INVUL_TIMER);
        }
        this.getWorld().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                ModSounds.FROSTMONARCH_SPAWNED_LAUGH.soundEvent(),
                SoundCategory.HOSTILE,
                1.0f, 1.0f
        );
        this.getWorld().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                ModSounds.FROSTMONARCH_SPAWNED_STORM.soundEvent(),
                SoundCategory.HOSTILE,
                1.0f, 1.0f
        );
        this.bossBar.setPercent(0.0F);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    public int getInvulnerableTimer() {
        return (Integer)this.dataTracker.get(INVUL_TIMER);
    }

    public void setInvulTimer(int ticks) {
        this.dataTracker.set(INVUL_TIMER, ticks);
    }

    public int getBarrierTimer() {
        return (Integer)this.dataTracker.get(BARRIER_TIMER);
    }

    public void setBarrierTimer(int ticks) {
        this.dataTracker.set(BARRIER_TIMER, ticks);
    }

    static {
        INVUL_TIMER = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.INTEGER);
        BARRIER_TIMER = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Override
    public void setPose(EntityPose pose) {
        if (pose == EntityPose.DYING && this.isFakeDeath()) {
            super.setPose(EntityPose.STANDING);
        } else {
            super.setPose(pose);
        }
    }

    @Override
    public EntityPose getPose() {
        if (this.isFakeDeath()) {
            return EntityPose.STANDING;
        }
        return super.getPose();
    }

    protected SoundEvent getAmbientSound() {
        if (this.isFakeDeath()) {
            return null;
        }
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }

    @Override
    public void playAmbientSound() {
        SoundEvent sound = this.getAmbientSound();
        if (sound != null) {
            this.playSound(sound, 1.5F, 0.5F + (this.random.nextFloat() - 0.5F) * 0.1F);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_STRAY_STEP, 0.6F, 0.6F);
        this.playSound(SoundEvents.BLOCK_GLASS_STEP, 0.3F, 0.7F);
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        if (this.isFakeDeath()) {
            return null;
        }
        return ModSounds.FROSTMONARCH_HURT.soundEvent();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FROSTMONARCH_DEATH.soundEvent();
    }

}



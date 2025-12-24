package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.goals.frostmonarch.*;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

public class FrostMonarchEntity extends SkeletonEntity {
    private static final TrackedData<Integer> INVUL_TIMER;
    private static final TrackedData<Integer> BARRIER_TIMER;
    private static final TrackedData<Boolean> SCREECHING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> CASTING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_SPAWNED = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FAKE_DEATH = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final int DEFAULT_INVUL_TIMER = 220;
    private static final int DEATH_ANIMATION_DURATION = 200;
    private final ServerBossBar bossBar;
    private int fakeDeathTimer = 0;
    // Store the damage source that triggered death. Cannot be saved to NBT, will be null after reload.
    // Falls back to generic damage source if null.
    private DamageSource lastFatalDamageSource = null;

    // Animation States
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState screechAnimationState = new AnimationState();
    public final AnimationState summonAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public final AnimationState spawnAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public int screechCooldown = 0;
    public int hailStormCooldown = 0;
    public int icicleCooldown = 0;
    public int distanceIcicleCooldown = 0;
    public int meleeHitCounter = 0;
    public int globalAbilityCooldown = 0;
    private float particleAnimationProgress = 0.0F;


    public FrostMonarchEntity(EntityType<? extends FrostMonarchEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.setHealth(this.getMaxHealth());
        this.experiencePoints += 50;

        this.moveControl = new SmoothMoveControl(this);
    }

    public ItemStack getWeaponForDifficulty() {
        Difficulty difficulty = this.getWorld().getDifficulty();
        return switch (difficulty) {
            case PEACEFUL, EASY -> new ItemStack(Items.WOODEN_AXE);
            case NORMAL -> new ItemStack(Items.STONE_AXE);
            case HARD -> new ItemStack(Items.IRON_AXE);
        };
    }

    public Float getHealingForDifficulty() {
        Difficulty difficulty = this.getWorld().getDifficulty();
        return switch (difficulty) {
            case PEACEFUL, EASY -> 0.25F;
            case NORMAL -> 0.35F;
            case HARD -> 0.5F;
        };
    }

    public RegistryEntry<StatusEffect> getFreezingEffect() {
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            return MRPGCEffects.FROSTED.entry;
        }
        return Effects.FREEZING.registryEntry;
    }

    public static DefaultAttributeContainer.Builder createFrostmonarchAttributes() {
        // Base attributes (for Normal difficulty)
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2705)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
        nbt.putInt("BarrierTimer", this.getBarrierTimer());
        nbt.putBoolean("HasSpawned", this.hasSpawned());
        nbt.putBoolean("FakeDeath", this.isFakeDeath());
        nbt.putInt("FakeDeathTimer", this.fakeDeathTimer);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
        this.setBarrierTimer(nbt.getInt("BarrierTimer"));
        this.setHasSpawned(nbt.getBoolean("HasSpawned"));
        this.setFakeDeath(nbt.getBoolean("FakeDeath"));
        this.fakeDeathTimer = nbt.getInt("FakeDeathTimer");
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

        this.goalSelector.add(2, new ScreechGoal(this));
        this.goalSelector.add(3, new HailstormGoal(this));
        this.goalSelector.add(4, new DistanceIcicleGoal(this));
        this.goalSelector.add(4, new IcicleAttackGoal(this));

        this.goalSelector.add(6, new ConditionalGoal(this, new MeleeAttackGoal(this, 1.2, false)));

        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        // Target selectors - NOT wrapped in ConditionalGoal so boss can acquire targets during spawn
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(INVUL_TIMER, 0);
        builder.add(BARRIER_TIMER, 0);
        builder.add(SCREECHING, false);
        builder.add(CASTING, false);
        builder.add(HAS_SPAWNED, false);
        builder.add(FAKE_DEATH, false);
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

    /**
     * Check if any ability is currently active
     * @return true if casting, screeching, in barrier, during invulnerability, or within global cooldown
     */
    public boolean isPerformingAbility() {
        return this.isCasting() || this.isScreeching() || this.getBarrierTimer() > 0
            || this.getInvulnerableTimer() > 0 || this.globalAbilityCooldown > 0;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
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
        // Prevent rotation during fake death, real death, or spawn
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

        // prevent any movement during fake death
        if(this.isFakeDeath()) {
            return;
        }
        if (this.getInvulnerableTimer() > 0) {
            Random random = this.getWorld().random;
            int particlesPerTick = 70;
            float radius = 3.0F;

            for (int i = 0; i < particlesPerTick; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = radius * (0.5 + random.nextDouble() * 0.5);

                double px = this.getX() + Math.cos(angle) * distance;
                double py = this.getY() + random.nextDouble() * 3.0;
                double pz = this.getZ() + Math.sin(angle) * distance;

                double vx = -Math.sin(angle) * 0.5 + (random.nextDouble() - 0.5) * 0.2;
                double vy = 0.2 + random.nextDouble() * 0.2;
                double vz = Math.cos(angle) * 0.5 + (random.nextDouble() - 0.5) * 0.2;

                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
                if (random.nextFloat() < 0.2F) {
                    this.getWorld().addParticle(ParticleTypes.ITEM_SNOWBALL, px, py, pz, vx * 0.5, vy * 0.5, vz * 0.5);
                }
            }
        }else{
            this.particleAnimationProgress += 0.05F;

            if (this.getWorld().isClient) {
                if(!this.isOnFire()){
                    Random random = this.getWorld().random;

                    int particleCount = 8;
                    float sphereRadius = 3.0F;
                    float rotationSpeed = 2.5F;
                    float progress = this.particleAnimationProgress;

                    for (int i = 0; i < particleCount; i++) {
                        double theta = Math.acos(2.0 * random.nextDouble() - 1.0);
                        double phi = 2.0 * Math.PI * random.nextDouble() + progress * rotationSpeed;

                        double xOffset = sphereRadius * Math.sin(theta) * Math.cos(phi);
                        double yOffset = sphereRadius * Math.cos(theta);
                        double zOffset = sphereRadius * Math.sin(theta) * Math.sin(phi);

                        double vx = -xOffset * 0.05 + (random.nextDouble() - 0.5) * 0.05;
                        double vy = -yOffset * 0.05 + (random.nextDouble() - 0.5) * 0.05;
                        double vz = -zOffset * 0.05 + (random.nextDouble() - 0.5) * 0.05;

                        this.getWorld().addParticle(Particles.FREEZING_SNOWFLAKE,
                                this.getX() + xOffset,
                                this.getY() + 1.5 + yOffset,
                                this.getZ() + zOffset,
                                vx, vy, vz
                        );
                    }

                }
            }
        }
        super.tickMovement();
    }

    protected void mobTick() {
        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            this.getLookControl().lookAt(target, 30.0F, 30.0F);
        }

        int i;
        if (this.getInvulnerableTimer() > 0) {

            i = this.getInvulnerableTimer() - 1;
            // dynamic calculation based on spawn timer (220 ticks)
            float spawnProgress = 1.0F - (float)i / 220.0F;
            this.bossBar.setPercent(spawnProgress);
            if (i <= 0) {
                if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;
                // Mark as spawned so animation doesn't replay
                this.setHasSpawned(true);

                //GIVE WEAPON
                this.equipStack(EquipmentSlot.MAINHAND, getWeaponForDifficulty());
                ///FROST EXPLOSION PARTICLES
                this.getWorld().playSound(
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        ModSounds.FROSTMONARCH_DEEP_FREEZE.soundEvent(),
                        SoundCategory.HOSTILE,
                        0.75f, 1.0f
                );
                int particleCount = 150;
                double speed = 2.5;
                for (int u= 0; u < particleCount; u++) {
                    double angle = 2 * Math.PI * u / particleCount;
                    double xSpeed = Math.cos(angle) * speed;
                    double zSpeed = Math.sin(angle) * speed;
                    double ySpeed = 0.05 + this.getWorld().random.nextDouble() * 0.1;

                    serverWorld.spawnParticles(
                            Particles.FREEZING_SNOWFLAKE,
                            this.getPos().x,
                            this.getPos().y + 1.0,
                            this.getPos().z,
                            1, 0, 0, 0, 0);
                    serverWorld.spawnParticles(
                            Particles.FREEZING_SNOWFLAKE,
                            this.getPos().x,
                            this.getPos().y + 1.0,
                            this.getPos().z,
                            0, xSpeed, ySpeed, zSpeed, 1.0);
                }
                //// FREEZE NEARBY ENTITIES ON SPAWN
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
                if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
                    effectEntry = MRPGCEffects.FROZEN_SOLID.entry;
                }
                for (LivingEntity livingEntity : livingEntities) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(
                            effectEntry, 100, 0
                    ));
                }

            }

            this.setInvulTimer(i);
            if (this.age % 10 == 0) {
                float healAmount = this.getMaxHealth() / 22.0F;
                this.heal(healAmount);
            }

        } else {
            super.mobTick(); //Let normal mob AI run when not spawning
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
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData entityData2 = super.initialize(world, difficulty, spawnReason, entityData);
        // If NOT spawned with blocks, mark as already spawned
        if (spawnReason != SpawnReason.STRUCTURE) {
            this.setHasSpawned(true);
        }

        Difficulty worldDifficulty = world.getDifficulty();
        float difficultyMultiplier = getDifficultyMultiplier(worldDifficulty);

        double baseHealth = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue();
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(baseHealth * difficultyMultiplier);

        double baseAttackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getBaseValue();
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(baseAttackDamage * difficultyMultiplier);

        double baseArmor = this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).getBaseValue();
        this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(baseArmor * difficultyMultiplier);

        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        if(FabricLoader.getInstance().isModLoaded("spell_power")){
            double baseFrostPower = 7.0;
            this.getAttributeInstance(SpellSchools.FROST.attributeEntry).setBaseValue(baseFrostPower * difficultyMultiplier);
        }

        this.updateAttackType();
        return entityData2;
    }

    private float getDifficultyMultiplier(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 0.75F;
            case NORMAL -> 1.0F;
            case HARD -> 1.25F;
            default -> 1.0F;
        };
    }

    @Override
    public boolean tryAttack(Entity target) {
        // Prevent attacking during ice barrier
        if (this.getBarrierTimer() > 0) {
            return false;
        }

        boolean success = super.tryAttack(target);
        if (success) {
            if (target instanceof LivingEntity entity) {
                applyStatusEffect(entity,0,6, getFreezingEffect(),2,
                        true,true,true,0);

                meleeHitCounter++;
                this.setAttacking(true);

                // After 6 hits, trigger frost explosion
                int hitsForExplosion = 6;
                if (meleeHitCounter >= hitsForExplosion) {
                    triggerFrostExplosion();
                    meleeHitCounter = 0;
                }
            }
        }
        return success;
    }

    private void triggerFrostExplosion() {
        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld serverWorld) {
            // Create frost explosion particles
            double radius = 4.0;
            int particleCount = 100;
            double speed = 2.0;

            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double xSpeed = Math.cos(angle) * speed;
                double zSpeed = Math.sin(angle) * speed;
                double ySpeed = 0.05 + this.random.nextDouble() * 0.1;

                serverWorld.spawnParticles(
                        Particles.FREEZING_SNOWFLAKE,
                        this.getPos().x,
                        this.getPos().y + 1.0,
                        this.getPos().z,
                        0, xSpeed, ySpeed, zSpeed, 1.0
                );
            }

            // Damage and freeze nearby entities
            List<LivingEntity> nearbyEntities = this.getWorld().getEntitiesByClass(
                    LivingEntity.class,
                    new Box(
                            this.getPos().x - radius, this.getPos().y - radius, this.getPos().z - radius,
                            this.getPos().x + radius, this.getPos().y + radius, this.getPos().z + radius
                    ),
                    livingEntity -> livingEntity != this && livingEntity.isAlive() && livingEntity.squaredDistanceTo(this.getPos()) <= radius * radius
            );

            for (LivingEntity entity : nearbyEntities) {
                entity.damage(
                        this.getDamageSources().indirectMagic(this, this),
                        (float) (this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2)
                );
                applyStatusEffect(entity,0,6, getFreezingEffect(),10,
                        true,true,true,0);
            }
        }
    }

    @Override
    public void heal(float amount) {
        super.heal(amount);
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (this.isFakeDeath() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (this.getInvulnerableTimer() > 0 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (this.getBarrierTimer() > 0 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
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

        // Check if this damage would kill the entity
        if (!this.getWorld().isClient && !this.isFakeDeath() && this.getHealth() - amount <= 0.0F) {
            this.setHealth(0.0F);
            this.dead = true;
            this.onDeath(source);
            return true;
        }

        return super.damage(source, amount);
    }

    private void triggerFakeDeath(DamageSource damageSource) {
        // Store the damage source for fake death animation
        this.lastFatalDamageSource = damageSource;
        //Stop all movement immediately
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
        this.fakeDeathTimer = DEATH_ANIMATION_DURATION;

        this.setHealth(0.5F);

        this.dead = false;

        this.deathTime = 0;

        this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());

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

                        // Drop experience orbs
                        this.dropExperience(serverWorld, deathSource.getAttacker());
                    }
                    this.setNoGravity(false);
                    this.setHealth(0.0F);
                    this.dead = true;
                    this.remove(RemovalReason.KILLED);
                }
            }

            // Handle client-side death animation
            if (this.getWorld().isClient) {
                setupAnimationStates();
            }
            return; // Don't process normal tick logic during fake death
        }

        if (this.deathTime > 0) {
            this.hurtTime = 0;
        }

        // Update cooldowns
        if (screechCooldown > 0) screechCooldown--;
        if (hailStormCooldown > 0) hailStormCooldown--;
        if (icicleCooldown > 0) icicleCooldown--;
        if (distanceIcicleCooldown > 0) distanceIcicleCooldown--;
        if (globalAbilityCooldown > 0) globalAbilityCooldown--;

        if (isCasting() || isScreeching() || getBarrierTimer() > 0) {
            this.setVelocity(Vec3d.ZERO);
            this.velocityModified = true;
        }

        // Handle client-side animations
        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.deathTime > 0 || this.isFakeDeath()) {
            this.deathAnimationState.startIfNotRunning(this.age);

            this.screechAnimationState.stop();
            this.summonAnimationState.stop();
            this.spawnAnimationState.stop();
            this.idleAnimationState.stop();
            return; // Don't process other animations
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

        // Play summon animation when casting icicles
        if (isCasting() && !isScreeching()) {
            this.summonAnimationState.startIfNotRunning(this.age);
        } else {
            this.summonAnimationState.stop();
        }

        if (!isScreeching() && !isCasting()  && (getInvulnerableTimer() <= 0 || hasSpawned())) {
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

    public void onSummoned() {
        if (this.hasSpawned()) {
            return;
        }

        this.setInvulTimer(220);
        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld serverWorld) {
            Text message = Text.translatable("entity.loot_n_explore.frost_monarch.spawn_message");
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                player.sendMessage(message, true);
            }
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
        return ModSounds.FROSTMONARCH_DEATH.soundEvent();
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

    static class SmoothMoveControl extends MoveControl {
        public SmoothMoveControl(FrostMonarchEntity entity) {
            super(entity);
        }

        @Override
        protected float wrapDegrees(float from, float to, float max) {
            float f = net.minecraft.util.math.MathHelper.wrapDegrees(to - from);
            if (f > 30.0F) {
                f = 30.0F;
            }
            if (f < -30.0F) {
                f = -30.0F;
            }
            float g = from + f;
            if (g < 0.0F) {
                g += 360.0F;
            } else if (g > 360.0F) {
                g -= 360.0F;
            }
            return g;
        }
    }

}



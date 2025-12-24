package more_rpg_loot.entity.mob;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.goals.frostmonarch.CallServantsGoal;
import more_rpg_loot.entity.goals.frostmonarch.ConditionalGoal;
import more_rpg_loot.entity.goals.frostmonarch.ScreechGoal;
import more_rpg_loot.entity.goals.frostmonarch.StayStillWhenServantsAliveGoal;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.*;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;
import static more_rpg_loot.util.HelperMethods.stackFreezeStacks;

public class FrostMonarchEntity extends SkeletonEntity {
    private static final TrackedData<Integer> INVUL_TIMER;
    private static final TrackedData<Boolean> SCREECHING = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final int DEFAULT_INVUL_TIMER = 220;
    private final ServerBossBar bossBar;
    public int callServantsCooldown = 0;
    public int callServantsMax = 4;
    public int screechCooldown = 0;
    public int hailStormCooldown = 0;
    private float particleAnimationProgress = 0.0F;


    public FrostMonarchEntity(EntityType<? extends FrostMonarchEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0F);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.setHealth(this.getMaxHealth());
        this.experiencePoints += 50;
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
            case PEACEFUL, EASY -> 0.1F;
            case NORMAL -> 0.2F;
            case HARD -> 0.3F;
        };
    }

    public static DefaultAttributeContainer.Builder createFrostmonarchAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2605)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
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
        this.goalSelector.add(0, new CallServantsGoal(this));
        this.goalSelector.add(1, new StayStillWhenServantsAliveGoal(this));
        this.goalSelector.add(2, new ScreechGoal(this));
        this.goalSelector.add(3, new ConditionalGoal(this, new MeleeAttackGoal(this, 1.2, false)));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ConditionalGoal(this, new ActiveTargetGoal<>(this, PlayerEntity.class, true)));
        this.targetSelector.add(3, new ConditionalGoal(this, new ActiveTargetGoal<>(this, IronGolemEntity.class, true)));
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(INVUL_TIMER, 0);
        builder.add(SCREECHING,false);
    }
    public boolean isScreeching() {
        return this.dataTracker.get(SCREECHING);
    }

    public void setScreeching(boolean value) {
        this.dataTracker.set(SCREECHING, value);
    }

    public boolean hasServants() {
        List<FrostMonarchServantEntity> servants = this.getWorld().getNonSpectatingEntities(
                FrostMonarchServantEntity.class,
                this.getBoundingBox().expand(32.0)
        );
        return servants.stream().anyMatch(FrostMonarchServantEntity::isAlive);
    }
    public boolean canHeal(){
        return this.getMaxHealth() != this.getHealth() && !this.isOnFire() && this.hasServants() && this.getInvulnerableTimer() == 0;
    }


    public void tickMovement() {
        if(this.getInvulnerableTimer() > 0 || this.canHeal()){
            this.setYaw(this.bodyYaw);
            this.setHeadYaw(this.bodyYaw);
            this.prevYaw = this.bodyYaw;
            this.prevHeadYaw = this.bodyYaw;
            this.setVelocity(Vec3d.ZERO);
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
            List<FrostMonarchServantEntity> list = this.getWorld().getNonSpectatingEntities(FrostMonarchServantEntity.class, this.getBoundingBox().expand(32.0));
            int servantsCount = list.size();
            if (this.getWorld().isClient) {
                if(!this.isOnFire()){
                    if(this.canHeal()){
                        for (LivingEntity target : list) {
                            Vec3d from = new Vec3d(this.getX(), this.getY() + this.getHeight() / 2, this.getZ());
                            Vec3d to = new Vec3d(target.getX(), target.getY() + target.getHeight() / 2, target.getZ());

                            Vec3d delta = to.subtract(from);
                            int steps = 10 + servantsCount;
                            long time = this.age;

                            for (int i = 0; i <= steps; i++) {
                                double t = i / (double) steps;
                                Vec3d point = from.add(delta.multiply(t));
                                double wave = Math.sin(time * 0.3 + t * 10.0) * 0.1;
                                Vec3d offset = delta.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(wave);
                                Vec3d finalPos = point.add(offset);
                                this.getWorld().addParticle(
                                        ParticleTypes.SCULK_SOUL,
                                        finalPos.x, finalPos.y, finalPos.z,
                                        0, 0, 0
                                );
                            }
                        }
                    }
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
        int i;
        if (this.getInvulnerableTimer() > 0) {
            i = this.getInvulnerableTimer() - 1;
            this.bossBar.setPercent(1.0F - (float)i / 220.0F);
            if (i <= 0) {
                if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;
                //GIVE AXE
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
                this.heal(10.0F);
            }

        } else {
            List<FrostMonarchServantEntity> list = this.getWorld().getNonSpectatingEntities(FrostMonarchServantEntity.class, this.getBoundingBox().expand(32.0));
            int servantsCount = list.size();
            if(canHeal()){
                this.heal(servantsCount * this.getHealingForDifficulty());
                for(Entity entities : list){
                    entities.damage(entities.getDamageSources().magic(),0.2F);
                }
            }
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
        super.dropEquipment(world, source, causedByPlayer);
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
        if(FabricLoader.getInstance().isModLoaded("thermoo")){
            this.getAttributeInstance(ThermooAttributes.MIN_TEMPERATURE).setBaseValue(5.0);
            this.getAttributeInstance(ThermooAttributes.FROST_RESISTANCE).setBaseValue(10.0);
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0);
        this.updateAttackType();
        return entityData2;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        if (success) {
            if (target instanceof LivingEntity entity) {
                stackFreezeStacks(entity, 20);
            }
        }
        return success;
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.getInvulnerableTimer() > 0 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if(canHeal() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)){
            return false;
        } else{
            if(source.isIn(DamageTypeTags.IS_FIRE) && !this.isInLava()){
                return false;
            }
        }
        if (!this.getWorld().isClient) {
            if(!source.isIn(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !source.isOf(DamageTypes.THORNS)){
                Entity attacker = source.getSource();
                if (attacker instanceof LivingEntity livingEntity) {
                    applyStatusEffect(livingEntity,0,4, Effects.FREEZING.registryEntry,1,
                            true,true,true,1);
                }
            }
        }
        return super.damage(source, amount);
    }

    private boolean callingServants;
    private int callAnimationTicks;

    public void startCallingServants() {
        this.callingServants = true;
        this.callAnimationTicks = 20;
    }

    @Override
    public void tick() {
        super.tick();
        if (callAnimationTicks > 0) {
            callAnimationTicks--;
        } else {
            callingServants = false;
        }
        if (screechCooldown > 0) screechCooldown--;
        if (hailStormCooldown > 0) hailStormCooldown--;
        if (callServantsCooldown > 0) callServantsCooldown--;
    }

    public void onSummoned() {
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

    static {
        INVUL_TIMER = DataTracker.registerData(FrostMonarchEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.FROSTMONARCH_DEATH.soundEvent();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.FROSTMONARCH_HURT.soundEvent();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FROSTMONARCH_DEATH.soundEvent();
    }

}



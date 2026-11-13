package lne.entity.frozen_depths.conjured;

import java.util.List;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import lne.LneMod;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import com.mrpg_lib.util.AllyHelper;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.fx.LNEFx;
import lne.util.damage.LneDamageTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EternalWinterPortalEntity extends HostileEntity {
    private static final TrackedData<Integer> LIFETIME = DataTracker.registerData(EternalWinterPortalEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SPAWN_INTERVAL = DataTracker.registerData(EternalWinterPortalEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> FIRST_SPAWN_DELAY = DataTracker.registerData(EternalWinterPortalEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final int DEFAULT_LIFETIME = 400;
    public static final int DEFAULT_FIRST_SPAWN_DELAY = 20;
    public static final int DEFAULT_SPAWN_INTERVAL = 100;
    private static final int CHARGE_TICKS = 30;
    private static final int HUM_INTERVAL = 40;
    private static final double TARGET_SEARCH_RANGE = 32.0;
    private static final Identifier SUMMON_STRENGTH = Identifier.of(LneMod.MOD_ID, "portal_summon_strength");

    @Nullable private UUID ownerUuid;
    @Nullable private LivingEntity owner;
    private Identifier summonType = Registries.ENTITY_TYPE.getId(ModEntities.FROST_HAUNTED);
    private int hitSoundCooldown;
    private int shieldTicks;
    private double summonStrength;
    @Nullable private String summonTag;

    public EternalWinterPortalEntity(EntityType<? extends EternalWinterPortalEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.experiencePoints = 0;
    }

    public EternalWinterPortalEntity(World world, @Nullable LivingEntity owner, EntityType<?> summonType, int lifetime, int firstSpawnDelay, int spawnInterval) {
        this(ModEntities.ETERNAL_WINTER_PORTAL, world);
        this.owner = owner;
        this.ownerUuid = owner != null ? owner.getUuid() : null;
        this.summonType = Registries.ENTITY_TYPE.getId(summonType);
        this.dataTracker.set(LIFETIME, Math.max(1, lifetime));
        this.dataTracker.set(FIRST_SPAWN_DELAY, Math.max(1, firstSpawnDelay));
        this.dataTracker.set(SPAWN_INTERVAL, Math.max(CHARGE_TICKS, spawnInterval));
    }

    public EternalWinterPortalEntity shielded(int ticks) {
        this.shieldTicks = ticks;
        return this;
    }

    public EternalWinterPortalEntity empowering(double strength, @Nullable String tag) {
        this.summonStrength = strength;
        this.summonTag = tag;
        return this;
    }

    private boolean isShielded() {
        return this.age < shieldTicks;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LIFETIME, DEFAULT_LIFETIME);
        builder.add(FIRST_SPAWN_DELAY, DEFAULT_FIRST_SPAWN_DELAY);
        builder.add(SPAWN_INTERVAL, DEFAULT_SPAWN_INTERVAL);
    }

    public int getLifetime() {
        return this.dataTracker.get(LIFETIME);
    }

    public int getSpawnInterval() {
        return this.dataTracker.get(SPAWN_INTERVAL);
    }

    public int getFirstSpawnDelay() {
        return this.dataTracker.get(FIRST_SPAWN_DELAY);
    }

    private int ticksUntilRelease() {
        int first = getFirstSpawnDelay();
        if (this.age <= first) {
            return first - this.age;
        }
        int interval = getSpawnInterval();
        return (interval - (this.age - first) % interval) % interval;
    }

    @Override
    protected void initGoals() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            int untilRelease = ticksUntilRelease();
            boolean charging = untilRelease < CHARGE_TICKS && this.age + untilRelease < getLifetime();
            if (this.age % (charging ? 1 : 2) == 0) {
                MrpgFx.playLocal(LNEFx.ETERNAL_WINTER_PORTAL, FxParams.on(this));
            }
            return;
        }
        if (hitSoundCooldown > 0) {
            hitSoundCooldown--;
        }
        if (ownerGone()) {
            close(false);
            return;
        }
        this.checkBlockCollision();
        if (this.isRemoved()) {
            return;
        }
        if (this.getFireTicks() > 0) {
            if (isShielded()) {
                this.extinguish();
            } else {
                close(true);
                return;
            }
        }
        if (this.age % HUM_INTERVAL == 1) {
            sound(SoundEvents.BLOCK_BEACON_AMBIENT, 0.8F, 0.6F);
        }
        if (this.age >= getLifetime()) {
            close(false);
            return;
        }
        if (ticksUntilRelease() == 0) {
            release();
        }
    }

    private boolean ownerGone() {
        if (ownerUuid == null) {
            return false;
        }
        if (owner == null && this.getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(ownerUuid) instanceof LivingEntity living) {
            owner = living;
        }
        if (owner == null) {
            return this.age > 20;
        }
        return !owner.isAlive() || owner.isRemoved() || owner instanceof FrostMonarchEntity monarch && monarch.isFakeDeath();
    }

    private void release() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            EntityType<?> type = Registries.ENTITY_TYPE.getOrEmpty(summonType).orElse(null);
            if (type != null && type.create(serverWorld) instanceof MobEntity mob) {
                mob.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                mob.initialize(serverWorld, serverWorld.getLocalDifficulty(mob.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
                empower(mob);
                LivingEntity target = summonTarget(serverWorld);
                if (target != null) {
                    mob.setTarget(target);
                }
                serverWorld.spawnEntityAndPassengers(mob);
            }
            MrpgFx.play(serverWorld, LNEFx.ETERNAL_WINTER_PORTAL_RELEASE, FxParams.at(this.getPos().add(0, this.getHeight() * 0.5, 0)));
        }
    }

    private void empower(MobEntity mob) {
        if (summonTag != null) {
            mob.addCommandTag(summonTag);
        }
        if (summonStrength <= 0) {
            return;
        }
        for (var attribute : List.of(EntityAttributes.GENERIC_MAX_HEALTH, EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            var instance = mob.getAttributeInstance(attribute);
            if (instance != null) {
                instance.addPersistentModifier(new EntityAttributeModifier(SUMMON_STRENGTH, summonStrength, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
        mob.setHealth(mob.getMaxHealth());
    }

    @Nullable
    private LivingEntity summonTarget(ServerWorld world) {
        if (owner instanceof MobEntity ownerMob && ownerMob.getTarget() != null && ownerMob.getTarget().isAlive()) {
            return ownerMob.getTarget();
        }
        PlayerEntity player = world.getClosestPlayer(this.getX(), this.getY(), this.getZ(), TARGET_SEARCH_RANGE,
                candidate -> candidate instanceof PlayerEntity p && !p.isSpectator() && !p.isCreative());
        return player;
    }

    public void close(boolean byFire) {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            MrpgFx.play(serverWorld, byFire ? LNEFx.ETERNAL_WINTER_PORTAL_CLOSE : LNEFx.ETERNAL_WINTER_PORTAL_FADE,
                    FxParams.at(this.getPos().add(0, this.getHeight() * 0.5, 0)));
        }
        this.discard();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getWorld().isClient || this.isRemoved()) {
            return false;
        }
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.damage(source, amount);
        }
        if (source.isIn(LneDamageTags.CLOSES_ETERNAL_WINTER_PORTAL) && !isShielded()) {
            close(true);
            return true;
        }
        Entity attacker = source.getAttacker();
        if (attacker == null || attacker == owner || owner != null && AllyHelper.canHelp(owner, attacker)) {
            return false;
        }
        if (attacker instanceof PlayerEntity && hitSoundCooldown <= 0) {
            hitSoundCooldown = 5;
            sound(SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, 1.0F, 0.6F);
        }
        return true;
    }

    private void sound(SoundEvent sound, float volume, float pitch) {
        this.getWorld().playSound(null, this.getX(), this.getY() + this.getHeight() * 0.5, this.getZ(), sound, SoundCategory.HOSTILE, volume, pitch);
    }

    @Override
    public void tickMovement() {
        this.setVelocity(Vec3d.ZERO);
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public boolean isAffectedBySplashPotions() {
        return false;
    }

    @Override
    public boolean canUsePortals(boolean allowVehicles) {
        return false;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Lifetime", getLifetime());
        nbt.putInt("SpawnInterval", getSpawnInterval());
        nbt.putInt("FirstSpawnDelay", getFirstSpawnDelay());
        nbt.putInt("PortalAge", this.age);
        nbt.putString("SummonType", summonType.toString());
        nbt.putInt("ShieldTicks", shieldTicks);
        nbt.putDouble("SummonStrength", summonStrength);
        if (summonTag != null) {
            nbt.putString("SummonTag", summonTag);
        }
        if (ownerUuid != null) {
            nbt.putUuid("Owner", ownerUuid);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Lifetime")) {
            this.dataTracker.set(LIFETIME, Math.max(1, nbt.getInt("Lifetime")));
        }
        if (nbt.contains("SpawnInterval")) {
            this.dataTracker.set(SPAWN_INTERVAL, Math.max(CHARGE_TICKS, nbt.getInt("SpawnInterval")));
        }
        if (nbt.contains("FirstSpawnDelay")) {
            this.dataTracker.set(FIRST_SPAWN_DELAY, Math.max(1, nbt.getInt("FirstSpawnDelay")));
        }
        this.age = nbt.getInt("PortalAge");
        Identifier type = Identifier.tryParse(nbt.getString("SummonType"));
        if (type != null && Registries.ENTITY_TYPE.containsId(type)) {
            this.summonType = type;
        }
        this.shieldTicks = nbt.getInt("ShieldTicks");
        this.summonStrength = nbt.getDouble("SummonStrength");
        this.summonTag = nbt.contains("SummonTag") ? nbt.getString("SummonTag") : null;
        this.ownerUuid = nbt.containsUuid("Owner") ? nbt.getUuid("Owner") : null;
        this.owner = null;
    }
}

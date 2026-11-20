package lne.entity.frozen_depths.projectile;

import com.mrpg_lib.sounds.MRPGLibSounds;
import com.mrpg_lib.util.AllyHelper;
import lne.particle.Particles;
import lne.util.damage.FrostDamageProfiles;
import com.mrpg_lib.util.damage.SpellDamage;

import lne.internal.ability.AbilityDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static lne.util.HelperMethods.applyStatusEffect;
import static lne.util.HelperMethods.getFreezingEffect;

public class StraightIcicleEntity extends Entity implements Ownable {
    private static final TrackedData<Integer> WARMUP = DataTracker.registerData(StraightIcicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TELEGRAPH = DataTracker.registerData(StraightIcicleEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final int WARMUP_TICKS = 10;
    public static final int ATTACK_TICK = 20;
    public static final int LIFETIME_TICKS = 28;
    public static final int DAMAGE_COOLDOWN = 10;

    private int warmup;
    private boolean startedAttack;
    private int ticksLeft;
    private boolean playingAnimation;
    private boolean eruptSound;
    private LivingEntity owner;
    private UUID ownerUuid;
    private int currentTick = 0;
    private Map<UUID, Integer> lastDamageTick = new HashMap<>();
    private float fixedDamage = -1.0F;
    private float fixedDamageScaling = 0.0F;

    public StraightIcicleEntity(EntityType<? extends StraightIcicleEntity> entityType, World world) {
        super(entityType, world);
        this.ticksLeft = LIFETIME_TICKS;
    }

    public StraightIcicleEntity(World world, double x, double y, double z, float yaw, int warmup, LivingEntity owner) {
        this(lne.entity.ModEntities.STRAIGHT_ICICLE, world);
        this.setWarmup(warmup);
        this.setOwner(owner);
        this.setYaw(yaw * 57.295776F);
        this.setPosition(x, y, z);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(WARMUP, 0);
        builder.add(TELEGRAPH, 0);
    }

    private void setWarmup(int warmup) {
        this.warmup = warmup;
        this.dataTracker.set(WARMUP, warmup);
    }

    public StraightIcicleEntity withTelegraph(int ticks) {
        if (ticks > 0) {
            this.setWarmup(this.warmup + ticks);
            this.dataTracker.set(TELEGRAPH, ticks);
        }
        return this;
    }

    public StraightIcicleEntity withEruptSound() {
        this.eruptSound = true;
        return this;
    }

    public void configureFixedDamage(float base, float scaling) {
        this.fixedDamage = base;
        this.fixedDamageScaling = scaling;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld serverWorld) {
            Entity entity = serverWorld.getEntity(this.ownerUuid);
            if (entity instanceof LivingEntity livingEntity) {
                this.owner = livingEntity;
            }
        }
        return this.owner;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.setWarmup(nbt.getInt("Warmup"));
        this.dataTracker.set(TELEGRAPH, nbt.getInt("Telegraph"));
        this.eruptSound = nbt.getBoolean("EruptSound");
        this.fixedDamage = nbt.contains("FixedDamage") ? nbt.getFloat("FixedDamage") : -1.0F;
        this.fixedDamageScaling = nbt.getFloat("FixedDamageScaling");
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Warmup", this.warmup);
        nbt.putInt("Telegraph", this.dataTracker.get(TELEGRAPH));
        nbt.putBoolean("EruptSound", this.eruptSound);
        nbt.putFloat("FixedDamage", this.fixedDamage);
        nbt.putFloat("FixedDamageScaling", this.fixedDamageScaling);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            if (this.playingAnimation) {
                --this.ticksLeft;
            } else if (this.isTelegraphing()) {
                spawnTelegraphParticles();
            }
        } else {
            currentTick++;

            if (this.eruptSound && this.warmup == this.dataTracker.get(TELEGRAPH) && this.warmup > 0) {
                playWorldSound(MRPGLibSounds.FROST_CRACKLE.soundEvent(), 0.6F, 1.4F);
            }

            if (--this.warmup < 0) {
                if (this.warmup >= -(ATTACK_TICK + 3) && this.warmup <= -(ATTACK_TICK - 3)) {
                    this.dealDamage();
                }

                if (!this.startedAttack) {
                    this.getWorld().sendEntityStatus(this, (byte)4);
                    this.startedAttack = true;
                    if (this.eruptSound) {
                        playWorldSound(MRPGLibSounds.GLACIAL_SPIKES.soundEvent(), 0.8F, 0.9F + this.random.nextFloat() * 0.2F);
                    }
                }

                if (--this.ticksLeft < 0) {
                    this.discard();
                }
            }
        }
    }

    private void playWorldSound(SoundEvent sound, float volume, float pitch) {
        if (!this.isSilent()) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
        }
    }

    public boolean hasTelegraph() {
        return this.dataTracker.get(TELEGRAPH) > 0;
    }

    public boolean isTelegraphing() {
        return !this.playingAnimation && this.hasTelegraph() && this.dataTracker.get(WARMUP) - this.age <= this.dataTracker.get(TELEGRAPH);
    }

    private void spawnTelegraphParticles() {
        if (this.age % 2 != 0) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * 0.9;
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * 0.9;
            this.getWorld().addParticle(Particles.FREEZING_SNOWFLAKE, x, this.getY() + 0.1, z, 0.0, 0.04 + this.random.nextDouble() * 0.04, 0.0);
        }
        if (this.random.nextInt(3) == 0) {
            this.getWorld().addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY() + 0.1, this.getZ(), 0.0, 0.05, 0.0);
        }
    }

    private void dealDamage() {
        List<LivingEntity> targets = this.getWorld().getNonSpectatingEntities(
            LivingEntity.class,
            this.getBoundingBox().expand(1.2, 0.5, 1.2)
        );

        LivingEntity owner = this.getOwner();

        for (LivingEntity target : targets) {
            if (!AllyHelper.canHurt(owner, target)) {
                continue;
            }

            UUID targetId = target.getUuid();
            Integer lastTick = lastDamageTick.get(targetId);

            if (lastTick == null || currentTick - lastTick >= DAMAGE_COOLDOWN) {
                this.damage(target);
                lastDamageTick.put(targetId, currentTick);
            }
        }
    }

    private void damage(LivingEntity target) {
        LivingEntity owner = this.getOwner();

        if (target.isAlive() && !target.isInvulnerable()) {
            if (owner == null) {
                target.damage(this.getDamageSources().freeze(), 6.0F);
            } else {
                float damage = calculateDamage(owner);

                DamageSource damageSource = this.getDamageSources().indirectMagic(this, owner);
                target.damage(damageSource, damage);

                applyStatusEffect(target,0,5, getFreezingEffect(),10,
                        true,true,true,1);
            }
        }
    }

    private float calculateDamage(LivingEntity owner) {
        if (fixedDamage >= 0.0F) {
            return AbilityDamage.scaled(owner, fixedDamage, fixedDamageScaling);
        }
        return SpellDamage.calculate(owner, FrostDamageProfiles.GROUND_ICICLE);
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == 4) {
            this.playingAnimation = true;
        }
    }

    public float getAnimationProgress(float tickDelta) {
        if (!this.playingAnimation) {
            return 0.0F;
        }

        int elapsed = LIFETIME_TICKS - this.ticksLeft;
        return Math.min(1.0F, ((float)elapsed - tickDelta) / (float)LIFETIME_TICKS);
    }

}

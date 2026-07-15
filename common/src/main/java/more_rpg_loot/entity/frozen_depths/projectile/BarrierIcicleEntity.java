package more_rpg_loot.entity.frozen_depths.projectile;

import more_rpg_loot.effects.Effects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_power.api.SpellSchools;

import java.util.UUID;

import static more_rpg_loot.util.HelperMethods.spawnCloudEntity;

public class BarrierIcicleEntity extends Entity implements Ownable {
    // Animation phases
    public static final int RISE_DURATION = 10;
    public static final int RETRACT_DURATION = 10;

    private int age = 0;
    private int maxLifetime;
    private LivingEntity owner;
    private UUID ownerUuid;
    private boolean isRetracting = false;
    private boolean hasSpawnedCloud = false;

    public BarrierIcicleEntity(EntityType<? extends BarrierIcicleEntity> entityType, World world) {
        super(entityType, world);
        this.maxLifetime = 120;
        this.noClip = false;
    }

    public BarrierIcicleEntity(World world, double x, double y, double z, int duration, LivingEntity owner) {
        this(more_rpg_loot.entity.ModEntities.BARRIER_ICICLE, world);
        this.setOwner(owner);
        this.setPosition(x, y, z);
        this.maxLifetime = duration;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
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
        this.age = nbt.getInt("Age");
        this.maxLifetime = nbt.getInt("MaxLifetime");
        this.isRetracting = nbt.getBoolean("IsRetracting");
        this.hasSpawnedCloud = nbt.getBoolean("HasSpawnedCloud");
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Age", this.age);
        nbt.putInt("MaxLifetime", this.maxLifetime);
        nbt.putBoolean("IsRetracting", this.isRetracting);
        nbt.putBoolean("HasSpawnedCloud", this.hasSpawnedCloud);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;

        if (!this.getWorld().isClient) {
            int retractStartAge = maxLifetime - RETRACT_DURATION;

            if (this.age >= retractStartAge && !isRetracting) {
                isRetracting = true;
                this.getWorld().sendEntityStatus(this, (byte)5);
            }

            if (this.age == RISE_DURATION && !hasSpawnedCloud) {
                spawnFreezingCloud();
                hasSpawnedCloud = true;
            }

            if (this.age >= maxLifetime) {
                this.discard();
            }
        }
    }

    private void spawnFreezingCloud() {
        LivingEntity owner = this.getOwner();
        if (owner == null) {
            return;
        }

        float damageAmount = calculateDamage(owner);

        // Calculate cloud duration based on remaining icicle lifetime
        int cloudDurationTicks = (maxLifetime - RISE_DURATION - RETRACT_DURATION);
        int cloudDurationSeconds = cloudDurationTicks / 20;

        spawnCloudEntity(
            ParticleTypes.SNOWFLAKE, owner, this, 0, 1.8F,
            cloudDurationSeconds, 1.8F, getFreezingEffect(), 1, 0,
            false, 0, true, damageAmount, this.getDamageSources().freeze()
        );
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == 5) {
            this.isRetracting = true;
        }
    }

    private float calculateDamage(LivingEntity owner) {
        float damage = (float) (owner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5);

        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            try {
                double frostPower = owner.getAttributeValue(SpellSchools.FROST.attributeEntry);
                damage += (float) (frostPower * 0.3);
            } catch (Exception e) {
            }
        }

        return Math.max(4.0F, damage);
    }

    public float getAnimationProgress(float tickDelta) {
        float totalAge = this.age + tickDelta;

        if (totalAge <= RISE_DURATION) {
            return Math.min(1.0F, totalAge / RISE_DURATION);
        } else if (isRetracting) {
            int retractStartAge = maxLifetime - RETRACT_DURATION;
            float retractProgress = (totalAge - retractStartAge) / RETRACT_DURATION;
            retractProgress = Math.min(1.0F, Math.max(0.0F, retractProgress));
            return 1.0F - retractProgress;
        } else {
            return 1.0F;
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }
    public RegistryEntry<StatusEffect> getFreezingEffect() {
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            return MRPGCEffects.FROSTED.entry;
        }
        return Effects.FREEZING.registryEntry;
    }
}

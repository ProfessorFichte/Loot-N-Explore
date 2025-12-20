package more_rpg_loot.entity.projectile;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_power.api.SpellSchools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;

public class StraightIcicleEntity extends Entity implements Ownable {
    // Animation timing
    public static final int WARMUP_TICKS = 10;
    public static final int ATTACK_TICK = 20;
    public static final int LIFETIME_TICKS = 28;
    public static final int DAMAGE_COOLDOWN = 10;

    private int warmup;
    private boolean startedAttack;
    private int ticksLeft;
    private boolean playingAnimation;
    private LivingEntity owner;
    private UUID ownerUuid;
    private int currentTick = 0;
    private Map<UUID, Integer> lastDamageTick = new HashMap<>();

    public StraightIcicleEntity(EntityType<? extends StraightIcicleEntity> entityType, World world) {
        super(entityType, world);
        this.ticksLeft = LIFETIME_TICKS;
    }

    public StraightIcicleEntity(World world, double x, double y, double z, float yaw, int warmup, LivingEntity owner) {
        this(more_rpg_loot.entity.ModEntities.STRAIGHT_ICICLE, world);
        this.warmup = warmup;
        this.setOwner(owner);
        this.setYaw(yaw * 57.295776F);
        this.setPosition(x, y, z);
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
        this.warmup = nbt.getInt("Warmup");
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Warmup", this.warmup);
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
            }
        } else {
            currentTick++;

            if (--this.warmup < 0) {
                if (this.warmup >= -(ATTACK_TICK + 3) && this.warmup <= -(ATTACK_TICK - 3)) {
                    this.dealDamage();
                }

                if (!this.startedAttack) {
                    this.getWorld().sendEntityStatus(this, (byte)4);
                    this.startedAttack = true;
                }

                if (--this.ticksLeft < 0) {
                    this.discard();
                }
            }
        }
    }

    private void dealDamage() {
        List<LivingEntity> targets = this.getWorld().getNonSpectatingEntities(
            LivingEntity.class,
            this.getBoundingBox().expand(1.2, 0.5, 1.2)
        );

        LivingEntity owner = this.getOwner();

        for (LivingEntity target : targets) {
            // Skip if target is the owner or a teammate
            if (target == owner) {
                continue;
            }
            if(owner instanceof MobEntity && target instanceof  MobEntity){
                continue;
            }
            if (owner != null && owner.isTeammate(target)) {
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
        float damage = (float) (owner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.75);

        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            try {
                double frostPower = owner.getAttributeValue(SpellSchools.FROST.attributeEntry);
                damage += (float) (frostPower * 0.5);
            } catch (Exception e) {
            }
        }

        return Math.max(6.0F, damage);
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == 4) {
            this.playingAnimation = true;
            if (!this.isSilent()) {
                this.getWorld().playSound(
                    this.getX(), this.getY(), this.getZ(),
                    ModSounds.FROSTMONARCH_DEEP_FREEZE.soundEvent(),
                    this.getSoundCategory(),
                    0.5F,
                    this.random.nextFloat() * 0.2F + 0.85F,
                    false
                );
            }
        }
    }

    public float getAnimationProgress(float tickDelta) {
        if (!this.playingAnimation) {
            return 0.0F;
        }

        int elapsed = LIFETIME_TICKS - this.ticksLeft;
        return Math.min(1.0F, ((float)elapsed - tickDelta) / (float)LIFETIME_TICKS);
    }

    public RegistryEntry<StatusEffect> getFreezingEffect() {
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            return MRPGCEffects.FROSTED.entry;
        }
        return Effects.FREEZING.registryEntry;
    }

}

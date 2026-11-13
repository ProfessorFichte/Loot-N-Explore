package lne.entity.frozen_depths.conjured;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import com.mrpg_lib.util.AllyHelper;
import lne.effects.Effects;
import lne.entity.ModEntities;
import lne.fx.LNEFx;
import lne.particle.Particles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static com.mrpg_lib.util.FreezeMethods.addFrozenTicksCapped;
import static lne.util.HelperMethods.applyStatusEffect;

public class FrostDecoyEntity extends Entity implements Ownable {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(FrostDecoyEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> SCALE = DataTracker.registerData(FrostDecoyEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final double TOUCH_MARGIN = 0.15;
    private static final int FREEZING_SECONDS = 3;
    private static final int FROZEN_TICKS = 40;
    private static final double KNOCKBACK = 0.4;

    @Nullable private UUID ownerUuid;
    @Nullable private LivingEntity owner;
    private double radius = 3.0;
    private float damage = 4.0F;

    public FrostDecoyEntity(EntityType<? extends FrostDecoyEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public FrostDecoyEntity(World world, LivingEntity owner, Vec3d pos, float yaw, float scale, int fuse, double radius, float damage) {
        this(ModEntities.FROST_DECOY, world);
        this.owner = owner;
        this.ownerUuid = owner.getUuid();
        this.radius = radius;
        this.damage = damage;
        this.dataTracker.set(FUSE, Math.max(1, fuse));
        this.dataTracker.set(SCALE, Math.max(0.1F, scale));
        this.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, 0.0F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FUSE, 40);
        builder.add(SCALE, 1.0F);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public float getScale() {
        return this.dataTracker.get(SCALE);
    }

    public float getFuseProgress(float tickDelta) {
        return MathHelper.clamp((this.age + tickDelta) / getFuse(), 0.0F, 1.0F);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (SCALE.equals(data)) {
            this.calculateDimensions();
        }
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(getScale());
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        if (owner == null && ownerUuid != null && this.getWorld() instanceof ServerWorld serverWorld
                && serverWorld.getEntity(ownerUuid) instanceof LivingEntity living) {
            owner = living;
        }
        return owner;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.age % 3 == 0) {
                this.getWorld().addParticle(Particles.FREEZING_SNOWFLAKE,
                        this.getParticleX(0.6), this.getRandomBodyY(), this.getParticleZ(0.6), 0.0, 0.02, 0.0);
            }
            return;
        }
        if (this.age >= getFuse() || isTouchedByEnemy()) {
            explode();
        }
    }

    private boolean isTouchedByEnemy() {
        Box touch = this.getBoundingBox().expand(TOUCH_MARGIN);
        return !this.getWorld().getEntitiesByClass(LivingEntity.class, touch, this::isEnemy).isEmpty();
    }

    private boolean isEnemy(Entity entity) {
        if (!(entity instanceof LivingEntity living) || !living.isAlive()) {
            return false;
        }
        LivingEntity decoyOwner = getOwner();
        if (decoyOwner == null) {
            return living instanceof PlayerEntity && AllyHelper.canHurt(null, living);
        }
        return living != decoyOwner && AllyHelper.canHurt(decoyOwner, living);
    }

    public void explode() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld) || this.isRemoved()) {
            return;
        }
        Vec3d center = this.getPos().add(0.0, this.getHeight() * 0.5, 0.0);
        MrpgFx.play(serverWorld, LNEFx.FROST_DECOY_BURST, FxParams.at(center).radius((float) radius));
        MrpgFx.play(serverWorld, LNEFx.FROST_DECOY_SHARDS, FxParams.at(center));
        LivingEntity decoyOwner = getOwner();
        DamageSource source = decoyOwner != null
                ? this.getDamageSources().indirectMagic(this, decoyOwner)
                : this.getDamageSources().magic();
        Box area = new Box(center, center).expand(radius);
        for (LivingEntity target : serverWorld.getEntitiesByClass(LivingEntity.class, area, this::isEnemy)) {
            if (target.squaredDistanceTo(center) > radius * radius && this.squaredDistanceTo(target) > radius * radius) {
                continue;
            }
            target.damage(source, damage);
            if (!target.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                addFrozenTicksCapped(target, FROZEN_TICKS);
                applyStatusEffect(target, 0, FREEZING_SECONDS, Effects.FREEZING.registryEntry, 0, false, true, false, 0);
            }
            Vec3d push = target.getPos().subtract(this.getPos()).multiply(1.0, 0.0, 1.0);
            if (push.lengthSquared() > 1.0E-4) {
                target.takeKnockback(KNOCKBACK, -push.x, -push.z);
                target.velocityModified = true;
            }
        }
        this.discard();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getWorld().isClient || this.isRemoved()) {
            return false;
        }
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.discard();
            return true;
        }
        if (source.getAttacker() != null && isEnemy(source.getAttacker())) {
            this.getWorld().playSound(null, this.getX(), this.getBodyY(0.5), this.getZ(), SoundEvents.BLOCK_GLASS_HIT, SoundCategory.HOSTILE, 1.0F, 0.8F);
            explode();
            return true;
        }
        return false;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean canUsePortals(boolean allowVehicles) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(FUSE, Math.max(1, nbt.getInt("Fuse")));
        this.dataTracker.set(SCALE, nbt.contains("Scale") ? Math.max(0.1F, nbt.getFloat("Scale")) : 1.0F);
        this.age = nbt.getInt("DecoyAge");
        this.radius = nbt.contains("Radius") ? nbt.getDouble("Radius") : 3.0;
        this.damage = nbt.contains("Damage") ? nbt.getFloat("Damage") : 4.0F;
        this.ownerUuid = nbt.containsUuid("Owner") ? nbt.getUuid("Owner") : null;
        this.owner = null;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Fuse", getFuse());
        nbt.putFloat("Scale", getScale());
        nbt.putInt("DecoyAge", this.age);
        nbt.putDouble("Radius", radius);
        nbt.putFloat("Damage", damage);
        if (ownerUuid != null) {
            nbt.putUuid("Owner", ownerUuid);
        }
    }
}

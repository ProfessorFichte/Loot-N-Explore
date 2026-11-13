package lne.entity.generic.projectile;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import lne.ability.RelicTargets;
import lne.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class OrbitingWitherSkullEntity extends WitherSkullEntity {
    private static final TrackedData<Float> BASE_ANGLE = DataTracker.registerData(OrbitingWitherSkullEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ORBIT_RADIUS = DataTracker.registerData(OrbitingWitherSkullEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final float ANGULAR_SPEED = 0.16F;
    private static final double CONTACT_MARGIN = 0.35;

    private int lifetimeTicks = 160;
    private float explosionDamage;
    private float explosionRadius = 2.0F;

    public OrbitingWitherSkullEntity(EntityType<? extends OrbitingWitherSkullEntity> type, World world) {
        super(type, world);
    }

    public OrbitingWitherSkullEntity(World world, LivingEntity owner, float baseAngle, float orbitRadius, int lifetimeTicks,
                                     float explosionDamage, float explosionRadius) {
        super(ModEntities.ORBITING_WITHER_SKULL, world);
        this.setOwner(owner);
        this.dataTracker.set(BASE_ANGLE, baseAngle);
        this.dataTracker.set(ORBIT_RADIUS, orbitRadius);
        this.lifetimeTicks = lifetimeTicks;
        this.explosionDamage = explosionDamage;
        this.explosionRadius = explosionRadius;
        this.setPosition(orbitPosition(owner, 0));
        this.resetPosition();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BASE_ANGLE, 0.0F);
        builder.add(ORBIT_RADIUS, 2.0F);
    }

    private Vec3d orbitPosition(LivingEntity owner, float ticks) {
        float angle = this.dataTracker.get(BASE_ANGLE) + ticks * ANGULAR_SPEED;
        float radius = this.dataTracker.get(ORBIT_RADIUS);
        return owner.getPos().add(MathHelper.cos(angle) * radius, owner.getHeight() * 0.55, MathHelper.sin(angle) * radius);
    }

    @Override
    public void tick() {
        this.baseTick();
        if (!(this.getOwner() instanceof LivingEntity owner) || !owner.isAlive()) {
            if (!this.getWorld().isClient) {
                this.discard();
            }
            return;
        }
        Vec3d next = orbitPosition(owner, this.age);
        Vec3d motion = next.subtract(this.getPos());
        this.setVelocity(motion);
        this.setPosition(next);
        if (motion.horizontalLengthSquared() > 1.0E-6) {
            this.setYaw((float) (MathHelper.atan2(motion.x, motion.z) * MathHelper.DEGREES_PER_RADIAN));
        }
        if (!(this.getWorld() instanceof ServerWorld world)) {
            if (this.age % 3 == 0) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, next.x, next.y, next.z, 0.0, 0.0, 0.0);
            }
            return;
        }
        if (this.age > lifetimeTicks) {
            LNEFx.play(world, LNEFx.SOUL_HIT, next);
            this.discard();
            return;
        }
        if (!world.getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(CONTACT_MARGIN),
                entity -> RelicTargets.isEnemy(owner, entity)).isEmpty()) {
            explode(world, owner, next);
        }
    }

    private void explode(ServerWorld world, LivingEntity owner, Vec3d center) {
        for (var enemy : RelicTargets.enemiesAround(world, owner, center, explosionRadius)) {
            RelicTargets.magicDamage(world, owner, enemy, explosionDamage);
        }
        MrpgFx.play(world, LNEFx.SKULL_BLAST, FxParams.at(center).radius(explosionRadius));
        this.discard();
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
        if (!this.getWorld().isClient) {
            super.updateTrackedPositionAndAngles(x, y, z, yaw, pitch, interpolationSteps);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("BaseAngle", this.dataTracker.get(BASE_ANGLE));
        nbt.putFloat("OrbitRadius", this.dataTracker.get(ORBIT_RADIUS));
        nbt.putInt("LifetimeTicks", lifetimeTicks);
        nbt.putFloat("ExplosionDamage", explosionDamage);
        nbt.putFloat("ExplosionRadius", explosionRadius);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(BASE_ANGLE, nbt.getFloat("BaseAngle"));
        this.dataTracker.set(ORBIT_RADIUS, nbt.getFloat("OrbitRadius"));
        this.lifetimeTicks = nbt.getInt("LifetimeTicks");
        this.explosionDamage = nbt.getFloat("ExplosionDamage");
        this.explosionRadius = nbt.getFloat("ExplosionRadius");
    }
}

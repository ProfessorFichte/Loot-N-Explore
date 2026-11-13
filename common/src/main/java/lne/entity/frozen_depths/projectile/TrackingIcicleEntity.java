package lne.entity.frozen_depths.projectile;

import net.minecraft.entity.damage.DamageSource;
import com.mrpg_lib.util.AllyHelper;
import net.minecraft.sound.SoundEvents;
import com.mrpg_lib.util.damage.SpellDamage;
import lne.ability.AbilityDamage;
import lne.effects.Effects;
import lne.entity.ModEntities;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import lne.util.damage.FrostDamageProfiles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static lne.util.HelperMethods.applyStatusEffect;
import static lne.util.HelperMethods.getFreezingEffect;

public class TrackingIcicleEntity extends Entity implements Ownable {
    private static final TrackedData<Boolean> HOVERING = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> SLOT_RIGHT = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SLOT_UP = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final int MAX_HOVER_TICKS = 200;
    private static final float HOVER_BACK_OFFSET = 0.4F;
    // the rendered icicle is 3 blocks long and centered on the entity, so its tip leads the position by 1.5
    private static final double TIP_REACH = 1.5;
    private static final double HIT_MARGIN = 0.4;

    public enum Shot {
        STAFF(1.6F, 1.6F, 0, 0.012F, 0.0F, 0, 40),
        RING(0.9F, 1.1F, 6, 0.02F, 0.0F, 0, 22),
        VOLLEY(0.32F, 0.92F, 14, 0.0F, 4.0F, 30, 75),
        SINGLE(0.44F, 0.96F, 10, 0.0F, 2.5F, 22, 62);

        final float startSpeed;
        final float maxSpeed;
        final int accelerationTicks;
        final float gravity;
        final float turnDegrees;
        final int homingTicks;
        final int lifetimeTicks;

        Shot(float startSpeed, float maxSpeed, int accelerationTicks, float gravity, float turnDegrees, int homingTicks, int lifetimeTicks) {
            this.startSpeed = startSpeed;
            this.maxSpeed = maxSpeed;
            this.accelerationTicks = accelerationTicks;
            this.gravity = gravity;
            this.turnDegrees = turnDegrees;
            this.homingTicks = homingTicks;
            this.lifetimeTicks = lifetimeTicks;
        }

        float speed(int ticksAlive) {
            if (accelerationTicks <= 0) return maxSpeed;
            float progress = MathHelper.clamp(ticksAlive / (float) accelerationTicks, 0.0F, 1.0F);
            return startSpeed + (maxSpeed - startSpeed) * progress * progress;
        }
    }

    private LivingEntity owner;
    private UUID ownerUuid;
    private Shot shot = Shot.SINGLE;
    private int ticksAlive = 0;
    private int hoverTicks = 0;
    private float damageScale = 1.0F;
    private int freezingTicks = 0;
    private float fixedDamage = -1.0F;
    private float fixedDamageScaling = 0.0F;

    public TrackingIcicleEntity(EntityType<? extends TrackingIcicleEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public TrackingIcicleEntity(World world, LivingEntity owner, LivingEntity target, double x, double y, double z) {
        this(ModEntities.TRACKING_ICICLE, world);
        this.setOwner(owner);
        this.setPosition(x, y, z);
        this.shot = Shot.SINGLE;
        this.launch(target);
    }

    public TrackingIcicleEntity(World world, LivingEntity owner, Vec3d direction, double x, double y, double z, Shot shot) {
        this(ModEntities.TRACKING_ICICLE, world);
        this.setOwner(owner);
        this.setPosition(x, y, z);
        this.shot = shot;
        this.launch(direction);
    }

    public TrackingIcicleEntity(World world, LivingEntity owner, LivingEntity target, float slotRight, float slotUp) {
        this(ModEntities.TRACKING_ICICLE, world);
        this.setOwner(owner);
        this.shot = Shot.VOLLEY;
        this.dataTracker.set(TARGET_ID, target.getId());
        this.dataTracker.set(SLOT_RIGHT, slotRight);
        this.dataTracker.set(SLOT_UP, slotUp);
        this.dataTracker.set(HOVERING, true);
        this.updateHover(owner);
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
    }

    public void configureStaffShot(float damageScale, int freezingTicks) {
        this.damageScale = damageScale;
        this.freezingTicks = freezingTicks;
    }

    public void configureFixedDamage(float base, float scaling) {
        this.fixedDamage = base;
        this.fixedDamageScaling = scaling;
    }

    public boolean isHovering() {
        return this.dataTracker.get(HOVERING);
    }

    public void launch(LivingEntity target) {
        this.dataTracker.set(TARGET_ID, target.getId());
        this.launch(aimPoint(target).subtract(this.getPos()));
    }

    public void launch(Vec3d direction) {
        Vec3d normalized = direction.normalize();
        this.setVelocity(normalized.multiply(shot.startSpeed));
        this.velocityDirty = true;
        this.ticksAlive = 0;
        this.dataTracker.set(HOVERING, false);
        this.setDirection(normalized);
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
        if (!this.getWorld().isClient && this.shot != Shot.RING) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_TRIDENT_THROW.value(),
                    this.getSoundCategory(), 0.4F, 1.7F);
        }
    }

    private static Vec3d aimPoint(LivingEntity target) {
        return target.getPos().add(0, target.getHeight() * 0.5, 0);
    }

    private void setDirection(Vec3d direction) {
        if (direction.lengthSquared() < 1.0E-7) return;
        Vec3d normalized = direction.normalize();
        this.setYaw((float) (MathHelper.atan2(-normalized.x, normalized.z) * (180.0 / Math.PI)));
        this.setPitch((float) (-Math.asin(MathHelper.clamp(normalized.y, -1.0, 1.0)) * (180.0 / Math.PI)));
    }

    private void updateHover(LivingEntity hoverOwner) {
        float scale = hoverOwner.getHeight() / 1.99F;
        float yawRad = hoverOwner.getBodyYaw() * ((float) Math.PI / 180.0F);
        Vec3d forward = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
        Vec3d right = new Vec3d(-MathHelper.cos(yawRad), 0.0, -MathHelper.sin(yawRad));
        Vec3d position = hoverOwner.getPos()
                .add(right.multiply(this.dataTracker.get(SLOT_RIGHT) * scale))
                .add(0.0, this.dataTracker.get(SLOT_UP) * scale, 0.0)
                .add(forward.multiply(-HOVER_BACK_OFFSET * scale));
        this.setPosition(position);

        Entity aim = this.getWorld().getEntityById(this.dataTracker.get(TARGET_ID));
        Vec3d direction = aim instanceof LivingEntity livingAim && livingAim.isAlive()
                ? aimPoint(livingAim).subtract(position)
                : forward;
        this.setDirection(direction);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(HOVERING, false);
        builder.add(OWNER_ID, -1);
        builder.add(TARGET_ID, -1);
        builder.add(SLOT_RIGHT, 0.0F);
        builder.add(SLOT_UP, 0.0F);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
        this.dataTracker.set(OWNER_ID, owner == null ? -1 : owner.getId());
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        if (owner != null && owner.isAlive()) return owner;
        if (ownerUuid != null && getWorld() instanceof ServerWorld sw) {
            Entity e = sw.getEntity(ownerUuid);
            if (e instanceof LivingEntity le) {
                owner = le;
                return le;
            }
        }
        return null;
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
        if (this.getWorld().isClient && this.isHovering()) {
            return;
        }
        super.updateTrackedPositionAndAngles(x, y, z, yaw, pitch, interpolationSteps);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        this.ticksAlive = nbt.getInt("TicksAlive");
        int shotIndex = nbt.getInt("Shot");
        this.shot = shotIndex >= 0 && shotIndex < Shot.values().length ? Shot.values()[shotIndex] : Shot.SINGLE;
        this.damageScale = nbt.contains("DamageScale") ? nbt.getFloat("DamageScale") : 1.0F;
        this.freezingTicks = nbt.getInt("FreezingTicks");
        this.fixedDamage = nbt.contains("FixedDamage") ? nbt.getFloat("FixedDamage") : -1.0F;
        this.fixedDamageScaling = nbt.getFloat("FixedDamageScaling");
        if (nbt.contains("VelX")) {
            this.setVelocity(nbt.getDouble("VelX"), nbt.getDouble("VelY"), nbt.getDouble("VelZ"));
        }
        if (nbt.getBoolean("Hovering")) {
            this.ticksAlive = shot.lifetimeTicks + 1;
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("TicksAlive", ticksAlive);
        nbt.putInt("Shot", shot.ordinal());
        nbt.putFloat("DamageScale", damageScale);
        nbt.putInt("FreezingTicks", freezingTicks);
        nbt.putFloat("FixedDamage", fixedDamage);
        nbt.putFloat("FixedDamageScaling", fixedDamageScaling);
        nbt.putBoolean("Hovering", isHovering());
        Vec3d velocity = this.getVelocity();
        nbt.putDouble("VelX", velocity.x);
        nbt.putDouble("VelY", velocity.y);
        nbt.putDouble("VelZ", velocity.z);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isHovering()) {
            ticksAlive = 0;
            tickHover();
            return;
        }

        if (this.getWorld().isClient) {
            Vec3d velocity = this.getVelocity();
            this.setPosition(this.getPos().add(velocity));
            this.setDirection(velocity);
            return;
        }

        ticksAlive++;
        if (ticksAlive > shot.lifetimeTicks) {
            shatter(this.getPos());
            this.discard();
            return;
        }

        Vec3d velocity = nextVelocity();
        this.setVelocity(velocity);
        this.velocityDirty = true;
        this.setDirection(velocity);

        Vec3d start = this.getPos();
        Vec3d end = start.add(velocity);
        Vec3d direction = velocity.normalize();
        BlockHitResult bodyHit = raycast(start, end);
        BlockHitResult tipHit = raycast(start, end.add(direction.multiply(TIP_REACH)));
        Vec3d reachEnd = tipHit.getPos();

        LivingEntity owner = this.getOwner();
        LivingEntity target = findTarget(owner, start, reachEnd);
        if (target != null) {
            hit(owner, target);
            this.discard();
            return;
        }
        if (bodyHit.getType() != HitResult.Type.MISS) {
            shatter(bodyHit.getPos());
            this.discard();
            return;
        }
        this.setPosition(end);
    }

    private Vec3d nextVelocity() {
        Vec3d direction = this.getVelocity().normalize();
        if (direction.lengthSquared() < 1.0E-7) {
            direction = Vec3d.fromPolar(this.getPitch(), this.getYaw());
        }
        if (shot.turnDegrees > 0 && ticksAlive <= shot.homingTicks
                && this.getWorld().getEntityById(this.dataTracker.get(TARGET_ID)) instanceof LivingEntity target && target.isAlive()) {
            Vec3d desired = aimPoint(target).subtract(this.getPos()).normalize();
            double dot = MathHelper.clamp(direction.dotProduct(desired), -1.0, 1.0);
            if (dot > 0.0) {
                double angle = Math.acos(dot);
                double maxTurn = Math.toRadians(shot.turnDegrees);
                direction = angle <= maxTurn ? desired : slerp(direction, desired, maxTurn / angle);
            }
        }
        return direction.multiply(shot.speed(ticksAlive)).add(0.0, -shot.gravity, 0.0);
    }

    private static Vec3d slerp(Vec3d from, Vec3d to, double t) {
        return from.multiply(1.0 - t).add(to.multiply(t)).normalize();
    }

    private BlockHitResult raycast(Vec3d from, Vec3d to) {
        return this.getWorld().raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
    }

    @Nullable
    private LivingEntity findTarget(@Nullable LivingEntity owner, Vec3d start, Vec3d end) {
        Box area = new Box(start, end).expand(HIT_MARGIN + 1.0);
        LivingEntity closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (LivingEntity candidate : this.getWorld().getNonSpectatingEntities(LivingEntity.class, area)) {
            if (candidate == owner || owner != null && candidate.isConnectedThroughVehicle(owner)) continue;
            if (!AllyHelper.canHurt(owner, candidate)) continue;
            Box box = candidate.getBoundingBox().expand(HIT_MARGIN);
            Vec3d hitPos = box.contains(start) ? start : box.raycast(start, end).orElse(null);
            if (hitPos == null) continue;
            double distance = start.squaredDistanceTo(hitPos);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = candidate;
            }
        }
        return closest;
    }

    private void hit(@Nullable LivingEntity owner, LivingEntity target) {
        Vec3d hitPos = aimPoint(target);
        // Magic damage goes through shields, so a raised shield takes the hit as a thrown projectile instead.
        DamageSource blockable = this.getDamageSources().thrown(this, owner);
        if (target.blockedByShield(blockable)) {
            target.damage(blockable, calculateDamage(owner));
            shatter(hitPos);
            return;
        }
        target.damage(this.getDamageSources().indirectMagic(this, owner), calculateDamage(owner));
        if (freezingTicks > 0) {
            target.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.registryEntry, freezingTicks, 0), owner);
            if (this.getWorld() instanceof ServerWorld sw) {
                MrpgFx.play(sw, LNEFx.STAFF_IMPACT, FxParams.at(hitPos));
            }
        } else {
            applyStatusEffect(target, 0, 5, getFreezingEffect(), 10, true, true, true, 1);
            shatter(hitPos);
        }
    }

    public void shatterInPlace() {
        if (this.isRemoved()) {
            return;
        }
        shatter(this.getPos());
        this.discard();
    }

    private void shatter(Vec3d pos) {
        if (this.getWorld() instanceof ServerWorld sw) {
            MrpgFx.play(sw, LNEFx.ICICLE_SHATTER, FxParams.at(pos));
        }
    }

    private void tickHover() {
        Entity hoverOwner = this.getWorld().getEntityById(this.dataTracker.get(OWNER_ID));
        if (!(hoverOwner instanceof LivingEntity livingOwner) || !livingOwner.isAlive()) {
            if (!this.getWorld().isClient) {
                this.discard();
            }
            return;
        }
        if (!this.getWorld().isClient && ++hoverTicks > MAX_HOVER_TICKS) {
            this.discard();
            return;
        }
        updateHover(livingOwner);
    }

    private float calculateDamage(@Nullable LivingEntity owner) {
        if (fixedDamage >= 0.0F) {
            return owner == null ? fixedDamage : AbilityDamage.scaled(owner, fixedDamage, fixedDamageScaling);
        }
        return SpellDamage.calculate(owner, FrostDamageProfiles.FLYING_ICICLE) * damageScale;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}

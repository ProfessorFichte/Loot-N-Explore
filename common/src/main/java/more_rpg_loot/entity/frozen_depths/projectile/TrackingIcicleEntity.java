package more_rpg_loot.entity.frozen_depths.projectile;

import com.mrpg_lib.util.AllyHelper;
import more_rpg_loot.util.damage.FrostDamageProfiles;
import com.mrpg_lib.util.damage.SpellDamage;


import more_rpg_loot.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;
import static more_rpg_loot.util.HelperMethods.getFreezingEffect;

public class TrackingIcicleEntity extends Entity implements Ownable {
    private static final TrackedData<Boolean> HOVERING = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> SLOT_RIGHT = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SLOT_UP = DataTracker.registerData(TrackingIcicleEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final float START_SPEED = 0.25F;
    private static final float MAX_SPEED = 1.2F;
    private static final int ACCELERATION_TICKS = 18;
    private static final int LIFETIME_TICKS = 50;
    private static final int MAX_HOVER_TICKS = 200;
    private static final float HOVER_BACK_OFFSET = 0.4F;

    private LivingEntity owner;
    private UUID ownerUuid;
    private int ticksAlive = 0;
    private int hoverTicks = 0;

    public TrackingIcicleEntity(EntityType<? extends TrackingIcicleEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public TrackingIcicleEntity(World world, LivingEntity owner, LivingEntity target, double x, double y, double z) {
        this(ModEntities.TRACKING_ICICLE, world);
        this.setOwner(owner);
        this.setPosition(x, y, z);
        this.launch(target);
    }

    public TrackingIcicleEntity(World world, LivingEntity owner, LivingEntity target, float slotRight, float slotUp) {
        this(ModEntities.TRACKING_ICICLE, world);
        this.setOwner(owner);
        this.dataTracker.set(TARGET_ID, target.getId());
        this.dataTracker.set(SLOT_RIGHT, slotRight);
        this.dataTracker.set(SLOT_UP, slotUp);
        this.dataTracker.set(HOVERING, true);
        this.updateHover(owner);
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
    }

    public boolean isHovering() {
        return this.dataTracker.get(HOVERING);
    }

    public void launch(LivingEntity target) {
        Vec3d targetCenter = target.getPos().add(0, target.getHeight() * 0.5, 0);
        Vec3d toTarget = targetCenter.subtract(this.getPos()).normalize();
        this.setVelocity(toTarget.multiply(START_SPEED));
        this.velocityDirty = true;
        this.ticksAlive = 0;
        this.dataTracker.set(HOVERING, false);
        this.setDirection(toTarget);
    }

    private void setDirection(Vec3d direction) {
        this.setYaw((float) (MathHelper.atan2(-direction.x, direction.z) * (180.0 / Math.PI)));
        this.setPitch((float) (-Math.asin(MathHelper.clamp(direction.y, -1.0, 1.0)) * (180.0 / Math.PI)));
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
                ? livingAim.getPos().add(0, livingAim.getHeight() * 0.5, 0).subtract(position).normalize()
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
        if (nbt.contains("VelX")) {
            this.setVelocity(nbt.getDouble("VelX"), nbt.getDouble("VelY"), nbt.getDouble("VelZ"));
        }
        if (nbt.getBoolean("Hovering")) {
            this.ticksAlive = LIFETIME_TICKS + 1;
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("TicksAlive", ticksAlive);
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

        boolean client = this.getWorld().isClient;
        ticksAlive++;
        if (!client && ticksAlive > LIFETIME_TICKS) {
            this.discard();
            return;
        }

        float progress = MathHelper.clamp(ticksAlive / (float) ACCELERATION_TICKS, 0.0F, 1.0F);
        float speed = START_SPEED + (MAX_SPEED - START_SPEED) * progress * progress;
        Vec3d step = this.getVelocity().normalize().multiply(speed);
        Box sweep = this.getBoundingBox().stretch(step).expand(0.3);
        this.setPosition(this.getPos().add(step));

        if (client) {
            return;
        }

        if (!this.getWorld().getBlockState(this.getBlockPos()).isAir()) {
            this.discard();
            return;
        }

        LivingEntity owner = this.getOwner();
        var nearby = this.getWorld().getNonSpectatingEntities(LivingEntity.class, sweep);

        for (LivingEntity target : nearby) {
            if (!AllyHelper.canHurt(owner, target)) continue;

            target.damage(this.getDamageSources().indirectMagic(this, owner), calculateDamage(owner));
            applyStatusEffect(target, 0, 5, getFreezingEffect(), 10, true, true, true, 1);
            this.discard();
            return;
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
        return SpellDamage.calculate(owner, FrostDamageProfiles.FLYING_ICICLE);
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

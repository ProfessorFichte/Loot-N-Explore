package lne.entity.frozen_depths.conjured;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import com.mrpg_lib.util.AllyHelper;
import lne.entity.ModEntities;
import lne.fx.LNEFx;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class IceWallEntity extends Entity implements Ownable {
    private static final TrackedData<Integer> LIFETIME = DataTracker.registerData(IceWallEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final int RISE_TICKS = 4;
    public static final int RETRACT_TICKS = 6;
    private static final double INTERCEPT_SCAN = 5.0;
    private static final double INTERCEPT_MARGIN = 0.3;

    @Nullable private UUID ownerUuid;
    @Nullable private LivingEntity owner;

    public IceWallEntity(EntityType<? extends IceWallEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public IceWallEntity(World world, LivingEntity owner, double x, double y, double z, float yaw, int lifetime) {
        this(ModEntities.ICE_WALL, world);
        this.owner = owner;
        this.ownerUuid = owner.getUuid();
        this.dataTracker.set(LIFETIME, Math.max(RISE_TICKS + RETRACT_TICKS, lifetime));
        this.refreshPositionAndAngles(x, y, z, yaw, 0.0F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(LIFETIME, 100);
    }

    public int getLifetime() {
        return this.dataTracker.get(LIFETIME);
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
            return;
        }
        interceptProjectiles();
        if (this.age >= getLifetime()) {
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                MrpgFx.play(serverWorld, LNEFx.ICE_WALL_SHATTER, FxParams.at(this.getPos().add(0, this.getHeight() * 0.5, 0)));
            }
            this.discard();
        }
    }

    private void interceptProjectiles() {
        Box wall = this.getBoundingBox().expand(INTERCEPT_MARGIN);
        for (ProjectileEntity projectile : this.getWorld().getEntitiesByClass(ProjectileEntity.class, wall.expand(INTERCEPT_SCAN), this::blocks)) {
            Vec3d start = projectile.getPos();
            Vec3d end = start.add(projectile.getVelocity());
            if (wall.contains(start) || wall.raycast(start, end).isPresent()) {
                shatter(projectile);
            }
        }
    }

    private boolean blocks(ProjectileEntity projectile) {
        if (!projectile.isAlive()) {
            return false;
        }
        LivingEntity wallOwner = this.getOwner();
        return wallOwner == null || !AllyHelper.canHelp(wallOwner, projectile);
    }

    private void shatter(ProjectileEntity projectile) {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            MrpgFx.play(serverWorld, LNEFx.ICICLE_SHATTER, FxParams.at(projectile.getPos()));
        }
        projectile.discard();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient && source.getSource() instanceof ProjectileEntity projectile && blocks(projectile)) {
            shatter(projectile);
        }
        return false;
    }

    public float getAnimationProgress(float tickDelta) {
        float time = this.age + tickDelta;
        if (time < RISE_TICKS) {
            return time / RISE_TICKS;
        }
        float retractStart = getLifetime() - RETRACT_TICKS;
        if (time > retractStart) {
            return Math.max(0.0F, 1.0F - (time - retractStart) / RETRACT_TICKS);
        }
        return 1.0F;
    }

    @Override
    public boolean isCollidable() {
        return true;
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
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(LIFETIME, Math.max(RISE_TICKS + RETRACT_TICKS, nbt.getInt("Lifetime")));
        this.age = nbt.getInt("WallAge");
        this.ownerUuid = nbt.containsUuid("Owner") ? nbt.getUuid("Owner") : null;
        this.owner = null;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Lifetime", getLifetime());
        nbt.putInt("WallAge", this.age);
        if (ownerUuid != null) {
            nbt.putUuid("Owner", ownerUuid);
        }
    }
}

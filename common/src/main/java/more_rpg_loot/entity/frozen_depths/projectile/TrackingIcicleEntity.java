package more_rpg_loot.entity.frozen_depths.projectile;

import more_rpg_loot.entity.ModEntities;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static more_rpg_loot.util.HelperMethods.applyStatusEffect;
import static more_rpg_loot.util.HelperMethods.getFreezingEffect;

public class TrackingIcicleEntity extends Entity implements Ownable {
    private static final float SPEED = 0.9F;
    private static final int LIFETIME_TICKS = 50;

    private LivingEntity owner;
    private UUID ownerUuid;
    private Vec3d velocity = Vec3d.ZERO;
    private int ticksAlive = 0;

    public TrackingIcicleEntity(EntityType<? extends TrackingIcicleEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public TrackingIcicleEntity(World world, LivingEntity owner, LivingEntity target, double x, double y, double z) {
        this(ModEntities.TRACKING_ICICLE, world);
        this.owner = owner;
        this.ownerUuid = owner.getUuid();
        this.setPosition(x, y, z);

        Vec3d targetCenter = target.getPos().add(0, target.getHeight() * 0.5, 0);
        Vec3d toTarget = targetCenter.subtract(x, y, z).normalize();
        this.velocity = toTarget.multiply(SPEED);
        this.setYaw((float) (MathHelper.atan2(-toTarget.x, toTarget.z) * (180.0 / Math.PI)));
        this.setPitch((float) (-Math.asin(toTarget.y) * (180.0 / Math.PI)));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
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
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        this.ticksAlive = nbt.getInt("TicksAlive");
        if (nbt.contains("VelX")) {
            this.velocity = new Vec3d(nbt.getDouble("VelX"), nbt.getDouble("VelY"), nbt.getDouble("VelZ"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("TicksAlive", ticksAlive);
        nbt.putDouble("VelX", velocity.x);
        nbt.putDouble("VelY", velocity.y);
        nbt.putDouble("VelZ", velocity.z);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            return;
        }

        ticksAlive++;
        if (ticksAlive > LIFETIME_TICKS) {
            this.discard();
            return;
        }

        this.setPosition(this.getPos().add(velocity));

        if (!this.getWorld().getBlockState(this.getBlockPos()).isAir()) {
            this.discard();
            return;
        }

        LivingEntity owner = this.getOwner();
        var nearby = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.5));

        for (LivingEntity target : nearby) {
            if (target == owner) continue;
            if (owner instanceof MobEntity && target instanceof MobEntity) continue;
            if (owner != null && owner.isTeammate(target)) continue;

            target.damage(this.getDamageSources().indirectMagic(this, owner), calculateDamage(owner));
            applyStatusEffect(target, 0, 5, getFreezingEffect(), 10, true, true, true, 1);
            this.discard();
            return;
        }
    }

    private float calculateDamage(@Nullable LivingEntity owner) {
        if (owner == null) return 5.0F;
        float base = (float) (owner.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.5);
        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            try {
                base += (float) (owner.getAttributeValue(SpellSchools.FROST.attributeEntry) * 0.5);
            } catch (Exception ignored) {}
        }
        return Math.max(5.0F, base);
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

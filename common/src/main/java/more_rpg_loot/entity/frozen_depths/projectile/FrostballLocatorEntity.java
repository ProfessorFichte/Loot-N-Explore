package more_rpg_loot.entity.frozen_depths.projectile;

import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.CommonItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FrostballLocatorEntity extends Entity implements FlyingItemEntity {
    private static final double HOP_DISTANCE = 12.0;
    private static final int FLIGHT_TICKS = 40;

    private double targetX;
    private double targetY;
    private double targetZ;
    private int age;

    public FrostballLocatorEntity(EntityType<? extends FrostballLocatorEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public FrostballLocatorEntity(World world, double x, double y, double z) {
        this(ModEntities.FROSTBALL_LOCATOR, world);
        this.setPosition(x, y, z);
    }

    public void initTargetPos(BlockPos pos) {
        double dx = pos.getX() - this.getX();
        double dz = pos.getZ() - this.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        double hopScale = horizontalDistance > 0 ? Math.min(horizontalDistance, HOP_DISTANCE) / horizontalDistance : 0;

        this.targetX = this.getX() + dx * hopScale;
        this.targetY = this.getY() + 8.0;
        this.targetZ = this.getZ() + dz * hopScale;
        this.age = 0;

        this.setVelocity(
                (this.targetX - this.getX()) / FLIGHT_TICKS,
                (this.targetY - this.getY()) / FLIGHT_TICKS,
                (this.targetZ - this.getZ()) / FLIGHT_TICKS
        );
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;

        var velocity = this.getVelocity();
        this.setPosition(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);

        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);

            if (this.age >= FLIGHT_TICKS) {
                serverWorld.spawnParticles(Particles.FREEZING_SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 12, 0.2, 0.2, 0.2, 0.05);
                this.discard();
            }
        }
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(CommonItems.FROSTBALL.item());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }
}

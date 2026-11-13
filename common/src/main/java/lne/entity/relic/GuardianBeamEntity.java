package lne.entity.relic;

import lne.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class GuardianBeamEntity extends Entity {
    private static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(GuardianBeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(GuardianBeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Vector3f> END = DataTracker.registerData(GuardianBeamEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
    private static final int KEEP_ALIVE_TICKS = 3;

    private int keepAlive = KEEP_ALIVE_TICKS;

    public GuardianBeamEntity(EntityType<? extends GuardianBeamEntity> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public GuardianBeamEntity(World world, LivingEntity owner) {
        this(ModEntities.GUARDIAN_BEAM, world);
        this.dataTracker.set(OWNER_ID, owner.getId());
        this.setPosition(owner.getEyePos());
    }

    public void refresh(Vec3d start, Vec3d end, @Nullable LivingEntity target) {
        this.setPosition(start);
        this.dataTracker.set(END, end.toVector3f());
        this.dataTracker.set(TARGET_ID, target == null ? -1 : target.getId());
        this.keepAlive = KEEP_ALIVE_TICKS;
    }

    @Nullable
    public Entity owner() {
        return this.getWorld().getEntityById(this.dataTracker.get(OWNER_ID));
    }

    @Nullable
    public Entity target() {
        int id = this.dataTracker.get(TARGET_ID);
        return id < 0 ? null : this.getWorld().getEntityById(id);
    }

    public Vec3d end() {
        return new Vec3d(this.dataTracker.get(END));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(OWNER_ID, -1);
        builder.add(TARGET_ID, -1);
        builder.add(END, new Vector3f());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && --keepAlive < 0) {
            this.discard();
        }
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }
}

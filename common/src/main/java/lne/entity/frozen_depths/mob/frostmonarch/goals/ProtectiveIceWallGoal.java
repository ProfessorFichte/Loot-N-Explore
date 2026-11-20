package lne.entity.frozen_depths.mob.frostmonarch.goals;

import com.mrpg_lib.sounds.MRPGLibSounds;
import com.mrpg_lib.entity.config.CustomValues;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.conjured.IceWallEntity;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchAnimationTimes;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ProtectiveIceWallGoal extends Goal {
    private static final double MIN_SPEED_SQUARED = 0.25;
    private static final double HIT_RADIUS = 2.5;
    private static final double SEGMENT_SPACING = 1.7;
    private static final double MAX_LATERAL_SHIFT = 3.0;
    private static final int GROUND_SCAN_UP = 3;
    private static final int GROUND_SCAN_DOWN = 6;
    private static final int OWNER_CHAIN_DEPTH = 4;
    private static final int RELEASE_SAFETY_TICKS = 2;
    private static final double FOOTPRINT_HALF = 0.3;

    private final FrostMonarchEntity monarch;
    @Nullable private ProjectileEntity threat;
    private final List<Vec3d> slots = new ArrayList<>();
    private float wallYaw;
    private int releaseTicks = -1;

    public ProtectiveIceWallGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.noneOf(Goal.Control.class));
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        if (monarch.iceWallCooldown > 0 || monarch.isFakeDeath() || monarch.getInvulnerableTimer() > 0 || monarch.getBarrierTimer() > 0) {
            return false;
        }
        CustomValues tuning = monarch.tuning();
        if (!tuning.getBoolean(FrozenDepthsTuning.ICE_WALL_ENABLED, true)) {
            return false;
        }
        this.threat = findThreat(tuning.getDouble(FrozenDepthsTuning.ICE_WALL_DETECTION_RANGE, 32.0),
                tuning.getDouble(FrozenDepthsTuning.ICE_WALL_DISTANCE, 5.0));
        return this.threat != null;
    }

    static boolean firedByPlayer(@Nullable Entity owner) {
        for (int depth = 0; owner != null && depth < OWNER_CHAIN_DEPTH; depth++) {
            if (owner instanceof PlayerEntity player) {
                return !player.isSpectator();
            }
            if (owner instanceof Tameable tameable) {
                owner = tameable.getOwner();
            } else if (owner instanceof Ownable ownable) {
                owner = ownable.getOwner();
            } else {
                return false;
            }
        }
        return false;
    }

    @Nullable
    private ProjectileEntity findThreat(double range, double wallDistance) {
        Vec3d center = monarch.getPos().add(0, monarch.getHeight() * 0.5, 0);
        ProjectileEntity closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (ProjectileEntity projectile : monarch.getWorld().getEntitiesByClass(ProjectileEntity.class, monarch.getBoundingBox().expand(range),
                p -> p.isAlive() && firedByPlayer(p.getOwner()))) {
            Vec3d velocity = projectile.getVelocity();
            if (velocity.lengthSquared() < MIN_SPEED_SQUARED) {
                continue;
            }
            Vec3d toMonarch = center.subtract(projectile.getPos());
            double horizontal = Math.sqrt(toMonarch.x * toMonarch.x + toMonarch.z * toMonarch.z);
            if (horizontal < wallDistance + 1.5) {
                continue;
            }
            Vec3d heading = velocity.normalize();
            double along = toMonarch.dotProduct(heading);
            if (along <= 0.0) {
                continue;
            }
            Vec3d closestPoint = projectile.getPos().add(heading.multiply(along));
            if (closestPoint.squaredDistanceTo(center) > HIT_RADIUS * HIT_RADIUS) {
                continue;
            }
            double distance = toMonarch.lengthSquared();
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = projectile;
            }
        }
        return closest;
    }

    @Override
    public void start() {
        ProjectileEntity projectile = this.threat;
        this.threat = null;
        this.slots.clear();
        this.releaseTicks = -1;
        if (projectile == null || !(monarch.getWorld() instanceof ServerWorld world)) {
            return;
        }
        CustomValues tuning = monarch.tuning();
        int segments = Math.max(1, tuning.getInt(FrozenDepthsTuning.ICE_WALL_SEGMENTS, 5));
        double distance = tuning.getDouble(FrozenDepthsTuning.ICE_WALL_DISTANCE, 5.0);
        int arrival = planWall(world, projectile, segments, distance);
        if (slots.isEmpty()) {
            monarch.iceWallCooldown = 20;
            return;
        }
        monarch.iceWallCooldown = Math.max(20, tuning.getInt(FrozenDepthsTuning.ICE_WALL_COOLDOWN, 300));
        boolean animate = !monarch.isMovementLocked() && !monarch.isCasting();
        if (animate) {
            monarch.playCastAnimation(FrostMonarchAnimationTimes.Cast.WALL);
        }
        this.releaseTicks = animate ? MathHelper.clamp(arrival - RELEASE_SAFETY_TICKS, 0, FrostMonarchAnimationTimes.WALL_RELEASE_TICK) : 0;
        if (this.releaseTicks == 0) {
            raiseWall(world);
        }
    }

    @Override
    public void tick() {
        if (releaseTicks > 0 && --releaseTicks == 0 && monarch.getWorld() instanceof ServerWorld world) {
            raiseWall(world);
        }
    }

    private int planWall(ServerWorld world, ProjectileEntity projectile, int segments, double distance) {
        Vec3d toThreat = projectile.getPos().subtract(monarch.getPos()).multiply(1.0, 0.0, 1.0);
        if (toThreat.lengthSquared() < 1.0E-4) {
            return 0;
        }
        Vec3d forward = toThreat.normalize();
        Vec3d side = new Vec3d(-forward.z, 0.0, forward.x);
        Vec3d planePoint = monarch.getPos().add(forward.multiply(distance));

        Vec3d velocity = projectile.getVelocity();
        double approach = velocity.dotProduct(forward);
        Vec3d crossing = planePoint.add(0.0, monarch.getHeight() * 0.5, 0.0);
        int arrival = 0;
        if (approach < -1.0E-4) {
            double t = planePoint.subtract(projectile.getPos()).dotProduct(forward) / approach;
            crossing = projectile.getPos().add(velocity.multiply(t));
            arrival = (int) Math.floor(t);
        }
        double lateral = MathHelper.clamp(crossing.subtract(planePoint).dotProduct(side), -MAX_LATERAL_SHIFT, MAX_LATERAL_SHIFT);
        Vec3d center = planePoint.add(side.multiply(lateral));
        this.wallYaw = (float) (MathHelper.atan2(-forward.x, forward.z) * MathHelper.DEGREES_PER_RADIAN);

        for (int i = 0; i < segments; i++) {
            Vec3d slot = center.add(side.multiply((i - (segments - 1) / 2.0) * SEGMENT_SPACING));
            Double ground = groundHeight(world, slot);
            if (ground != null) {
                slots.add(new Vec3d(slot.x, ground, slot.z));
            }
        }
        return arrival;
    }

    private void raiseWall(ServerWorld world) {
        if (slots.isEmpty()) {
            return;
        }
        int duration = Math.max(20, monarch.tuning().getInt(FrozenDepthsTuning.ICE_WALL_DURATION, 100));
        double x = 0.0;
        double z = 0.0;
        for (Vec3d slot : slots) {
            world.spawnEntity(new IceWallEntity(world, monarch, slot.x, slot.y, slot.z, wallYaw, duration));
            x += slot.x;
            z += slot.z;
        }
        x /= slots.size();
        z /= slots.size();
        slots.clear();
        monarch.playCastFx();
        world.playSound(null, x, monarch.getY(), z, MRPGLibSounds.FROZEN_WALL.soundEvent(), SoundCategory.HOSTILE, 1.2F, 1.0F);
    }

    @Nullable
    private Double groundHeight(ServerWorld world, Vec3d slot) {
        float height = ModEntities.ICE_WALL.getHeight();
        int origin = monarch.getBlockY();
        BlockPos.Mutable below = new BlockPos.Mutable();
        for (int step = 0; step <= 2 * Math.max(GROUND_SCAN_UP, GROUND_SCAN_DOWN); step++) {
            int offset = (step + 1) / 2 * (step % 2 == 1 ? 1 : -1);
            if (offset > GROUND_SCAN_UP || -offset > GROUND_SCAN_DOWN) {
                continue;
            }
            below.set(MathHelper.floor(slot.x), origin + offset - 1, MathHelper.floor(slot.z));
            BlockState state = world.getBlockState(below);
            VoxelShape shape = state.getCollisionShape(world, below);
            if (shape.isEmpty()) {
                continue;
            }
            double surface = below.getY() + shape.getMax(Direction.Axis.Y);
            Box box = new Box(slot.x - FOOTPRINT_HALF, surface, slot.z - FOOTPRINT_HALF, slot.x + FOOTPRINT_HALF, surface + height, slot.z + FOOTPRINT_HALF);
            if (world.isBlockSpaceEmpty(null, box)) {
                return surface;
            }
        }
        return null;
    }

    @Override
    public boolean shouldContinue() {
        return releaseTicks > 0;
    }

    @Override
    public void stop() {
        if (!slots.isEmpty() && monarch.getWorld() instanceof ServerWorld world) {
            raiseWall(world);
        }
        releaseTicks = -1;
    }
}

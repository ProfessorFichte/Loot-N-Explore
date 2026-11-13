package lne.entity.frozen_depths.conjured;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PortalPlacement {
    private static final double HOVER = 0.5;
    private static final double HALF_WIDTH = 2.0;
    private static final double HEIGHT = 4.0;
    private static final double MIN_SPACING = 5.0;
    private static final int ATTEMPTS = 40;
    private static final int ATTEMPTS_WITH_SIGHT = 30;
    private static final int SCAN_UP = 6;
    private static final int SCAN_DOWN = 10;

    private PortalPlacement() {
    }

    public static EntityType<?> resolveType(String id, EntityType<?> fallback) {
        Identifier parsed = Identifier.tryParse(id);
        return parsed == null ? fallback : Registries.ENTITY_TYPE.getOrEmpty(parsed).orElse(fallback);
    }

    public static int open(LivingEntity caster, EntityType<?> summonType, int count, double radius, int lifetime, int firstSpawnDelay, int spawnInterval) {
        if (!(caster.getWorld() instanceof ServerWorld world) || count <= 0) {
            return 0;
        }
        List<Vec3d> spots = findSpots(world, caster.getPos(), caster.getRandom(), count, Math.max(2.0, radius), caster, false);
        for (Vec3d spot : spots) {
            openAt(world, caster, spot, summonType, lifetime, firstSpawnDelay, spawnInterval);
        }
        return spots.size();
    }

    public static EternalWinterPortalEntity openAt(ServerWorld world, @Nullable LivingEntity owner, Vec3d spot, EntityType<?> summonType,
                                                   int lifetime, int firstSpawnDelay, int spawnInterval) {
        EternalWinterPortalEntity portal = new EternalWinterPortalEntity(world, owner, summonType, lifetime, firstSpawnDelay, spawnInterval);
        portal.refreshPositionAndAngles(spot.x, spot.y, spot.z, world.getRandom().nextFloat() * 360.0F, 0.0F);
        world.spawnEntity(portal);
        MrpgFx.play(world, LNEFx.ETERNAL_WINTER_PORTAL_OPEN, FxParams.at(spot.add(0, portal.getHeight() * 0.5, 0)));
        return portal;
    }

    public static List<Vec3d> findSpots(ServerWorld world, Vec3d center, Random random, int count, double radius,
                                        @Nullable Entity viewer, boolean surfaceOnly) {
        List<Vec3d> spots = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Vec3d spot = findSpot(world, center, random, radius, spots, viewer, surfaceOnly);
            if (spot != null) {
                spots.add(spot);
            }
        }
        return spots;
    }

    @Nullable
    private static Vec3d findSpot(ServerWorld world, Vec3d center, Random random, double radius, List<Vec3d> taken,
                                  @Nullable Entity viewer, boolean surfaceOnly) {
        double minDistance = Math.min(4.0, radius * 0.5);
        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = minDistance + random.nextDouble() * (radius - minDistance);
            double x = center.x + Math.cos(angle) * distance;
            double z = center.z + Math.sin(angle) * distance;
            Vec3d spot = surfaceOnly ? surfaceSpot(world, x, z) : groundSpot(world, x, z, MathHelper.floor(center.y));
            if (spot == null || tooClose(spot, taken)) {
                continue;
            }
            if (viewer != null && attempt < ATTEMPTS_WITH_SIGHT && !inSight(world, viewer, spot)) {
                continue;
            }
            return spot;
        }
        return null;
    }

    @Nullable
    private static Vec3d groundSpot(ServerWorld world, double x, double z, int centerY) {
        for (int y = centerY + SCAN_UP; y >= centerY - SCAN_DOWN; y--) {
            Vec3d spot = spotOn(world, x, y, z);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }

    @Nullable
    private static Vec3d surfaceSpot(ServerWorld world, double x, double z) {
        int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MathHelper.floor(x), MathHelper.floor(z));
        Vec3d spot = spotOn(world, x, y, z);
        return spot != null && world.isSkyVisible(BlockPos.ofFloored(spot)) ? spot : null;
    }

    @Nullable
    private static Vec3d spotOn(ServerWorld world, double x, int y, double z) {
        BlockPos below = new BlockPos(MathHelper.floor(x), y - 1, MathHelper.floor(z));
        BlockState ground = world.getBlockState(below);
        if (!ground.isSideSolidFullSquare(world, below, Direction.UP)) {
            return null;
        }
        Box space = new Box(x - HALF_WIDTH, y + HOVER, z - HALF_WIDTH, x + HALF_WIDTH, y + HOVER + HEIGHT, z + HALF_WIDTH);
        return world.isSpaceEmpty(space) && !world.containsFluid(space) ? new Vec3d(x, y + HOVER, z) : null;
    }

    private static boolean tooClose(Vec3d spot, List<Vec3d> taken) {
        for (Vec3d other : taken) {
            if (other.squaredDistanceTo(spot) < MIN_SPACING * MIN_SPACING) {
                return true;
            }
        }
        return false;
    }

    private static boolean inSight(ServerWorld world, Entity viewer, Vec3d spot) {
        Vec3d center = spot.add(0, HEIGHT * 0.5, 0);
        return world.raycast(new RaycastContext(viewer.getEyePos(), center, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, viewer)).getType() == HitResult.Type.MISS;
    }
}

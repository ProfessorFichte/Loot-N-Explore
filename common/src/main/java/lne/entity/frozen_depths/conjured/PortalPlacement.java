package lne.entity.frozen_depths.conjured;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PortalPlacement {
    private static final double HOVER = 0.3;
    private static final double MIN_SPACING = 3.0;
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
        List<Vec3d> taken = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Vec3d spot = findSpot(world, caster, Math.max(2.0, radius), taken);
            if (spot == null) {
                continue;
            }
            taken.add(spot);
            EternalWinterPortalEntity portal = new EternalWinterPortalEntity(world, caster, summonType, lifetime, firstSpawnDelay, spawnInterval);
            portal.refreshPositionAndAngles(spot.x, spot.y, spot.z, caster.getRandom().nextFloat() * 360.0F, 0.0F);
            world.spawnEntity(portal);
            MrpgFx.play(world, LNEFx.ETERNAL_WINTER_PORTAL_OPEN, FxParams.at(spot.add(0, portal.getHeight() * 0.5, 0)));
        }
        return taken.size();
    }

    @Nullable
    private static Vec3d findSpot(ServerWorld world, LivingEntity caster, double radius, List<Vec3d> taken) {
        Random random = caster.getRandom();
        double minDistance = Math.min(4.0, radius * 0.5);
        Vec3d eyes = caster.getEyePos();
        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = minDistance + random.nextDouble() * (radius - minDistance);
            double x = caster.getX() + Math.cos(angle) * distance;
            double z = caster.getZ() + Math.sin(angle) * distance;
            Vec3d spot = groundSpot(world, x, z, caster.getBlockY());
            if (spot == null || tooClose(spot, taken)) {
                continue;
            }
            if (attempt < ATTEMPTS_WITH_SIGHT && !inSight(world, caster, eyes, spot)) {
                continue;
            }
            return spot;
        }
        return null;
    }

    @Nullable
    private static Vec3d groundSpot(ServerWorld world, double x, double z, int casterY) {
        BlockPos.Mutable pos = new BlockPos.Mutable(MathHelper.floor(x), casterY + SCAN_UP, MathHelper.floor(z));
        for (int y = casterY + SCAN_UP; y >= casterY - SCAN_DOWN; y--) {
            pos.setY(y - 1);
            BlockState below = world.getBlockState(pos);
            if (!below.isSideSolidFullSquare(world, pos, Direction.UP)) {
                continue;
            }
            Box space = new Box(x - 1.0, y + HOVER, z - 1.0, x + 1.0, y + HOVER + 2.0, z + 1.0);
            if (world.isSpaceEmpty(space) && !world.containsFluid(space)) {
                return new Vec3d(x, y + HOVER, z);
            }
        }
        return null;
    }

    private static boolean tooClose(Vec3d spot, List<Vec3d> taken) {
        for (Vec3d other : taken) {
            if (other.squaredDistanceTo(spot) < MIN_SPACING * MIN_SPACING) {
                return true;
            }
        }
        return false;
    }

    private static boolean inSight(ServerWorld world, LivingEntity caster, Vec3d eyes, Vec3d spot) {
        Vec3d center = spot.add(0, 1.0, 0);
        return world.raycast(new RaycastContext(eyes, center, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, caster)).getType() == HitResult.Type.MISS;
    }
}

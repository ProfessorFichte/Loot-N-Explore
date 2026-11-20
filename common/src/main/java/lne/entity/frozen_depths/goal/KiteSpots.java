package lne.entity.frozen_depths.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class KiteSpots {
    private static final int DIRECTIONS = 16;
    private static final double[] DISTANCES = {5.0, 8.0, 12.0};
    private static final int[] HEIGHTS = {0, 1, -1, 2, -2};
    private static final int OPEN_RADIUS = 2;
    private static final int DROP_DEPTH = 4;
    private static final int PATH_CHECKS = 4;
    private static final double BEHIND_CHECK = 3.0;

    private KiteSpots() {
    }

    @Nullable
    public static Vec3d findEscape(MobEntity mob, LivingEntity threat) {
        List<Candidate> candidates = new ArrayList<>();
        Vec3d from = mob.getPos();
        Vec3d threatPos = threat.getPos();
        double currentDistance = horizontalDistance(from, threatPos);
        float offset = mob.getRandom().nextFloat() * MathHelper.TAU / DIRECTIONS;
        for (int i = 0; i < DIRECTIONS; i++) {
            float angle = offset + i * MathHelper.TAU / DIRECTIONS;
            Vec3d direction = new Vec3d(MathHelper.cos(angle), 0.0, MathHelper.sin(angle));
            for (double distance : DISTANCES) {
                Vec3d column = from.add(direction.multiply(distance));
                for (int dy : HEIGHTS) {
                    Vec3d spot = standingSpot(mob, column, mob.getBlockY() + dy);
                    if (spot == null) {
                        continue;
                    }
                    double score = score(mob.getWorld(), spot, from, threatPos, currentDistance);
                    candidates.add(new Candidate(spot, score));
                    break;
                }
            }
        }
        candidates.sort(Comparator.comparingDouble(Candidate::score).reversed());
        int checked = 0;
        for (Candidate candidate : candidates) {
            if (checked++ >= PATH_CHECKS) {
                break;
            }
            Path path = mob.getNavigation().findPathTo(candidate.spot().x, candidate.spot().y, candidate.spot().z, 0);
            if (path != null && path.reachesTarget()) {
                return candidate.spot();
            }
        }
        return null;
    }

    public static boolean startEscape(MobEntity mob, LivingEntity threat, double speed) {
        Vec3d spot = findEscape(mob, threat);
        return spot != null && mob.getNavigation().startMovingTo(spot.x, spot.y, spot.z, speed);
    }

    public static boolean isOpenBehind(MobEntity mob, LivingEntity threat) {
        Vec3d away = mob.getPos().subtract(threat.getPos()).multiply(1.0, 0.0, 1.0);
        if (away.lengthSquared() < 1.0E-4) {
            return false;
        }
        Vec3d direction = away.normalize();
        for (double step = 1.0; step <= BEHIND_CHECK; step += 1.0) {
            Vec3d column = mob.getPos().add(direction.multiply(step));
            Vec3d spot = standingSpot(mob, column, mob.getBlockY());
            if (spot == null || nearDrop(mob.getWorld(), BlockPos.ofFloored(spot))) {
                return false;
            }
        }
        return true;
    }

    public static double openness(World world, BlockPos feet) {
        int open = 0;
        int total = 0;
        for (int dx = -OPEN_RADIUS; dx <= OPEN_RADIUS; dx++) {
            for (int dz = -OPEN_RADIUS; dz <= OPEN_RADIUS; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }
                total++;
                BlockPos cell = feet.add(dx, 0, dz);
                if (isWalkable(world, cell) || isWalkable(world, cell.up()) || isWalkable(world, cell.down())) {
                    open++;
                }
            }
        }
        return (double) open / total;
    }

    public static boolean nearDrop(World world, BlockPos feet) {
        for (int dx = -OPEN_RADIUS; dx <= OPEN_RADIUS; dx++) {
            for (int dz = -OPEN_RADIUS; dz <= OPEN_RADIUS; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }
                BlockPos cell = feet.add(dx, 0, dz);
                if (!isPassable(world, cell) || !isPassable(world, cell.up())) {
                    continue;
                }
                boolean ground = false;
                for (int down = 1; down <= DROP_DEPTH; down++) {
                    if (!isPassable(world, cell.down(down))) {
                        ground = true;
                        break;
                    }
                }
                if (!ground) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public static Vec3d standingSpot(MobEntity mob, Vec3d column, int y) {
        World world = mob.getWorld();
        BlockPos feet = BlockPos.ofFloored(column.x, y, column.z);
        BlockPos ground = feet.down();
        if (!world.getBlockState(ground).isSideSolidFullSquare(world, ground, Direction.UP)) {
            return null;
        }
        Vec3d spot = new Vec3d(feet.getX() + 0.5, feet.getY(), feet.getZ() + 0.5);
        Box box = mob.getDimensions(mob.getPose()).getBoxAt(spot);
        return world.isSpaceEmpty(mob, box) && !world.containsFluid(box) ? spot : null;
    }

    private static double score(World world, Vec3d spot, Vec3d from, Vec3d threat, double currentDistance) {
        BlockPos feet = BlockPos.ofFloored(spot);
        double distance = horizontalDistance(spot, threat);
        double score = Math.min(distance, 16.0) - currentDistance;
        score += openness(world, feet) * 8.0;
        if (nearDrop(world, feet)) {
            score -= 10.0;
        }
        double pass = closestApproach(from, spot, threat);
        if (pass < 2.5) {
            score -= (2.5 - pass) * 4.0;
        }
        return score;
    }

    private static double closestApproach(Vec3d from, Vec3d to, Vec3d point) {
        Vec3d segment = to.subtract(from).multiply(1.0, 0.0, 1.0);
        Vec3d toPoint = point.subtract(from).multiply(1.0, 0.0, 1.0);
        double lengthSq = segment.lengthSquared();
        double t = lengthSq < 1.0E-6 ? 0.0 : MathHelper.clamp(toPoint.dotProduct(segment) / lengthSq, 0.0, 1.0);
        return toPoint.subtract(segment.multiply(t)).length();
    }

    private static double horizontalDistance(Vec3d a, Vec3d b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    private static boolean isWalkable(World world, BlockPos feet) {
        BlockPos ground = feet.down();
        return isPassable(world, feet) && isPassable(world, feet.up())
                && world.getBlockState(ground).isSideSolidFullSquare(world, ground, Direction.UP);
    }

    private static boolean isPassable(World world, BlockPos pos) {
        return world.getBlockState(pos).getCollisionShape(world, pos).isEmpty();
    }

    private record Candidate(Vec3d spot, double score) {
    }
}

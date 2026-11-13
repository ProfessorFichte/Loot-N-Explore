package lne.entity.relic;

import com.mrpg_lib.util.AllyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;

public final class RelicMinionGoals {
    private static final int RECENT_TICKS = 100;
    private static final double GUARD_RADIUS = 16.0;
    private static final double LEASH_RADIUS = 24.0;

    private RelicMinionGoals() {
    }

    @Nullable
    private static LivingEntity ownerOf(MobEntity minion) {
        return minion instanceof Ownable ownable && ownable.getOwner() instanceof LivingEntity owner && owner.isAlive() ? owner : null;
    }

    public static class ProtectOwner<T extends MobEntity & Ownable> extends Goal {
        private final T minion;
        private int scanCooldown;

        public ProtectOwner(T minion) {
            this.minion = minion;
            setControls(EnumSet.of(Control.TARGET));
        }

        @Override
        public boolean canStart() {
            return ownerOf(minion) != null;
        }

        @Override
        public boolean shouldContinue() {
            return canStart();
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void stop() {
            minion.setTarget(null);
        }

        @Override
        public void tick() {
            var owner = ownerOf(minion);
            if (owner == null) return;
            var current = minion.getTarget();
            var wanted = firstValid(owner,
                    recent(owner.getAttacker(), owner.getLastAttackedTime(), owner),
                    recent(owner.getAttacking(), owner.getLastAttackTime(), owner),
                    recent(minion.getAttacker(), minion.getLastAttackedTime(), minion),
                    current);
            if (wanted == null && --scanCooldown <= 0) {
                scanCooldown = 10;
                wanted = nearestThreat(owner);
            }
            if (wanted != current) {
                minion.setTarget(wanted);
            }
        }

        @Nullable
        private static LivingEntity recent(@Nullable LivingEntity candidate, int since, LivingEntity holder) {
            return candidate != null && holder.age - since < RECENT_TICKS ? candidate : null;
        }

        @Nullable
        private LivingEntity firstValid(LivingEntity owner, LivingEntity... candidates) {
            for (var candidate : candidates) {
                if (isValid(owner, candidate)) return candidate;
            }
            return null;
        }

        private boolean isValid(LivingEntity owner, @Nullable LivingEntity candidate) {
            return candidate != null && candidate.isAlive() && candidate != owner && candidate != minion
                    && candidate.squaredDistanceTo(owner) <= LEASH_RADIUS * LEASH_RADIUS
                    && AllyHelper.canHurt(minion, candidate);
        }

        @Nullable
        private LivingEntity nearestThreat(LivingEntity owner) {
            var box = new Box(owner.getBlockPos()).expand(GUARD_RADIUS);
            return minion.getWorld().getEntitiesByClass(MobEntity.class, box, mob -> isThreat(owner, mob)).stream()
                    .min(Comparator.comparingDouble(mob -> mob.squaredDistanceTo(owner)))
                    .orElse(null);
        }

        private boolean isThreat(LivingEntity owner, MobEntity mob) {
            if (!isValid(owner, mob) || mob instanceof Ownable ownable && ownable.getOwner() == owner) return false;
            var target = mob.getTarget();
            boolean targetsUs = target == owner || target instanceof Ownable ownable && ownable.getOwner() == owner;
            return (mob instanceof Monster || targetsUs) && minion.getVisibilityCache().canSee(mob);
        }
    }

    public static class FollowOwner<T extends MobEntity & Ownable> extends Goal {
        private static final double START_DISTANCE = 6.0;
        private static final double STOP_DISTANCE = 2.5;
        private static final double TELEPORT_DISTANCE = 20.0;

        private final T minion;
        private int repathCooldown;

        public FollowOwner(T minion) {
            this.minion = minion;
            setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            var owner = ownerOf(minion);
            return owner != null && minion.getTarget() == null && minion.squaredDistanceTo(owner) > START_DISTANCE * START_DISTANCE;
        }

        @Override
        public boolean shouldContinue() {
            var owner = ownerOf(minion);
            return owner != null && minion.getTarget() == null && minion.squaredDistanceTo(owner) > STOP_DISTANCE * STOP_DISTANCE;
        }

        @Override
        public void start() {
            repathCooldown = 0;
        }

        @Override
        public void stop() {
            minion.getNavigation().stop();
        }

        @Override
        public void tick() {
            var owner = ownerOf(minion);
            if (owner == null) return;
            minion.getLookControl().lookAt(owner, 10.0F, minion.getMaxLookPitchChange());
            if (--repathCooldown > 0) return;
            repathCooldown = 10;
            if (minion.squaredDistanceTo(owner) > TELEPORT_DISTANCE * TELEPORT_DISTANCE && teleportNear(owner)) {
                return;
            }
            minion.getNavigation().startMovingTo(owner, 1.2);
        }

        private boolean teleportNear(LivingEntity owner) {
            var world = minion.getWorld();
            var random = minion.getRandom();
            for (int attempt = 0; attempt < 10; attempt++) {
                int x = MathHelper.floor(owner.getX()) + random.nextBetween(-3, 3);
                int z = MathHelper.floor(owner.getZ()) + random.nextBetween(-3, 3);
                int y = MathHelper.floor(owner.getY());
                if (Math.abs(x - owner.getX()) < 2 && Math.abs(z - owner.getZ()) < 2) continue;
                var below = new BlockPos(x, y - 1, z);
                if (!world.getBlockState(below).isSideSolidFullSquare(world, below, Direction.UP)) continue;
                var box = minion.getDimensions(minion.getPose()).getBoxAt(x + 0.5, y, z + 0.5);
                if (!world.isSpaceEmpty(minion, box)) continue;
                minion.refreshPositionAndAngles(x + 0.5, y, z + 0.5, minion.getYaw(), minion.getPitch());
                minion.getNavigation().stop();
                return true;
            }
            return false;
        }
    }

    public static class WanderNearOwner<T extends MobEntity & Ownable> extends Goal {
        private static final double WANDER_RADIUS = 4.0;

        private final T minion;

        public WanderNearOwner(T minion) {
            this.minion = minion;
            setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return ownerOf(minion) != null && minion.getTarget() == null && minion.getNavigation().isIdle()
                    && minion.getRandom().nextInt(30) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return minion.getTarget() == null && !minion.getNavigation().isIdle();
        }

        @Override
        public void start() {
            var owner = ownerOf(minion);
            if (owner == null) return;
            double angle = minion.getRandom().nextDouble() * Math.PI * 2;
            double distance = 1.5 + minion.getRandom().nextDouble() * (WANDER_RADIUS - 1.5);
            minion.getNavigation().startMovingTo(owner.getX() + Math.cos(angle) * distance, owner.getY(),
                    owner.getZ() + Math.sin(angle) * distance, 0.9);
        }
    }
}

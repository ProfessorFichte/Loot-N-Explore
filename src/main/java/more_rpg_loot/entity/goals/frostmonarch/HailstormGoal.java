package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.projectile.FrostballEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;
import java.util.List;

public class HailstormGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private boolean hasStarted;
    private java.util.List<DelayedHailball> delayedHailballs = new java.util.ArrayList<>();

    public static final float HAILSTORM_RADIUS = 40.0F;
    public static final float HAILSTORM_MIN_DISTANCE = 15.0F;

    private static class DelayedHailball {
        double x, y, z;
        int delay;

        DelayedHailball(double x, double y, double z, int delay) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.delay = delay;
        }
    }

    public HailstormGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if(!monarch.isPerformingAbility() && !isCasting){
            LivingEntity target = monarch.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            if (monarch.hailStormCooldown > 0) {
                return false;
            }

            boolean canSeeTarget = monarch.getVisibilityCache().canSee(target);
            if (!canSeeTarget) {
                return false;
            }

            double distanceSq = monarch.squaredDistanceTo(target);
            if (distanceSq > HAILSTORM_MIN_DISTANCE * HAILSTORM_MIN_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.isCasting = true;
        this.channelTicks = 20;
        this.hasStarted = false;
        this.delayedHailballs.clear();
        monarch.setCasting(true);
    }

    @Override
    public void tick() {
        if (!hasStarted) {
            hasStarted = true;
        }

        // Only keep casting flag during the initial channel
        if (channelTicks > 0) {
            if (!monarch.isCasting()) {
                monarch.setCasting(true);
            }

            if (channelTicks == 10) {
                castHailstormSpell();
            }

            channelTicks--;
        } else {
            // Channel complete - allow monarch to move
            if (monarch.isCasting()) {
                monarch.setCasting(false);
            }
        }

        // Process delayed hailballs (continues even after channel ends)
        if (!monarch.getWorld().isClient()) {
            java.util.Iterator<DelayedHailball> iterator = delayedHailballs.iterator();
            while (iterator.hasNext()) {
                DelayedHailball delayed = iterator.next();
                delayed.delay--;
                if (delayed.delay <= 0) {
                    spawnHailball(delayed.x, delayed.y, delayed.z);
                    iterator.remove();
                }
            }
        }
    }

    private void castHailstormSpell() {
        if (!monarch.getWorld().isClient()) {
            List<Entity> entities = monarch.getWorld().getOtherEntities(
                monarch,
                monarch.getBoundingBox().expand(HAILSTORM_RADIUS)
            );

            net.minecraft.util.math.random.Random random = monarch.getRandom();
            int targetedHailballsPerEntity = monarch.getScaledCount(8);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living && !(entity instanceof MobEntity)) {
                    for (int i = 0; i < targetedHailballsPerEntity; i++) {
                        spawnHailball(living.getX() + (random.nextDouble() - 0.5) * 4.0,
                                     living.getY() + 10.0 + random.nextDouble() * 8.0,
                                     living.getZ() + (random.nextDouble() - 0.5) * 4.0);
                    }
                }
            }

            int randomHailballs = monarch.getScaledCount(40);
            for (int i = 0; i < randomHailballs; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = random.nextDouble() * HAILSTORM_RADIUS;
                double x = monarch.getX() + Math.cos(angle) * distance;
                double z = monarch.getZ() + Math.sin(angle) * distance;
                double y = monarch.getY() + 10.0 + random.nextDouble() * 10.0;

                spawnHailball(x, y, z);
            }

            int delayedHailballCount = monarch.getScaledCount(15 + random.nextInt(11));
            for (int i = 0; i < delayedHailballCount; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double distance = random.nextDouble() * HAILSTORM_RADIUS;
                double x = monarch.getX() + Math.cos(angle) * distance;
                double z = monarch.getZ() + Math.sin(angle) * distance;
                double y = monarch.getY() + 10.0 + random.nextDouble() * 12.0;

                int delay = 10 + random.nextInt(41);
                delayedHailballs.add(new DelayedHailball(x, y, z, delay));
            }
        }
    }

    private void spawnHailball(double x, double y, double z) {
        FrostballEntity frostball = new FrostballEntity(monarch.getWorld(), monarch);
        frostball.setPosition(x, y, z);

        net.minecraft.util.math.random.Random random = monarch.getRandom();
        frostball.setVelocity(
            (random.nextDouble() - 0.5) * 0.1,
            -0.5,
            (random.nextDouble() - 0.5) * 0.1
        );

        monarch.getWorld().spawnEntity(frostball);
    }

    @Override
    public boolean shouldContinue() {
        return !hasStarted || channelTicks > 0 || !delayedHailballs.isEmpty();
    }

    @Override
    public void stop() {
        this.isCasting = false;
        this.channelTicks = 0;
        this.hasStarted = false;
        this.delayedHailballs.clear();
        monarch.setCasting(false);

        monarch.hailStormCooldown = 400;
        monarch.globalAbilityCooldown = 20;
    }
}

package more_rpg_loot.entity.goals.frostmonarch;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.mob.FrosthauntEntity;
import more_rpg_loot.entity.projectile.BarrierIcicleEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Difficulty;

import java.util.EnumSet;

public class IceBarrierGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;
    private int barrierUsesRemaining;
    private int lastHealthThresholdReached = -1;

    private static final int BARRIER_DURATION = 50;
    private static final int BARRIER_GAME_TICKS = 100;
    private static final float[] HEALTH_THRESHOLDS = {0.75F, 0.5F, 0.25F};

    public IceBarrierGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.barrierUsesRemaining = getBarrierUsesForDifficulty();
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP, Goal.Control.TARGET));
    }

    private int getBarrierUsesForDifficulty() {
        if (monarch.getWorld() == null) {
            return 3; // Default fallback
        }

        Difficulty difficulty = monarch.getWorld().getDifficulty();
        return switch (difficulty) {
            case EASY -> 2;
            case NORMAL -> 3;
            case HARD -> 4;
            default -> 3; // PEACEFUL
        };
    }

    @Override
    public boolean canStart() {
        if (!monarch.isPerformingAbility() && barrierUsesRemaining > 0 && !isCasting) {
            float healthPercent = monarch.getHealth() / monarch.getMaxHealth();

            for (int i = 0; i < HEALTH_THRESHOLDS.length; i++) {
                if (healthPercent <= HEALTH_THRESHOLDS[i] && i > lastHealthThresholdReached) {
                    lastHealthThresholdReached = i;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.isCasting = true;
        this.channelTicks = BARRIER_DURATION;
        monarch.setCasting(true);
        barrierUsesRemaining--;

        if (!monarch.getWorld().isClient) {
            monarch.setBarrierTimer(BARRIER_GAME_TICKS);
            spawnProtectiveIcicles();

            // Spawn FrostHaunt entities when barrier starts
            spawnFrostHaunts();

            monarch.getWorld().playSound(
                    null,
                    monarch.getX(), monarch.getY(), monarch.getZ(),
                    SoundEvents.BLOCK_GLASS_BREAK,
                    SoundCategory.HOSTILE,
                    1.0f, 0.5f
            );
        }
    }

    @Override
    public void tick() {
        if (channelTicks > 0 && !monarch.isCasting()) {
            monarch.setCasting(true);
        }

        if (!monarch.getWorld().isClient() && channelTicks > 0) {
            float totalHealPercent = 0.3F - ((3 - barrierUsesRemaining - 1) * 0.05F);
            float difficultyMultiplier = monarch.getHealingForDifficulty();
            float healPerTick = (monarch.getMaxHealth() * totalHealPercent * difficultyMultiplier) / BARRIER_DURATION;
            monarch.heal(healPerTick);

            monarch.setBarrierTimer(channelTicks * 2);

            spawnBarrierParticles();
        }

        channelTicks--;
    }

    private void spawnProtectiveIcicles() {
        int icicleCount = 8;
        double radius = 0.8;

        for (int i = 0; i < icicleCount; i++) {
            double angle = 2 * Math.PI * i / icicleCount;
            double x = monarch.getX() + Math.cos(angle) * radius;
            double z = monarch.getZ() + Math.sin(angle) * radius;
            double y = monarch.getY();

            BarrierIcicleEntity icicle = new BarrierIcicleEntity(
                monarch.getWorld(),
                x,
                y,
                z,
                BARRIER_GAME_TICKS + BarrierIcicleEntity.RETRACT_DURATION,
                monarch
            );

            monarch.getWorld().spawnEntity(icicle);
        }
    }

    private void spawnBarrierParticles() {
        if (monarch.getWorld() instanceof ServerWorld serverWorld) {
            double radius = 3.5;
            int particleCount = 12;

            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double xOffset = radius * Math.cos(angle);
                double zOffset = radius * Math.sin(angle);
                double yOffset = monarch.getHeight() / 2 + (monarch.getRandom().nextDouble() - 0.5) * monarch.getHeight();

                serverWorld.spawnParticles(
                        ParticleTypes.SNOWFLAKE,
                        monarch.getX() + xOffset,
                        monarch.getY() + yOffset,
                        monarch.getZ() + zOffset,
                        1, 0, 0, 0, 0
                );

                if (monarch.getRandom().nextFloat() < 0.3f) {
                    serverWorld.spawnParticles(
                            ParticleTypes.ITEM_SNOWBALL,
                            monarch.getX() + xOffset,
                            monarch.getY() + yOffset,
                            monarch.getZ() + zOffset,
                            1, 0, 0.1, 0, 0
                    );
                }
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return channelTicks > 0;
    }

    @Override
    public void stop() {
        this.isCasting = false;
        this.channelTicks = 0;

        monarch.setCasting(false);
        monarch.setBarrierTimer(0);

        if (!monarch.getWorld().isClient) {
            monarch.getWorld().playSound(
                    null,
                    monarch.getX(), monarch.getY(), monarch.getZ(),
                    SoundEvents.BLOCK_GLASS_BREAK,
                    SoundCategory.HOSTILE,
                    0.8f, 1.5f
            );
        }

        monarch.globalAbilityCooldown = 20;
    }

    public Integer getSpawnAmountPerDifficulty() {
        Difficulty difficulty = monarch.getWorld().getDifficulty();
        return switch (difficulty) {
            case PEACEFUL, EASY -> 1;
            case NORMAL -> 2;
            case HARD -> 3;
        };
    }

    private void spawnFrostHaunts() {
        if (!(monarch.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        int baseCount = getSpawnAmountPerDifficulty() + monarch.getRandom().nextInt(2);
        int hauntCount = monarch.getScaledCount(baseCount);

        for (int i = 0; i < hauntCount; i++) {
            double angle = 2 * Math.PI * i / hauntCount;
            double radius = 3.0 + monarch.getRandom().nextDouble() * 2.0;
            double x = monarch.getX() + Math.cos(angle) * radius;
            double z = monarch.getZ() + Math.sin(angle) * radius;
            double y = monarch.getY();

            FrosthauntEntity haunt = new FrosthauntEntity(ModEntities.FROST_HAUNT, serverWorld);
            haunt.refreshPositionAndAngles(x, y, z, (float) (angle * 180 / Math.PI), 0);
            haunt.initialize(serverWorld, serverWorld.getLocalDifficulty(haunt.getBlockPos()),
                SpawnReason.MOB_SUMMONED, null, null);
            haunt.setTarget(monarch.getTarget());

            serverWorld.spawnEntity(haunt);

            serverWorld.spawnParticles(
                ParticleTypes.SNOWFLAKE,
                x, y + 1.0, z,
                20, 0.3, 0.5, 0.3, 0.05
            );
        }
    }
}

package more_rpg_loot.entity.frozen_depths.mob.frostmonarch.goals;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import com.mrpg_lib.entity.config.CustomValues;
import more_rpg_loot.entity.frozen_depths.projectile.BarrierIcicleEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.EnumSet;

public class IceBarrierGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;

    private static final int BARRIER_DURATION = 50;
    private static final int BARRIER_GAME_TICKS = 100;
    private static final float[] HEALTH_THRESHOLDS = {0.75F, 0.5F, 0.25F};

    public IceBarrierGoal(FrostMonarchEntity monarch) {
        this.monarch = monarch;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP, Goal.Control.TARGET));
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean canStart() {
        if (!monarch.isPerformingAbility() && monarch.getBarrierUsesRemaining() > 0 && !isCasting) {
            float healthPercent = monarch.getHealth() / monarch.getMaxHealth();

            for (int i = 0; i < HEALTH_THRESHOLDS.length; i++) {
                if (healthPercent <= HEALTH_THRESHOLDS[i] && i > monarch.getBarrierThresholdReached()) {
                    monarch.setBarrierThresholdReached(i);
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
        monarch.consumeBarrierUse();

        if (!monarch.getWorld().isClient) {
            monarch.setBarrierTimer(BARRIER_GAME_TICKS);
            spawnProtectiveIcicles();

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
            float totalHealPercent = 0.3F - (Math.max(0, monarch.getBarrierThresholdReached()) * 0.05F);
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

    private EntityType<?> summonType(CustomValues tuning) {
        Identifier id = Identifier.tryParse(tuning.getString(FrozenDepthsTuning.BARRIER_SUMMON_ENTITY, ""));
        return id != null ? Registries.ENTITY_TYPE.getOrEmpty(id).orElse(ModEntities.FROST_HAUNT) : ModEntities.FROST_HAUNT;
    }

    private void spawnFrostHaunts() {
        if (!(monarch.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        CustomValues tuning = monarch.tuning();
        int baseCount = tuning.getInt(FrozenDepthsTuning.BARRIER_SUMMON_COUNT, 1);
        if (baseCount <= 0) {
            return;
        }
        int hauntCount = monarch.getScaledCount(baseCount + monarch.getRandom().nextInt(2));
        EntityType<?> type = summonType(tuning);

        for (int i = 0; i < hauntCount; i++) {
            double angle = 2 * Math.PI * i / hauntCount;
            double radius = 3.0 + monarch.getRandom().nextDouble() * 2.0;
            double x = monarch.getX() + Math.cos(angle) * radius;
            double z = monarch.getZ() + Math.sin(angle) * radius;
            double y = monarch.getY();

            if (!(type.create(serverWorld) instanceof MobEntity haunt)) {
                return;
            }
            haunt.refreshPositionAndAngles(x, y, z, (float) (angle * 180 / Math.PI), 0);
            haunt.initialize(serverWorld, serverWorld.getLocalDifficulty(haunt.getBlockPos()),
                SpawnReason.MOB_SUMMONED, null);
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

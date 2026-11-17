package lne.entity.frozen_depths.mob.frostmonarch.goals;

import lne.entity.ModEntities;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchAnimationTimes;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchLines;
import com.mrpg_lib.entity.config.CustomValues;
import lne.entity.frozen_depths.conjured.EternalWinterPortalEntity;
import lne.entity.frozen_depths.conjured.PortalPlacement;
import lne.entity.frozen_depths.projectile.BarrierIcicleEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import com.mrpg_lib.sounds.MRPGLibSounds;

import java.util.EnumSet;

public class IceBarrierGoal extends Goal {
    private final FrostMonarchEntity monarch;
    private int channelTicks;
    private boolean isCasting;

    private static final int BARRIER_DURATION = 50;
    private static final int WALL_FX_INTERVAL = 10;
    private static final int BARRIER_GAME_TICKS = 100;
    private static final float[] HEALTH_THRESHOLDS = {0.75F, 0.5F, 0.25F};
    private static final int[] DEFAULT_PORTAL_COUNTS = {1, 2, 2};
    private static final EntityType<?>[] DEFAULT_PORTAL_TYPES = {ModEntities.FROST_HAUNTED, ModEntities.MONARCHS_SOLDIER, ModEntities.MONARCHS_GUARD};

    public static boolean hasPendingThreshold(FrostMonarchEntity monarch) {
        if (monarch.getBarrierUsesRemaining() <= 0 || !monarch.tuning().getBoolean(FrozenDepthsTuning.BARRIER_ENABLED, true)) {
            return false;
        }
        float healthPercent = monarch.getHealth() / monarch.getMaxHealth();
        for (int i = monarch.getBarrierThresholdReached() + 1; i < HEALTH_THRESHOLDS.length; i++) {
            if (healthPercent <= HEALTH_THRESHOLDS[i]) {
                return true;
            }
        }
        return false;
    }

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
        if (!monarch.isPerformingAbility() && monarch.getBarrierUsesRemaining() > 0 && !isCasting
                && monarch.tuning().getBoolean(FrozenDepthsTuning.BARRIER_ENABLED, true)) {
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
        monarch.setChanneling(true);
        monarch.playCastAnimation(FrostMonarchAnimationTimes.Cast.SUMMON);
        monarch.consumeBarrierUse();

        if (!monarch.getWorld().isClient) {
            monarch.setBarrierTimer(BARRIER_GAME_TICKS);
            if (monarch.getWorld() instanceof ServerWorld serverWorld) {
                MrpgFx.play(serverWorld, LNEFx.MONARCH_BARRIER_RAISE, FxParams.at(monarch.getPos()));
            }
            spawnProtectiveIcicles();

            openPortals();

            sound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 1.5F, 0.5F);
            sound(SoundEvents.BLOCK_BEACON_ACTIVATE, 0.8F, 1.6F);
            sound(MRPGLibSounds.ICICLE_CRASH.soundEvent(), 0.6F, 1.0F);
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
        if (channelTicks % WALL_FX_INTERVAL == 0 && monarch.getWorld() instanceof ServerWorld serverWorld) {
            MrpgFx.play(serverWorld, LNEFx.MONARCH_BARRIER_WALL, FxParams.at(monarch.getPos()));
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
        monarch.setChanneling(false);
        monarch.setBarrierTimer(0);

        if (monarch.getWorld() instanceof ServerWorld serverWorld) {
            MrpgFx.play(serverWorld, LNEFx.MONARCH_ICE_BURST, FxParams.at(monarch.getPos().add(0, 1.5, 0)));
            sound(SoundEvents.BLOCK_GLASS_BREAK, 0.8F, 1.5F);
            sound(SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, 1.0F, 0.8F);
            sound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, 0.6F, 0.7F);
        }

        monarch.startAbilityPause();
    }

    private void sound(SoundEvent sound, float volume, float pitch) {
        monarch.getWorld().playSound(null, monarch.getX(), monarch.getY(), monarch.getZ(), sound, SoundCategory.HOSTILE, volume, pitch);
    }

    private void openPortals() {
        CustomValues tuning = monarch.tuning();
        int phase = Math.min(HEALTH_THRESHOLDS.length - 1, Math.max(0, monarch.getBarrierThresholdReached()));
        int count = tuning.getInt(FrozenDepthsTuning.barrierPortalCount(phase), DEFAULT_PORTAL_COUNTS[phase]);
        if (count <= 0) {
            return;
        }
        EntityType<?> type = PortalPlacement.resolveType(tuning.getString(FrozenDepthsTuning.barrierPortalEntity(phase), ""), DEFAULT_PORTAL_TYPES[phase]);
        double radius = tuning.getDouble(FrozenDepthsTuning.PORTAL_SPAWN_RADIUS, 25.0);
        int lifetime = Math.max(20, tuning.getInt(FrozenDepthsTuning.PORTAL_LIFETIME, EternalWinterPortalEntity.DEFAULT_LIFETIME));
        int firstDelay = tuning.getInt(FrozenDepthsTuning.PORTAL_FIRST_SPAWN_DELAY, EternalWinterPortalEntity.DEFAULT_FIRST_SPAWN_DELAY);
        int interval = tuning.getInt(FrozenDepthsTuning.PORTAL_SPAWN_INTERVAL, EternalWinterPortalEntity.DEFAULT_SPAWN_INTERVAL);
        if (PortalPlacement.open(monarch, type, count, radius, lifetime, firstDelay, interval) > 0) {
            FrostMonarchLines.barrierSummon(monarch);
        }
    }
}

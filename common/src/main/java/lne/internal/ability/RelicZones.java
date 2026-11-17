package lne.internal.ability;

import net.minecraft.entity.effect.StatusEffects;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import com.mrpg_lib.util.AllyHelper;
import lne.effects.Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RelicZones {
    private record Storm(double radius, int freezeTicks, long endsAt) {}

    private record DragonCloud(UUID owner, RegistryKey<World> dimension, Vec3d center, double startRadius, double endRadius,
                               long startsAt, int duration, float heal, float damage) {}

    private record Whirlpool(UUID owner, RegistryKey<World> dimension, double radius, long startsAt, int duration,
                             float damage, double push) {}

    private record Tempest(UUID owner, RegistryKey<World> dimension, Vec3d center, double radius, long startsAt, int duration,
                           float damage, int freezeTicks) {}

    private record Bubble(UUID owner, RegistryKey<World> dimension, UUID target, Vec3d offset, Vec3d fallback, long burstsAt,
                          double radius, float damage, int slownessTicks) {}

    private static final Map<UUID, Storm> STORMS = new HashMap<>();
    private static final List<Tempest> TEMPESTS = new ArrayList<>();
    private static final List<Bubble> BUBBLES = new ArrayList<>();
    private static final List<DragonCloud> CLOUDS = new ArrayList<>();
    private static final List<Whirlpool> WHIRLPOOLS = new ArrayList<>();

    private RelicZones() {
    }

    public static void clear() {
        STORMS.clear();
        TEMPESTS.clear();
        BUBBLES.clear();
        CLOUDS.clear();
        WHIRLPOOLS.clear();
    }

    public static void startStorm(ServerPlayerEntity player, double radius, int durationTicks, int freezeTicks) {
        STORMS.put(player.getUuid(), new Storm(radius, freezeTicks, player.getServerWorld().getTime() + durationTicks));
        MrpgFx.play(player.getServerWorld(), LNEFx.FROST_STORM, FxParams.on(player).radius((float) radius).duration(durationTicks));
    }

    public static void startDragonCloud(ServerPlayerEntity player, Vec3d center, double startRadius, double endRadius,
                                        int durationTicks, float heal, float damage) {
        var world = player.getServerWorld();
        CLOUDS.removeIf(cloud -> cloud.owner().equals(player.getUuid()));
        CLOUDS.add(new DragonCloud(player.getUuid(), world.getRegistryKey(), center, startRadius, endRadius,
                world.getTime(), durationTicks, heal, damage));
        MrpgFx.play(world, LNEFx.DRAGON_CLOUD, FxParams.at(center).radius((float) endRadius).duration(durationTicks));
    }

    public static void startWhirlpool(LivingEntity owner, double radius, int durationTicks, float damage, double push) {
        if (!(owner.getWorld() instanceof ServerWorld world)) return;
        WHIRLPOOLS.removeIf(pool -> pool.owner().equals(owner.getUuid()));
        WHIRLPOOLS.add(new Whirlpool(owner.getUuid(), world.getRegistryKey(), radius, world.getTime(), durationTicks, damage, push));
        MrpgFx.play(world, LNEFx.WHIRLPOOL, FxParams.on(owner).radius((float) radius).duration(durationTicks));
    }

    public static void startTempest(LivingEntity owner, Vec3d center, double radius, int durationTicks, float damage, int freezeTicks) {
        if (!(owner.getWorld() instanceof ServerWorld world)) return;
        TEMPESTS.add(new Tempest(owner.getUuid(), world.getRegistryKey(), center, radius, world.getTime(), durationTicks, damage, freezeTicks));
        MrpgFx.play(world, LNEFx.FROZEN_TEMPEST, FxParams.at(center).radius((float) radius).duration(durationTicks));
    }

    public static void startBubbleBarrage(LivingEntity owner, LivingEntity target, int bubbles, double radius, int delayTicks,
                                          float damage, int slownessTicks) {
        if (!(owner.getWorld() instanceof ServerWorld world)) return;
        long now = world.getTime();
        var random = owner.getRandom();
        for (int i = 0; i < Math.max(1, bubbles); i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = i == 0 ? 0.0 : 1.0 + random.nextDouble() * 1.5;
            Vec3d offset = new Vec3d(Math.cos(angle) * distance, 0, Math.sin(angle) * distance);
            BUBBLES.add(new Bubble(owner.getUuid(), world.getRegistryKey(), target.getUuid(), offset, target.getPos().add(offset),
                    now + (long) i * Math.max(1, delayTicks), radius, damage, slownessTicks));
        }
    }

    public static void tick(ServerWorld world) {
        tickTempests(world);
        tickBubbles(world);
        tickStorms(world);
        tickClouds(world);
        tickWhirlpools(world);
    }

    private static void tickWhirlpools(ServerWorld world) {
        if (WHIRLPOOLS.isEmpty()) return;
        long time = world.getTime();
        Iterator<Whirlpool> iterator = WHIRLPOOLS.iterator();
        while (iterator.hasNext()) {
            var pool = iterator.next();
            if (!pool.dimension().equals(world.getRegistryKey())) continue;
            long elapsed = time - pool.startsAt();
            if (!(world.getEntity(pool.owner()) instanceof LivingEntity owner) || !owner.isAlive() || elapsed >= pool.duration()) {
                iterator.remove();
                continue;
            }
            Vec3d center = owner.getPos();
            if (elapsed % 2 == 0) {
                for (var enemy : RelicTargets.enemiesAround(world, owner, center, pool.radius())) {
                    Vec3d away = new Vec3d(enemy.getX() - center.x, 0, enemy.getZ() - center.z);
                    if (away.lengthSquared() < 1.0E-4) away = new Vec3d(1, 0, 0);
                    away = away.normalize();
                    Vec3d swirl = new Vec3d(-away.z, 0, away.x).multiply(0.4);
                    enemy.addVelocity(away.add(swirl).multiply(pool.push()).add(0, 0.04, 0));
                    enemy.velocityModified = true;
                }
            }
            if (elapsed % 20 == 10) {
                for (var enemy : RelicTargets.enemiesAround(world, owner, center, pool.radius())) {
                    RelicTargets.magicDamage(world, owner, enemy, pool.damage());
                }
            }
        }
    }

    private static void tickTempests(ServerWorld world) {
        if (TEMPESTS.isEmpty()) return;
        long time = world.getTime();
        Iterator<Tempest> iterator = TEMPESTS.iterator();
        while (iterator.hasNext()) {
            var tempest = iterator.next();
            if (!tempest.dimension().equals(world.getRegistryKey())) continue;
            long elapsed = time - tempest.startsAt();
            if (!(world.getEntity(tempest.owner()) instanceof LivingEntity owner) || !owner.isAlive() || elapsed >= tempest.duration()) {
                iterator.remove();
                continue;
            }
            if (elapsed % 10 == 0) {
                for (var enemy : RelicTargets.enemiesAround(world, owner, tempest.center(), tempest.radius())) {
                    enemy.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.registryEntry, tempest.freezeTicks(), 0), owner);
                    if (elapsed % 20 == 0) {
                        RelicTargets.magicDamage(world, owner, enemy, tempest.damage());
                    }
                }
            }
        }
    }

    private static void tickBubbles(ServerWorld world) {
        if (BUBBLES.isEmpty()) return;
        long time = world.getTime();
        Iterator<Bubble> iterator = BUBBLES.iterator();
        while (iterator.hasNext()) {
            var bubble = iterator.next();
            if (!bubble.dimension().equals(world.getRegistryKey()) || time < bubble.burstsAt()) continue;
            iterator.remove();
            if (!(world.getEntity(bubble.owner()) instanceof LivingEntity owner) || !owner.isAlive()) continue;
            Vec3d center = world.getEntity(bubble.target()) instanceof LivingEntity target && target.isAlive()
                    ? target.getPos().add(bubble.offset()) : bubble.fallback();
            Vec3d burst = center.add(0, 0.8, 0);
            for (var enemy : RelicTargets.enemiesAround(world, owner, burst, bubble.radius())) {
                RelicTargets.magicDamage(world, owner, enemy, bubble.damage());
                if (bubble.slownessTicks() > 0) {
                    enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, bubble.slownessTicks(), 0), owner);
                }
            }
            MrpgFx.play(world, LNEFx.BUBBLE_BURST, FxParams.at(burst).radius((float) bubble.radius()));
        }
    }

    private static void tickStorms(ServerWorld world) {
        if (STORMS.isEmpty()) return;
        long time = world.getTime();
        Iterator<Map.Entry<UUID, Storm>> iterator = STORMS.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var player = world.getServer().getPlayerManager().getPlayer(entry.getKey());
            var storm = entry.getValue();
            if (player == null || !player.isAlive() || time >= storm.endsAt()) {
                iterator.remove();
                continue;
            }
            if (player.getServerWorld() != world) continue;
            Vec3d center = player.getPos();
            if (time % 10 == 0) {
                for (var enemy : RelicTargets.enemiesAround(world, player, center, storm.radius())) {
                    enemy.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.registryEntry, storm.freezeTicks(), 0), player);
                }
            }
        }
    }

    private static void tickClouds(ServerWorld world) {
        if (CLOUDS.isEmpty()) return;
        long time = world.getTime();
        Iterator<DragonCloud> iterator = CLOUDS.iterator();
        while (iterator.hasNext()) {
            var cloud = iterator.next();
            if (!cloud.dimension().equals(world.getRegistryKey())) continue;
            var owner = world.getServer().getPlayerManager().getPlayer(cloud.owner());
            long elapsed = time - cloud.startsAt();
            if (owner == null || !owner.isAlive() || elapsed >= cloud.duration()) {
                iterator.remove();
                continue;
            }
            double progress = (double) elapsed / cloud.duration();
            double radius = cloud.startRadius() + (cloud.endRadius() - cloud.startRadius()) * progress;
            if (elapsed % 20 == 10) {
                pulseCloud(world, owner, cloud, radius);
            }
        }
    }

    private static void pulseCloud(ServerWorld world, ServerPlayerEntity owner, DragonCloud cloud, double radius) {
        var center = cloud.center();
        double radiusSquared = radius * radius;
        var box = new net.minecraft.util.math.Box(center.x - radius, center.y - 1, center.z - radius, center.x + radius, center.y + 3, center.z + radius);
        var inside = world.getEntitiesByClass(LivingEntity.class, box,
                entity -> entity.isAlive() && entity.squaredDistanceTo(center.x, entity.getY(), center.z) <= radiusSquared);
        for (var entity : inside) {
            if (entity == owner || AllyHelper.canHelp(owner, entity)) {
                entity.heal(cloud.heal());
                LNEFx.play(world, LNEFx.HEAL, entity.getPos().add(0, entity.getHeight() * 0.6, 0));
            } else if (RelicTargets.isEnemy(owner, entity)) {
                RelicTargets.magicDamage(world, owner, entity, cloud.damage());
            }
        }
    }
}

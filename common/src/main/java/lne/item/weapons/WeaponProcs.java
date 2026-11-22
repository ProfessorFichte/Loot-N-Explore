package lne.item.weapons;

import net.minecraft.sound.SoundEvents;
import com.mrpg_lib.util.AllyHelper;
import com.mrpg_lib.util.CloudMethods;
import lne.internal.ability.AbilityDamage;
import lne.internal.ability.RelicTargets;
import lne.internal.ability.RelicZones;
import lne.internal.ability.impl.FrozenSoulAbility;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.effects.Effects;
import lne.internal.fx.LNEFx;
import lne.entity.frozen_depths.projectile.FrostballEntity;
import lne.entity.frozen_depths.projectile.StraightIcicleEntity;
import lne.entity.generic.projectile.OrbitingWitherSkullEntity;
import lne.item.weapons.WeaponAbility.Slot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public final class WeaponProcs {
    private WeaponProcs() {
    }

    public static void tryMelee(WeaponAbility ability, Item item, LivingEntity attacker, LivingEntity target) {
        if (!(attacker.getWorld() instanceof ServerWorld) || !LNE_WeaponItems.vanillaAbilitiesActive()
                || !(attacker instanceof PlayerEntity player)
                || player.getItemCooldownManager().isCoolingDown(item)
                || !ability.roll(Slot.MELEE, attacker.getRandom())) {
            return;
        }
        fire(ability, Slot.MELEE, player, target);
        player.getItemCooldownManager().set(item, ability.cooldownTicks(Slot.MELEE));
    }

    public static void tryBlock(WeaponAbility ability, Item item, PlayerEntity blocker, LivingEntity attacker) {
        if (!(blocker.getWorld() instanceof ServerWorld) || !LNE_WeaponItems.vanillaAbilitiesActive()
                || blocker.getItemCooldownManager().isCoolingDown(item)
                || !ability.roll(Slot.BLOCK, blocker.getRandom())) {
            return;
        }
        fire(ability, Slot.BLOCK, blocker, attacker);
        blocker.getItemCooldownManager().set(item, ability.cooldownTicks(Slot.BLOCK));
    }

    public static void fire(WeaponAbility ability, Slot slot, LivingEntity user, LivingEntity target) {
        if (!(user.getWorld() instanceof ServerWorld world)) return;
        float damage = AbilityDamage.scaled(user, ability.value(slot, "base_damage"), ability.value(slot, "damage_scaling"));
        switch (ability) {
            case ENDER_DRAGON -> {
                switch (slot) {
                    case MELEE -> dragonDrain(world, user, target, damage);
                    case RANGED -> dragonBurst(world, user, target, center(target), ability.value(slot, "radius"), damage, 0.0);
                    case BLOCK -> dragonBurst(world, user, null, center(user), ability.value(slot, "radius"), damage,
                            ability.value(slot, "knockback_strength"));
                }
            }
            case WITHER -> {
                switch (slot) {
                    case MELEE, RANGED -> witherBlast(world, user, target, ability, slot, damage);
                    case BLOCK -> witherSkulls(world, user, ability, damage);
                }
            }
            case GLACIAL -> {
                switch (slot) {
                    case MELEE -> RelicZones.startTempest(user, target.getPos(), ability.value(slot, "radius"),
                            ability.ticks(slot, "duration_ticks"), damage, ability.ticks(slot, "freezing_ticks"));
                    case RANGED -> frostballRing(world, user, target, (int) ability.value(slot, "frostballs"));
                    case BLOCK -> icicleLine(world, user, ability, damage);
                }
            }
            case ELDER_GUARDIAN -> {
                switch (slot) {
                    case MELEE -> rainCloud(world, user, target, ability, slot, damage);
                    case RANGED -> RelicZones.startBubbleBarrage(user, target, (int) ability.value(slot, "bubbles"), ability.value(slot, "radius"),
                            ability.ticks(slot, "delay_ticks"), damage, ability.ticks(slot, "slowness_ticks"));
                    case BLOCK -> RelicZones.startWhirlpool(user, ability.value(slot, "radius"), ability.ticks(slot, "duration_ticks"),
                            damage, ability.value(slot, "push_strength"));
                }
            }
        }
    }

    public static void applyFrozenWeaponHit(LivingEntity attacker, LivingEntity target) {
        var config = WeaponAbilities.config.value.frozen_weapons;
        if (!config.enabled || !LNE_WeaponItems.vanillaAbilitiesActive() || attacker.getWorld().isClient) return;
        target.addStatusEffect(new StatusEffectInstance(Effects.FREEZING.registryEntry, Math.max(1, config.freezing_ticks),
                Math.max(0, config.freezing_amplifier)), attacker);
    }

    private static Vec3d center(LivingEntity entity) {
        return entity.getPos().add(0, entity.getHeight() * 0.5, 0);
    }

    private static List<LivingEntity> enemies(ServerWorld world, LivingEntity user, Vec3d center, double radius, LivingEntity directTarget) {
        var enemies = new ArrayList<>(RelicTargets.enemiesAround(world, user, center, radius));
        if (directTarget != null && directTarget.isAlive() && !enemies.contains(directTarget) && AllyHelper.canHurt(user, directTarget)) {
            enemies.add(directTarget);
        }
        return enemies;
    }

    private static void dragonDrain(ServerWorld world, LivingEntity user, LivingEntity target, float damage) {
        RelicTargets.magicDamage(world, user, target, damage);
        user.heal(damage);
        MrpgFx.play(world, LNEFx.DRAGON_DRAIN, FxParams.at(center(target)).target(center(user)));
        Vec3d toUser = user.getPos().subtract(target.getPos()).multiply(1, 0, 1);
        Vec3d facing = toUser.lengthSquared() < 1.0E-4 ? Vec3d.ZERO : toUser.normalize();
        LNEFx.play(world, LNEFx.DRAGON_CLAW, center(target).add(facing.multiply(target.getWidth() * 0.5 + 0.35)));
    }

    private static void dragonBurst(ServerWorld world, LivingEntity user, LivingEntity directTarget, Vec3d center, double radius,
                                    float damage, double knockback) {
        for (var enemy : enemies(world, user, center, radius, directTarget)) {
            RelicTargets.magicDamage(world, user, enemy, damage);
            if (knockback > 0) {
                enemy.takeKnockback(knockback, center.x - enemy.getX(), center.z - enemy.getZ());
                enemy.velocityModified = true;
            }
        }
        MrpgFx.play(world, LNEFx.DRAGON_BURST, FxParams.at(center).radius((float) radius));
        if (knockback > 0) {
            LNEFx.sound(world, center, SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 1.2F, 0.8F);
        }
    }

    private static void witherBlast(ServerWorld world, LivingEntity user, LivingEntity target, WeaponAbility ability, Slot slot, float damage) {
        Vec3d center = center(target);
        double radius = ability.value(slot, "radius");
        int maxAmplifier = AbilityDamage.scaledAmplifier(user, ability.value(slot, "amplifier_scaling"), (int) ability.value(slot, "max_amplifier"));
        int witherTicks = ability.ticks(slot, "wither_ticks");
        for (var enemy : enemies(world, user, center, radius, target)) {
            RelicTargets.magicDamage(world, user, enemy, damage);
            if (slot == Slot.RANGED) {
                AbilityDamage.stackWither(user, enemy, witherTicks, maxAmplifier);
            } else {
                enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, witherTicks, maxAmplifier), user);
            }
        }
        var effect = slot == Slot.RANGED ? LNEFx.WITHER_BLAST : LNEFx.WITHER_AURA;
        MrpgFx.play(world, effect, FxParams.at(slot == Slot.RANGED ? center : target.getPos()).radius((float) radius));
    }

    private static void witherSkulls(ServerWorld world, LivingEntity user, WeaponAbility ability, float damage) {
        int skulls = Math.max(1, (int) ability.value(Slot.BLOCK, "skulls"));
        double orbit = ability.value(Slot.BLOCK, "orbit_radius");
        int lifetime = ability.ticks(Slot.BLOCK, "duration_ticks");
        double explosion = ability.value(Slot.BLOCK, "explosion_radius");
        for (var old : world.getEntitiesByClass(OrbitingWitherSkullEntity.class, user.getBoundingBox().expand(16), skull -> skull.getOwner() == user)) {
            old.discard();
        }
        float offset = user.getRandom().nextFloat() * MathHelper.TAU;
        for (int i = 0; i < skulls; i++) {
            world.spawnEntity(new OrbitingWitherSkullEntity(world, user, offset + MathHelper.TAU * i / skulls, (float) orbit,
                    lifetime, damage, (float) explosion));
        }
        MrpgFx.play(world, LNEFx.WITHER_SUMMON, FxParams.at(center(user)));
    }

    private static void frostballRing(ServerWorld world, LivingEntity user, LivingEntity target, int count) {
        double bx = target.getX(), by = target.getY() + 6.0, bz = target.getZ();
        double r = 1.5;
        int balls = Math.max(1, count);
        for (int i = 0; i < balls; i++) {
            double angle = (2 * Math.PI / balls) * i;
            FrostballEntity ball = new FrostballEntity(world, bx + r * Math.cos(angle), by, bz + r * Math.sin(angle));
            ball.setVelocity(0, -1, 0);
            ball.setOwner(user);
            ball.withImpactVolume(0.5F);
            world.spawnEntity(ball);
        }
        MrpgFx.play(world, LNEFx.FROSTBALL_RING, FxParams.at(new Vec3d(bx, by, bz)).radius((float) r).target(target.getPos().add(0, 0.2, 0)));
    }

    private static void icicleLine(ServerWorld world, LivingEntity user, WeaponAbility ability, float damage) {
        double length = ability.value(Slot.BLOCK, "length");
        double spacing = Math.max(0.5, ability.value(Slot.BLOCK, "spacing"));
        float yawRad = user.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        double forwardX = -MathHelper.sin(yawRad);
        double forwardZ = MathHelper.cos(yawRad);
        float angle = (float) Math.atan2(forwardZ, forwardX);
        int count = Math.max(1, (int) Math.ceil(length / spacing));
        boolean sounded = false;
        for (int i = 0; i < count; i++) {
            double distance = spacing * (i + 1);
            double x = user.getX() + forwardX * distance;
            double z = user.getZ() + forwardZ * distance;
            double y = FrozenSoulAbility.groundY(world, x, z, user.getY());
            if (Double.isNaN(y)) continue;
            var icicle = new StraightIcicleEntity(world, x, y, z, angle, i, user);
            icicle.configureFixedDamage(damage, 0.0F);
            if (!sounded) {
                icicle.withEruptSound();
                sounded = true;
            }
            world.spawnEntity(icicle);
        }
        Vec3d front = user.getPos().add(forwardX, 0.2, forwardZ);
        MrpgFx.play(world, LNEFx.ICICLE_LINE, FxParams.at(front).direction(new Vec3d(forwardX, 0, forwardZ)).length((float) length));
    }

    private static void rainCloud(ServerWorld world, LivingEntity user, LivingEntity target, WeaponAbility ability, Slot slot, float damage) {
        float radius = (float) ability.value(slot, "radius");
        int seconds = Math.max(1, ability.ticks(slot, "duration_ticks") / 20);
        CloudMethods.spawnCustomCloud(ParticleTypes.RAIN, user, target, 1, radius, seconds, radius,
                null, 0, 0, false, 0, true, damage, world.getDamageSources().magic());
        MrpgFx.play(world, LNEFx.GUARDIAN_SPLASH, FxParams.at(center(target)));
    }
}

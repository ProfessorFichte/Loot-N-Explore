package lne.internal.ability.impl;

import lne.internal.ability.RelicTriggers;
import org.jetbrains.annotations.Nullable;
import lne.internal.fx.LNEFx;
import lne.internal.ability.AbilityDamage;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicTargets;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileUtil;
import lne.entity.relic.GuardianBeamEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class GuardianBeamAbility extends RelicAbility {
    private static final Map<ServerPlayerEntity, GuardianBeamEntity> BEAMS = new WeakHashMap<>();
    private static final int GUARDIAN_ATTACK_SOUND_INTERVAL = 40;

    public GuardianBeamAbility() {
        super("elder_guardian_eye");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.ACTIVE_USE);
    }

    @Override
    public Set<TriggerType> passiveTriggers() {
        return java.util.EnumSet.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.SPELL_HIT);
    }

    @Override
    public boolean channeled() {
        return true;
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 600.0);
        values.put("channel_ticks", 100.0);
        values.put("range", 16.0);
        values.put("base_damage", 2.0);
        values.put("damage_scaling", 0.15);
        values.put("damage_interval_ticks", 10.0);
        values.put("weakness_ticks", 60.0);
    }

    @Override
    public String name() {
        return "Guardian's Gaze";
    }

    @Override
    public String description() {
        return "Hold right click for up to %s to channel a guardian beam on the enemy you aim at. It deals %s magic damage every %s and weakens the target.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.seconds(value("channel_ticks")), scaledNumber(viewer, "base_damage", "damage_scaling"), RelicText.seconds(value("damage_interval_ticks"))};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        if (context.type() == TriggerType.ACTIVE_USE || !context.ready()) {
            return false;
        }
        RelicTriggers.forceChannel(context.player(), this);
        context.startCooldown();
        return true;
    }

    private static GuardianBeamEntity beamOf(ServerPlayerEntity player) {
        var beam = BEAMS.get(player);
        if (beam == null || beam.isRemoved() || beam.getWorld() != player.getWorld()) {
            beam = new GuardianBeamEntity(player.getServerWorld(), player);
            player.getServerWorld().spawnEntity(beam);
            BEAMS.put(player, beam);
        }
        return beam;
    }

    @Override
    public void onChannelTick(ServerPlayerEntity player, int ticksUsed) {
        var world = player.getServerWorld();
        Vec3d eye = player.getEyePos();
        Vec3d start = eye.add(player.getRotationVec(1.0F).multiply(0.6)).add(0, -0.25, 0);
        Vec3d end = eye.add(player.getRotationVec(1.0F).multiply(value("range")));
        var blockHit = world.raycast(new RaycastContext(eye, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        Vec3d limit = blockHit.getPos();
        var box = player.getBoundingBox().stretch(limit.subtract(eye)).expand(1.0);
        var entityHit = ProjectileUtil.getEntityCollision(world, player, eye, limit, box,
                entity -> entity instanceof LivingEntity living && RelicTargets.isEnemy(player, living));
        Vec3d beamEnd = entityHit != null ? entityHit.getPos() : limit;
        if (ticksUsed == 0) {
            LNEFx.sound(world, player.getPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.7F, 1.5F);
        }
        if (ticksUsed % GUARDIAN_ATTACK_SOUND_INTERVAL == 0) {
            LNEFx.sound(world, player.getPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, 0.7F, 1.0F);
        }
        var hitTarget = entityHit != null && entityHit.getEntity() instanceof LivingEntity living ? living : null;
        beamOf(player).refresh(start, beamEnd, hitTarget);
        if (hitTarget == null) {
            return;
        }
        var target = hitTarget;
        if (ticksUsed % 2 == 0) {
            LNEFx.play(world, LNEFx.BEAM_HIT, beamEnd);
        }
        int interval = Math.max(1, ticks("damage_interval_ticks"));
        if (ticksUsed % interval != 0) {
            return;
        }
        float damage = AbilityDamage.scaled(player, value("base_damage"), value("damage_scaling"));
        RelicTargets.magicDamage(world, player, target, damage);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, ticks("weakness_ticks"), 0), player);
        LNEFx.sound(world, target.getPos(), SoundEvents.BLOCK_CONDUIT_ATTACK_TARGET, 0.6F, 1.2F);
    }
}

package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.fx.LNEFx;
import lne.ability.AbilityDamage;
import lne.ability.RelicAbility;
import lne.ability.RelicTargets;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;
import lne.entity.ModEntities;
import lne.entity.relic.EmergingMinion;
import lne.entity.relic.RelicSkeletonEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.Monster;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;
import java.util.Set;

public class SkeletonSummonAbility extends RelicAbility {
    public SkeletonSummonAbility() {
        super("unknown_remains");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.KILL);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 1200.0);
        values.put("kills_required", 25.0);
        values.put("duration_ticks", 400.0);
        values.put("health_scaling", 0.5);
        values.put("damage_scaling", 0.10);
    }

    @Override
    public String name() {
        return "Risen Remains";
    }

    @Override
    public String description() {
        return "After %s kills, a friendly skeleton rises from the ground and fights alongside you for %s. It cannot burn. Kills do not count while it is alive or the relic is on cooldown.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.number(value("kills_required")), RelicText.seconds(value("duration_ticks"))};
    }

    @Override
    public Progress progress() {
        return new Progress(relic() + ".kills", (int) value("kills_required"), PROGRESS_KILLS);
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var victim = context.target();
        var player = context.player();
        if (!context.forced() && (victim == null || !(victim instanceof Monster) || victim instanceof RelicSkeletonEntity || !RelicTargets.isEnemyType(player, victim))) {
            return false;
        }
        var world = context.world();
        if (!context.ready() || !context.forced() && hasLivingSkeleton(world, player)) {
            return false;
        }
        if (!context.countUp(relic() + ".kills", (int) value("kills_required"))) return false;
        double highest = AbilityDamage.highest(player);
        if (summon(world, player, ticks("duration_ticks"), highest * value("health_scaling"), highest * value("damage_scaling")) == null) return false;
        context.startCooldown();
        return true;
    }

    @Nullable
    public static RelicSkeletonEntity summon(ServerWorld world, ServerPlayerEntity player, int durationTicks, double bonusHealth, double bonusDamage) {
        var skeleton = ModEntities.RELIC_SKELETON.create(world);
        if (skeleton == null) return null;
        double angle = player.getRandom().nextDouble() * Math.PI * 2;
        double x = player.getX() + Math.cos(angle) * 1.8;
        double z = player.getZ() + Math.sin(angle) * 1.8;
        skeleton.refreshPositionAndAngles(x, player.getY(), z, player.getYaw(), 0);
        skeleton.initialize(world, world.getLocalDifficulty(skeleton.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
        skeleton.setOwner(player, durationTicks);
        EmergingMinion.addOwnerScaling(skeleton, bonusHealth, bonusDamage);
        skeleton.setArrowDamageBonus((float) bonusDamage);
        skeleton.setPersistent();
        world.spawnEntity(skeleton);
        LNEFx.play(world, LNEFx.SKELETON_EMERGE, skeleton.getPos());
        return skeleton;
    }

    public static boolean hasLivingSkeleton(ServerWorld world, ServerPlayerEntity player) {
        return !world.getEntitiesByClass(RelicSkeletonEntity.class, player.getBoundingBox().expand(128),
                skeleton -> skeleton.isAlive() && skeleton.getOwner() == player).isEmpty();
    }
}

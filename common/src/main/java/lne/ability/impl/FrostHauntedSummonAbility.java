package lne.ability.impl;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.fx.LNEFx;
import lne.ability.RelicAbility;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;
import lne.entity.ModEntities;
import lne.entity.relic.EmergingMinion;
import lne.entity.relic.RelicFrostHauntedEntity;
import net.minecraft.entity.SpawnReason;

import java.util.Map;
import java.util.Set;

public class FrostHauntedSummonAbility extends RelicAbility {
    public FrostHauntedSummonAbility() {
        super("frozen_rib");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.DAMAGE_TAKEN);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 1200.0);
        values.put("chance", 0.10);
        values.put("count", 3.0);
        values.put("duration_ticks", 400.0);
        values.put("health_scaling", 0.5);
        values.put("damage_scaling", 0.10);
    }

    @Override
    public String name() {
        return "Frozen Legion";
    }

    @Override
    public String description() {
        return "%s chance when you take damage to raise %s friendly Frost Haunted from the ground that fight alongside you for %s. They cannot burn.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.percent(value("chance")), RelicText.number(value("count")), RelicText.seconds(value("duration_ticks"))};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var player = context.player();
        if (!context.forced() && (context.amount() <= 0 || context.target() instanceof RelicFrostHauntedEntity)) {
            return false;
        }
        if (!context.ready() || !context.roll(value("chance"))) {
            return false;
        }
        double highest = lne.ability.AbilityDamage.highest(player);
        summon(context.world(), player, Math.max(1, (int) value("count")), ticks("duration_ticks"),
                highest * value("health_scaling"), highest * value("damage_scaling"));
        context.startCooldown();
        return true;
    }

    public static void summon(ServerWorld world, ServerPlayerEntity player, int count, int durationTicks, double bonusHealth, double bonusDamage) {
        var box = player.getBoundingBox().expand(128);
        for (var old : world.getEntitiesByClass(RelicFrostHauntedEntity.class, box, haunt -> haunt.getOwner() == player)) {
            old.discard();
        }
        double offset = player.getRandom().nextDouble() * Math.PI * 2;
        for (int i = 0; i < count; i++) {
            var haunt = ModEntities.RELIC_FROST_HAUNTED.create(world);
            if (haunt == null) continue;
            double angle = offset + Math.PI * 2 * i / count;
            haunt.refreshPositionAndAngles(player.getX() + Math.cos(angle) * 1.9, player.getY(), player.getZ() + Math.sin(angle) * 1.9, player.getYaw(), 0);
            haunt.initialize(world, world.getLocalDifficulty(haunt.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
            haunt.setOwner(player, durationTicks);
            EmergingMinion.addOwnerScaling(haunt, bonusHealth, bonusDamage);
            world.spawnEntity(haunt);
            LNEFx.play(world, LNEFx.FROST_HAUNTED_EMERGE, haunt.getPos());
        }
    }
}

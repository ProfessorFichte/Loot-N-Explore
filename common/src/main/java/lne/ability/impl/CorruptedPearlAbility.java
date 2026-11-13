package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import lne.ability.RelicAbilities;
import lne.ability.AbilityDamage;
import lne.ability.RelicAbility;
import lne.ability.RelicTargets;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;
import lne.entity.relic.CorruptedPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.Set;

public class CorruptedPearlAbility extends RelicAbility {
    public static final String RELIC = "corrupted_ender_pearl";

    public CorruptedPearlAbility() {
        super(RELIC);
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.ACTIVE_USE);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 1200.0);
        values.put("radius", 7.0);
        values.put("base_damage", 6.0);
        values.put("damage_scaling", 0.10);
        values.put("throw_velocity", 1.5);
    }

    @Override
    public String name() {
        return "Void Rift";
    }

    @Override
    public String description() {
        return "Throw a corrupted ender pearl and teleport to where it lands. A %s block explosion there deals %s magic damage to enemies.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.number(value("radius")), scaledNumber(viewer, "base_damage", "damage_scaling")};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var player = context.player();
        var world = context.world();
        var pearl = new CorruptedPearlEntity(world, player);
        pearl.setItem(context.stack().copyWithCount(1));
        pearl.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, (float) value("throw_velocity"), 1.0F);
        world.spawnEntity(pearl);
        LNEFx.sound(world, player.getPos(), SoundEvents.ENTITY_ENDER_PEARL_THROW, 0.6F, 0.6F);
        context.startCooldown();
        return true;
    }

    public static void explode(ServerWorld world, ServerPlayerEntity owner, Vec3d center) {
        var ability = (CorruptedPearlAbility) lne.ability.RelicAbilities.get(RELIC);
        double radius = ability.value("radius");
        float damage = AbilityDamage.scaled(owner, ability.value("base_damage"), ability.value("damage_scaling"));
        for (var enemy : RelicTargets.enemiesAround(world, owner, center, radius)) {
            RelicTargets.magicDamage(world, owner, enemy, damage);
        }
        MrpgFx.play(world, LNEFx.PEARL_BLAST, FxParams.at(center.add(0, 0.5, 0)).radius((float) radius));
    }
}

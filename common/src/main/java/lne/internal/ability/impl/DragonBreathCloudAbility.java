package lne.internal.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.internal.ability.AbilityDamage;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicText;
import lne.internal.ability.RelicZones;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;

import java.util.Map;
import java.util.Set;

public class DragonBreathCloudAbility extends RelicAbility {
    public DragonBreathCloudAbility() {
        super("ender_dragon_scales");
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
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 1800.0);
        values.put("start_radius", 2.0);
        values.put("end_radius", 5.0);
        values.put("duration_ticks", 120.0);
        values.put("base_amount", 4.0);
        values.put("amount_scaling", 0.10);
    }

    @Override
    public String name() {
        return "Dragon's Breath";
    }

    @Override
    public String description() {
        return "Create a growing dragon breath cloud around you for %s. Every second it heals you and your allies and damages enemies inside for %s.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.seconds(value("duration_ticks")), scaledNumber(viewer, "base_amount", "amount_scaling")};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        if (!context.ready()) {
            return false;
        }
        var player = context.player();
        float amount = AbilityDamage.scaled(player, value("base_amount"), value("amount_scaling"));
        RelicZones.startDragonCloud(player, player.getPos(), value("start_radius"), value("end_radius"),
                ticks("duration_ticks"), amount, amount);
        context.startCooldown();
        return true;
    }
}

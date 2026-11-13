package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.ability.RelicAbility;
import lne.ability.RelicText;
import lne.ability.RelicZones;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;

import java.util.Map;
import java.util.Set;

public class FrostStormAbility extends RelicAbility {
    public FrostStormAbility() {
        super("eternal_snowflake");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.DAMAGE_TAKEN);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 300.0);
        values.put("chance", 0.05);
        values.put("radius", 4.0);
        values.put("duration_ticks", 140.0);
        values.put("freezing_ticks", 60.0);
    }

    @Override
    public String name() {
        return "Eternal Blizzard";
    }

    @Override
    public String description() {
        return "%s chance after taking damage to summon a freezing storm around you for %s, inflicting Freezing on enemies within %s blocks.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.percent(value("chance")), RelicText.seconds(value("duration_ticks")), RelicText.number(value("radius"))};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var player = context.player();
        if (!context.ready() || !context.roll(value("chance"))) {
            return false;
        }
        RelicZones.startStorm(player, value("radius"), ticks("duration_ticks"), ticks("freezing_ticks"));
        context.startCooldown();
        return true;
    }
}

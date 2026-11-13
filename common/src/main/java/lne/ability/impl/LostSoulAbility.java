package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.fx.LNEFx;
import lne.ability.RelicTargets;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Monster;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;
import java.util.Set;

public class LostSoulAbility extends StackBuffAbility {
    public LostSoulAbility() {
        super("lost_soul");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.KILL);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put("kills_required", 10.0);
        values.put("health_per_stack", 1.0);
        values.put("max_stacks", 5.0);
    }

    @Override
    public String name() {
        return "Soul Harvest";
    }

    @Override
    public String description() {
        return "Every %s enemy you kill grants +%s max health, up to %s stacks. Stacks are lost on death.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.number(value("kills_required")), RelicText.number(value("health_per_stack")), RelicText.number(value("max_stacks"))};
    }

    @Override
    public Progress progress() {
        return new Progress(relic() + ".kills", (int) value("kills_required"), PROGRESS_KILLS);
    }

    @Override
    protected RegistryEntry<EntityAttribute> attribute() {
        return EntityAttributes.GENERIC_MAX_HEALTH;
    }

    @Override
    protected EntityAttributeModifier.Operation operation() {
        return EntityAttributeModifier.Operation.ADD_VALUE;
    }

    @Override
    protected double amountPerStack() {
        return value("health_per_stack");
    }

    @Override
    protected int maxStacks() {
        return (int) value("max_stacks");
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var victim = context.target();
        var player = context.player();
        if (!context.forced() && (victim == null || !(victim instanceof Monster) || !RelicTargets.isEnemyType(player, victim))) return false;
        var data = context.data();
        if (stacks(data) >= maxStacks()) return false;
        if (!context.countUp(relic() + ".kills", (int) value("kills_required"))) return false;
        data.setCounter(stacksKey(), stacks(data) + 1);
        sync(player, data, true);
        var pos = player.getPos().add(0, 1.0, 0);
        LNEFx.play(context.world(), LNEFx.LOST_SOUL, pos);
        return true;
    }
}

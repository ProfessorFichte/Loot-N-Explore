package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.fx.LNEFx;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;
import java.util.Set;

public class DragonToothAbility extends StackBuffAbility {
    public DragonToothAbility() {
        super("ender_dragon_tooth");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.MELEE_HIT);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put("chance", 0.10);
        values.put("bonus_per_stack", 0.01);
        values.put("max_stacks", 20.0);
    }

    @Override
    public String name() {
        return "Draconic Hunger";
    }

    @Override
    public String description() {
        return "%s chance to gain a stack of +%s attack damage, up to %s stacks. Stacks are lost on death.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.percent(value("chance")), RelicText.percent(value("bonus_per_stack")), RelicText.number(value("max_stacks"))};
    }

    @Override
    protected RegistryEntry<EntityAttribute> attribute() {
        return EntityAttributes.GENERIC_ATTACK_DAMAGE;
    }

    @Override
    protected EntityAttributeModifier.Operation operation() {
        return EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE;
    }

    @Override
    protected double amountPerStack() {
        return value("bonus_per_stack");
    }

    @Override
    protected int maxStacks() {
        return (int) value("max_stacks");
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var player = context.player();
        int stacks = stacks(context.data());
        if (stacks >= maxStacks() || !context.roll(value("chance"))) {
            return false;
        }
        context.data().setCounter(stacksKey(), stacks + 1);
        sync(player, context.data(), true);
        var pos = player.getPos().add(0, 1.0, 0);
        LNEFx.play(context.world(), LNEFx.TOOTH_STACK, pos);
        return true;
    }
}

package lne.internal.ability.impl;

import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicPlayerData;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public abstract class StackBuffAbility extends RelicAbility {
    public static final String STACKS = "stacks";

    protected StackBuffAbility(String relic) {
        super(relic);
    }

    protected abstract RegistryEntry<EntityAttribute> attribute();

    protected abstract EntityAttributeModifier.Operation operation();

    protected abstract double amountPerStack();

    protected abstract int maxStacks();

    protected String stacksKey() {
        return STACKS + "." + relic();
    }

    public int stacks(RelicPlayerData data) {
        return data.counter(stacksKey());
    }

    public int stackLimit() {
        return maxStacks();
    }

    private Identifier modifierId() {
        return Identifier.of(MOD_ID, "relic_stacks/" + relic());
    }

    public void sync(ServerPlayerEntity player, RelicPlayerData data, boolean equipped) {
        var instance = player.getAttributeInstance(attribute());
        if (instance == null) return;
        var id = modifierId();
        int stacks = equipped && enabled() ? Math.min(stacks(data), maxStacks()) : 0;
        var existing = instance.getModifier(id);
        if (stacks == 0) {
            if (existing != null) instance.removeModifier(id);
            return;
        }
        double amount = amountPerStack() * stacks;
        if (existing != null) {
            if (existing.value() == amount) return;
            instance.removeModifier(id);
        }
        instance.addTemporaryModifier(new EntityAttributeModifier(id, amount, operation()));
    }
}

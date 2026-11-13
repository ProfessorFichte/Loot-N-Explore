package lne.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class TriggerContext {
    private final ServerPlayerEntity player;
    private final ItemStack stack;
    private final TriggerType type;
    private final RelicAbility ability;
    private final RelicPlayerData data;
    @Nullable private final LivingEntity target;
    @Nullable private final DamageSource source;
    private final float amount;
    private final boolean forced;
    private boolean cancelled;

    public TriggerContext(ServerPlayerEntity player, ItemStack stack, TriggerType type, RelicAbility ability,
                          RelicPlayerData data, @Nullable LivingEntity target, @Nullable DamageSource source, float amount) {
        this(player, stack, type, ability, data, target, source, amount, false);
    }

    public TriggerContext(ServerPlayerEntity player, ItemStack stack, TriggerType type, RelicAbility ability,
                          RelicPlayerData data, @Nullable LivingEntity target, @Nullable DamageSource source, float amount, boolean forced) {
        this.forced = forced;
        this.player = player;
        this.stack = stack;
        this.type = type;
        this.ability = ability;
        this.data = data;
        this.target = target;
        this.source = source;
        this.amount = amount;
    }

    public ServerPlayerEntity player() { return player; }
    public ServerWorld world() { return player.getServerWorld(); }
    public ItemStack stack() { return stack; }
    public TriggerType type() { return type; }
    public RelicPlayerData data() { return data; }
    @Nullable public LivingEntity target() { return target; }
    @Nullable public DamageSource source() { return source; }
    public float amount() { return amount; }

    public long now() {
        return world().getTime();
    }

    public boolean forced() {
        return forced;
    }

    public boolean ready() {
        return forced || data.isReady(ability.relic(), now());
    }

    public boolean roll(double chance) {
        return forced || player.getRandom().nextDouble() < chance;
    }

    public boolean countUp(String key, int required) {
        if (forced) return true;
        int next = data.counter(key) + 1;
        if (next < required) {
            data.setCounter(key, next);
            return false;
        }
        data.setCounter(key, 0);
        return true;
    }

    public void startCooldown() {
        if (forced) return;
        int ticks = ability.cooldownTicks();
        data.startCooldown(ability.relic(), now(), ticks);
        if (ticks > 0) {
            player.getItemCooldownManager().set(stack.getItem(), ticks);
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}

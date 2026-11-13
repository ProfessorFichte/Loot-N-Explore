package lne.ability;

import lne.ability.impl.StackBuffAbility;
import lne.network.RelicStacksPayload;
import lne.platform.LNEEvents;
import lne.platform.LNEPlatform;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static lne.LneMod.MOD_ID;

public final class RelicTriggers {
    private static int suppressed = 0;
    private static final Map<ServerPlayerEntity, List<RelicStacksPayload.Entry>> LAST_SENT = new WeakHashMap<>();

    private RelicTriggers() {
    }

    public static void runSuppressed(Runnable action) {
        suppressed++;
        try {
            action.run();
        } finally {
            suppressed--;
        }
    }

    public static List<ItemStack> equippedStacks(LivingEntity entity) {
        var stacks = new ArrayList<ItemStack>();
        stacks.add(entity.getMainHandStack());
        stacks.add(entity.getOffHandStack());
        stacks.addAll(LNEPlatform.equippedAccessories(entity));
        return stacks;
    }

    public static List<ItemStack> passiveStacks(LivingEntity entity) {
        return LNEPlatform.hasAccessorySlots() ? LNEPlatform.equippedAccessories(entity) : List.of(entity.getOffHandStack());
    }

    @Nullable
    public static RelicAbility abilityOf(ItemStack stack) {
        if (stack.isEmpty()) return null;
        var id = Registries.ITEM.getId(stack.getItem());
        if (!id.getNamespace().equals(MOD_ID)) return null;
        return RelicAbilities.get(id.getPath());
    }

    private static Map<RelicAbility, ItemStack> collect(LivingEntity entity, TriggerType type) {
        var result = new LinkedHashMap<RelicAbility, ItemStack>();
        for (var stack : passiveStacks(entity)) {
            var ability = abilityOf(stack);
            if (ability == null || !ability.enabled() || !ability.passiveTriggerSet().contains(type)) continue;
            result.putIfAbsent(ability, stack);
        }
        return result;
    }

    private static final Map<ServerPlayerEntity, ForcedChannel> FORCED_CHANNELS = new WeakHashMap<>();

    private static final class ForcedChannel {
        final RelicAbility ability;
        int ticksUsed = 0;

        ForcedChannel(RelicAbility ability) {
            this.ability = ability;
        }
    }

    public static void forceChannel(ServerPlayerEntity player, RelicAbility ability) {
        FORCED_CHANNELS.put(player, new ForcedChannel(ability));
    }

    public static void tickForcedChannels(ServerWorld world) {
        if (FORCED_CHANNELS.isEmpty()) return;
        var iterator = FORCED_CHANNELS.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var player = entry.getKey();
            if (player.getServerWorld() != world) continue;
            var channel = entry.getValue();
            if (!player.isAlive() || channel.ticksUsed >= channel.ability.channelTicks()) {
                iterator.remove();
                continue;
            }
            channel.ability.onChannelTick(player, channel.ticksUsed++);
        }
    }

    @Nullable
    public static ItemStack equippedStack(LivingEntity entity, RelicAbility ability) {
        for (var stack : equippedStacks(entity)) {
            if (abilityOf(stack) == ability) return stack;
        }
        return null;
    }

    public static RelicPlayerData dataOf(ServerPlayerEntity player) {
        return RelicDataState.get(player.getServer()).of(player.getUuid());
    }

    public static void fire(ServerPlayerEntity player, TriggerType type, @Nullable LivingEntity target,
                            @Nullable DamageSource source, float amount) {
        if (suppressed > 0 || !player.isAlive()) return;
        var abilities = collect(player, type);
        if (abilities.isEmpty()) return;
        var data = dataOf(player);
        abilities.forEach((ability, stack) ->
                ability.onTrigger(new TriggerContext(player, stack, type, ability, data, target, source, amount)));
    }

    public interface SpellBridge {
        boolean beforeDamage(ServerPlayerEntity player, float amount);

        void onHeal(ServerPlayerEntity player);
    }

    @Nullable
    public static SpellBridge spellBridge;

    public static boolean beforeDamage(ServerPlayerEntity player, DamageSource source, float amount) {
        if (suppressed > 0 || !player.isAlive() || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        if (spellBridge != null && spellBridge.beforeDamage(player, amount)) return true;
        var abilities = collect(player, TriggerType.LOW_HEALTH);
        if (abilities.isEmpty()) return false;
        var data = dataOf(player);
        var attacker = source.getAttacker() instanceof LivingEntity living ? living : null;
        boolean cancelled = false;
        for (var entry : abilities.entrySet()) {
            var context = new TriggerContext(player, entry.getValue(), TriggerType.LOW_HEALTH, entry.getKey(), data, attacker, source, amount);
            entry.getKey().onTrigger(context);
            cancelled |= context.isCancelled();
        }
        return cancelled;
    }

    public static void onDamageApplied(LivingEntity victim, DamageSource source, float amount) {
        if (suppressed > 0 || victim.getWorld().isClient) return;
        if (source.getAttacker() instanceof ServerPlayerEntity attacker && attacker != victim) {
            if (source.getSource() instanceof PersistentProjectileEntity) {
                fire(attacker, TriggerType.ARROW_HIT, victim, source, amount);
            } else if (source.isOf(DamageTypes.PLAYER_ATTACK)) {
                fire(attacker, TriggerType.MELEE_HIT, victim, source, amount);
            } else if (isSpellDamage(source)) {
                fire(attacker, TriggerType.SPELL_HIT, victim, source, amount);
            }
        }
        if (victim instanceof ServerPlayerEntity player) {
            var attacker = source.getAttacker() instanceof LivingEntity living ? living : null;
            fire(player, TriggerType.DAMAGE_TAKEN, attacker, source, amount);
        }
    }

    private static boolean isSpellDamage(DamageSource source) {
        return source.getTypeRegistryEntry().getKey()
                .map(key -> key.getValue().getNamespace())
                .filter(namespace -> !namespace.equals("minecraft") && !namespace.equals(MOD_ID))
                .isPresent();
    }

    public static void onDeath(LivingEntity victim, DamageSource source) {
        if (victim.getWorld().isClient) return;
        if (victim instanceof ServerPlayerEntity player) {
            dataOf(player).resetOnDeath();
            syncBuffs(player);
        }
        if (source.getAttacker() instanceof ServerPlayerEntity killer && killer != victim) {
            fire(killer, TriggerType.KILL, victim, source, 0);
        }
    }

    public static void onHeal(ServerPlayerEntity player, float amount) {
        if (amount <= 0 || player.getHealth() >= player.getMaxHealth()) return;
        if (spellBridge != null && suppressed == 0) spellBridge.onHeal(player);
        fire(player, TriggerType.HEALED, null, null, amount);
    }

    public static TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        var ability = abilityOf(stack);
        if (ability == null || !ability.isActive() || !ability.enabled() || hand != Hand.MAIN_HAND) {
            return TypedActionResult.pass(stack);
        }
        if (user.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            return TypedActionResult.pass(stack);
        }
        if (ability.channeled()) {
            if (user instanceof ServerPlayerEntity player && !dataOf(player).isReady(ability.relic(), player.getServerWorld().getTime())) {
                player.sendMessage(Text.translatable(MOD_ID + ".relic.on_cooldown"), true);
                return TypedActionResult.fail(stack);
            }
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        if (!(user instanceof ServerPlayerEntity player)) {
            return TypedActionResult.consume(stack);
        }
        var context = new TriggerContext(player, stack, TriggerType.ACTIVE_USE, ability, dataOf(player), null, null, 0);
        if (!context.ready()) {
            player.sendMessage(Text.translatable(MOD_ID + ".relic.on_cooldown"), true);
            return TypedActionResult.fail(stack);
        }
        if (!ability.onTrigger(context)) {
            return TypedActionResult.pass(stack);
        }
        int remaining = (int) (context.data().cooldownEnd(ability.relic()) - context.now());
        if (remaining > 0) {
            player.getItemCooldownManager().set(stack.getItem(), remaining);
        }
        return TypedActionResult.success(stack);
    }

    public static int maxUseTime(ItemStack stack) {
        var ability = abilityOf(stack);
        return ability != null && ability.enabled() && ability.channeled() ? ability.channelTicks() : 0;
    }

    public static void stopChanneling(LivingEntity user, ItemStack stack, int remainingUseTicks) {
        var ability = abilityOf(stack);
        if (ability == null || !ability.channeled() || !(user instanceof ServerPlayerEntity player)) return;
        int used = maxUseTime(stack) - remainingUseTicks;
        if (used < 10) return;
        int cooldown = ability.cooldownTicks();
        dataOf(player).startCooldown(ability.relic(), player.getServerWorld().getTime(), cooldown);
        if (cooldown > 0) {
            player.getItemCooldownManager().set(stack.getItem(), cooldown);
        }
    }

    public static void onArrowShot(LivingEntity shooter) {
        if (shooter instanceof ServerPlayerEntity player) {
            fire(player, TriggerType.ARROW_SHOT, null, null, 0);
        }
    }

    public static void usageTick(LivingEntity user, ItemStack stack, int remainingUseTicks) {
        var ability = abilityOf(stack);
        if (ability == null || !ability.channeled() || !(user instanceof ServerPlayerEntity player)) return;
        ability.onChannelTick(player, maxUseTime(stack) - remainingUseTicks);
    }

    public static void onWorldTick(ServerWorld world) {
        long time = world.getTime();
        if (time % 5 != 0) return;
        boolean syncBuffs = time % 10 == 0;
        for (var player : world.getPlayers()) {
            if (!player.isAlive()) continue;
            var data = dataOf(player);
            for (var ability : RelicAbilities.all) {
                ability.onServerTick(player, data);
            }
            syncCooldownOverlays(player, data, time);
            if (syncBuffs) {
                syncBuffs(player);
            }
        }
    }

    private static void syncCooldownOverlays(ServerPlayerEntity player, RelicPlayerData data, long now) {
        var manager = player.getItemCooldownManager();
        for (var ability : RelicAbilities.all) {
            long remaining = data.cooldownEnd(ability.relic()) - now;
            if (remaining <= 0) continue;
            var item = Registries.ITEM.get(Identifier.of(MOD_ID, ability.relic()));
            if (!manager.isCoolingDown(item)) {
                manager.set(item, (int) remaining);
            }
        }
    }

    public static void syncBuffs(ServerPlayerEntity player) {
        var data = dataOf(player);
        Set<String> equipped = new HashSet<>();
        for (var stack : passiveStacks(player)) {
            var ability = abilityOf(stack);
            if (ability != null) equipped.add(ability.relic());
        }
        var snapshot = new ArrayList<RelicStacksPayload.Entry>();
        for (var ability : RelicAbilities.all) {
            int stacks = 0;
            int max = 0;
            if (ability instanceof StackBuffAbility buff) {
                buff.sync(player, data, equipped.contains(ability.relic()));
                stacks = buff.stacks(data);
                max = buff.enabled() ? buff.stackLimit() : 0;
            }
            var progress = ability.progress();
            if (max > 0 || progress != null) {
                snapshot.add(new RelicStacksPayload.Entry(ability.relic(), stacks, max,
                        progress == null ? 0 : data.counter(progress.counterKey()), progress == null ? 0 : progress.goal()));
            }
        }
        if (!snapshot.equals(LAST_SENT.get(player))) {
            LAST_SENT.put(player, snapshot);
            LNEEvents.get().sendToClient(player, new RelicStacksPayload(snapshot));
        }
    }
}

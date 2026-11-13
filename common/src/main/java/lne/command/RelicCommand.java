package lne.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lne.ability.RelicAbilities;
import lne.ability.RelicAbility;
import lne.ability.RelicDataState;
import lne.ability.RelicTargets;
import lne.ability.RelicTriggers;
import lne.ability.TriggerContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static lne.LneMod.MOD_ID;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class RelicCommand {
    private static final int PERMISSION_LEVEL = 2;
    private static final double TARGET_RANGE = 24.0;

    private RelicCommand() {
    }

    private static boolean allowed(ServerCommandSource source) {
        if (!source.hasPermissionLevel(PERMISSION_LEVEL)) return false;
        var player = source.getPlayer();
        return player == null || player.isCreative();
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(MOD_ID)
                .then(literal("relics")
                        .requires(RelicCommand::allowed)
                        .then(literal("reset_cooldowns")
                                .executes(context -> resetCooldowns(context.getSource(), List.of(context.getSource().getPlayerOrThrow())))
                                .then(argument("targets", EntityArgumentType.players())
                                        .executes(context -> resetCooldowns(context.getSource(), EntityArgumentType.getPlayers(context, "targets")))))
                        .then(literal("trigger")
                                .then(argument("relic", StringArgumentType.word())
                                        .suggests((context, builder) -> CommandSource.suggestMatching(RelicAbilities.all.stream().map(RelicAbility::relic), builder))
                                        .executes(context -> trigger(context.getSource(), StringArgumentType.getString(context, "relic"), null))
                                        .then(argument("target", EntityArgumentType.entity())
                                                .executes(context -> trigger(context.getSource(), StringArgumentType.getString(context, "relic"),
                                                        EntityArgumentType.getEntity(context, "target"))))))));
    }

    private static int resetCooldowns(ServerCommandSource source, Collection<ServerPlayerEntity> players) {
        for (var player : players) {
            RelicDataState.get(source.getServer()).of(player.getUuid()).clearCooldowns();
            var manager = player.getItemCooldownManager();
            for (var ability : RelicAbilities.all) {
                manager.remove(Registries.ITEM.get(Identifier.of(MOD_ID, ability.relic())));
            }
        }
        int count = players.size();
        source.sendFeedback(() -> Text.translatable(MOD_ID + ".command.relics.reset_cooldowns", count), true);
        return count;
    }

    private static int trigger(ServerCommandSource source, String relic, @Nullable Entity targetArgument) throws CommandSyntaxException {
        var player = source.getPlayerOrThrow();
        var ability = RelicAbilities.get(relic);
        if (ability == null) {
            source.sendError(Text.translatable(MOD_ID + ".command.relics.unknown", relic));
            return 0;
        }
        var name = Text.translatable("item." + MOD_ID + "." + relic);
        if (!ability.enabled()) {
            source.sendError(Text.translatable(MOD_ID + ".command.relics.disabled", name));
            return 0;
        }
        if (ability.channeled()) {
            RelicTriggers.forceChannel(player, ability);
            source.sendFeedback(() -> Text.translatable(MOD_ID + ".command.relics.triggered", name), false);
            return 1;
        }
        LivingEntity target = targetArgument instanceof LivingEntity living ? living : findTarget(player);
        var stack = RelicTriggers.equippedStack(player, ability);
        if (stack == null) {
            stack = new ItemStack(Registries.ITEM.get(Identifier.of(MOD_ID, relic)));
        }
        var type = ability.triggers().iterator().next();
        var context = new TriggerContext(player, stack, type, ability, RelicTriggers.dataOf(player), target, null, 0.0F, true);
        boolean fired = ability.onTrigger(context);
        RelicTriggers.syncBuffs(player);
        if (!fired) {
            source.sendError(Text.translatable(MOD_ID + ".command.relics.not_triggered", name));
            return 0;
        }
        source.sendFeedback(() -> Text.translatable(MOD_ID + ".command.relics.triggered", name), false);
        return 1;
    }

    private static LivingEntity findTarget(ServerPlayerEntity player) {
        var eye = player.getEyePos();
        var end = eye.add(player.getRotationVec(1.0F).multiply(TARGET_RANGE));
        var box = player.getBoundingBox().stretch(end.subtract(eye)).expand(1.0);
        var hit = ProjectileUtil.raycast(player, eye, end, box, entity -> entity instanceof LivingEntity && entity.isAlive(), TARGET_RANGE * TARGET_RANGE);
        if (hit != null && hit.getEntity() instanceof LivingEntity living) {
            return living;
        }
        return RelicTargets.enemiesAround(player.getServerWorld(), player, player.getPos(), 16.0).stream()
                .min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(player)))
                .orElse(player);
    }
}

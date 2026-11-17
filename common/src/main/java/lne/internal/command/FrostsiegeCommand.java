package lne.internal.command;

import com.mojang.brigadier.CommandDispatcher;
import lne.server.frostsiege.Frostsiege;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static lne.LneMod.MOD_ID;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class FrostsiegeCommand {
    private static final int PERMISSION_LEVEL = 2;

    private FrostsiegeCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(MOD_ID)
                .then(literal("frostsiege")
                        .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                        .then(literal("start")
                                .executes(context -> start(context.getSource(), context.getSource().getPlayerOrThrow()))
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context -> start(context.getSource(), EntityArgumentType.getPlayer(context, "player")))))
                        .then(literal("stop")
                                .executes(context -> stop(context.getSource(), context.getSource().getPlayerOrThrow()))
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context -> stop(context.getSource(), EntityArgumentType.getPlayer(context, "player")))))));
    }

    private static int start(ServerCommandSource source, ServerPlayerEntity player) {
        if (!Frostsiege.start(player)) {
            source.sendError(Text.translatable("commands.loot_n_explore.frostsiege.start.failed"));
            return 0;
        }
        source.sendFeedback(() -> Text.translatable("commands.loot_n_explore.frostsiege.start", player.getDisplayName()), true);
        return 1;
    }

    private static int stop(ServerCommandSource source, ServerPlayerEntity player) {
        if (!Frostsiege.stop(player)) {
            source.sendError(Text.translatable("commands.loot_n_explore.frostsiege.stop.failed"));
            return 0;
        }
        source.sendFeedback(() -> Text.translatable("commands.loot_n_explore.frostsiege.stop", player.getDisplayName()), true);
        return 1;
    }
}

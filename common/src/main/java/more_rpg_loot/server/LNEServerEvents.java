package more_rpg_loot.server;

import more_rpg_loot.network.FrozenDepthsMusicPayload;
import more_rpg_loot.platform.LNEEvents;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNEServerEvents {
    private static final TagKey<Structure> FROZEN_DEPTHS_MUSIC_TAG =
            TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(MOD_ID, "plays_frozen_depths_music"));
    private static final Map<MinecraftServer, Set<UUID>> PLAYERS_IN_FROZEN_DEPTHS = new WeakHashMap<>();

    public static void register() {
        LNEEvents.get().registerServerWorldTick(LNEServerEvents::onWorldTick);
    }

    private static void onWorldTick(ServerWorld world) {
        if (world.getTime() % 40 != 0) return;

        Set<UUID> playersInFrozenDepths = PLAYERS_IN_FROZEN_DEPTHS.computeIfAbsent(world.getServer(), k -> new HashSet<>());
        playersInFrozenDepths.removeIf(uuid -> world.getServer().getPlayerManager().getPlayer(uuid) == null);

        for (ServerPlayerEntity player : world.getPlayers()) {
            StructureStart start = world.getStructureAccessor()
                    .getStructureContaining(player.getBlockPos(), FROZEN_DEPTHS_MUSIC_TAG);
            boolean inStructure = start.hasChildren();
            boolean wasInStructure = playersInFrozenDepths.contains(player.getUuid());

            if (inStructure && !wasInStructure) {
                playersInFrozenDepths.add(player.getUuid());
                LNEEvents.get().sendToClient(player, new FrozenDepthsMusicPayload(true));
            } else if (!inStructure && wasInStructure) {
                playersInFrozenDepths.remove(player.getUuid());
                LNEEvents.get().sendToClient(player, new FrozenDepthsMusicPayload(false));
            }
        }
    }
}

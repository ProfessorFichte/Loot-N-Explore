package lne.server;

import lne.server.frostsiege.Frostsiege;
import lne.blocks.frozen_depths.FrozenBonesAmbush;
import lne.ability.RelicTriggers;
import lne.ability.RelicZones;
import lne.mixin.SinglePoolElementAccessor;
import lne.network.StructureMusicPayload;
import lne.platform.LNEEvents;
import lne.sounds.StructureMusicTrack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import static lne.LneMod.MOD_ID;

public class LNEServerEvents {
    private static final TagKey<Structure> FROZEN_DEPTHS_MUSIC_TAG =
            TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(MOD_ID, "plays_frozen_depths_music"));
    private static final TagKey<Structure> INN_MUSIC_TAG =
            TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(MOD_ID, "plays_inn_music"));
    private static final Map<MinecraftServer, Map<UUID, StructureMusicTrack>> PLAYING_TRACKS = new WeakHashMap<>();

    public static void register() {
        LNEEvents.get().registerServerWorldTick(LNEServerEvents::onWorldTick);
        LNEEvents.get().registerServerWorldTick(RelicTriggers::onWorldTick);
        LNEEvents.get().registerServerWorldTick(RelicZones::tick);
        LNEEvents.get().registerServerWorldTick(RelicTriggers::tickForcedChannels);
        LNEEvents.get().registerServerWorldTick(FrozenBonesAmbush::tick);
        LNEEvents.get().registerServerWorldTick(Frostsiege::tick);
        LNEEvents.get().onServerStarted(server -> RelicZones.clear());
    }

    private static void onWorldTick(ServerWorld world) {
        if (world.getTime() % 40 != 0) return;

        Map<UUID, StructureMusicTrack> playing = PLAYING_TRACKS.computeIfAbsent(world.getServer(), k -> new HashMap<>());
        playing.keySet().removeIf(uuid -> world.getServer().getPlayerManager().getPlayer(uuid) == null);

        for (ServerPlayerEntity player : world.getPlayers()) {
            StructureMusicTrack track = Frostsiege.isBesieged(player) ? StructureMusicTrack.FROZEN_DEPTHS : trackAt(world, player.getBlockPos());
            StructureMusicTrack previous = playing.get(player.getUuid());

            if (previous != null && previous != track) {
                LNEEvents.get().sendToClient(player, new StructureMusicPayload(previous, false));
            }
            if (track != null) {
                playing.put(player.getUuid(), track);
                LNEEvents.get().sendToClient(player, new StructureMusicPayload(track, true));
            } else {
                playing.remove(player.getUuid());
            }
        }
    }

    @Nullable
    private static StructureMusicTrack trackAt(ServerWorld world, BlockPos pos) {
        StructureAccessor structures = world.getStructureAccessor();
        if (structures.getStructureContaining(pos, FROZEN_DEPTHS_MUSIC_TAG).hasChildren()) {
            return StructureMusicTrack.FROZEN_DEPTHS;
        }
        if (structures.getStructureContaining(pos, INN_MUSIC_TAG).hasChildren()
                || isInsideVillageInn(structures.getStructureContaining(pos, StructureTags.VILLAGE), pos)) {
            return StructureMusicTrack.INN;
        }
        return null;
    }

    private static boolean isInsideVillageInn(StructureStart village, BlockPos pos) {
        if (!village.hasChildren()) {
            return false;
        }
        for (StructurePiece piece : village.getChildren()) {
            if (piece instanceof PoolStructurePiece poolPiece
                    && poolPiece.getBoundingBox().contains(pos)
                    && poolPiece.getPoolElement() instanceof SinglePoolElement element
                    && ((SinglePoolElementAccessor) element).lne$getLocation().left().filter(LNEServerEvents::isInnTemplate).isPresent()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInnTemplate(Identifier id) {
        return id.getNamespace().equals(MOD_ID) && id.getPath().startsWith("village/") && id.getPath().endsWith("/small_inn");
    }
}

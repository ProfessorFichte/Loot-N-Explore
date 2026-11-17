package lne.internal.ability;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static lne.LneMod.MOD_ID;

public class RelicDataState extends PersistentState {
    private static final String KEY = MOD_ID + "_relic_data";
    private static final Type<RelicDataState> TYPE = new Type<>(RelicDataState::new, RelicDataState::fromNbt, (DataFixTypes) null);

    private final Map<UUID, RelicPlayerData> players = new HashMap<>();

    public static RelicDataState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE, KEY);
    }

    public RelicPlayerData of(UUID player) {
        return players.computeIfAbsent(player, id -> new RelicPlayerData(this));
    }

    private static RelicDataState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        var state = new RelicDataState();
        var playersNbt = nbt.getCompound("players");
        for (var key : playersNbt.getKeys()) {
            UUID player;
            try {
                player = UUID.fromString(key);
            } catch (IllegalArgumentException e) {
                continue;
            }
            var data = new RelicPlayerData(state);
            data.load(playersNbt.getCompound(key));
            state.players.put(player, data);
        }
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        var playersNbt = new NbtCompound();
        players.forEach((id, data) -> playersNbt.put(id.toString(), data.toNbt()));
        nbt.put("players", playersNbt);
        return nbt;
    }
}

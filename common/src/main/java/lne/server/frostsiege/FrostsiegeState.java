package lne.server.frostsiege;

import org.jetbrains.annotations.Nullable;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static lne.LneMod.MOD_ID;

public class FrostsiegeState extends PersistentState {
    private static final String KEY = MOD_ID + "_frostsiege";
    private static final Type<FrostsiegeState> TYPE = new Type<>(FrostsiegeState::new, FrostsiegeState::fromNbt, (DataFixTypes) null);

    private final Map<UUID, Long> cooldownUntil = new HashMap<>();
    private final Map<UUID, Float> chanceBonus = new HashMap<>();

    public static FrostsiegeState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(TYPE, KEY);
    }

    public boolean onCooldown(UUID player, long time) {
        return cooldownUntil.getOrDefault(player, 0L) > time;
    }

    public void startCooldown(UUID player, long until) {
        cooldownUntil.put(player, until);
        chanceBonus.remove(player);
        markDirty();
    }

    public float chanceBonus(UUID player) {
        return chanceBonus.getOrDefault(player, 0.0F);
    }

    public void addChanceBonus(UUID player, float bonus, float max) {
        chanceBonus.put(player, Math.min(max, chanceBonus(player) + bonus));
        markDirty();
    }

    private static FrostsiegeState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        var state = new FrostsiegeState();
        var cooldowns = nbt.getCompound("cooldowns");
        for (var key : cooldowns.getKeys()) {
            UUID player = parse(key);
            if (player != null) state.cooldownUntil.put(player, cooldowns.getLong(key));
        }
        var bonuses = nbt.getCompound("chance_bonus");
        for (var key : bonuses.getKeys()) {
            UUID player = parse(key);
            if (player != null) state.chanceBonus.put(player, bonuses.getFloat(key));
        }
        return state;
    }

    @Nullable
    private static UUID parse(String key) {
        try {
            return UUID.fromString(key);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        var cooldowns = new NbtCompound();
        cooldownUntil.forEach((id, until) -> cooldowns.putLong(id.toString(), until));
        nbt.put("cooldowns", cooldowns);
        var bonuses = new NbtCompound();
        chanceBonus.forEach((id, bonus) -> bonuses.putFloat(id.toString(), bonus));
        nbt.put("chance_bonus", bonuses);
        return nbt;
    }
}

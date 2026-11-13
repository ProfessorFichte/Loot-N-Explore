package lne.ability;

import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class RelicPlayerData {
    private final Map<String, Integer> counters = new HashMap<>();
    private final Map<String, Double> values = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final RelicDataState owner;

    RelicPlayerData(RelicDataState owner) {
        this.owner = owner;
    }

    public int counter(String key) {
        return counters.getOrDefault(key, 0);
    }

    public void setCounter(String key, int value) {
        if (value <= 0) {
            counters.remove(key);
        } else {
            counters.put(key, value);
        }
        owner.markDirty();
    }

    public int increment(String key) {
        int next = counter(key) + 1;
        setCounter(key, next);
        return next;
    }

    public double value(String key) {
        return values.getOrDefault(key, 0.0);
    }

    public void setValue(String key, double value) {
        if (value == 0) {
            values.remove(key);
        } else {
            values.put(key, value);
        }
        owner.markDirty();
    }

    public long cooldownEnd(String key) {
        return cooldowns.getOrDefault(key, 0L);
    }

    public boolean isReady(String key, long now) {
        return now >= cooldownEnd(key);
    }

    public void startCooldown(String key, long now, int ticks) {
        if (ticks <= 0) return;
        cooldowns.put(key, now + ticks);
        owner.markDirty();
    }

    public void clearCooldowns() {
        cooldowns.clear();
        owner.markDirty();
    }

    public void resetOnDeath() {
        counters.clear();
        values.clear();
        owner.markDirty();
    }

    NbtCompound toNbt() {
        var nbt = new NbtCompound();
        var counterNbt = new NbtCompound();
        counters.forEach(counterNbt::putInt);
        var valueNbt = new NbtCompound();
        values.forEach(valueNbt::putDouble);
        var cooldownNbt = new NbtCompound();
        cooldowns.forEach(cooldownNbt::putLong);
        nbt.put("counters", counterNbt);
        nbt.put("values", valueNbt);
        nbt.put("cooldowns", cooldownNbt);
        return nbt;
    }

    void load(NbtCompound nbt) {
        var counterNbt = nbt.getCompound("counters");
        counterNbt.getKeys().forEach(key -> counters.put(key, counterNbt.getInt(key)));
        var valueNbt = nbt.getCompound("values");
        valueNbt.getKeys().forEach(key -> values.put(key, valueNbt.getDouble(key)));
        var cooldownNbt = nbt.getCompound("cooldowns");
        cooldownNbt.getKeys().forEach(key -> cooldowns.put(key, cooldownNbt.getLong(key)));
    }
}

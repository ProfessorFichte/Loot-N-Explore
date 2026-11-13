package lne.ability;

import lne.config.TweaksConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static lne.LneMod.MOD_ID;

public abstract class RelicAbility {
    public static final String COOLDOWN = "cooldown_ticks";
    public static final String PROGRESS_KILLS = MOD_ID + ".relic.progress.kills";
    public static final String PROGRESS_HITS = MOD_ID + ".relic.progress.hits";
    public static final String PROGRESS_HITS_TAKEN = MOD_ID + ".relic.progress.hits_taken";

    public record Progress(String counterKey, int goal, String labelKey) {
    }

    private final String relic;

    protected RelicAbility(String relic) {
        this.relic = relic;
    }

    public String relic() {
        return relic;
    }

    public abstract Set<TriggerType> triggers();

    public Set<TriggerType> passiveTriggers() {
        return Set.of();
    }

    public boolean hybrid() {
        return !passiveTriggers().isEmpty();
    }

    public Set<TriggerType> passiveTriggerSet() {
        return hybrid() ? passiveTriggers() : triggers();
    }

    public AbilityTheme theme() {
        return AbilityTheme.ofRelic(relic);
    }

    protected abstract void defaults(Map<String, Double> values);

    public abstract String name();

    public abstract String description();

    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[0];
    }

    @Nullable
    public Progress progress() {
        return null;
    }

    protected String scaledNumber(@Nullable LivingEntity viewer, String baseKey, String scalingKey) {
        double base = value(baseKey);
        return RelicText.number(viewer == null ? base : AbilityDamage.scaled(viewer, base, value(scalingKey)));
    }

    public abstract boolean onTrigger(TriggerContext context);

    public void onServerTick(ServerPlayerEntity player, RelicPlayerData data) {
    }

    public boolean channeled() {
        return false;
    }

    public void onChannelTick(ServerPlayerEntity player, int ticksUsed) {
    }

    public final Map<String, Double> defaultValues() {
        var values = new LinkedHashMap<String, Double>();
        values.put(COOLDOWN, 0.0);
        defaults(values);
        return values;
    }

    public boolean isActive() {
        return triggers().contains(TriggerType.ACTIVE_USE);
    }

    public String langKey() {
        return "item." + MOD_ID + "." + relic + ".ability";
    }

    public String nameKey() {
        return langKey() + ".name";
    }

    public boolean replacedBySpellEngine() {
        return true;
    }

    public boolean enabled() {
        if (!TweaksConfig.relicAbilities() || replacedBySpellEngine() && TweaksConfig.spellEngineAbilities()) {
            return false;
        }
        var entry = RelicAbilities.entry(relic);
        return entry == null || entry.enabled;
    }

    public int channelTicks() {
        return Math.max(1, ticks("channel_ticks"));
    }

    public double value(String key) {
        var entry = RelicAbilities.entry(relic);
        if (entry != null && entry.values.containsKey(key)) {
            return entry.values.get(key);
        }
        return defaultValues().getOrDefault(key, 0.0);
    }

    public int ticks(String key) {
        return (int) Math.round(value(key));
    }

    public int cooldownTicks() {
        return ticks(COOLDOWN);
    }

}

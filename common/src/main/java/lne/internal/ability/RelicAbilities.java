package lne.internal.ability;

import lne.internal.ability.impl.AmethystBurstAbility;
import lne.internal.ability.impl.CorruptedPearlAbility;
import lne.internal.ability.impl.DragonToothAbility;
import lne.internal.ability.impl.FrostStormAbility;
import lne.internal.ability.impl.DragonBreathCloudAbility;
import lne.internal.ability.impl.FrostHauntedSummonAbility;
import lne.internal.ability.impl.FrozenSoulAbility;
import lne.internal.ability.impl.GuardianBeamAbility;
import lne.internal.ability.impl.IcicleRingAbility;
import lne.internal.ability.impl.SoothingWaterAbility;
import lne.internal.ability.impl.WitherSkullVolleyAbility;
import lne.internal.ability.impl.LostSoulAbility;
import lne.internal.ability.impl.CoralShieldAbility;
import lne.internal.ability.impl.AmphoraWardAbility;
import lne.internal.ability.impl.SkeletonSummonAbility;
import lne.internal.ability.impl.WitherAuraAbility;
import net.tiny_config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static lne.LneMod.MOD_ID;

public class RelicAbilities {
    public static final ConfigManager<RelicAbilityConfig> config = new ConfigManager<>("relic_abilities_v2", new RelicAbilityConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    private static final Map<String, RelicAbility> BY_RELIC = new LinkedHashMap<>();

    public static final List<RelicAbility> all = new ArrayList<>();

    static {
        add(new DragonToothAbility());
        add(new AmethystBurstAbility());
        add(new CorruptedPearlAbility());
        add(new SkeletonSummonAbility());
        add(new LostSoulAbility());
        add(new WitherAuraAbility());
        add(new FrostStormAbility());
        add(new CoralShieldAbility());
        add(new AmphoraWardAbility());
        add(new SoothingWaterAbility());
        add(new IcicleRingAbility());
        add(new FrostHauntedSummonAbility());
        add(new DragonBreathCloudAbility());
        add(new WitherSkullVolleyAbility());
        add(new FrozenSoulAbility());
        add(new GuardianBeamAbility());
    }

    private static void add(RelicAbility ability) {
        all.add(ability);
        BY_RELIC.put(ability.relic(), ability);
    }

    @Nullable
    public static RelicAbility get(String relic) {
        return BY_RELIC.get(relic);
    }

    @Nullable
    public static RelicAbilityConfig.Entry entry(String relic) {
        return config.value.abilities.get(relic);
    }

    public static void loadConfig() {
        config.refresh();
        for (var ability : all) {
            var entry = config.value.abilities.computeIfAbsent(ability.relic(), key -> new RelicAbilityConfig.Entry());
            var defaults = ability.defaultValues();
            entry.values.keySet().retainAll(defaults.keySet());
            defaults.forEach(entry.values::putIfAbsent);
        }
        config.save();
    }
}

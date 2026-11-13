package lne.ability;

import lne.ability.impl.AmethystBurstAbility;
import lne.ability.impl.CorruptedPearlAbility;
import lne.ability.impl.DragonToothAbility;
import lne.ability.impl.FrostStormAbility;
import lne.ability.impl.DragonBreathCloudAbility;
import lne.ability.impl.FrostHauntedSummonAbility;
import lne.ability.impl.FrozenSoulAbility;
import lne.ability.impl.GuardianBeamAbility;
import lne.ability.impl.IcicleRingAbility;
import lne.ability.impl.SoothingWaterAbility;
import lne.ability.impl.WitherSkullVolleyAbility;
import lne.ability.impl.LostSoulAbility;
import lne.ability.impl.CoralShieldAbility;
import lne.ability.impl.AmphoraWardAbility;
import lne.ability.impl.SkeletonSummonAbility;
import lne.ability.impl.WitherAuraAbility;
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

package lne.internal.ability;

import java.util.LinkedHashMap;

public class RelicAbilityConfig {
    public LinkedHashMap<String, Entry> abilities = new LinkedHashMap<>();

    public static class Entry {
        public boolean enabled = true;
        public LinkedHashMap<String, Double> values = new LinkedHashMap<>();
    }
}

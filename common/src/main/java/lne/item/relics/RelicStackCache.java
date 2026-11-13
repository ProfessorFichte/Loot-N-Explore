package lne.item.relics;

import lne.network.RelicStacksPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RelicStackCache {
    private static final Map<String, RelicStacksPayload.Entry> ENTRIES = new HashMap<>();

    private RelicStackCache() {
    }

    public static void update(List<RelicStacksPayload.Entry> entries) {
        ENTRIES.clear();
        entries.forEach(entry -> ENTRIES.put(entry.relic(), entry));
    }

    public static RelicStacksPayload.Entry get(String relic) {
        return ENTRIES.get(relic);
    }

    public static void clear() {
        ENTRIES.clear();
    }
}

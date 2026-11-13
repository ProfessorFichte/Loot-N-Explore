package lne.util;

import lne.LneMod;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Map;

public final class LegacyIds {
    public static final Map<String, String> ENTITY_TYPES = Map.of(
            "frost_haunt", "frost_haunted",
            "relic_frosthaunt", "relic_frost_haunted",
            "undead_frozen_mage", "frozen_magician",
            "general_undead_frozen_mage", "general_frozen_magician",
            "frosted_ranger", "frozen_ranger",
            "general_frosted_ranger", "general_frozen_ranger");

    public static final Map<String, String> ITEMS = Map.of(
            "frost_haunt_egg", "frost_haunted_egg",
            "undead_frozen_mage_egg", "frozen_magician_egg",
            "general_undead_frozen_mage_egg", "general_frozen_magician_egg",
            "frosted_ranger_egg", "frozen_ranger_egg",
            "general_frosted_ranger_egg", "general_frozen_ranger_egg");

    private LegacyIds() {
    }

    public static Identifier remap(RegistryKey<? extends Registry<?>> registry, Identifier id) {
        if (id == null || !LneMod.MOD_ID.equals(id.getNamespace())) return id;
        Map<String, String> aliases = registry == RegistryKeys.ENTITY_TYPE ? ENTITY_TYPES
                : registry == RegistryKeys.ITEM ? ITEMS : null;
        if (aliases == null) return id;
        String renamed = aliases.get(id.getPath());
        return renamed != null ? LneMod.id(renamed) : id;
    }

    public static String remapEntityId(String id) {
        Identifier parsed = Identifier.tryParse(id);
        Identifier renamed = remap(RegistryKeys.ENTITY_TYPE, parsed);
        return renamed != parsed ? renamed.toString() : id;
    }
}

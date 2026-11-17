package lne.worldgen.map;

import lne.LneMod;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModMapDecorations {
    public static final RegistryKey<MapDecorationType> MONARCHS_TEMPLE_KEY = key("monarchs_temple");
    public static final RegistryKey<MapDecorationType> MONARCHS_CASTLE_KEY = key("monarchs_castle");
    public static final RegistryKey<MapDecorationType> GLACIAL_TOMB_KEY = key("glacial_tomb");

    public static RegistryEntry<MapDecorationType> MONARCHS_TEMPLE;
    public static RegistryEntry<MapDecorationType> MONARCHS_CASTLE;
    public static RegistryEntry<MapDecorationType> GLACIAL_TOMB;

    public static void register() {
        MONARCHS_TEMPLE = register(MONARCHS_TEMPLE_KEY);
        MONARCHS_CASTLE = register(MONARCHS_CASTLE_KEY);
        GLACIAL_TOMB = register(GLACIAL_TOMB_KEY);
    }

    private static RegistryKey<MapDecorationType> key(String name) {
        return RegistryKey.of(RegistryKeys.MAP_DECORATION_TYPE, Identifier.of(LneMod.MOD_ID, name));
    }

    private static RegistryEntry<MapDecorationType> register(RegistryKey<MapDecorationType> key) {
        MapDecorationType decorationType = new MapDecorationType(key.getValue(), true, -1, false, true);
        return Registry.registerReference(Registries.MAP_DECORATION_TYPE, key, decorationType);
    }
}

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
    public static final RegistryKey<MapDecorationType> MONARCHS_TEMPLE_KEY = RegistryKey.of(
            RegistryKeys.MAP_DECORATION_TYPE,
            Identifier.of(LneMod.MOD_ID, "monarchs_temple")
    );

    public static RegistryEntry<MapDecorationType> MONARCHS_TEMPLE;

    public static void register() {
        MapDecorationType decorationType = new MapDecorationType(
                Identifier.of(LneMod.MOD_ID, "monarchs_temple"),
                true,
                -1,
                false,
                true
        );

        MONARCHS_TEMPLE = Registry.registerReference(
                Registries.MAP_DECORATION_TYPE,
                MONARCHS_TEMPLE_KEY,
                decorationType
        );
    }
}

package more_rpg_loot.worldgen.map;

import more_rpg_loot.RPGLoot;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModMapDecorations {
    public static final RegistryKey<MapDecorationType> FROSTMONARCH_TEMPLE_KEY = RegistryKey.of(
            RegistryKeys.MAP_DECORATION_TYPE,
            Identifier.of(RPGLoot.MOD_ID, "frostmonarch_temple")
    );

    public static RegistryEntry<MapDecorationType> FROSTMONARCH_TEMPLE;

    public static void register() {
        MapDecorationType decorationType = new MapDecorationType(
                Identifier.of(RPGLoot.MOD_ID, "frostmonarch_temple"),
                true,
                -1,
                false,
                true
        );

        FROSTMONARCH_TEMPLE = Registry.registerReference(
                Registries.MAP_DECORATION_TYPE,
                FROSTMONARCH_TEMPLE_KEY,
                decorationType
        );
    }
}

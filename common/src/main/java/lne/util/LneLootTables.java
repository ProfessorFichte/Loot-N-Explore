package lne.util;

import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public class LneLootTables {
    public static final RegistryKey<LootTable> FROZEN_OMINOUS_ITEMS =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "spawners/frozen/ominous/items_to_drop"));
    public static final RegistryKey<LootTable> FROZEN_TRAPS_FROSTBALLS =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "spawners/frozen_depths/traps/frostballs"));
    public static final RegistryKey<LootTable> FROZEN_TRAPS_FROSTBALLS_ARROWS =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "spawners/frozen_depths/traps/frostballs_and_arrows"));
    public static final RegistryKey<LootTable> FROZEN_TRAPS_FREEZING_CLOUDS =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "spawners/frozen_depths/traps/freezing_clouds"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_INN =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/inn"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_BOOKSHELF =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/bookshelf"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_STRONGHOLD_LIBRARY =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/stronghold_library"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_WOODLAND_MANSION =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/woodland_mansion"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_SNOWY_VILLAGE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/village/snowy"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_DESERT_VILLAGE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/village/desert"));
    public static final RegistryKey<LootTable> ARCHAEOLOGY_PLAINS_SAVANNA_TAIGA_VILLAGE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "archaeology/village/plains_savanna_taiga"));

    public static final RegistryKey<LootTable> FROSTSIEGE_REWARD =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "gameplay/frostsiege/reward"));
    public static final RegistryKey<LootTable> FROSTSIEGE_REWARD_OMINOUS =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, "gameplay/frostsiege/reward_ominous"));

    public static RegistryKey<LootTable> frozenDepthsEntity(EntityType<?> type) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Registries.ENTITY_TYPE.getId(type).withPrefixedPath("entities/frozen_depths/"));
    }
}

package more_rpg_loot.item.relics;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.config.TweaksConfig;
import more_rpg_loot.entity.ModEntities;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RelicLootInjection {

    private static RegistryKey<LootTable> chestKey(String namespace, String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(namespace, "chests/" + path));
    }
    private static RegistryKey<LootTable> blockKey(String namespace, String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(namespace, "blocks/" + path));
    }
    private static RegistryKey<LootTable> spawnerKey(String namespace, String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(namespace, path));
    }

    private static final RegistryKey<LootTable> UNDERWATER_RUIN_BIG = chestKey("minecraft", "underwater_ruin_big");
    private static final RegistryKey<LootTable> SHIPWRECK_TREASURE = chestKey("minecraft", "shipwreck_treasure");
    private static final RegistryKey<LootTable> BURIED_TREASURE = chestKey("minecraft", "buried_treasure");
    private static final RegistryKey<LootTable> END_CITY_TREASURE = chestKey("minecraft", "end_city_treasure");
    private static final RegistryKey<LootTable> IGLOO = chestKey("minecraft", "igloo_chest");
    private static final RegistryKey<LootTable> BASTION_TREASURE = chestKey("minecraft", "bastion_treasure");
    private static final RegistryKey<LootTable> AMETHYST_CLUSTER = blockKey("minecraft", "amethyst_cluster");
    private static final RegistryKey<LootTable> GLAZE_TOWER_NORMAL = spawnerKey("loot_n_explore", "spawners/frozen/normal/glaze_tower");
    private static final RegistryKey<LootTable> GLAZE_TOWER_OMINOUS = spawnerKey("loot_n_explore", "spawners/frozen/ominous/glaze_tower");
    private static final RegistryKey<LootTable> GLACIAL_TOMB_VAULT = spawnerKey("loot_n_explore", "chests/trials/frozen/reward_glacial_tomb");
    private static final RegistryKey<LootTable> GLACIAL_TOMB_OMINOUS = spawnerKey("loot_n_explore", "chests/trials/frozen/reward_ominous_glacial_tomb");

    public static void inject() {
        TweaksConfig tweaks;
        try {
            tweaks = RPGLoot.tweaksConfig != null ? RPGLoot.tweaksConfig.value : new TweaksConfig();
        } catch (NoClassDefFoundError e) {
            tweaks = new TweaksConfig();
        }
        final TweaksConfig cfg = tweaks;

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;

            float boss = cfg.boss_relic_dropchance;
            float chest = cfg.chest_relic_dropchance;
            float block = cfg.block_relic_dropchance;
            float entity = cfg.entity_relic_dropchance;
            float trial = cfg.trial_spawner_relic_dropchance;
            float trialOminous = cfg.trial_spawner_ominous_relic_dropchance;
            float vault = cfg.vault_relic_dropchance;
            float vaultOminous = cfg.vault_ominous_relic_dropchance;

            if (EntityType.ENDER_DRAGON.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.ENDER_DRAGON_SCALES, boss));
            else if (EntityType.ELDER_GUARDIAN.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.ELDER_GUARDIAN_EYE, boss));
            else if (EntityType.WITHER.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.WITHER_SPINE, boss));
            else if (ModEntities.FROST_MONARCH.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.FROZEN_SOUL, boss));

            if (UNDERWATER_RUIN_BIG.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.POSEIDONS_AMPHORA, chest));
            if (SHIPWRECK_TREASURE.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.AMPHITRITE_DIADEM, chest));
            if (BURIED_TREASURE.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.RAINBOW_CORAL, chest));
            if (END_CITY_TREASURE.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.ENDER_DRAGON_TOOTH, chest));
            if (IGLOO.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.ETERNAL_SNOWFLAKE, chest));
            if (BASTION_TREASURE.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.WITHERED_OBSIDIAN_SHARD, chest));

            if (AMETHYST_CLUSTER.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.CHARGED_AMETHYST, block));

            if (EntityType.ENDERMAN.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.CORRUPTED_ENDER_PEARL, entity));
            if (EntityType.SKELETON.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.UNKNOWN_REMAINS, entity));
            if (EntityType.WITHER_SKELETON.getLootTableId().equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.LOST_SOUL, entity));

            if (GLAZE_TOWER_NORMAL.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.GLACIER_SHARD, trial));
            if (GLAZE_TOWER_OMINOUS.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.GLACIER_SHARD, trialOminous));
            if (GLACIAL_TOMB_VAULT.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.FROZEN_RIB, vault));
            if (GLACIAL_TOMB_OMINOUS.equals(key))
                tableBuilder.pool(relicPool(LNE_RelicItems.FROZEN_RIB, vaultOminous));
        });
    }

    private static LootPool relicPool(LNE_RelicItems.Entry entry, float chance) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(entry.item().get()))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build())
                .build();
    }
}

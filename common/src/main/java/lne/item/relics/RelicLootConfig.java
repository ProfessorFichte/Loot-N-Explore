package lne.item.relics;

import java.util.LinkedHashMap;
import java.util.List;

public class RelicLootConfig {
    public record Item(String itemId, int weight) {}

    public record Entry(float rolls, List<Item> items) {}

    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    public static RelicLootConfig defaults() {
        RelicLootConfig config = new RelicLootConfig();

        drop(config, "minecraft:entities/ender_dragon", 1.0F, "ender_dragon_scales");
        drop(config, "minecraft:entities/elder_guardian", 1.0F, "elder_guardian_eye");
        drop(config, "minecraft:entities/wither", 1.0F, "wither_spine");
        drop(config, "loot_n_explore:entities/frozen_depths/frost_monarch", 1.0F, "frozen_soul");

        drop(config, "minecraft:chests/underwater_ruin_big", 0.4F, "poseidons_amphora");
        drop(config, "minecraft:chests/shipwreck_treasure", 0.4F, "amphitrite_diadem");
        drop(config, "minecraft:chests/buried_treasure", 0.4F, "rainbow_coral");
        drop(config, "minecraft:chests/end_city_treasure", 0.4F, "ender_dragon_tooth");
        drop(config, "minecraft:chests/bastion_treasure", 0.4F, "withered_obsidian_shard");

        drop(config, "minecraft:blocks/amethyst_cluster", 0.025F, "charged_amethyst");

        drop(config, "loot_n_explore:entities/frozen_depths/glaze", 0.025F, "eternal_snowflake");
        drop(config, "minecraft:entities/enderman", 0.025F, "corrupted_ender_pearl");
        drop(config, "minecraft:entities/skeleton", 0.025F, "unknown_remains");
        drop(config, "minecraft:entities/wither_skeleton", 0.025F, "lost_soul");

        drop(config, "loot_n_explore:vaults/frozen_depths/frost_tower/reward", 0.5F, "glacier_shard");
        drop(config, "loot_n_explore:vaults/frozen_depths/frost_tower/reward_ominous", 0.75F, "glacier_shard");
        drop(config, "loot_n_explore:vaults/frozen_depths/glacial_tomb/reward", 0.75F, "frozen_rib");
        drop(config, "loot_n_explore:vaults/frozen_depths/glacial_tomb/reward_ominous", 1.0F, "frozen_rib");

        return config;
    }

    private static void drop(RelicLootConfig config, String lootTable, float rolls, String relic) {
        config.entries.put(lootTable, new Entry(rolls, List.of(new Item("loot_n_explore:" + relic, 1))));
    }
}

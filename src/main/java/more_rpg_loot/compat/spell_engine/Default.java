package more_rpg_loot.compat.spell_engine;

import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.List;

public class Default {

    public final static LootConfig itemLootConfig;
    public final static LootConfig scrollLootConfig;

    private static String armors(int tier) {
        return "#" + RPGSeriesItemTags.LootTiers.id(tier, RPGSeriesItemTags.LootCategory.ARMORS).toString();
    }

    private static String weapons(int tier) {
        return "#" + RPGSeriesItemTags.LootTiers.id(tier, RPGSeriesItemTags.LootCategory.WEAPONS).toString();
    }

    private static String relics(int tier) {
        return "#" + RPGSeriesItemTags.LootTiers.id(tier, RPGSeriesItemTags.LootCategory.RELICS).toString();
    }

    private static String accessories(int tier) {
        return "#" + RPGSeriesItemTags.LootTiers.id(tier, RPGSeriesItemTags.LootCategory.ACCESSORIES).toString();
    }

    static {
        var B0 = "#loot_n_explore:innkeeper_items/tier_0_buffs";
        var B1 = "#loot_n_explore:innkeeper_items/tier_1_buffs";
        var B2 = "#loot_n_explore:innkeeper_items/tier_2_buffs";
        var B3 = "#loot_n_explore:innkeeper_items/tier_3_buffs";

        var W0 = weapons(0);
        var W1 = weapons(1);
        var W2 = weapons(2);
        var W3 = weapons(3);
        var W4 = weapons(4);
        var W5 = weapons(5);

        var A1 = armors(1);
        var A2 = armors(2);
        var A3 = armors(3);

        var X0 = accessories(0);
        var X1 = accessories(1);
        var X2 = accessories(2);
        var X3 = accessories(3);
        var X4 = accessories(4);

        var R1 = relics(1);
        var R2 = relics(2);
        var R3 = relics(3);
        var R4 = relics(4);

        itemLootConfig = new LootConfig();
        var items = itemLootConfig.injectors;
        var items_regex = itemLootConfig.regex_injectors;

        scrollLootConfig = new LootConfig();
        var scrolls = scrollLootConfig.injectors;
        var scrolls_regex = scrollLootConfig.regex_injectors;

        // Vanilla Minecraft Tables
        List.of("minecraft:chests/abandoned_mineshaft",
                        "minecraft:chests/igloo_chest",
                        "minecraft:chests/shipwreck_supply",
                        "minecraft:chests/spawn_bonus_chest").
                forEach(id ->
                        items.put(id, new LootConfig.Pool()
                                .rolls(0.5)
                                .add(B0)
                        ));
        List.of("minecraft:chests/desert_pyramid",
                        "minecraft:chests/shipwreck_treasure",
                        "minecraft:chests/underwater_ruin_small",
                        "minecraft:chests/jungle_temple",
                        "minecraft:chests/pillager_outpost",
                        "minecraft:chests/woodland_mansion",
                        "minecraft:chests/underwater_ruin_big",
                        "minecraft:chests/stronghold/library")
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(0.4)
                            .add(B1).weight(2)
                            .add(B2)
                    );
                });
        List.of("minecraft:chests/ancient_city",
                        "minecraft:chests/end_city_treasure",
                        "minecraft:chests/trial_chambers/reward_ominous_rare",
                        "minecraft:chests/trial_chambers/reward_rare")
                .forEach(id -> items.put(id, new LootConfig.Pool()
                        .rolls(0.5)
                        .add(B2).weight(2)
                        .add(B3)
                ));
        List.of("minecraft:chests/trial_chambers/reward_ominous_unique",
                        "minecraft:chests/trial_chambers/reward_unique")
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(1)
                            .add(B3)
                    );
                });

        //LOOT&EXPLORE CHESTS
        List.of("loot_n_explore:chests/inns/desert",
                "loot_n_explore:chests/inns/plains",
                "loot_n_explore:chests/vilages/plains/small_inn",
                "loot_n_explore:chests/vilages/desert/small_inn",
                "loot_n_explore:chests/vilages/savanna/small_inn",
                "loot_n_explore:chests/vilages/snowy/small_inn",
                "loot_n_explore:chests/vilages/taiga/small_inn"
                ).forEach(id -> items.put(id,  new LootConfig.Pool()
                        .rolls(0.5)
                        .add(W0)
                        .add(X0)
                        .add(A1)
                        .add(R1)
                        .add(B0)
                ));
        List.of("loot_n_explore:chests/dungeons/glacial_tomb/common",
                "loot_n_explore:spawners/frozen/normal/reward",
                "loot_n_explore:spawners/frozen/ominous/reward"
                )                .forEach(id -> {
            items.put(id, new LootConfig.Pool()
                    .rolls(0.75)
                    .add(W2)
                    .add(X2));
            scrolls.put(id, new LootConfig.Pool()
                    .rolls(0.2)
                    .scroll(2, 3));
        });

        List.of("loot_n_explore:chests/trials/frozen/reward_ominous_common",
                        "loot_n_explore:chests/trials/frozen/trial_chambers/reward_common")
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(0.5)
                            .add(W1).weight(2)
                            .add(A1).weight(2)
                            .add(R1)
                    );
                    scrolls.put(id, new LootConfig.Pool()
                            .rolls(0.5)
                            .scroll(1, 2)
                    );
                });
        List.of("loot_n_explore:chests/trials/frozen/reward_ominous_unique",
                        "loot_n_explore:chests/trials/frozen/reward_unique")
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(1)
                            .add(W3, true).weight(2)
                            .add(X4).weight(2)
                            .add(R3)
                            .add(B3)
                    );
                });

        //LOOT&EXPLORE ENTITIES
        items.put("loot_n_explore:entities/frost_monarch",  new LootConfig.Pool()
                .rolls(2)
                .add(W3).enchant().weight(4)
                .add(A3).enchant().weight(4)
                .add(X3)
                .add(R3)
                .add(W5)
        );
        /// TAVERNS RPG SERIES
        items.put("village_taverns:chests/tavern",  new LootConfig.Pool()
                .rolls(0.5)
                .add(B0)
        );
        /// AETHER
        items.put("aether:chests/dungeon/silver/silver_dungeon_reward", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("aether:chests/dungeon/gold/gold_dungeon_reward", new LootConfig.Pool()
                .rolls(0.5)
                .add(B3)
        );
        /// DNT
        items.put("nova_structures:chests/undead_crypts_grave", new LootConfig.Pool()
                .rolls(0.1)
                .add(B1)
        );
        items.put("nova_structures:chests/ancient_city", new LootConfig.Pool()
                .rolls(0.25)
                .add(B2)
        );
        items.put("nova_structures:chests/desert_ruins/desert_ruin_main_temple", new LootConfig.Pool()
                .rolls(0.4)
                .add(B1)
        );
        items.put("nova_structures:chests/end_castle/greater_loot", new LootConfig.Pool()
                .rolls(0.7)
                .add(B3)
        );
        // GRAVEYARD
        items.put("graveyard:chests/large_loot", new LootConfig.Pool()
                .rolls(0.5)
                .add(B1)
                .add(B2)
        );
        // YUNGS BETTER DUNGEON
        items.put("betterdungeons:zombie_dungeon/chests/special", new LootConfig.Pool()
                .rolls(0.3)
                .add(B0)
                .add(B1)
        );

        items.put("betterdungeons:zombie_dungeon/chests/tombstone", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        /// YUNGS BETTER DESERT TEMPLE
        items.put("betterdeserttemples:chests/tomb_pharaoh", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        /// YUNGS BETTER STRONGHOLD
        items.put("betterstrongholds:chests/grand_library", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        /// PHILIPS RUINS
        items.put("philipsruins:chest/badlands_dungeon_loot_high", new LootConfig.Pool()
                .rolls(0.5)
                .add(B1)
        );
        items.put("philipsruins:chest/end_ruins_loot", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        /// BETTER NETHER
        items.put("betternether:chests/city_surprise", new LootConfig.Pool()
                .rolls(0.7)
                .add(B3)
        );
        /// BETTER END
        items.put("betterend:chests/shadow_forest", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
                .add(B3)
        );
        /// WHEN DUNGEONS ARISE
        items.put("dungeons_arise:chests/bathhouse/bathhouse_normal", new LootConfig.Pool()
                .rolls(0.25)
                .add(B1)
                .add(B2)
        );
        items.put("dungeons_arise:chests/jungle_tree_house/jungle_tree_house_treasure", new LootConfig.Pool()
                .rolls(0.25)
                .add(B0)
                .add(B1)
        );
        items.put("dungeons_arise:chests/undead_pirate_ship/undead_pirate_ship_treasure", new LootConfig.Pool()
                .rolls(0.25)
                .add(B0)
                .add(B1)
        );
        items.put("dungeons_arise:chests/mushroom_house/mushroom_house_treasure", new LootConfig.Pool()
                .rolls(0.25)
                .add(B0)
                .add(B1)
        );
        items.put("dungeons_arise:chests/plague_asylum/plague_asylum_treasure", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("dungeons_arise:chests/shiraz_palace/shiraz_palace_treasure", new LootConfig.Pool()
                .rolls(0.7)
                .add(B3)
        );
    }
}


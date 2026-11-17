package lne.compat.spell_engine;

import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.List;

public class LootInjectionDefaults {

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
        var A4 = armors(4);
        var A5 = armors(5);

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

        // LnE - Generic
        List.of("loot_n_explore:chests/inns/desert",
                "loot_n_explore:chests/inns/plains",
                "loot_n_explore:chests/villages/plains/small_inn",
                "loot_n_explore:chests/villages/desert/small_inn",
                "loot_n_explore:chests/villages/savanna/small_inn",
                "loot_n_explore:chests/villages/snowy/small_inn",
                "loot_n_explore:chests/villages/taiga/small_inn"
        ).forEach(id -> items.put(id,  new LootConfig.Pool()
                .rolls(0.5)
                .add(B0)
        ));
        List.of("loot_n_explore:chests/inns/desert",
                "loot_n_explore:chests/inns/plains"
        ).forEach(id -> items.put(id,  new LootConfig.Pool()
                .rolls(1)
                .add(B1)
        ));
        // LnE - Frozen Depths
        // Chests
        items.put("loot_n_explore:chests/frozen_depths/outpost/supply",  new LootConfig.Pool()
                .rolls(0.3)
                .add(A1).enchant()
                .add(W1).enchant()
                .add(X1)
        );
        items.put("loot_n_explore:chests/frozen_depths/frost_tower/hidden",  new LootConfig.Pool()
                .rolls(0.5)
                .add(A2).enchant()
                .add(W2).enchant()
                .add(X1)
                .scroll(2)
        );
        items.put("loot_n_explore:chests/frozen_depths/glacial_tomb/hidden",  new LootConfig.Pool()
                .rolls(0.75)
                .add(X2)
                .add(R2)
        );
        items.put("loot_n_explore:chests/frozen_depths/monarchs_castle/hidden",  new LootConfig.Pool()
                .rolls(0.75)
                .add(A3)
                .add(W3)
                .scroll(3)
        );
        items.put("loot_n_explore:chests/frozen_depths/monarchs_temple/hidden",  new LootConfig.Pool()
                .rolls(1)
                .add(A3).enchant()
                .add(A3).enchant()
                .add(X3)
                .add(R2)
        );
        // Trial Spawners
        // Vaults
        List.of("loot_n_explore:vaults/frozen_depths/outpost/reward"
                )
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(0.5)
                            .add(W1).weight(2)
                            .add(A1).weight(2)
                            .add(R1)
                    );
                    scrolls.put(id, new LootConfig.Pool()
                            .rolls(0.25)
                            .scroll(2)
                    );
                });
        List.of("loot_n_explore:vaults/frozen_depths/glacial_tomb/reward",
                        "loot_n_explore:vaults/frozen_depths/frost_tower/reward",
                        "loot_n_explore:vaults/frozen_depths/monarchs_castle/reward"
                )
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(0.5)
                            .add(W2).weight(2).enchant()
                            .add(W2).weight(2).enchant()
                            .add(R2)
                            .add(X2)
                    );
                    scrolls.put(id, new LootConfig.Pool()
                            .rolls(0.4)
                            .scroll(3)
                    );
                });
        List.of("loot_n_explore:vaults/frozen_depths/frost_tower/reward_ominous",
                        "loot_n_explore:vaults/frozen_depths/monarchs_castle/reward_ominous"
                )
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(2)
                            .add(W3).enchant()
                            .add(X3)
                            .add(R2)
                            .scroll(3)
                    );
                });
        List.of("loot_n_explore:vaults/frozen_depths/glacial_tomb/reward_ominous"
                )
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(2)
                            .add(W4).enchant()
                            .add(X4)
                            .add(R3)
                    );
                });
        // Entities
        List.of("loot_n_explore:entities/frozen_depths/general_frozen_magician",
                        "loot_n_explore:entities/frozen_depths/general_frozen_ranger",
                        "loot_n_explore:entities/frozen_depths/general_monarchs_guard",
                        "loot_n_explore:entities/frozen_depths/general_monarchs_soldier"
                        )
                .forEach(id -> {
                    items.put(id, new LootConfig.Pool()
                            .rolls(0.75)
                            .add(R1)
                            .add(A2, true)
                            .add(W2)
                            .add(W3)
                    );
                });
        items.put("loot_n_explore:entities/frozen_depths/frost_monarch",  new LootConfig.Pool()
                .rolls(2)
                .add(W3).enchant().weight(4)
                .add(A3).enchant().weight(4)
                .add(X3)
                .add(R3)
                .add(W5)
                .add(A5)
        );
        /// Minecraft Structures
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
                            .rolls(0.7)
                            .add(B3)
                    );
                });


        /// Modded Structures
        items.put("village_taverns:chests/tavern",  new LootConfig.Pool()
                .rolls(0.5)
                .add(B0)
                .add(B1)
        );
        items.put("aether:chests/dungeon/silver/silver_dungeon_reward", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("aether:chests/dungeon/gold/gold_dungeon_reward", new LootConfig.Pool()
                .rolls(0.5)
                .add(B3)
        );
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
                .rolls(0.35)
                .add(B3)
        );
        items.put("graveyard:chests/large_loot", new LootConfig.Pool()
                .rolls(0.5)
                .add(B1)
                .add(B2)
        );
        items.put("betterdungeons:zombie_dungeon/chests/special", new LootConfig.Pool()
                .rolls(0.3)
                .add(B0)
                .add(B1)
        );

        items.put("betterdungeons:zombie_dungeon/chests/tombstone", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("betterdeserttemples:chests/tomb_pharaoh", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("betterstrongholds:chests/grand_library", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("philipsruins:chest/badlands_dungeon_loot_high", new LootConfig.Pool()
                .rolls(0.5)
                .add(B1)
        );
        items.put("philipsruins:chest/end_ruins_loot", new LootConfig.Pool()
                .rolls(0.5)
                .add(B2)
        );
        items.put("betternether:chests/city_surprise", new LootConfig.Pool()
                .rolls(0.35)
                .add(B3)
        );
        items.put("betterend:chests/shadow_forest", new LootConfig.Pool()
                .rolls(0.35)
                .add(B2)
                .add(B3)
        );
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
                .rolls(0.35)
                .add(B3)
        );
    }
}


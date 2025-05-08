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

        //CHESTS
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
                ));
        List.of("loot_n_explore:chests/dungeons/glacial_tomb/common",
                "loot_n_explore:chests/glaze_tower",
                "loot_n_explore:chests/dungeons/glacial_tomb/spawner_room"
                )                .forEach(id -> {
            items.put(id, new LootConfig.Pool()
                    .rolls(0.75)
                    .add(W2)
                    .add(X2));
            scrolls.put(id, new LootConfig.Pool()
                    .rolls(0.2)
                    .scroll(2, 3));
        });

        //ENTITIES
        items.put("loot_n_explore:entities/frost_monarch",  new LootConfig.Pool()
                .rolls(2)
                .add(W3).enchant().weight(4)
                .add(A3).enchant().weight(4)
                .add(X3)
                .add(R3)
                .add(W5)
        );
    }
}


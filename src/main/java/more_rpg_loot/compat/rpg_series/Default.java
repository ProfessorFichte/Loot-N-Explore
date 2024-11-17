package more_rpg_loot.compat.rpg_series;

import net.spell_engine.api.loot.LootConfigV2;

import java.util.List;

public class Default {
    public final static LootConfigV2 lootConfig;

    static {
        var WG = "#rpg_series:golden_weapons";
        var W0 = "#rpg_series:tier_0_weapons";
        var W1 = "#rpg_series:tier_1_weapons";
        var W2 = "#rpg_series:tier_2_weapons";
        var W3 = "#rpg_series:tier_3_weapons";
        var W4 = "#rpg_series:tier_4_weapons";
        var W5 = "#rpg_series:tier_5_weapons";
        var A1 = "#rpg_series:tier_1_armors";
        var A2 = "#rpg_series:tier_2_armors";
        var A3 = "#rpg_series:tier_3_armors";
        var X0 = "#rpg_series:tier_0_accessories";
        var X1 = "#rpg_series:tier_1_accessories";
        var X2 = "#rpg_series:tier_2_accessories";
        var X3 = "#rpg_series:tier_3_accessories";
        var X4 = "#rpg_series:tier_4_accessories";

        lootConfig = new LootConfigV2();
        var injectors = lootConfig.injectors;

        List.of("loot_n_explore:chests/inns/desert",
                "loot_n_explore:chests/inns/plains",
                "loot_n_explore:chests/vilages/plains/small_inn",
                "loot_n_explore:chests/vilages/desert/small_inn",
                "loot_n_explore:chests/vilages/savanna/small_inn",
                "loot_n_explore:chests/vilages/snowy/small_inn",
                "loot_n_explore:chests/vilages/taiga/small_inn"
                ).forEach(id -> injectors.put(id, new LootConfigV2.Pool()
                        .rolls(0.5)
                        .add(W0)
                        .add(X0)
                        .add(A1)
                ));
        List.of("loot_n_explore:chests/dungeons/glacial_tomb/common",
                "loot_n_explore:chests/glaze_tower",
                "loot_n_explore:chests/dungeons/glacial_tomb/spawner_room"
                ).forEach(id -> injectors.put(id, new LootConfigV2.Pool()
                        .rolls(0.5)
                        .add(W1)
                        .add(X2)
                        .add(A1)
                ));
    }
}


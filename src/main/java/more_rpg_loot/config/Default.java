package more_rpg_loot.config;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Default {
    public static final StructurePoolConfig villages;

    static{
        villages = new StructurePoolConfig();

        var weight = 15;
        var limit = 1;
        villages.entries = new ArrayList<>(List.of(
                new StructurePoolConfig.Entry("minecraft:village/desert/houses", MOD_ID + ":village/desert/small_inn", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/plains/houses", MOD_ID + ":village/plains/small_inn", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/savanna/houses", MOD_ID + ":village/savanna/small_inn", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/snowy/houses", MOD_ID + ":village/snowy/small_inn", weight, limit),
                new StructurePoolConfig.Entry("minecraft:village/taiga/houses", MOD_ID + ":village/taiga/small_inn", weight, limit)
        ));
    }
}

package more_rpg_loot.config;

import net.fabric_extras.structure_pool.api.StructurePoolConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Default {
    public final static ItemConfig itemConfig;
    public static final StructurePoolConfig villages;

    static{
        itemConfig = new ItemConfig();
        villages = new StructurePoolConfig();

        var weight = 50;
        var limit = 1;
        villages.entries = new ArrayList<>(List.of(
                new StructurePoolConfig.Entry("minecraft:village/desert/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("loot_n_explore:village/desert/small_inn", weight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/savanna/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("loot_n_explore:village/savanna/small_inn", weight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/plains/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("loot_n_explore:village/plains/small_inn", weight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/taiga/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("loot_n_explore:village/taiga/small_inn", weight, limit))
                )),
                new StructurePoolConfig.Entry("minecraft:village/snowy/houses", new ArrayList<>(Arrays.asList(
                        new StructurePoolConfig.Entry.Structure("loot_n_explore:village/snowy/small_inn", weight, limit))
                ))
        ));
    }
}

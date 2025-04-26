package more_rpg_loot.compat.spell_engine;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.loot.LootHelper;
import net.tinyconfig.ConfigManager;

import java.util.HashMap;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class RPGSeriesLootInjectionLNE {

    public static ConfigManager<LootConfig> lootEquipmentConfig = new ConfigManager<>
            ("loot_equipment", Default.itemLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();

    public static void initialize() {
        lootEquipmentConfig.refresh();
        LootHelper.TAG_CACHE.refresh();
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            LootHelper.configureV2(registries,
                    key.getValue(),
                    tableBuilder,
                    lootEquipmentConfig.value,
                    new HashMap<>());
        });
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });
    }
}

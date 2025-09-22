package more_rpg_loot.compat.spell_engine;


import more_rpg_loot.item.Group;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.loot.LootHelper;
import net.tiny_config.ConfigManager;

import java.util.HashMap;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.compat.spell_engine.LNE_Weapons.*;

public class SpellEngine_LNE {

    public static ConfigManager<LootConfig> lootEquipmentConfig = new ConfigManager<>
            ("loot_equipment_v1", Default.itemLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();
    public static ConfigManager<LootConfig> lootScrollsConfig = new ConfigManager<>
            ("loot_scrolls", Default.scrollLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .constrain(LootConfig::constrainValues)
            .build();
    public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
            ("equipment_v1", new ConfigFile.Equipment())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();
    public static ConfigManager<LNE_RelicsConfig> relicsConfig = new ConfigManager<>
            ("relics", new LNE_RelicsConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void initialize() {
        lootEquipmentConfig.refresh();
        itemConfig.refresh();
        relicsConfig.refresh();
        lootScrollsConfig.refresh();
        LootInjection.modifyChestLootTables();
        Group.RPG_LOOT = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ender_dragon_sword.item().asItem()))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".loot.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.RPG_LOOT_KEY, Group.RPG_LOOT);
        SmithingTemplates.registerSmithingUpgrades();
        LNE_Relics.register(relicsConfig.value.entries);
        LNE_Weapons.register(itemConfig.value.weapons);
        itemConfig.save();
        relicsConfig.save();
        LootHelper.TAG_CACHE.refresh();
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            LootHelper.configureV2(registries, key.getValue(), tableBuilder, lootEquipmentConfig.value, new HashMap<>());
            LootHelper.configureV2(registries, key.getValue(), tableBuilder, lootScrollsConfig.value, new HashMap<>());
        });
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, serverResourceManager, success) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });

    }
}

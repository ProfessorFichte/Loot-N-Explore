package more_rpg_loot.compat.spell_engine;


import com.google.common.base.Suppliers;
import more_rpg_loot.item.Group;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.spell_engine.PlatformEvents;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.loot.LootConfig;
import net.spell_engine.rpg_series.loot.LootHelper;
import net.tiny_config.ConfigManager;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.compat.spell_engine.LNE_Weapons.*;

public class SpellEngine_LNE {

    public static ConfigManager<LootConfig> lootEquipmentConfig = new ConfigManager<>
            ("loot_equipment_v1", Default.itemLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            // 1.20.1 SpellEngine: `constrainValues` takes the defaults as a second argument.
            .constrain(config -> LootConfig.constrainValues(config, Default.itemLootConfig))
            .build();
    public static ConfigManager<LootConfig> lootScrollsConfig = new ConfigManager<>
            ("loot_scrolls", Default.scrollLootConfig)
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .constrain(config -> LootConfig.constrainValues(config, Default.scrollLootConfig))
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
        // 1.20.1: Fabric's loot API v3 and the dynamic-registry lookup are gone; Spell Engine's own
        // platform event carries the table id, its existing pools and a pool sink, and `LootHelper`
        // takes a report label instead of a scratch map.
        PlatformEvents.onLootTableModify(context -> {
            var existingPools = Suppliers.memoize(context::existingPools);
            LootHelper.configure(context.tableId(), existingPools, context::addPool, lootEquipmentConfig.value, "equipment");
            LootHelper.configure(context.tableId(), existingPools, context::addPool, lootScrollsConfig.value, "scrolls");
        });
        PlatformEvents.onServerStarted((server) -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });
        PlatformEvents.onDataPackReloadComplete(() -> {
            LootHelper.updateTagCache(lootEquipmentConfig.value);
        });

    }
}

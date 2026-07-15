package more_rpg_loot.item;

import more_rpg_loot.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModSpawnEggs {
    public record Entry(String name, Item item, String translation, ItemModelType modelType) {
        public Entry(String name, Item item, String translation) {
            this(name, item, translation, new ItemModelType.SpawnEgg());
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Item item, String translation) {
        var entry = new Entry(name, item, translation);
        all.add(entry);
        return entry;
    }

    public static final Entry FROSTHAUNT_SPAWN_EGG = entry("frost_haunt_egg",
            new SpawnEggItem(ModEntities.FROST_HAUNT, 12508647, 7907010, new Item.Settings()),
            "Frost Haunt Spawn Egg");
    public static final Entry FROSTMONARCH_SPAWN_EGG = entry("frost_monarch_egg",
            new SpawnEggItem(ModEntities.FROST_MONARCH, 12508647, 7907010, new Item.Settings()),
            "Frost Monarch Spawn Egg");
    public static final Entry GLAZE_SPAWN_EGG = entry("glaze_egg",
            new SpawnEggItem(ModEntities.GLAZE, 5954523, 13038065, new Item.Settings()),
            "Glaze Spawn Egg");
    public static final Entry FROST_HOUND_SPAWN_EGG = entry("frost_hound_egg",
            new SpawnEggItem(ModEntities.FROST_HOUND, 9424874, 15267583, new Item.Settings()),
            "Frost Hound Spawn Egg");
    public static final Entry UNDEAD_FROZEN_MAGE_SPAWN_EGG = entry("undead_frozen_mage_egg",
            new SpawnEggItem(ModEntities.UNDEAD_FROZEN_MAGE, 4877194, 12116722, new Item.Settings()),
            "Undead Frozen Mage Spawn Egg");
    public static final Entry GENERAL_UNDEAD_FROZEN_MAGE_SPAWN_EGG = entry("general_undead_frozen_mage_egg",
            new SpawnEggItem(ModEntities.GENERAL_UNDEAD_FROZEN_MAGE, 3033702, 13938487, new Item.Settings()),
            "General Undead Frozen Mage Spawn Egg");
    public static final Entry FROSTED_RANGER_SPAWN_EGG = entry("frosted_ranger_egg",
            new SpawnEggItem(ModEntities.FROSTED_RANGER, 6130344, 13232368, new Item.Settings()),
            "Frosted Ranger Spawn Egg");
    public static final Entry GENERAL_FROSTED_RANGER_SPAWN_EGG = entry("general_frosted_ranger_egg",
            new SpawnEggItem(ModEntities.GENERAL_FROSTED_RANGER, 3889776, 13938487, new Item.Settings()),
            "General Frosted Ranger Spawn Egg");
    public static final Entry MONARCHS_SOLDIER_SPAWN_EGG = entry("monarchs_soldier_egg",
            new SpawnEggItem(ModEntities.MONARCHS_SOLDIER, 8031129, 12116722, new Item.Settings()),
            "Monarchs Soldier Spawn Egg");
    public static final Entry GENERAL_MONARCHS_SOLDIER_SPAWN_EGG = entry("general_monarchs_soldier_egg",
            new SpawnEggItem(ModEntities.GENERAL_MONARCHS_SOLDIER, 4872806, 13938487, new Item.Settings()),
            "General Monarchs Soldier Spawn Egg");
    public static final Entry MONARCHS_GUARD_SPAWN_EGG = entry("monarchs_guard_egg",
            new SpawnEggItem(ModEntities.MONARCHS_GUARD, 7244960, 14742266, new Item.Settings()),
            "Monarchs Guard Spawn Egg");

    public static void register(){
        // Register all spawn eggs
        for (var entry : all) {
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, entry.name()), entry.item());
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((content) -> {
            for (var entry : all) {
                content.add(entry.item());
            }
        });
    }
}

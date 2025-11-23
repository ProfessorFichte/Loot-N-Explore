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

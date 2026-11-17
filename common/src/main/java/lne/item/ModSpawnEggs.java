package lne.item;

import lne.internal.platform.LNEEvents;

import lne.entity.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static lne.LneMod.MOD_ID;

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

    public static final Entry FROST_HAUNTED_SPAWN_EGG = entry("frost_haunted_egg",
            new SpawnEggItem(ModEntities.FROST_HAUNTED, 12508647, 7907010, new Item.Settings()),
            "Frost Haunted Spawn Egg");
    public static final Entry GLAZE_SPAWN_EGG = entry("glaze_egg",
            new SpawnEggItem(ModEntities.GLAZE, 5954523, 13038065, new Item.Settings()),
            "Glaze Spawn Egg");
    public static final Entry FROST_HOUND_SPAWN_EGG = entry("frost_hound_egg",
            new SpawnEggItem(ModEntities.FROST_HOUND, 9424874, 15267583, new Item.Settings()),
            "Frost Hound Spawn Egg");
    public static final Entry FROZEN_MAGICIAN_SPAWN_EGG = entry("frozen_magician_egg",
            new SpawnEggItem(ModEntities.FROZEN_MAGICIAN, 4877194, 12116722, new Item.Settings()),
            "Frozen Magician Spawn Egg");
    public static final Entry FROZEN_RANGER_SPAWN_EGG = entry("frozen_ranger_egg",
            new SpawnEggItem(ModEntities.FROZEN_RANGER, 6130344, 13232368, new Item.Settings()),
            "Frozen Ranger Spawn Egg");
    public static final Entry MONARCHS_SOLDIER_SPAWN_EGG = entry("monarchs_soldier_egg",
            new SpawnEggItem(ModEntities.MONARCHS_SOLDIER, 8031129, 12116722, new Item.Settings()),
            "Monarchs Soldier Spawn Egg");
    public static final Entry MONARCHS_GUARD_SPAWN_EGG = entry("monarchs_guard_egg",
            new SpawnEggItem(ModEntities.MONARCHS_GUARD, 7244960, 14742266, new Item.Settings()),
            "Monarchs Guard Spawn Egg");
    public static final Entry GENERAL_FROZEN_MAGICIAN_SPAWN_EGG = entry("general_frozen_magician_egg",
            new SpawnEggItem(ModEntities.GENERAL_FROZEN_MAGICIAN, 3033702, 13938487, new Item.Settings()),
            "General Ysmerith Spawn Egg");
    public static final Entry GENERAL_FROZEN_RANGER_SPAWN_EGG = entry("general_frozen_ranger_egg",
            new SpawnEggItem(ModEntities.GENERAL_FROZEN_RANGER, 3889776, 13938487, new Item.Settings()),
            "General Kaelthorn Spawn Egg");
    public static final Entry GENERAL_MONARCHS_SOLDIER_SPAWN_EGG = entry("general_monarchs_soldier_egg",
            new SpawnEggItem(ModEntities.GENERAL_MONARCHS_SOLDIER, 4872806, 13938487, new Item.Settings()),
            "General Haldrec Spawn Egg");
    public static final Entry GENERAL_MONARCHS_GUARD_SPAWN_EGG = entry("general_monarchs_guard_egg",
            new SpawnEggItem(ModEntities.GENERAL_MONARCHS_GUARD, 7244960, 13938487, new Item.Settings()),
            "General Dunmarth Spawn Egg");
    public static final Entry FROSTMONARCH_SPAWN_EGG = entry("frost_monarch_egg",
            new SpawnEggItem(ModEntities.FROST_MONARCH, 12508647, 7907010, new Item.Settings()),
            "Frost Monarch Spawn Egg");

    public static void register(){
        for (var entry : all) {
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, entry.name()), entry.item());
        }

        LNEEvents.get().modifyItemGroup(Group.RPG_FROZEN_DEPTHS_KEY, (content) -> {
            for (var entry : all) {
                content.add(entry.item());
            }
        });
    }
}

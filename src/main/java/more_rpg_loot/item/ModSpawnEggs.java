package more_rpg_loot.item;

import more_rpg_loot.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModSpawnEggs {
    public static final Item FROSTHAUNT_SPAWN_EGG = new SpawnEggItem(ModEntities.FROST_HAUNT, 12508647, 7907010, new Item.Settings());
    public static final Item FROSTMONARCH_SPAWN_EGG = new SpawnEggItem(ModEntities.FROST_MONARCH, 12508647, 7907010, new Item.Settings());
    public static final Item GLAZE_SPAWN_EGG = new SpawnEggItem(ModEntities.GLAZE, 5954523, 13038065, new Item.Settings());

    public static void register(){
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "frost_haunt_egg"), FROSTHAUNT_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "glaze_egg"), GLAZE_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "frost_monarch_egg"), FROSTMONARCH_SPAWN_EGG);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register((content) -> {
            content.add(FROSTHAUNT_SPAWN_EGG);
            content.add(GLAZE_SPAWN_EGG);
            content.add(FROSTMONARCH_SPAWN_EGG);
        });
    }
}

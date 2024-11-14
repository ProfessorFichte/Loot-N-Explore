package more_rpg_loot.item;

import more_rpg_loot.RPGLoot;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class Group {
    public static Identifier ID = new Identifier(RPGLoot.MOD_ID, "loot.generic");
    public static RegistryKey<ItemGroup> RPG_LOOT_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(RPGLoot.MOD_ID,"loot.generic"));
    public static ItemGroup RPG_LOOT;

    public static Identifier FOOD_ID = new Identifier(RPGLoot.MOD_ID, "food.generic");
    public static RegistryKey<ItemGroup> RPG_FOOD_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(RPGLoot.MOD_ID,"food.generic"));
    public static ItemGroup RPG_FOOD;

    public static Identifier BLOCK_ID = new Identifier(RPGLoot.MOD_ID, "blocks.generic");
    public static RegistryKey<ItemGroup> RPG_BLOCK_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(RPGLoot.MOD_ID,"blocks.generic"));
    public static ItemGroup RPG_BLOCKS;

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
        entries.add(Items.ELDER_GUARDIAN_UPGRADE);
        entries.add(Items.ENDER_DRAGON_UPGRADE);
        entries.add(Items.WITHER_UPGRADE);
    }

    public static void registerItemGroups() {
        RPGLoot.LOGGER.info("Registering Item Groups for " + RPGLoot.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(Group::addItemsToIngredientItemGroup);
    }
}

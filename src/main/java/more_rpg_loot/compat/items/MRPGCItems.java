package more_rpg_loot.compat.items;

import more_rpg_loot.compat.effects.MRPG_LIB_Effects;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class MRPGCItems {

    public static Item WATERMELON_DRINK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPG_LIB_Effects.WATERMELON_DRINK.registryEntry,1);
    public static Item BLUE_BERRY_PUNCH = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPG_LIB_Effects.BLUE_BERRY_PUNCH.registryEntry, 1);
    public static Item GREEN_CHILLI = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON),
            MRPG_LIB_Effects.GREEN_CHILLI.registryEntry,1);
    public static Item HONEY_MET = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPG_LIB_Effects.HONEY_MET.registryEntry,1);
    public static Item CACTUS_JUICE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPG_LIB_Effects.CACTUS_JUICE.registryEntry,1);

    public static void registerMRPGCItems() {
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"green_chilli"),GREEN_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"blue_berry_punch"),BLUE_BERRY_PUNCH);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"watermelon_drink"),WATERMELON_DRINK);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"honey_met"),HONEY_MET);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"cactus_juice"),CACTUS_JUICE);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.addAfter(CommonItems.MALT_EXTRACT,GREEN_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT,BLUE_BERRY_PUNCH);
            content.addAfter(CommonItems.MALT_EXTRACT,WATERMELON_DRINK);
            content.addAfter(CommonItems.MALT_EXTRACT,HONEY_MET);
            content.addAfter(CommonItems.MALT_EXTRACT,CACTUS_JUICE);
        });

    }
}

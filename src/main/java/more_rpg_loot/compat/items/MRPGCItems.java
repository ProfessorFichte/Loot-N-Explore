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

import static more_rpg_loot.RPGLoot.MOD_ID;

public class MRPGCItems {

    public static Item WATERMELON_DRINK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            MRPG_LIB_Effects.WATERMELON_DRINK.registryEntry,1);
    public static Item BLUE_BERRY_PUNCH = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            MRPG_LIB_Effects.BLUE_BERRY_PUNCH.registryEntry, 1);
    public static Item GREEN_CHILLI = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL),
            MRPG_LIB_Effects.GREEN_CHILLI.registryEntry,1);

    public static Item HONEY_MET = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            MRPG_LIB_Effects.HONEY_MET.registryEntry,2);

    public static void registerMRPGCItems() {
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"green_chilli"),GREEN_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"blue_berry_punch"),BLUE_BERRY_PUNCH);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"watermelon_drink"),WATERMELON_DRINK);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"honey_met"),HONEY_MET);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.addAfter(CommonItems.MALT_EXTRACT,GREEN_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT,BLUE_BERRY_PUNCH);
            content.addAfter(CommonItems.MALT_EXTRACT,WATERMELON_DRINK);
            content.addAfter(CommonItems.ESPRESSO,HONEY_MET);
        });

    }
}

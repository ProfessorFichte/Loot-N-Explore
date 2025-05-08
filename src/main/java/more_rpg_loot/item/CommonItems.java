package more_rpg_loot.item;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class CommonItems {
    public static Item HOT_CHOCOLATE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON),
            Effects.HOT_CHOCOLATE.registryEntry, 0);
    public static Item POTATO_SOUP = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.COMMON),
            Effects.POTATO_SOUP.registryEntry,0);
    public static Item SWEET_BERRY_PUNCH = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON),
            Effects.SWEET_BERRY_PUNCH.registryEntry, 0);

    public static Item BEET_ROOTBEER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            Effects.BEET_ROOTBEER.registryEntry, 1);
    public static Item MALT_EXTRACT = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            Effects.MALT_EXTRACT.registryEntry, 1);

    public static Item VITAL_DRINK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE),
            Effects.VITAL_DRINK.registryEntry, 2);
    public static Item ESPRESSO = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE),
            Effects.ESPRESSO.registryEntry, 2);

    public static Item FROSTBALL =  new FrostballItem(new Item.Settings().maxCount(16));
    public static Item GLAZE_ROD =  new Item(new Item.Settings());
    public static Item FROZEN_KEY =  new Item(new Item.Settings());
    public static Item MONARCHS_KEY =  new Item(new Item.Settings());


    public static void registerCommonItems(){
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"sweet_berry_punch"),SWEET_BERRY_PUNCH);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"hot_chocolate"),HOT_CHOCOLATE);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"potato_soup"),POTATO_SOUP);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"malt_extract"),MALT_EXTRACT);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"beet_rootbeer"),BEET_ROOTBEER);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"espresso"),ESPRESSO);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"vital_drink"),VITAL_DRINK);

        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"frostball"),FROSTBALL);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"glaze_rod"),GLAZE_ROD);

        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"frozen_key"),FROZEN_KEY);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"monarchs_key"),MONARCHS_KEY);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.add(SWEET_BERRY_PUNCH);
            content.add(HOT_CHOCOLATE);
            content.add(POTATO_SOUP);
            content.add(MALT_EXTRACT);
            content.add(BEET_ROOTBEER);
            content.add(VITAL_DRINK);
            content.add(ESPRESSO);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((content) -> {
            content.addAfter(Items.SNOWBALL,FROSTBALL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((content) -> {
            content.addAfter(Items.BLAZE_ROD,GLAZE_ROD);
            content.addAfter(Items.TRIAL_KEY,FROZEN_KEY);
            content.addAfter(Items.OMINOUS_TRIAL_KEY,MONARCHS_KEY);
        });
    }
}

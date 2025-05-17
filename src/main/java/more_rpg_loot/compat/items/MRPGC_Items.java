package more_rpg_loot.compat.items;

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

public class MRPGC_Items {
    /// T1 BUFF ITEMS
    public static Item WATERMELON_DRINK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPGC_Effects.WATERMELON_DRINK.registryEntry,1);
    public static Item BLUE_BERRY_PUNCH = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPGC_Effects.BLUE_BERRY_PUNCH.registryEntry, 1);
    public static Item GREEN_CHILLI = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON),
            MRPGC_Effects.GREEN_CHILLI.registryEntry,1);
    public static Item HONEY_MET = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPGC_Effects.HONEY_MET.registryEntry,1);
    public static Item CACTUS_JUICE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            MRPGC_Effects.CACTUS_JUICE.registryEntry,1);
    /// T3 BUFF ITEMS
    public static Item DETTLAFFS_BLOOD = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            MRPGC_Effects.DETTLAFFS_BLOOD.registryEntry,3);
    public static Item SCARLET_ESSENCE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            MRPGC_Effects.SCARLET_ESSENCE.registryEntry,3);
    public static Item SVABLODS_BREW = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            MRPGC_Effects.SVABLODS_BREW.registryEntry,3);

    public static void registerMRPGCItems() {
        /// T1 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"green_chilli"),GREEN_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"blue_berry_punch"),BLUE_BERRY_PUNCH);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"watermelon_drink"),WATERMELON_DRINK);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"honey_met"),HONEY_MET);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"cactus_juice"),CACTUS_JUICE);
        /// T3 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"dettlaffs_blood"),DETTLAFFS_BLOOD);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"scarlet_essence"),SCARLET_ESSENCE);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"svablods_brew"),SVABLODS_BREW);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            /// T1 BUFF ITEMS
            content.addAfter(CommonItems.MALT_EXTRACT,GREEN_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT,BLUE_BERRY_PUNCH);
            content.addAfter(CommonItems.MALT_EXTRACT,WATERMELON_DRINK);
            content.addAfter(CommonItems.MALT_EXTRACT,HONEY_MET);
            content.addAfter(CommonItems.MALT_EXTRACT,CACTUS_JUICE);
            /// T3 BUFF ITEMS
            content.addAfter(CommonItems.THE_UNSHAKABLE,DETTLAFFS_BLOOD);
            content.addAfter(CommonItems.THE_UNSHAKABLE,SCARLET_ESSENCE);
            content.addAfter(CommonItems.THE_UNSHAKABLE,SVABLODS_BREW);
        });

    }
}

package more_rpg_loot.compat.items;

import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class RWA_Items {
    /// T0 BUFF ITEMS
    public static Item APPLE_JUICE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON),
            RWA_Effects.APPLE_JUICE.registryEntry, 0);
    /// T1 BUFF ITEMS
    public static Item WALDMEISTER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            RWA_Effects.WALDMEISTER.registryEntry, 1);
    /// T2 BUFF ITEMS
    public static Item FORREST_SPIRIT = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE),
            RWA_Effects.FORREST_SPIRIT.registryEntry, 2);
    /// T3 BUFF ITEMS
    public static Item WOODSNAKE_POTION = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            RWA_Effects.WOODSNAKE_POTION.registryEntry, 3);

    public static void registerRangedWeaponAPIItems(){
        /// T0 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"apple_juice"),APPLE_JUICE);
        /// T1 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"waldmeister"),WALDMEISTER);
        /// T2 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"forrest_spirit"),FORREST_SPIRIT);
        /// T3 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"woodsnake_potion"),WOODSNAKE_POTION);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            /// T0 BUFF ITEMS
            content.addAfter(CommonItems.SWEET_BERRY_PUNCH,APPLE_JUICE);
            /// T1 BUFF ITEMS
            content.addAfter(CommonItems.MALT_EXTRACT,WALDMEISTER);
            /// T2 BUFF ITEMS
            content.addAfter(CommonItems.ESPRESSO,FORREST_SPIRIT);
            /// T3 BUFF ITEMS
            content.addAfter(CommonItems.THE_UNSHAKABLE,WOODSNAKE_POTION);
        });
    }
}

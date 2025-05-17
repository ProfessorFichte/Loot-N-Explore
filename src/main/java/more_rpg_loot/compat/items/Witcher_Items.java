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

public class Witcher_Items {
    /// T0 BUFF ITEMS
    public static Item BEAUCLAIR_WHITE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON),
            Witcher_Effects.BEAUCLAIR_WHITE.registryEntry, 0);
    /// T1 BUFF ITEMS
    public static Item RIVIAN_KRIEK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            Witcher_Effects.RIVIAN_KRIEK.registryEntry, 1);
    /// T2 BUFF ITEMS
    public static Item BUTCHER_OF_BLAVIKEN = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE),
            Witcher_Effects.BUTCHER_OF_BLAVIKEN.registryEntry, 2);
    /// T3 BUFF ITEMS
    public static Item WHITE_WOLF = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            Witcher_Effects.WHITE_WOLF.registryEntry, 3);

    public static void registerWitcherItems() {
        /// T0 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"beauclair_white"),BEAUCLAIR_WHITE);
        /// T1 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"rivian_kriek"),RIVIAN_KRIEK);
        /// T2 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"butcher_of_blaviken"),BUTCHER_OF_BLAVIKEN);
        /// T3 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"white_wolf"),WHITE_WOLF);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            /// T0 BUFF ITEMS
            content.addAfter(CommonItems.HOT_CHOCOLATE,BEAUCLAIR_WHITE);
            /// T1 BUFF ITEMS
            content.addAfter(CommonItems.MALT_EXTRACT,RIVIAN_KRIEK);
            /// T2 BUFF ITEMS
            content.addAfter(CommonItems.VITAL_DRINK,BUTCHER_OF_BLAVIKEN);
            /// T3 BUFF ITEMS
            content.addAfter(CommonItems.THE_UNSHAKABLE,WHITE_WOLF);
        });

    }

}

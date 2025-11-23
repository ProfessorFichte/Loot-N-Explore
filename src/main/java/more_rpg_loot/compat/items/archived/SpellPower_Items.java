package more_rpg_loot.compat.items.archived;

import more_rpg_loot.compat.items.SpellPower_Effects;
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

public class SpellPower_Items {
    // TO DO: LIGHTNING AND SOUL SPELL POWER BOOSTING ITEMS
    /// T0 BUFF ITEMS
    public static Item ORANGE_JUICE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON),
            SpellPower_Effects.ORANGE_JUICE.registryEntry, 0);
    /// T1 BUFF ITEMS
    public static Item SWEET_CHILLI= new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON),
            SpellPower_Effects.SWEET_CHILLI.registryEntry,1);
    public static Item FRUIT_ICEWATER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPower_Effects.FRUIT_ICEWATER.registryEntry, 1);
    public static Item CHORUS_EXTRACT = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPower_Effects.CHORUS_EXTRACT.registryEntry, 1);
    public static Item HOT_CHILLI = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON),
            SpellPower_Effects.HOT_CHILLI.registryEntry,1);
    public static Item HOLY_WATER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPower_Effects.HOLY_WATER.registryEntry, 1);
    public static Item ENCHANTED_ALE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPower_Effects.ENCHANTED_ALE.registryEntry,1);
    /// T2 BUFF ITEMS
    public static Item WIZARDS_ELIXIR = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE),
            SpellPower_Effects.WIZARDS_ELIXIR.registryEntry, 2);
    /// T3 BUFF ITEMS
    public static Item MERLINS_FLASK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            SpellPower_Effects.MERLINS_FLASK.registryEntry, 3);
    public static Item CRUSADERS_REST = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC),
            SpellPower_Effects.CRUSADERS_REST.registryEntry, 3);

    public static void registerSpellPowerItems() {
        /// T0 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"orange_juice"),ORANGE_JUICE);
        /// T1 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"fruit_icewater"),FRUIT_ICEWATER);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"chorus_extract"),CHORUS_EXTRACT);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"hot_chilli"),HOT_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"sweet_chilli"),SWEET_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"holy_water"),HOLY_WATER);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"enchanted_ale"),ENCHANTED_ALE);
        /// T2 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"wizards_elixir"),WIZARDS_ELIXIR);
        /// T3 BUFF ITEMS
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"merlins_flask"),MERLINS_FLASK);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"crusaders_rest"),CRUSADERS_REST);


        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            /// T0 BUFF ITEMS
            content.addAfter(CommonItems.SWEET_BERRY_PUNCH.item(),ORANGE_JUICE);
            /// T1 BUFF ITEMS
            content.addAfter(CommonItems.MALT_EXTRACT.item(),FRUIT_ICEWATER);
            content.addAfter(CommonItems.MALT_EXTRACT.item(),HOLY_WATER);
            content.addAfter(CommonItems.MALT_EXTRACT.item(),CHORUS_EXTRACT);
            content.addAfter(CommonItems.MALT_EXTRACT.item(),HOT_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT.item(),SWEET_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT.item(),ENCHANTED_ALE);
            /// T2 BUFF ITEMS
            content.addAfter(CommonItems.ESPRESSO.item(),WIZARDS_ELIXIR);
            /// T3 BUFF ITEMS
            content.addAfter(CommonItems.THE_UNSHAKABLE.item(),MERLINS_FLASK);
            content.addAfter(CommonItems.THE_UNSHAKABLE.item(),CRUSADERS_REST);
        });
    }
}

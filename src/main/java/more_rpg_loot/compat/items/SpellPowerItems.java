package more_rpg_loot.compat.items;

import more_rpg_loot.compat.effects.SpellPowerEffects;
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

public class SpellPowerItems {
    // TO DO: LIGHTNING AND SOUL SPELL POWER BOOSTING ITEMS
    //T0
    public static Item ORANGE_JUICE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON),
            SpellPowerEffects.ORANGE_JUICE.registryEntry, 0);
    //T1
    public static Item SWEET_CHILLI= new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON),
            SpellPowerEffects.SWEET_CHILLI.registryEntry,1);
    public static Item FRUIT_ICEWATER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPowerEffects.FRUIT_ICEWATER.registryEntry, 1);
    public static Item CHORUS_EXTRACT = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPowerEffects.CHORUS_EXTRACT.registryEntry, 1);
    public static Item HOT_CHILLI = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON),
            SpellPowerEffects.HOT_CHILLI.registryEntry,1);
    public static Item HOLY_WATER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPowerEffects.HOLY_WATER.registryEntry, 1);
    public static Item ENCHANTED_ALE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON),
            SpellPowerEffects.ENCHANTED_ALE.registryEntry,1);
    //T2
    public static Item WIZARDS_ELIXIR = new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE),
            SpellPowerEffects.WIZARDS_ELIXIR.registryEntry, 2);

    public static void registerSpellPowerItems() {
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"orange_juice"),ORANGE_JUICE);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"fruit_icewater"),FRUIT_ICEWATER);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"chorus_extract"),CHORUS_EXTRACT);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"hot_chilli"),HOT_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"sweet_chilli"),SWEET_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"holy_water"),HOLY_WATER);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"enchanted_ale"),ENCHANTED_ALE);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"wizards_elixir"),WIZARDS_ELIXIR);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.addAfter(CommonItems.SWEET_BERRY_PUNCH,ORANGE_JUICE);
            content.addAfter(CommonItems.MALT_EXTRACT,FRUIT_ICEWATER);
            content.addAfter(CommonItems.MALT_EXTRACT,HOLY_WATER);
            content.addAfter(CommonItems.MALT_EXTRACT,CHORUS_EXTRACT);
            content.addAfter(CommonItems.MALT_EXTRACT,HOT_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT,SWEET_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT,ENCHANTED_ALE);
            content.addAfter(CommonItems.ESPRESSO,WIZARDS_ELIXIR);
        });
    }
}

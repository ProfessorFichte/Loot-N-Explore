package more_rpg_loot.compat.items;

import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_power.api.SpellPowerMechanics;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class MRPGCItems {

    public static Item WATERMELON_DRINK = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            (RegistryEntry<StatusEffect>) MoreSpellSchools.WATER.ownedBoostEffect, (RegistryEntry<StatusEffect>) SpellPowerMechanics.HASTE.boostEffect,null,1);
    public static Item BLUE_BERRY_PUNCH = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            (RegistryEntry<StatusEffect>) MoreSpellSchools.AIR.ownedBoostEffect, (RegistryEntry<StatusEffect>) SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,null,1);
    public static Item GREEN_CHILLI = new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL),
            (RegistryEntry<StatusEffect>) MoreSpellSchools.EARTH.ownedBoostEffect, (RegistryEntry<StatusEffect>) SpellPowerMechanics.CRITICAL_CHANCE.boostEffect,null,1);

    public static void registerMRPGCItems() {
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"green_chilli"),GREEN_CHILLI);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"blue_berry_punch"),BLUE_BERRY_PUNCH);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"watermelon_drink"),WATERMELON_DRINK);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.addAfter(CommonItems.MALT_EXTRACT,GREEN_CHILLI);
            content.addAfter(CommonItems.MALT_EXTRACT,BLUE_BERRY_PUNCH);
            content.addAfter(CommonItems.MALT_EXTRACT,WATERMELON_DRINK);
        });

    }
}

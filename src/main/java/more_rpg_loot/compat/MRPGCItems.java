package more_rpg_loot.compat;

import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_power.api.SpellPowerMechanics;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class MRPGCItems {

    public static Item WATERMELON_DRINK = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            MoreSpellSchools.WATER.boostEffect, SpellPowerMechanics.HASTE.boostEffect,null,1);
    public static Item BLUE_BERRY_PUNCH = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            MoreSpellSchools.AIR.boostEffect, SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,null,1);
    public static Item GREEN_CHILLI = new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            MoreSpellSchools.EARTH.boostEffect,SpellPowerMechanics.CRITICAL_CHANCE.boostEffect,null,1);

    public static void registerMRPGCItems() {
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"green_chilli"),GREEN_CHILLI);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"blue_berry_punch"),BLUE_BERRY_PUNCH);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"watermelon_drink"),WATERMELON_DRINK);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.add(GREEN_CHILLI);
            content.add(BLUE_BERRY_PUNCH);
            content.add(WATERMELON_DRINK);
        });

    }
}

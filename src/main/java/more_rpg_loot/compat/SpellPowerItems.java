package more_rpg_loot.compat;

import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class SpellPowerItems {
    //T1
    public static Item ORANGE_JUICE = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            SpellPowerMechanics.HASTE.boostEffect, null,null,0);
    //T2
    public static Item SWEET_CHILLI= new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            StatusEffects.STRENGTH, SpellSchools.HEALING.boostEffect,null,1);
    public static Item FRUIT_ICEWATER = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            SpellSchools.FROST.boostEffect,SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,null,1);
    public static Item CHORUS_EXTRACT = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            SpellSchools.ARCANE.boostEffect,SpellPowerMechanics.HASTE.boostEffect,null,1);
    public static Item HOT_CHILLI = new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            SpellSchools.FIRE.boostEffect,SpellPowerMechanics.CRITICAL_CHANCE.boostEffect,null,1);
    public static Item HOLY_WATER = new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            SpellSchools.HEALING.boostEffect,SpellPowerMechanics.HASTE.boostEffect,null,1);
    public static Item ENCHANTED_ALE = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            SpellPowerMechanics.CRITICAL_CHANCE.boostEffect,SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,null,1);
    //T3
    public static Item WIZARDS_ELIXIR = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            SpellPowerMechanics.CRITICAL_CHANCE.boostEffect, SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,
            SpellPowerMechanics.HASTE.boostEffect,2);

    public static void registerSpellPowerItems() {
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"orange_juice"),ORANGE_JUICE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"fruit_icewater"),FRUIT_ICEWATER);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"chorus_extract"),CHORUS_EXTRACT);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"hot_chilli"),HOT_CHILLI);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"sweet_chilli"),SWEET_CHILLI);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"holy_water"),HOLY_WATER);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wizards_elixir"),WIZARDS_ELIXIR);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"enchanted_ale"),ENCHANTED_ALE);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.add(ORANGE_JUICE);
            content.add(FRUIT_ICEWATER);
            content.add(HOLY_WATER);
            content.add(CHORUS_EXTRACT);
            content.add(HOT_CHILLI);
            content.add(SWEET_CHILLI);
            content.add(ENCHANTED_ALE);
            content.add(WIZARDS_ELIXIR);
        });
    }
}

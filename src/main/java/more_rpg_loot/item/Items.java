package more_rpg_loot.item;

import more_rpg_loot.compat.MRPGCItems;
import more_rpg_loot.compat.SpellPowerItems;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.items.FrostballItem;
import net.fabric_extras.ranged_weapon.api.StatusEffects_RangedWeapon;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Items {
    //UPGRADES
    public static final List<Identifier> BASE_ITEMS = Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier("item/empty_slot_axe"));
                identifiers.add(new Identifier("item/empty_slot_sword"));
                identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_bow"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_DRAGON= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier("item/empty_slot_amethyst_shard"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_WITHER= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier("textures/item/empty_slot_amethyst_shard"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_GUARDIAN= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier(MOD_ID,"textures/item/empty_slot/empty_slot_prismarine_shard"));
            });

    public static Item ENDER_DRAGON_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_DRAGON
    );
    public static Item ELDER_GUARDIAN_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.additions_slot_description"),
            BASE_ITEMS,
            List.of(new Identifier(MOD_ID,"item/template/empty_slot_prismarine_shard"))
    );
    public static Item WITHER_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.wither.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.wither.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.wither.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.wither.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.wither.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITHER
    );


    //DRINKS
    /*public static Item ORANGE_JUICE = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            SpellPowerMechanics.HASTE.boostEffect, null,null,0);*/
    public static Item HOT_CHOCOLATE = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects.HASTE,null,null,0);
    public static Item APPLE_JUICE = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects_RangedWeapon.DAMAGE.effect,null,null,0);
    public static Item POTATO_SOUP = new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            StatusEffects.STRENGTH,null,null,0);
    public static Item SWEET_BERRY_PUNCH = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects.RESISTANCE,null,null,0);

/*
    public static Item WATERMELON_DRINK = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            MoreSpellSchools.WATER.boostEffect, SpellPowerMechanics.HASTE.boostEffect,null,1);
    public static Item BLUE_BERRY_PUNCH = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            MoreSpellSchools.AIR.boostEffect, SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,null,1);
    public static Item GREEN_CHILLI = new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            MoreSpellSchools.EARTH.boostEffect,SpellPowerMechanics.CRITICAL_CHANCE.boostEffect,null,1);
*/

    public static Item BEET_ROOTBEER = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects.STRENGTH,StatusEffects.RESISTANCE,null,1);
    public static Item MALT_EXTRACT = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects.STRENGTH,StatusEffects.HASTE,null,1);
    public static Item WALDMEISTER = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects_RangedWeapon.DAMAGE.effect,StatusEffects_RangedWeapon.HASTE.effect,null,1);

    /*
    public static Item SWEET_CHILLI= new InnkeeperBowlItem(new FabricItemSettings().maxCount(16),
            StatusEffects.STRENGTH,SpellSchools.HEALING.boostEffect,null,1);
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
*/
    /*
           public static Item WIZARDS_ELIXIR = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
           SpellPowerMechanics.CRITICAL_CHANCE.boostEffect, SpellPowerMechanics.CRITICAL_DAMAGE.boostEffect,
           SpellPowerMechanics.HASTE.boostEffect,2);
    */

    public static Item VITAL_DRINK = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects.HEALTH_BOOST,StatusEffects.RESISTANCE,StatusEffects.STRENGTH,2);
    public static Item ESPRESSO = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects.HASTE,StatusEffects.SPEED,StatusEffects.STRENGTH,2);
    public static Item FORREST_SPIRIT = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            StatusEffects_RangedWeapon.DAMAGE.effect,StatusEffects_RangedWeapon.HASTE.effect,
            StatusEffects.SPEED,2);


    public static Item FROSTBALL =  new FrostballItem(new FabricItemSettings().maxCount(16));
    public static Item GLAZE_ROD =  new Item(new FabricItemSettings());

    public static void registerModItems() {
        if(FabricLoader.getInstance().isModLoaded("spell_power")){
            SpellPowerItems.registerSpellPowerItems();
        }
        if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            MRPGCItems.registerMRPGCItems();
        }

        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"dragon_upgrade_smithing_template"),ENDER_DRAGON_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"guardian_upgrade_smithing_template"),ELDER_GUARDIAN_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wither_upgrade_smithing_template"),WITHER_UPGRADE);

        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"sweet_berry_punch"),SWEET_BERRY_PUNCH);
        //Registry.register(Registries.ITEM,new Identifier(MOD_ID,"orange_juice"),ORANGE_JUICE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"hot_chocolate"),HOT_CHOCOLATE);


        /*
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"fruit_icewater"),FRUIT_ICEWATER);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"chorus_extract"),CHORUS_EXTRACT);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"hot_chilli"),HOT_CHILLI);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"sweet_chilli"),SWEET_CHILLI);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"holy_water"),HOLY_WATER);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"green_chilli"),GREEN_CHILLI);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"blue_berry_punch"),BLUE_BERRY_PUNCH);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"watermelon_drink"),WATERMELON_DRINK);*/
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"apple_juice"),APPLE_JUICE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"potato_soup"),POTATO_SOUP);


        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"waldmeister"),WALDMEISTER);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"malt_extract"),MALT_EXTRACT);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"beet_rootbeer"),BEET_ROOTBEER);
        //Registry.register(Registries.ITEM,new Identifier(MOD_ID,"enchanted_ale"),ENCHANTED_ALE);

        //Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wizards_elixir"),WIZARDS_ELIXIR);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"espresso"),ESPRESSO);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"vital_drink"),VITAL_DRINK);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"forrest_spirit"),FORREST_SPIRIT);

        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"frostball"),FROSTBALL);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"glaze_rod"),GLAZE_ROD);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.add(SWEET_BERRY_PUNCH);
       //     content.add(ORANGE_JUICE);
            content.add(HOT_CHOCOLATE);
            content.add(APPLE_JUICE);
            content.add(POTATO_SOUP);

            /*content.add(WATERMELON_DRINK);
            content.add(BLUE_BERRY_PUNCH);
            content.add(FRUIT_ICEWATER);
            content.add(HOLY_WATER);
            content.add(CHORUS_EXTRACT);
            content.add(HOT_CHILLI);
            content.add(SWEET_CHILLI);
            content.add(GREEN_CHILLI);*/
            content.add(WALDMEISTER);
            content.add(MALT_EXTRACT);
            content.add(BEET_ROOTBEER);
       //     content.add(ENCHANTED_ALE);

            content.add(VITAL_DRINK);
            content.add(FORREST_SPIRIT);
            //content.add(WIZARDS_ELIXIR);
            content.add(ESPRESSO);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((content) -> {
            content.add(FROSTBALL);
        });
    }

}

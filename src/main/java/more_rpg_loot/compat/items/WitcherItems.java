package more_rpg_loot.compat.items;

import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.witcher_rpg.effect.Effects;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class WitcherItems {
    public static Item BEAUCLAIR_WHITE = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            Effects.SIGN_INTENSITY, null,null,0);
    public static Item RIVIAN_KRIEK = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            Effects.SIGN_INTENSITY, Effects.ADRENALINE_BURST,null,1);
    public static Item BUTCHER_OF_BLAVIKEN = new InnkeeperDrinkItem(new FabricItemSettings().maxCount(16),
            Effects.SIGN_INTENSITY, Effects.ADRENALINE_BURST, StatusEffects.HASTE,2);

    public static void registerWitcherItems() {
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"beauclair_white"),BEAUCLAIR_WHITE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"rivian_kriek"),RIVIAN_KRIEK);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"butcher_of_blaviken"),BUTCHER_OF_BLAVIKEN);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.addAfter(CommonItems.HOT_CHOCOLATE,BEAUCLAIR_WHITE);
            content.addAfter(CommonItems.MALT_EXTRACT,RIVIAN_KRIEK);
            content.addAfter(CommonItems.VITAL_DRINK,BUTCHER_OF_BLAVIKEN);
        });

    }
}

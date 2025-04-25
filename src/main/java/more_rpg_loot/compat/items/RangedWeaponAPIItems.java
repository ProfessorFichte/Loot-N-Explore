package more_rpg_loot.compat.items;

import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import net.fabric_extras.ranged_weapon.api.StatusEffects_RangedWeapon;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class RangedWeaponAPIItems {

    public static Item APPLE_JUICE = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            (RegistryEntry<StatusEffect>) StatusEffects_RangedWeapon.DAMAGE.effect,null,null,0);
    public static Item WALDMEISTER = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            (RegistryEntry<StatusEffect>) StatusEffects_RangedWeapon.DAMAGE.effect, (RegistryEntry<StatusEffect>) StatusEffects_RangedWeapon.HASTE.effect,null,1);
    public static Item FORREST_SPIRIT = new InnkeeperDrinkItem(new Item.Settings().maxCount(16),
            (RegistryEntry<StatusEffect>) StatusEffects_RangedWeapon.DAMAGE.effect, (RegistryEntry<StatusEffect>) StatusEffects_RangedWeapon.HASTE.effect,
            StatusEffects.SPEED,2);

    public static void registerRangedWeaponAPIItems(){
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"apple_juice"),APPLE_JUICE);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"waldmeister"),WALDMEISTER);
        Registry.register(Registries.ITEM,Identifier.of(MOD_ID,"forrest_spirit"),FORREST_SPIRIT);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            content.addAfter(CommonItems.SWEET_BERRY_PUNCH,APPLE_JUICE);
            content.addAfter(CommonItems.MALT_EXTRACT,WALDMEISTER);
            content.addAfter(CommonItems.ESPRESSO,FORREST_SPIRIT);
        });
    }
}

package more_rpg_loot.item;

import more_rpg_loot.item.weapons.DragonMeeleeWeapon;
import more_rpg_loot.item.weapons.ElderGuardianMeleeWeapon;
import more_rpg_loot.item.weapons.WitherMeleeWeapon;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class WeaponRegister {
    private static final float swordVanillaAttackSpeed = -2.4F;
    private static final float axeVanillaAttackSpeed = -3.0F;
    public static Item ELDER_GUARDIAN_SWORD = new ElderGuardianMeleeWeapon(ToolMaterials.NETHERITE,4,swordVanillaAttackSpeed, new FabricItemSettings().rarity(Rarity.EPIC));
    public static Item ELDER_GUARDIAN_AXE = new ElderGuardianMeleeWeapon(ToolMaterials.NETHERITE,5,axeVanillaAttackSpeed, new FabricItemSettings().rarity(Rarity.EPIC));
    public static Item ENDER_DRAGON_SWORD = new DragonMeeleeWeapon(ToolMaterials.NETHERITE,4,swordVanillaAttackSpeed, new FabricItemSettings().rarity(Rarity.EPIC));
    public static Item ENDER_DRAGON_AXE = new DragonMeeleeWeapon(ToolMaterials.NETHERITE,5,axeVanillaAttackSpeed, new FabricItemSettings().rarity(Rarity.EPIC));
    public static Item WITHER_SWORD = new WitherMeleeWeapon(ToolMaterials.NETHERITE,4,swordVanillaAttackSpeed, new FabricItemSettings().rarity(Rarity.EPIC));
    public static Item WITHER_AXE = new WitherMeleeWeapon(ToolMaterials.NETHERITE,5,axeVanillaAttackSpeed, new FabricItemSettings().rarity(Rarity.EPIC));

    public static void registerWeapons(){
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"elder_guardian_sword"),ELDER_GUARDIAN_SWORD);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"elder_guardian_axe"),ELDER_GUARDIAN_AXE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"ender_dragon_sword"),ENDER_DRAGON_SWORD);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"ender_dragon_axe"),ENDER_DRAGON_AXE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wither_sword"),WITHER_SWORD);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wither_axe"),WITHER_AXE);

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register((content) -> {
            content.add(ELDER_GUARDIAN_SWORD);
            content.add(ELDER_GUARDIAN_AXE);
            content.add(ENDER_DRAGON_SWORD);
            content.add(ENDER_DRAGON_AXE);
            content.add(WITHER_SWORD);
            content.add(WITHER_AXE);
        });
    }
}

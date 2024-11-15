package more_rpg_loot.item;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.blocks.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Group {
    public static Identifier ID = new Identifier(MOD_ID, "loot.generic");
    public static RegistryKey<ItemGroup> RPG_LOOT_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(MOD_ID,"loot.generic"));
    public static ItemGroup RPG_LOOT;

    public static Identifier FOOD_ID = new Identifier(MOD_ID, "food.generic");
    public static RegistryKey<ItemGroup> RPG_FOOD_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(MOD_ID,"food.generic"));
    public static ItemGroup RPG_FOOD;

    public static Identifier BLOCK_ID = new Identifier(MOD_ID, "blocks.generic");
    public static RegistryKey<ItemGroup> RPG_BLOCK_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(MOD_ID,"blocks.generic"));
    public static ItemGroup RPG_BLOCKS;

    private static void registerLootItemGroup() {
        Group.RPG_LOOT = FabricItemGroup.builder()
                .icon(() -> new ItemStack(WeaponRegister.ELDER_GUARDIAN_SWORD))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".loot.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.RPG_LOOT_KEY, Group.RPG_LOOT);
    }
    private static void registerFoodItemGroup() {
        Group.RPG_FOOD = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModBlocks.INNKEEPER_SHELF.block()))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".food.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.RPG_FOOD_KEY, Group.RPG_FOOD);
    }
    private static void registerBlockItemGroup() {
        Group.RPG_BLOCKS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModBlocks.BLUE_ICE_BRICKS.block()))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".blocks.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.RPG_BLOCK_KEY, Group.RPG_BLOCKS);
    }

    public static void registerItemGroups() {
        registerLootItemGroup();
        registerFoodItemGroup();
        registerBlockItemGroup();
        RPGLoot.LOGGER.info("Registering Item Groups for " + MOD_ID);;
    }
}

package more_rpg_loot.item;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.blocks.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Group {
    public static Identifier ID = Identifier.of(MOD_ID, "loot.generic");
    public static RegistryKey<ItemGroup> RPG_LOOT_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),Identifier.of(MOD_ID,"loot.generic"));
    public static String lootTranslationKey = "itemGroup." + ID.getNamespace() + "." + ID.getPath();
    public static ItemGroup RPG_LOOT;

    public static Identifier FOOD_ID = Identifier.of(MOD_ID, "food.generic");
    public static RegistryKey<ItemGroup> RPG_FOOD_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),Identifier.of(MOD_ID,"food.generic"));
    public static String foodTranslationKey = "itemGroup." + FOOD_ID.getNamespace() + "." + FOOD_ID.getPath();
    public static ItemGroup RPG_FOOD;

    public static Identifier BLOCK_ID = Identifier.of(MOD_ID, "blocks.generic");
    public static RegistryKey<ItemGroup> RPG_BLOCK_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(),Identifier.of(MOD_ID,"blocks.generic"));
    public static String blocksTranslationKey = "itemGroup." + BLOCK_ID.getNamespace() + "." + BLOCK_ID.getPath();
    public static ItemGroup RPG_BLOCKS;

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
        registerFoodItemGroup();
        registerBlockItemGroup();
        RPGLoot.LOGGER.info("Registering Item Groups for " + MOD_ID);;
    }
}

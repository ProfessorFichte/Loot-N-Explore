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

import java.util.function.Supplier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Group {
    public static Identifier ID = Identifier.of(MOD_ID, "loot.general");
    public static RegistryKey<ItemGroup> RPG_LOOT_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "loot.general"));
    public static String lootTranslationKey = "itemGroup." + ID.getNamespace() + "." + ID.getPath();
    public static ItemGroup RPG_LOOT;

    public static Identifier FOOD_ID = Identifier.of(MOD_ID, "food.general");
    public static RegistryKey<ItemGroup> RPG_FOOD_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "food.general"));
    public static String foodTranslationKey = "itemGroup." + MOD_ID + "." + FOOD_ID.getPath();
    public static ItemGroup RPG_FOOD;

    public static Identifier BLOCK_ID = Identifier.of(MOD_ID, "blocks.general");
    public static RegistryKey<ItemGroup> RPG_BLOCK_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "blocks.general"));
    public static String blocksTranslationKey = "itemGroup." + MOD_ID + "." + BLOCK_ID.getPath();
    public static ItemGroup RPG_BLOCKS;

    public static void registerLootItemGroup(Supplier<net.minecraft.item.Item> iconItem) {
        RPG_LOOT = FabricItemGroup.builder()
                .icon(() -> new ItemStack(iconItem.get()))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".loot.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, RPG_LOOT_KEY, RPG_LOOT);
    }

    private static void registerFoodItemGroup() {
        RPG_FOOD = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModBlocks.INNKEEPER_SHELF.block()))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".food.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, RPG_FOOD_KEY, RPG_FOOD);
    }

    private static void registerBlockItemGroup() {
        RPG_BLOCKS = FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModBlocks.BLUE_ICE_BRICKS.block()))
                .displayName(Text.translatable("itemGroup." + MOD_ID + ".blocks.general"))
                .build();
        Registry.register(Registries.ITEM_GROUP, RPG_BLOCK_KEY, RPG_BLOCKS);
    }

    public static void registerItemGroups() {
        registerFoodItemGroup();
        registerBlockItemGroup();
        RPGLoot.LOGGER.info("Registering Item Groups for " + MOD_ID);
    }
}

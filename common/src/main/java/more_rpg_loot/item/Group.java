package more_rpg_loot.item;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.platform.LNEEvents;
import net.minecraft.item.Item;
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
    public static final RegistryKey<ItemGroup> RPG_LOOT_KEY = key("loot.general");
    public static final String lootTranslationKey = translationKey("loot.general");
    public static ItemGroup RPG_LOOT;

    public static final RegistryKey<ItemGroup> RPG_FOOD_KEY = key("food.general");
    public static final String foodTranslationKey = translationKey("food.general");
    public static ItemGroup RPG_FOOD;

    public static final RegistryKey<ItemGroup> RPG_GENERIC_KEY = key("generic");
    public static final String genericTranslationKey = translationKey("generic");
    public static ItemGroup RPG_GENERIC;

    public static final RegistryKey<ItemGroup> RPG_FROZEN_DEPTHS_KEY = key("frozen_depths");
    public static final String frozenDepthsTranslationKey = translationKey("frozen_depths");
    public static ItemGroup RPG_FROZEN_DEPTHS;

    private static RegistryKey<ItemGroup> key(String path) {
        return RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, path));
    }

    private static String translationKey(String path) {
        return "itemGroup." + MOD_ID + "." + path;
    }

    public static void registerLootItemGroup(Supplier<Item> iconItem) {
        RPG_LOOT = create(RPG_LOOT_KEY, lootTranslationKey, () -> new ItemStack(iconItem.get()));
    }

    public static void registerItemGroups() {
        RPG_FOOD = create(RPG_FOOD_KEY, foodTranslationKey, () -> new ItemStack(CommonItems.MALT_EXTRACT.item()));
        RPG_GENERIC = create(RPG_GENERIC_KEY, genericTranslationKey, () -> new ItemStack(ModBlocks.INNKEEPER_SHELF.block()));
        RPG_FROZEN_DEPTHS = create(RPG_FROZEN_DEPTHS_KEY, frozenDepthsTranslationKey, () -> new ItemStack(CommonItems.MONARCHS_KEY.item()));
        RPGLoot.LOGGER.info("Registering Item Groups for " + MOD_ID);
    }

    private static ItemGroup create(RegistryKey<ItemGroup> groupKey, String translationKey, Supplier<ItemStack> icon) {
        ItemGroup group = LNEEvents.get().createItemGroup(icon, Text.translatable(translationKey));
        Registry.register(Registries.ITEM_GROUP, groupKey, group);
        return group;
    }
}

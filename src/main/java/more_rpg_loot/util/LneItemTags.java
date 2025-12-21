package more_rpg_loot.util;

import more_rpg_loot.RPGLoot;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class LneItemTags {
    public static final TagKey<Item> INNKEEPER_BUFFS_0 = register("innkeeper_items/tier_0_buffs");
    public static final TagKey<Item> INNKEEPER_BUFFS_1 = register("innkeeper_items/tier_1_buffs");
    public static final TagKey<Item> INNKEEPER_BUFFS_2 = register("innkeeper_items/tier_2_buffs");
    public static final TagKey<Item> INNKEEPER_BUFFS_3 = register("innkeeper_items/tier_3_buffs");

    public static final TagKey<Item> RELICS = register("relics");

    public static final TagKey<Item> WEAPON_THEME_ENDER_DRAGON = register("weapon_themes/ender_dragon");
    public static final TagKey<Item> WEAPON_THEME_WITHER = register("weapon_themes/wither");
    public static final TagKey<Item> WEAPON_THEME_GLACIAL = register("weapon_themes/glacial");
    public static final TagKey<Item> WEAPON_THEME_ELDER_GUARDIAN = register("weapon_themes/elder_guardian");

    private static TagKey<Item> register(String id) {
        return TagKey.of(RegistryKeys.ITEM, RPGLoot.id(id));
    }
}

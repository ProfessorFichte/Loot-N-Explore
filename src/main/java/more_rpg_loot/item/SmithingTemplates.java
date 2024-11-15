package more_rpg_loot.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class SmithingTemplates {
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
    public static void registerSmithingUpgrades(){
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"dragon_upgrade_smithing_template"),ENDER_DRAGON_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"guardian_upgrade_smithing_template"),ELDER_GUARDIAN_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wither_upgrade_smithing_template"),WITHER_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((content) -> {
            content.add(SmithingTemplates.ELDER_GUARDIAN_UPGRADE);
            content.add(SmithingTemplates.ENDER_DRAGON_UPGRADE);
            content.add(SmithingTemplates.WITHER_UPGRADE);
        });
    }
}

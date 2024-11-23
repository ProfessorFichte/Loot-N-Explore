package more_rpg_loot.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
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
                if(!FabricLoader.getInstance().isModLoaded("archers")){
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_bow"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_crossbow"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_spear"));
                }
                if(!FabricLoader.getInstance().isModLoaded("paladins")){
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_hammer"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_holy"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_mace"));
                }
                if(!FabricLoader.getInstance().isModLoaded("rogues")){
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_dagger"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_sickle"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_glaive"));
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_double_axe"));
                }
                if(!FabricLoader.getInstance().isModLoaded("wizards")){
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_wand"));
                }
                if(!FabricLoader.getInstance().isModLoaded("forcemaster")){
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_knuckle"));
                }
                if(!FabricLoader.getInstance().isModLoaded("berserker_axe")){
                    identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_berserker_axe"));
                }

            });
    public static final List<Identifier> INGREDIENT_ITEMS_DRAGON= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_ender_dragon_scales"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_WITHER= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_nether_star"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_GUARDIAN = Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_elder_guardian_eye"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_FROSTMONARCH= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(new Identifier(MOD_ID,"item/template/empty_slot_elder_frozen_soul"));
            });

    public static Item ENDER_DRAGON_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.ender_dragon.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_DRAGON
    );
    public static Item ELDER_GUARDIAN_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.elder_guardian.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_GUARDIAN
    );
    public static Item WITHER_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.wither.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.wither.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.wither.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_WITHER
    );
    public static Item FROSTMONARCH_UPGRADE = new SmithingTemplateItem(
            Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.frostmonarch.ingredients").formatted(Formatting.BLUE),
            Text.translatable("smithing_template.loot_n_explore.frostmonarch.title").formatted(Formatting.GRAY),
            Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
            Text.translatable("smithing_template.loot_n_explore.frostmonarch.additions_slot_description"),
            BASE_ITEMS,
            INGREDIENT_ITEMS_FROSTMONARCH
    );

    public static void registerSmithingUpgrades(){
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"dragon_upgrade_smithing_template"),ENDER_DRAGON_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"guardian_upgrade_smithing_template"),ELDER_GUARDIAN_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"wither_upgrade_smithing_template"),WITHER_UPGRADE);
        Registry.register(Registries.ITEM,new Identifier(MOD_ID,"frostmonarch_upgrade_smithing_template"),FROSTMONARCH_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((content) -> {
            content.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,SmithingTemplates.ELDER_GUARDIAN_UPGRADE);
            content.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,ENDER_DRAGON_UPGRADE);
            content.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,WITHER_UPGRADE);
            content.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,FROSTMONARCH_UPGRADE);
        });
    }
}

package more_rpg_loot.compat.spell_engine;

import more_rpg_loot.item.Group;
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
import java.util.function.Function;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class SmithingTemplates {

    public static class Container { Item item; }

    // Entry record for smithing templates with datagen support
    // Stores all translation strings needed for datagen
    public record Entry(
            Identifier id,                      // Item registry ID
            String templateKey,                 // Key for translation (e.g., "ender_dragon")
            String appliesTo,                   // Translation: what items this applies to
            String ingredientText,              // Translation: what ingredient is required
            String titleText,                   // Translation: template title
            String baseSlotDescription,         // Translation: base slot description
            String additionsSlotDescription,    // Translation: additions slot description
            List<Identifier> baseItems,         // Base items list for template
            List<Identifier> ingredientItems,   // Ingredient items list for template
            Function<Item.Settings, Item> factory,
            Item.Settings settings,
            Container container
    ) {
        public Entry(Identifier id, String templateKey, String appliesTo, String ingredientText,
                     String titleText, String baseSlotDescription, String additionsSlotDescription,
                     List<Identifier> baseItems, List<Identifier> ingredientItems,
                     Function<Item.Settings, Item> factory, Item.Settings settings) {
            this(id, templateKey, appliesTo, ingredientText, titleText, baseSlotDescription,
                    additionsSlotDescription, baseItems, ingredientItems, factory, settings, new Container());
        }

        // Gets the actual item instance
        public Item item() { return container.item; }
    }

    // List of all smithing template entries for datagen access
    public static final ArrayList<Entry> ENTRIES = new ArrayList<>();

    // Helper method to add entries to the list
    private static Entry add(Entry entry) {
        ENTRIES.add(entry);
        return entry;
    }
    //UPGRADES
    public static final List<Identifier> BASE_ITEMS = Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of("item/empty_slot_axe"));
                identifiers.add(Identifier.of("item/empty_slot_sword"));
                if(FabricLoader.getInstance().isModLoaded("archers")){
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_bow"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_crossbow"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_spear"));
                }
                if(FabricLoader.getInstance().isModLoaded("paladins")){
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_hammer"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_holy"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_mace"));
                }
                if(FabricLoader.getInstance().isModLoaded("rogues")){
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_dagger"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_sickle"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_glaive"));
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_double_axe"));
                }
                if(FabricLoader.getInstance().isModLoaded("wizards")){
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_wand"));
                }
                if(FabricLoader.getInstance().isModLoaded("forcemaster")){
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_knuckle"));
                }
                if(FabricLoader.getInstance().isModLoaded("berserker_axe")){
                    identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_berserker_axe"));
                }

            });
    public static final List<Identifier> INGREDIENT_ITEMS_DRAGON= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_ender_dragon_scales"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_WITHER= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_wither_spine"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_GUARDIAN = Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_elder_guardian_eye"));
            });
    public static final List<Identifier> INGREDIENT_ITEMS_FROSTMONARCH= Util.make(new ArrayList<>(),
            identifiers -> {
                identifiers.add(Identifier.of(MOD_ID,"item/template/empty_slot_frozen_soul"));
            });

    // Smithing template entries with translation data for datagen
    // Each entry contains the template key and all required translation strings
    static {
        // Ender Dragon Upgrade Template
        add(new Entry(
                Identifier.of(MOD_ID, "dragon_upgrade_smithing_template"),
                "ender_dragon",
                "Netherite Weapons.",
                "Ender Dragon Scales",
                "Ender Upgrade",
                "Put a Netherite Weapon here.",
                "Add Ender Dragon Scales",
                BASE_ITEMS,
                INGREDIENT_ITEMS_DRAGON,
                settings -> new SmithingTemplateItem(
                        Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.ender_dragon.ingredients").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.ender_dragon.title").formatted(Formatting.GRAY),
                        Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
                        Text.translatable("smithing_template.loot_n_explore.ender_dragon.additions_slot_description"),
                        BASE_ITEMS,
                        INGREDIENT_ITEMS_DRAGON
                ),
                new Item.Settings()
        ));

        // Elder Guardian Upgrade Template
        add(new Entry(
                Identifier.of(MOD_ID, "guardian_upgrade_smithing_template"),
                "elder_guardian",
                "Netherite Weapons.",
                "Elder Guardian Eye",
                "Deep Ocean Upgrade",
                "Put a Netherite Weapon here.",
                "Add Elder Guardian Eye",
                BASE_ITEMS,
                INGREDIENT_ITEMS_GUARDIAN,
                settings -> new SmithingTemplateItem(
                        Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.elder_guardian.ingredients").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.elder_guardian.title").formatted(Formatting.GRAY),
                        Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
                        Text.translatable("smithing_template.loot_n_explore.elder_guardian.additions_slot_description"),
                        BASE_ITEMS,
                        INGREDIENT_ITEMS_GUARDIAN
                ),
                new Item.Settings()
        ));

        // Wither Upgrade Template
        add(new Entry(
                Identifier.of(MOD_ID, "wither_upgrade_smithing_template"),
                "wither",
                "Netherite Weapons.",
                "Wither Spine",
                "Withered Upgrade",
                "Put a Netherite Weapon here.",
                "Add Wither Spine",
                BASE_ITEMS,
                INGREDIENT_ITEMS_WITHER,
                settings -> new SmithingTemplateItem(
                        Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.wither.ingredients").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.wither.title").formatted(Formatting.GRAY),
                        Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
                        Text.translatable("smithing_template.loot_n_explore.wither.additions_slot_description"),
                        BASE_ITEMS,
                        INGREDIENT_ITEMS_WITHER
                ),
                new Item.Settings()
        ));

        // Frost Monarch Upgrade Template
        add(new Entry(
                Identifier.of(MOD_ID, "frostmonarch_upgrade_smithing_template"),
                "frostmonarch",
                "Netherite Weapons.",
                "Frozen Soul",
                "Glacial Upgrade",
                "Put a Netherite Weapon here.",
                "Add Frozen Soul",
                BASE_ITEMS,
                INGREDIENT_ITEMS_FROSTMONARCH,
                settings -> new SmithingTemplateItem(
                        Text.translatable("smithing_template.loot_n_explore.applies_to").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.frostmonarch.ingredients").formatted(Formatting.BLUE),
                        Text.translatable("smithing_template.loot_n_explore.frostmonarch.title").formatted(Formatting.GRAY),
                        Text.translatable("smithing_template.loot_n_explore.base_slot_description"),
                        Text.translatable("smithing_template.loot_n_explore.frostmonarch.additions_slot_description"),
                        BASE_ITEMS,
                        INGREDIENT_ITEMS_FROSTMONARCH
                ),
                new Item.Settings()
        ));
    }

    // Registers all smithing template entries to the game
    // Iterates through all entries and registers each item to the item registry
    public static void registerSmithingUpgrades(){
        // Register each entry using the Entry system
        for (Entry entry : ENTRIES) {
            Item item = entry.factory().apply(entry.settings());
            entry.container.item = item;
            Registry.register(Registries.ITEM, entry.id(), item);
        }

        // Add templates to vanilla INGREDIENTS item group
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((content) -> {
            for (var entry : ENTRIES) {
                content.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, entry.item());
            }
        });

        // Add templates to custom RPG_LOOT item group
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register((content) -> {
            for (var entry : ENTRIES) {
                content.add(entry.item());
            }
        });
    }
}

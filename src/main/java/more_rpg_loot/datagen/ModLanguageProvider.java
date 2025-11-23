package more_rpg_loot.datagen;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.compat.items.*;
import more_rpg_loot.compat.spell_engine.LNE_Abilities;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.compat.spell_engine.LNE_Weapons;
import more_rpg_loot.compat.spell_engine.SmithingTemplates;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.item.Group;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {

    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder builder) {
        builder.add(Group.lootTranslationKey, "LNE Equipment");
        builder.add(Group.foodTranslationKey, "LNE Drinks & Food");
        builder.add(Group.blocksTranslationKey, "LNE Items & Blocks");
        // BLOCKS
        for (var entry : ModBlocks.all) {
            builder.add(entry.block().getTranslationKey(), entry.translation());
        }

        builder.add(ModBlocks.POTTED_FROST_BLOOM, "Potted Frostbloom");

        // COMMON ITEMS (with optional lore text)
        for (var entry : more_rpg_loot.item.CommonItems.all) {
            builder.add(entry.item().getTranslationKey(), entry.translation());

            // Add lore text if present (shown in item tooltip)
            if (entry.loreText() != null && !entry.loreText().isEmpty()) {
                builder.add(entry.item().getTranslationKey() + ".lore", entry.loreText());
            }
        }

        // SPAWN EGGS
        for (var entry : more_rpg_loot.item.ModSpawnEggs.all) {
            builder.add(entry.item().getTranslationKey(), entry.translation());
        }

        // POTIONS
        for (var entry : more_rpg_loot.item.ModPotions.all) {
            builder.add("item.minecraft.potion.effect." + entry.name(), "Potion of " + entry.translation());
            builder.add("item.minecraft.splash_potion.effect." + entry.name(), "Splash Potion of " + entry.translation());
            builder.add("item.minecraft.lingering_potion.effect." + entry.name(), "Lingering Potion of " + entry.translation());
            builder.add("item.minecraft.tipped_arrow.effect." + entry.name(), "Arrow of " + entry.translation());
        }

        // STATUS EFFECTS (Main mod effects)
        // Generates language entries for effect names and descriptions
        for (var entry : Effects.getEntries()) {
            builder.add(entry.effect.getTranslationKey(), entry.title);
            builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
        }

        // CONDITIONAL STATUS EFFECTS (Loaded only when compatible mods are present)
        // More RPG Classes compatible effects
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            for (var entry : MRPGC_Effects.getEntries()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        }

        // Ranged Weapon API compatible effects
        if (FabricLoader.getInstance().isModLoaded("ranged_weapon_api")) {
            for (var entry : RWA_Effects.getEntries()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        }

        // Spell Power API compatible effects
        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            for (var entry : SpellPower_Effects.getEntries()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        }

        // Witcher RPG compatible effects (generate even when mod is only compiled)
        // Constructs translation keys directly from ID to avoid needing registered effects
        for (var entry : Witcher_Effects.getEntries()) {
            // Build translation key from ID: effect.<namespace>.<path>
            String translationKey = "effect." + entry.id.getNamespace() + "." + entry.id.getPath();
            builder.add(translationKey, entry.title);
            builder.add(translationKey + ".description", entry.description);
        }

        // COMPAT ITEMS (drink items with translations and optional lore)
        for (var entry : CompatItems.getAllEntries()) {
            // Add item name translation (uses full format: item.loot_n_explore.<name>)
            builder.add("item.loot_n_explore." + entry.name(), entry.translation());

            // Add lore text if present (shown in item tooltip)
            if (entry.loreText() != null && !entry.loreText().isEmpty()) {
                builder.add("item.loot_n_explore." + entry.name() + ".lore", entry.loreText());
            }
        }

        // RELICS (Spell Engine compatible items with lore support)
        // Generates language entries for relic names and optional lore text
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            for (var entry : LNE_Relics.entries) {
                if (!entry.translatedName().isEmpty()) {
                    builder.add(entry.item().get().getTranslationKey(), entry.translatedName());
                }
                // Add lore text if present (shown in item tooltip)
                if (!entry.loreText().isEmpty()) {
                    builder.add(entry.item().get().getTranslationKey() + ".lore", entry.loreText());
                }
            }
        }

        // WEAPONS (Spell Engine weapons with built-in translations)
        // Generates language entries for weapon names using translatedName()
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            for (var entry : LNE_Weapons.entries) {
                // Only add translation if translatedName() is set
                if (entry.translatedName() != null && !entry.translatedName().isEmpty()) {
                    builder.add(entry.item().getTranslationKey(), entry.translatedName());
                }
            }
        }

        // SMITHING TEMPLATES (Upgrade templates with multiple translation keys)
        // Generates all required language entries for smithing templates
        // Each template has 5 translation keys: applies_to, ingredients, title, base_slot_description, additions_slot_description
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            // Generate shared translation strings (used by all templates)
            builder.add("smithing_template.loot_n_explore.applies_to", "Netherite Weapons.");
            builder.add("smithing_template.loot_n_explore.base_slot_description", "Put a Netherite Weapon here.");

            // Generate template-specific translations
            for (var entry : SmithingTemplates.ENTRIES) {
                String key = entry.templateKey();
                // Item name (e.g., "item.loot_n_explore.dragon_upgrade_smithing_template": "Smithing Template")
                builder.add(entry.item().getTranslationKey(), "Smithing Template");

                // Template-specific keys
                builder.add("smithing_template.loot_n_explore." + key + ".ingredients", entry.ingredientText());
                builder.add("smithing_template.loot_n_explore." + key + ".title", entry.titleText());
                builder.add("smithing_template.loot_n_explore." + key + ".additions_slot_description", entry.additionsSlotDescription());
            }
        }

        // SPELLS (Spell Engine abilities with names and descriptions)
        // Generates language entries for all spell names and descriptions
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            for (var entry : LNE_Abilities.entries) {
                var id = entry.id();
                builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name", entry.title());
                builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description", entry.description());
            }
        }

        // ADVANCEMENTS (Auto-generated advancement titles and descriptions)
        // Simply reads title and description from advancement entries - no duplicate data!
        for (var entry : ModAdvancementProvider.getAllEntries()) {
            builder.add(entry.titleKey(), entry.title());
            builder.add(entry.descriptionKey(), entry.description());
        }

        // ENTITIES
        builder.add("entity.minecraft.villager.innkeeper", "Innkeeper");
        builder.add("entity.loot_n_explore.glaze", "Glaze");
        builder.add("entity.loot_n_explore.frost_haunt", "Frosthaunt");
        builder.add("entity.loot_n_explore.frost_monarch", "Frost Monarch");
        builder.add("entity.loot_n_explore.frost_monarch.spawn_message", "§5You fools have freed me!");
        builder.add("entity.loot_n_explore.monarchs_servant", "Frost Monarch' Servant");
    }
}

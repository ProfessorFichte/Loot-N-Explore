package more_rpg_loot.datagen;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.compat.items.*;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.compat.spell_engine.LNE_Abilities;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
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

        // COMMON ITEMS
        for (var entry : more_rpg_loot.item.CommonItems.all) {
            builder.add(entry.item().getTranslationKey(), entry.translation());

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

        // STATUS EFFECTS
        for (var entry : Effects.getEntries()) {
            builder.add(entry.effect.getTranslationKey(), entry.title);
            builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
        }

        // CONDITIONAL STATUS EFFECTS
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

        // Witcher RPG
        for (var entry : Witcher_Effects.getEntries()) {
            String translationKey = "effect." + entry.id.getNamespace() + "." + entry.id.getPath();
            builder.add(translationKey, entry.title);
            builder.add(translationKey + ".description", entry.description);
        }
        for (var entry : CriticalStrike_Effects.getEntries()) {
            String translationKey = "effect." + entry.id.getNamespace() + "." + entry.id.getPath();
            builder.add(translationKey, entry.title);
            builder.add(translationKey + ".description", entry.description);
        }

        // COMPAT ITEMS
        for (var entry : CompatItems.getAllEntries()) {
            builder.add("item.loot_n_explore." + entry.name(), entry.translation());

            if (entry.loreText() != null && !entry.loreText().isEmpty()) {
                builder.add("item.loot_n_explore." + entry.name() + ".lore", entry.loreText());
            }
        }

        // RELICS
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            for (var entry : LNE_Relics.entries) {
                if (!entry.translatedName().isEmpty()) {
                    builder.add(entry.item().get().getTranslationKey(), entry.translatedName());
                }
                if (!entry.loreText().isEmpty()) {
                    builder.add(entry.item().get().getTranslationKey() + ".lore", entry.loreText());
                }
            }
        }

        // WEAPONS (always registered via LNE_WeaponItems)
        for (var entry : LNE_WeaponItems.entries) {
            String key = entry.item().getTranslationKey();
            builder.add(key, entry.translatedName());
            builder.add(key + ".lore", entry.loreText());
        }

        // RANGED WEAPONS (registered only when lne_archers is not installed)
        for (var entry : LNE_WeaponItems.rangedEntries) {
            String key = entry.item().getTranslationKey();
            builder.add(key, entry.translatedName());
            builder.add(key + ".lore", entry.loreText());
        }

        // MACES (registered only when lne_paladins is not installed)
        for (var entry : LNE_WeaponItems.maceEntries) {
            String key = entry.item().getTranslationKey();
            builder.add(key, entry.translatedName());
            builder.add(key + ".lore", entry.loreText());
        }

        // SMITHING TEMPLATE
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            builder.add("smithing_template.loot_n_explore.applies_to", "Netherite Weapons.");
            builder.add("smithing_template.loot_n_explore.base_slot_description", "Put a Netherite Weapon here.");

            for (var entry : SmithingTemplates.ENTRIES) {
                String key = entry.templateKey();
                builder.add(entry.item().getTranslationKey(), "Smithing Template");

                // Template-specific keys
                builder.add("smithing_template.loot_n_explore." + key + ".ingredients", entry.ingredientText());
                builder.add("smithing_template.loot_n_explore." + key + ".title", entry.titleText());
                builder.add("smithing_template.loot_n_explore." + key + ".additions_slot_description", entry.additionsSlotDescription());
            }
        }

        // SPELLS
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            for (var entry : LNE_Abilities.entries) {
                var id = entry.id();
                builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name", entry.title());
                builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description", entry.description());
            }
        }

        // ADVANCEMENTS
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
        builder.add("block.loot_n_explore.frozen_soul_block.hint", "Please free our soul, bring us the cursed Crown");

        // MAPS
        builder.add("filled_map.loot_n_explore.frostmonarch_temple", "Frost Monarch Temple Map");

        // JUKEBOX SONGS
        builder.add("jukebox_song.loot_n_explore.frozen_depths", "Frozen Depths");
    }
}

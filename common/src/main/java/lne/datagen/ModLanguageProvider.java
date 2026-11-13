package lne.datagen;

import lne.blocks.generic.VariantControls;
import lne.blocks.FrozenDepthsBlocks;
import lne.blocks.ModBlocks;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchLines;
import lne.compat.items.*;
import lne.item.weapons.LNE_WeaponItems;
import lne.item.weapons.WeaponAbility;
import lne.item.weapons.frozen_depths.FreezingWeapons;
import lne.compat.spell_engine.LNE_Abilities;
import lne.item.relics.LNE_RelicItems;
import lne.item.relics.RelicTooltips;
import lne.ability.RelicAbilities;
import lne.ability.RelicAbility;
import lne.ability.TriggerType;
import lne.compat.spell_engine.SmithingTemplates;
import lne.effects.Effects;
import lne.item.Group;
import lne.sounds.ModSounds;
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
        builder.add(Group.genericTranslationKey, "LNE - Generic Items & Blocks");
        builder.add(Group.frozenDepthsTranslationKey, "LNE-Frozen Depths");
        for (var entry : ModBlocks.all) {
            builder.add(entry.block().getTranslationKey(), entry.translation());
        }

        builder.add(FrozenDepthsBlocks.POTTED_FROST_BLOOM, "Potted Frostbloom");
        builder.add(FrozenDepthsBlocks.FROSTFLAME, "Frostflame");
        builder.add(VariantControls.TOOLTIP, "Press Shift and Right Click to change the variant.");

        for (var entry : lne.item.CommonItems.all) {
            builder.add(entry.item().getTranslationKey(), entry.translation());

            if (entry.loreText() != null && !entry.loreText().isEmpty()) {
                builder.add(entry.item().getTranslationKey() + ".lore", entry.loreText());
            }
        }

        for (var entry : lne.item.ModSpawnEggs.all) {
            builder.add(entry.item().getTranslationKey(), entry.translation());
        }

        for (var entry : lne.item.ModPotions.all) {
            builder.add("item.minecraft.potion.effect." + entry.name(), "Potion of " + entry.translation());
            builder.add("item.minecraft.splash_potion.effect." + entry.name(), "Splash Potion of " + entry.translation());
            builder.add("item.minecraft.lingering_potion.effect." + entry.name(), "Lingering Potion of " + entry.translation());
            builder.add("item.minecraft.tipped_arrow.effect." + entry.name(), "Arrow of " + entry.translation());
        }

        for (var entry : Effects.getEntries()) {
            builder.add(entry.effect.getTranslationKey(), entry.title);
            builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
        }

        if (FabricLoader.getInstance().isModLoaded("mrpg_lib")) {
            for (var entry : MRPGC_Effects.getEntries()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        }

        if (FabricLoader.getInstance().isModLoaded("ranged_weapon_api")) {
            for (var entry : RWA_Effects.getEntries()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        }

        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            for (var entry : SpellPower_Effects.getEntries()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        }

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

        for (var entry : CompatItems.getAllEntries()) {
            builder.add("item.loot_n_explore." + entry.name(), entry.translation());

            if (entry.loreText() != null && !entry.loreText().isEmpty()) {
                builder.add("item.loot_n_explore." + entry.name() + ".lore", entry.loreText());
            }
        }

        for (var entry : LNE_RelicItems.entries) {
            var key = entry.item().get().getTranslationKey();
            builder.add(key, entry.translatedName());
            builder.add(key + ".lore", entry.loreText());
        }
        for (var ability : RelicAbilities.all) {
            builder.add(ability.langKey(), ability.description());
            builder.add(ability.nameKey(), ability.name());
        }
        for (var trigger : TriggerType.values()) {
            builder.add(trigger.langKey(), trigger.label());
        }
        builder.add(RelicTooltips.BONUSES_KEY, "When carried or equipped:");
        builder.add(RelicTooltips.PASSIVE_KEY, "Passive");
        builder.add(RelicTooltips.ACTIVE_KEY, "Active");
        builder.add(RelicTooltips.COOLDOWN_KEY, "Cooldown: %s");
        builder.add(RelicTooltips.STACKS_KEY, "Stacks: %s/%s");
        builder.add(RelicTooltips.ON_COOLDOWN_KEY, "Relic is on cooldown");
        builder.add(RelicTooltips.DEALING_DAMAGE_KEY, "On dealing damage");
        builder.add(RelicTooltips.OFF_HAND_KEY, " (off hand)");
        builder.add(RelicTooltips.RELIC_SLOT_KEY, " (relic slot)");
        builder.add("entity.loot_n_explore.relic_skeleton.owned", "Summoned Skeleton (%s)");
        builder.add("entity.loot_n_explore.relic_frost_haunted.owned", "Summoned Frost Haunted (%s)");
        builder.add("loot_n_explore.command.relics.reset_cooldowns", "Reset relic cooldowns for %s player(s)");
        builder.add("loot_n_explore.command.relics.triggered", "Triggered %s");
        builder.add("loot_n_explore.command.relics.not_triggered", "%s did not trigger (for example max stacks reached or the ward is already active)");
        builder.add("loot_n_explore.command.relics.disabled", "%s has no Loot & Explore ability right now (disabled in the config or replaced by its Spell Engine spell)");
        builder.add("loot_n_explore.command.relics.unknown", "Unknown relic: %s");
        builder.add(RelicAbility.PROGRESS_KILLS, "Kills: %s/%s");
        builder.add(RelicAbility.PROGRESS_HITS, "Hits: %s/%s");
        builder.add(RelicAbility.PROGRESS_HITS_TAKEN, "Hits taken: %s/%s");

        for (var entry : LNE_WeaponItems.entries) {
            builder.add(entry.item().getTranslationKey(), entry.translatedName());
        }

        for (var entry : LNE_WeaponItems.rangedEntries) {
            builder.add(entry.item().getTranslationKey(), entry.translatedName());
        }

        for (var entry : LNE_WeaponItems.maceEntries) {
            builder.add(entry.item().getTranslationKey(), entry.translatedName());
        }

        for (var ability : WeaponAbility.values()) {
            for (var trigger : WeaponAbility.TRIGGERS) {
                builder.add(ability.langKey(trigger), ability.description(trigger));
                builder.add(ability.nameKey(trigger), ability.name(trigger));
            }
        }
        builder.add("item.loot_n_explore.monarchs_frost_staff.ability", "Launches an icicle that damages your target and inflicts Freezing for %s.");
        builder.add(FreezingWeapons.LANG_KEY, "Inflicts Freezing for %s on every hit.");
        builder.add(FreezingWeapons.LANG_KEY + ".name", "Frostbite");
        builder.add("item.loot_n_explore.monarchs_frost_staff.ability.name", "Monarch's Icicle");

        builder.add("item.loot_n_explore.ender_dragon_shield", "Dragon Bulwark");
        builder.add("item.loot_n_explore.wither_shield", "The Mouth of the Wither");
        builder.add("item.loot_n_explore.glacial_shield", "Frozen Wall");
        builder.add("item.loot_n_explore.elder_guardian_shield", "Coral Reef Guardian");

        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            builder.add("smithing_template.loot_n_explore.applies_to", "Netherite Weapons.");
            builder.add("smithing_template.loot_n_explore.base_slot_description", "Put a Netherite Weapon here.");

            for (var entry : SmithingTemplates.ENTRIES) {
                String key = entry.templateKey();
                builder.add(entry.item().getTranslationKey(), "Smithing Template");

                builder.add("smithing_template.loot_n_explore." + key + ".ingredients", entry.ingredientText());
                builder.add("smithing_template.loot_n_explore." + key + ".title", entry.titleText());
                builder.add("smithing_template.loot_n_explore." + key + ".additions_slot_description", entry.additionsSlotDescription());
            }
        }

        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            builder.add("trinkets.slot.spell.trinket", "Relic");
            for (var entry : LNE_Abilities.entries) {
                var id = entry.id();
                builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name", entry.title());
                builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description", entry.description());
            }
        }

        for (var entry : ModAdvancementProvider.getAllEntries()) {
            builder.add(entry.titleKey(), entry.title());
            builder.add(entry.descriptionKey(), entry.description());
        }

        builder.add("entity.minecraft.villager.innkeeper", "Innkeeper");
        for (var entry : lne.entity.ModEntities.all) {
            builder.add("entity." + lne.LneMod.MOD_ID + "." + entry.id(), entry.translation());
        }
        builder.add(FrostMonarchLines.INTRO_KEY, "You fools have freed me!");
        builder.add(FrostMonarchLines.FAKE_DEATH_KEY, "I have lost, again?");
        builder.add(FrostMonarchLines.BARRIER_SUMMON_KEYS.get(0), "Protect me, my servants!");
        builder.add(FrostMonarchLines.BARRIER_SUMMON_KEYS.get(1), "What are you doing? Help me, you fools!");
        builder.add(FrostMonarchLines.BARRIER_SUMMON_KEYS.get(2), "Have fun with my subjects!");
        builder.add(FrostMonarchLines.BARRIER_SUMMON_KEYS.get(3), "When I'm finished with you, you will join them.");
        builder.add("block.loot_n_explore.frozen_soul_block.hint", "Please free our soul, bring us the cursed Crown");
        builder.add("block.loot_n_explore.frozen_soul_block.lore", "It seems powerful magic compressed this block. The souls constantly call for something.");
        builder.add("block.loot_n_explore.monarchs_crown.lore", "This crown once belonged to a king who lost everything to his obsession with power and the freezing cold.");
        builder.add("block.loot_n_explore.monarchs_crown.ability_hint", "[Shift + Right Click] Listen to the soul's voices.");

        builder.add("filled_map.loot_n_explore.monarchs_temple", "Frost Monarch Temple Map");
        builder.add("filled_map.loot_n_explore.frost_tower", "Frost Tower Map");
        builder.add("filled_map.loot_n_explore.desert_pyramid", "Desert Pyramid Map");
        builder.add("filled_map.loot_n_explore.pillager_outpost", "Pillager Outpost Map");

        builder.add("jukebox_song.loot_n_explore.frozen_depths", "KaktusDoesMusic - Frozen Depths");
        builder.add("jukebox_song.loot_n_explore.the_king_cursed_by_winter", "KaktusDoesMusic - The King Cursed by Winter");
        builder.add("jukebox_song.loot_n_explore.jolly_time", "KaktusDoesMusic - Jolly Time");

        for (var entry : ModSounds.entries) {
            if (entry.subtitle() != null) {
                builder.add(entry.subtitleKey(), entry.subtitle());
            }
        }
    }
}

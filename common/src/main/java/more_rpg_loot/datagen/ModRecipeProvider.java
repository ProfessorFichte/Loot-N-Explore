package more_rpg_loot.datagen;

import more_rpg_loot.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.LIGHT_BLUE_DYE)
                .input(ModBlocks.FROST_BLOOM.block())
                .criterion(hasItem(Items.LIGHT_BLUE_DYE), conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .criterion(hasItem(ModBlocks.FROST_BLOOM.block()), conditionsFromItem(ModBlocks.FROST_BLOOM.block()))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/light_blue_dye_from_frost_bloom"));

        Item glazeRod = getItem("loot_n_explore:glaze_rod");
        Item frostball = getItem("loot_n_explore:frostball");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, frostball, 4)
                .input(glazeRod)
                .criterion(hasItem(glazeRod), conditionsFromItem(glazeRod))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/frostball"));

        Item frozenSoulItem = getItem("loot_n_explore:frozen_soul");
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.FROZEN_SOULS.block())
                .pattern("OOO")
                .pattern("OSO")
                .pattern("OOO")
                .input('O', Items.OBSIDIAN)
                .input('S', frozenSoulItem)
                .criterion(hasItem(frozenSoulItem), conditionsFromItem(frozenSoulItem))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/frozen_soul_block"));

        Item dragonTemplate = getItem("loot_n_explore:dragon_upgrade_smithing_template");
        Item dragonTooth = getItem("loot_n_explore:ender_dragon_tooth");
        Item chargedAmethyst = getItem("loot_n_explore:charged_amethyst");
        Item corruptedPearl = getItem("loot_n_explore:corrupted_ender_pearl");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, dragonTemplate)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("#C#")
                .input('#', Items.END_STONE)
                .input('A', chargedAmethyst)
                .input('B', corruptedPearl)
                .input('C', dragonTooth)
                .criterion(hasItem(chargedAmethyst), conditionsFromItem(chargedAmethyst))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/dragon_upgrade_smithing_template"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, dragonTemplate, 2)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("###")
                .input('#', Items.END_STONE)
                .input('A', Items.NETHERITE_INGOT)
                .input('B', dragonTemplate)
                .criterion(hasItem(dragonTemplate), conditionsFromItem(dragonTemplate))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/dragon_upgrade_smithing_template_multiply"));

        Item guardianTemplate = getItem("loot_n_explore:guardian_upgrade_smithing_template");
        Item amphora = getItem("loot_n_explore:poseidons_amphora");
        Item diadem = getItem("loot_n_explore:amphitrite_diadem");
        Item coral = getItem("loot_n_explore:rainbow_coral");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, guardianTemplate)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("#C#")
                .input('#', Items.PRISMARINE)
                .input('A', diadem)
                .input('B', coral)
                .input('C', amphora)
                .criterion(hasItem(diadem), conditionsFromItem(diadem))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/guardian_upgrade_smithing_template"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, guardianTemplate, 2)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("###")
                .input('#', Items.PRISMARINE)
                .input('A', Items.NETHERITE_INGOT)
                .input('B', guardianTemplate)
                .criterion(hasItem(guardianTemplate), conditionsFromItem(guardianTemplate))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/guardian_upgrade_smithing_template_multiply"));

        Item witherTemplate = getItem("loot_n_explore:wither_upgrade_smithing_template");
        Item unknownRemains = getItem("loot_n_explore:unknown_remains");
        Item lostSoul = getItem("loot_n_explore:lost_soul");
        Item witheredShard = getItem("loot_n_explore:withered_obsidian_shard");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, witherTemplate)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("#C#")
                .input('#', Items.NETHER_BRICKS)
                .input('A', unknownRemains)
                .input('B', lostSoul)
                .input('C', witheredShard)
                .criterion(hasItem(unknownRemains), conditionsFromItem(unknownRemains))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/wither_upgrade_smithing_template"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, witherTemplate, 2)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("###")
                .input('#', Items.NETHER_BRICKS)
                .input('A', Items.NETHERITE_INGOT)
                .input('B', witherTemplate)
                .criterion(hasItem(witherTemplate), conditionsFromItem(witherTemplate))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/wither_upgrade_smithing_template_multiply"));

        Item frostTemplate = getItem("loot_n_explore:frostmonarch_upgrade_smithing_template");
        Item snowflake = getItem("loot_n_explore:eternal_snowflake");
        Item glacierShard = getItem("loot_n_explore:glacier_shard");
        Item frozenRib = getItem("loot_n_explore:frozen_rib");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, frostTemplate)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("#C#")
                .input('#', Items.PACKED_ICE)
                .input('A', snowflake)
                .input('B', glacierShard)
                .input('C', frozenRib)
                .criterion(hasItem(snowflake), conditionsFromItem(snowflake))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/frostmonarch_upgrade_smithing_template"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, frostTemplate, 2)
                .pattern("#A#")
                .pattern("#B#")
                .pattern("###")
                .input('#', Items.PACKED_ICE)
                .input('A', Items.NETHERITE_INGOT)
                .input('B', frostTemplate)
                .criterion(hasItem(frostTemplate), conditionsFromItem(frostTemplate))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/frostmonarch_upgrade_smithing_template_multiply"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.AMETHYST_SHARD, 5)
                .input(chargedAmethyst)
                .criterion(hasItem(chargedAmethyst), conditionsFromItem(chargedAmethyst))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/uncraft_charged_amethyst"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BONE, 8)
                .input(unknownRemains)
                .criterion(hasItem(unknownRemains), conditionsFromItem(unknownRemains))
                .offerTo(exporter, Identifier.of("loot_n_explore", "generic/uncraft_unknown_remains"));

        Item dragonScales = getItem("loot_n_explore:ender_dragon_scales");
        Item guardianEye   = getItem("loot_n_explore:elder_guardian_eye");
        Item witherSpine   = getItem("loot_n_explore:wither_spine");
        Item frozenSoul    = getItem("loot_n_explore:frozen_soul");

        createSmithingTransformRecipe(exporter, dragonTemplate, Items.NETHERITE_SWORD, dragonScales, getItem("loot_n_explore:ender_dragon_sword"), "generic/ender_dragon_sword");
        createSmithingTransformRecipe(exporter, dragonTemplate, Items.NETHERITE_AXE,   dragonScales, getItem("loot_n_explore:ender_dragon_axe"),   "generic/ender_dragon_axe");
        createSmithingTransformRecipe(exporter, guardianTemplate, Items.NETHERITE_SWORD, guardianEye, getItem("loot_n_explore:elder_guardian_sword"), "generic/elder_guardian_sword");
        createSmithingTransformRecipe(exporter, guardianTemplate, Items.NETHERITE_AXE,   guardianEye, getItem("loot_n_explore:elder_guardian_axe"),   "generic/elder_guardian_axe");
        createSmithingTransformRecipe(exporter, witherTemplate, Items.NETHERITE_SWORD, witherSpine, getItem("loot_n_explore:wither_sword"), "generic/wither_sword");
        createSmithingTransformRecipe(exporter, witherTemplate, Items.NETHERITE_AXE,   witherSpine, getItem("loot_n_explore:wither_axe"),   "generic/wither_axe");
        createSmithingTransformRecipe(exporter, frostTemplate, Items.NETHERITE_SWORD, frozenSoul, getItem("loot_n_explore:glacial_sword"), "frozen_depths/glacial_sword");
        createSmithingTransformRecipe(exporter, frostTemplate, Items.NETHERITE_AXE,   frozenSoul, getItem("loot_n_explore:glacial_axe"),   "frozen_depths/glacial_axe");

        // Bow/crossbow (unless lne_archers installed) and mace (unless lne_paladins installed) recipes
        // need both fabric:load_conditions and neoforge:conditions, which the FabricRecipeProvider
        // exporter can't emit together - see LootExploreConditionalRecipeProvider.
    }


    private void createSmithingTransformRecipe(RecipeExporter exporter, Item template, Item base, Item addition, Item result, String recipeName) {
        SmithingTransformRecipeJsonBuilder.create(
                Ingredient.ofItems(template),
                Ingredient.ofItems(base),
                Ingredient.ofItems(addition),
                RecipeCategory.COMBAT,
                result
        )
        .criterion(hasItem(addition), conditionsFromItem(addition))
        .offerTo(exporter, Identifier.of("loot_n_explore", recipeName));
    }


    private Item getItem(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid item ID: " + itemId);
        }
        Item item = Registries.ITEM.get(id);
        if (item == Items.AIR) {
            System.out.println("WARNING: Item not found: " + itemId + ", using BARRIER as placeholder");
            return Items.BARRIER;
        }
        return item;
    }
}

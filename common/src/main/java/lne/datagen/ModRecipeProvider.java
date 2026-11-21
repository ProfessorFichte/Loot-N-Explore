package lne.datagen;

import lne.blocks.FrozenDepthsBlocks;
import lne.item.CommonItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
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
                .input(FrozenDepthsBlocks.FROST_BLOOM.block())
                .criterion(hasItem(Items.LIGHT_BLUE_DYE), conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .criterion(hasItem(FrozenDepthsBlocks.FROST_BLOOM.block()), conditionsFromItem(FrozenDepthsBlocks.FROST_BLOOM.block()))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/light_blue_dye_from_frost_bloom"));

        Item glazeRod = getItem("loot_n_explore:glaze_rod");
        Item frostball = getItem("loot_n_explore:frostball");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, frostball, 4)
                .input(glazeRod)
                .criterion(hasItem(glazeRod), conditionsFromItem(glazeRod))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/frostball"));

        Item frozenSoulItem = getItem("loot_n_explore:frozen_soul");
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FrozenDepthsBlocks.FROZEN_SOULS.block())
                .pattern("CCC")
                .pattern("CSC")
                .pattern("CCC")
                .input('C', FrozenDepthsBlocks.CRYOTIC_BLOCK.block())
                .input('S', frozenSoulItem)
                .criterion(hasItem(frozenSoulItem), conditionsFromItem(frozenSoulItem))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/frozen_soul_block"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, CommonItems.MONARCHS_FROST_STAFF.item())
                .pattern(" CM")
                .pattern(" GC")
                .pattern("F  ")
                .input('G', glazeRod)
                .input('F', frozenSoulItem)
                .input('C', FrozenDepthsBlocks.CRYOTIC_BLOCK.block())
                .input('M', FrozenDepthsBlocks.MONARCHS_CROWN.item())
                .criterion(hasItem(FrozenDepthsBlocks.MONARCHS_CROWN.item()), conditionsFromItem(FrozenDepthsBlocks.MONARCHS_CROWN.item()))
                .offerTo(exporter, Identifier.of("loot_n_explore", "frozen_depths/monarchs_frost_staff"));

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

        generateFrozenDepthsBlocks(exporter);

        // these need both fabric:load_conditions and neoforge:conditions, which the
        // FabricRecipeProvider exporter can't emit together, see LootExploreConditionalRecipeProvider
    }


    private void generateFrozenDepthsBlocks(RecipeExporter exporter) {
        Item cryoEssence = CommonItems.CRYO_ESSENCE.item();
        Block cryoticBlock = FrozenDepthsBlocks.CRYOTIC_BLOCK.block();
        Block blueIceBricks = FrozenDepthsBlocks.BLUE_ICE_BRICKS.block();
        Block frozenTorch = FrozenDepthsBlocks.FROZEN_TORCH.block();
        Block icicle = FrozenDepthsBlocks.ICICLE.block();

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, cryoticBlock)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .input('#', cryoEssence)
                .criterion(hasItem(cryoEssence), conditionsFromItem(cryoEssence))
                .offerTo(exporter, frozenDepths("cryotic_block"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, FrozenDepthsBlocks.DEEPSLATE_BOOKSHELF.block())
                .pattern("###")
                .pattern("XXX")
                .pattern("###")
                .input('#', Items.POLISHED_DEEPSLATE)
                .input('X', Items.BOOK)
                .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                .offerTo(exporter, frozenDepths("deepslate_bookshelf"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, frozenTorch, 4)
                .pattern("X")
                .pattern("#")
                .input('X', cryoEssence)
                .input('#', Items.IRON_INGOT)
                .criterion(hasItem(cryoEssence), conditionsFromItem(cryoEssence))
                .offerTo(exporter, frozenDepths("frozen_torch"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FrozenDepthsBlocks.SOULFROST_LANTERN.block())
                .pattern("XXX")
                .pattern("X#X")
                .pattern("XXX")
                .input('#', frozenTorch)
                .input('X', Items.IRON_NUGGET)
                .criterion(hasItem(frozenTorch), conditionsFromItem(frozenTorch))
                .offerTo(exporter, frozenDepths("soulfrost_lantern"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FrozenDepthsBlocks.FROZEN_CHAIN.block())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .input('I', cryoEssence)
                .input('N', Items.IRON_NUGGET)
                .criterion(hasItem(cryoEssence), conditionsFromItem(cryoEssence))
                .offerTo(exporter, frozenDepths("frozen_chain"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FrozenDepthsBlocks.ICICLE_BAR.block(), 16)
                .pattern("###")
                .pattern("###")
                .input('#', icicle)
                .criterion(hasItem(icicle), conditionsFromItem(icicle))
                .offerTo(exporter, frozenDepths("icicle_bar"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, blueIceBricks, 4)
                .pattern("##")
                .pattern("##")
                .input('#', Items.BLUE_ICE)
                .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                .offerTo(exporter, frozenDepths("blue_ice_bricks"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, FrozenDepthsBlocks.BLUE_ICE_BRICK_SLAB.block(), 6)
                .pattern("###")
                .input('#', blueIceBricks)
                .criterion(hasItem(blueIceBricks), conditionsFromItem(blueIceBricks))
                .offerTo(exporter, frozenDepths("blue_ice_brick_slab"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, FrozenDepthsBlocks.BLUE_ICE_BRICK_STAIRS.block(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .input('#', blueIceBricks)
                .criterion(hasItem(blueIceBricks), conditionsFromItem(blueIceBricks))
                .offerTo(exporter, frozenDepths("blue_ice_brick_stairs"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block(), 6)
                .pattern("###")
                .pattern("###")
                .input('#', blueIceBricks)
                .criterion(hasItem(blueIceBricks), conditionsFromItem(blueIceBricks))
                .offerTo(exporter, frozenDepths("blue_ice_brick_wall"));
        stonecutting(exporter, RecipeCategory.BUILDING_BLOCKS, blueIceBricks, FrozenDepthsBlocks.BLUE_ICE_BRICK_SLAB.block(), 2);
        stonecutting(exporter, RecipeCategory.BUILDING_BLOCKS, blueIceBricks, FrozenDepthsBlocks.BLUE_ICE_BRICK_STAIRS.block(), 1);
        stonecutting(exporter, RecipeCategory.DECORATIONS, blueIceBricks, FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block(), 1);
        stonecutting(exporter, RecipeCategory.BUILDING_BLOCKS, blueIceBricks, FrozenDepthsBlocks.POLISHED_ICE_BRICKS.block(), 1);
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(blueIceBricks), RecipeCategory.BUILDING_BLOCKS,
                        FrozenDepthsBlocks.CRACKED_BLUE_ICE_BRICKS.block(), 0.1F, 200)
                .criterion(hasItem(blueIceBricks), conditionsFromItem(blueIceBricks))
                .offerTo(exporter, frozenDepths("cracked_blue_ice_bricks"));
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, FrozenDepthsBlocks.SNOWY_BLUE_ICE_BRICKS.block())
                .input(blueIceBricks)
                .input(Items.SNOWBALL)
                .criterion(hasItem(blueIceBricks), conditionsFromItem(blueIceBricks))
                .offerTo(exporter, frozenDepths("snowy_blue_ice_bricks"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, FrozenDepthsBlocks.POLISHED_ICE_BLOCK.block(), 4)
                .pattern("#X")
                .pattern("X#")
                .input('#', Items.ICE)
                .input('X', Items.BLUE_ICE)
                .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                .offerTo(exporter, frozenDepths("polished_ice_block"));
    }

    private static void stonecutting(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output, int count) {
        StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(input), category, output, count)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, frozenDepths(getItemPath(output) + "_from_" + getItemPath(input) + "_stonecutting"));
    }

    private static Identifier frozenDepths(String name) {
        return Identifier.of("loot_n_explore", "frozen_depths/" + name);
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
            throw new IllegalStateException("Item not found: " + itemId);
        }
        return item;
    }
}

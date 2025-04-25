package more_rpg_loot.datagen;

import more_rpg_loot.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.LIGHT_BLUE_DYE).input(ModBlocks.FROST_BLOOM.block()).criterion(FabricRecipeProvider.hasItem(Items.LIGHT_BLUE_DYE),
                FabricRecipeProvider.conditionsFromItem(Items.LIGHT_BLUE_DYE)).criterion(FabricRecipeProvider.hasItem(ModBlocks.FROST_BLOOM.block()),
                FabricRecipeProvider.conditionsFromItem(ModBlocks.FROST_BLOOM.block())).offerTo(exporter);
    }
}

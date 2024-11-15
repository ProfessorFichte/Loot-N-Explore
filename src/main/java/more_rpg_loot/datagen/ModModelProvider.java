package more_rpg_loot.datagen;

import more_rpg_loot.item.CommonItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import more_rpg_loot.blocks.ModBlocks;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateModelGenerator.BlockTexturePool blueIceBrickPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.BLUE_ICE_BRICKS.block());

        blueIceBrickPool.stairs(ModBlocks.BLUE_ICE_BRICK_STAIRS.block());
        blueIceBrickPool.wall(ModBlocks.BLUE_ICE_BRICK_WALL.block());
        blueIceBrickPool.slab(ModBlocks.BLUE_ICE_BRICK_SLAB.block());

        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block());
        blockStateModelGenerator.registerLantern(ModBlocks.SOULFROST_LANTERN.block());

        blockStateModelGenerator.registerFlowerPotPlant(ModBlocks.FROST_BLOOM.block(), ModBlocks.POTTED_FROST_BLOOM, BlockStateModelGenerator.TintType.NOT_TINTED);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModBlocks.FROZEN_CHAIN.item(), Models.GENERATED);
        itemModelGenerator.register(CommonItems.GLAZE_ROD, Models.HANDHELD);
        itemModelGenerator.register(CommonItems.FROSTBALL, Models.GENERATED);
    }


}




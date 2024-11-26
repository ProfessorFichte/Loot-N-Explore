package more_rpg_loot.datagen;

import more_rpg_loot.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider  extends FabricTagProvider.BlockTagProvider{
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.SOULFROST_LANTERN.block())
                .add(ModBlocks.FROZEN_CHAIN.block())
                .add(ModBlocks.BLUE_ICE_BRICKS.block())
                .add(ModBlocks.MONARCHS_CROWN.block())
                .add(ModBlocks.BLUE_ICE_BRICK_SLAB.block())
                .add(ModBlocks.BLUE_ICE_BRICK_STAIRS.block())
                .add(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block())
        ;

        getOrCreateTagBuilder(BlockTags.ICE)
                .add(ModBlocks.BLUE_ICE_BRICKS.block())
                .add(ModBlocks.BLUE_ICE_BRICK_SLAB.block())
                .add(ModBlocks.BLUE_ICE_BRICK_STAIRS.block())
                .add(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block())
        ;

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlocks.INNKEEPER_SHELF.block())
        ;

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.SOULFROST_LANTERN.block())
                .add(ModBlocks.FROZEN_CHAIN.block())
                .add(ModBlocks.BLUE_ICE_BRICKS.block())
                .add(ModBlocks.MONARCHS_CROWN.block())
                .add(ModBlocks.BLUE_ICE_BRICK_SLAB.block())
                .add(ModBlocks.BLUE_ICE_BRICK_STAIRS.block())
                .add(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block())
        ;

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.BLUE_ICE_BRICK_WALL.block())
        ;
    }

}

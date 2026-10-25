package more_rpg_loot.datagen;

import more_rpg_loot.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registries;
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
                .addOptional(Registries.BLOCK.getId(ModBlocks.SOULFROST_LANTERN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.FROZEN_CHAIN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.MONARCHS_CROWN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_SLAB.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_STAIRS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_WALL.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.SNOWY_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.POLISHED_ICE_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.POLISHED_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.FROZEN_SOULS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.FROZEN_TRIAL_SPAWNER.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.FROZEN_VAULT.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.ICICLE.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.ICICLE_BAR.block()))
        ;

        getOrCreateTagBuilder(BlockTags.ICE)
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_SLAB.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_STAIRS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_WALL.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.SNOWY_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.POLISHED_ICE_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.POLISHED_ICE_BRICKS.block()))
        ;

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addOptional(Registries.BLOCK.getId(ModBlocks.INNKEEPER_SHELF.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.OAK_HANGING_INN_SIGN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.ACACIA_HANGING_INN_SIGN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.SPRUCE_HANGING_INN_SIGN.block()))
        ;

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .addOptional(Registries.BLOCK.getId(ModBlocks.SOULFROST_LANTERN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.FROZEN_CHAIN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.MONARCHS_CROWN.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_SLAB.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_STAIRS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_WALL.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.SNOWY_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.POLISHED_ICE_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.POLISHED_ICE_BRICKS.block()))
        ;

        getOrCreateTagBuilder(BlockTags.WALLS)
                .addOptional(Registries.BLOCK.getId(ModBlocks.BLUE_ICE_BRICK_WALL.block()))
        ;

        getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                .addOptional(Registries.BLOCK.getId(ModBlocks.FROST_BLOOM.block()))
        ;
    }

}

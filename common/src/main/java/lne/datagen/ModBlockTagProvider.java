package lne.datagen;

import lne.blocks.FrozenDepthsBlocks;
import lne.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import lne.LneMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider  extends FabricTagProvider.BlockTagProvider{
    public static final TagKey<Block> STRUCTURE_POTS = TagKey.of(RegistryKeys.BLOCK, LneMod.id("structure_pots"));

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        var pickaxe = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
        pickaxe
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SOULFROST_LANTERN.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.FROZEN_CHAIN.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.MONARCHS_CROWN.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_SLAB.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_STAIRS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.CRACKED_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SNOWY_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.POLISHED_ICE_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.POLISHED_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.FROZEN_SOULS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SEALED_MONARCH.block()))
        ;
        for (var entry : ModBlocks.all) {
            if (entry.block() instanceof lne.blocks.themed.ThemedBlock) pickaxe.addOptional(Registries.BLOCK.getId(entry.block()));
        }
        pickaxe
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.ICICLE.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.ICICLE_BAR.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.CRYOTIC_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.DEEPSLATE_BOOKSHELF.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.ICY_DEEPSLATE_BOOKSHELF.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SUSPICIOUS_DEEPSLATE_BOOKSHELF.block()))
        ;

        getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.DEEPSLATE_BOOKSHELF.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.ICY_DEEPSLATE_BOOKSHELF.block()))
        ;

        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(FrozenDepthsBlocks.FROSTFLAME)
        ;

        getOrCreateTagBuilder(BlockTags.ICE)
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_SLAB.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_STAIRS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.CRACKED_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SNOWY_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.POLISHED_ICE_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.POLISHED_ICE_BRICKS.block()))
        ;

        var axeMineable = getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addOptional(Registries.BLOCK.getId(ModBlocks.INNKEEPER_SHELF.block()))
                .addOptional(Registries.BLOCK.getId(ModBlocks.SUSPICIOUS_BOOKSHELF.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.FROZEN_KINGDOM_FLAG.block()));
        ModBlocks.INN_SIGNS.forEach(sign -> axeMineable.addOptional(Registries.BLOCK.getId(sign.block())));

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SOULFROST_LANTERN.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.FROZEN_CHAIN.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.MONARCHS_CROWN.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_SLAB.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_STAIRS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.CRACKED_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SNOWY_BLUE_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.POLISHED_ICE_BLOCK.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.POLISHED_ICE_BRICKS.block()))
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.CRYOTIC_BLOCK.block()))
        ;

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.SEALED_MONARCH.block()))
        ;

        getOrCreateTagBuilder(BlockTags.WALLS)
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block()))
        ;

        getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                .addOptional(Registries.BLOCK.getId(FrozenDepthsBlocks.FROST_BLOOM.block()))
        ;

        getOrCreateTagBuilder(STRUCTURE_POTS)
                .add(Blocks.DECORATED_POT)
                .add(FrozenDepthsBlocks.FROZEN_DECORATED_POT.block())
        ;
    }

}

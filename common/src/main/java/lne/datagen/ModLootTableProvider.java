package lne.datagen;

import net.minecraft.predicate.StatePredicate;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.loot.entry.DynamicEntry;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.block.DecoratedPotBlock;
import lne.blocks.FrozenDepthsBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import lne.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(FrozenDepthsBlocks.BLUE_ICE_BRICKS.block());
        addDrop(FrozenDepthsBlocks.CRACKED_BLUE_ICE_BRICKS.block());
        addDrop(FrozenDepthsBlocks.BLUE_ICE_BRICK_SLAB.block());
        addDrop(FrozenDepthsBlocks.BLUE_ICE_BRICK_WALL.block());
        addDrop(FrozenDepthsBlocks.BLUE_ICE_BRICK_STAIRS.block());
        addDrop(FrozenDepthsBlocks.FROZEN_CHAIN.block());
        addDrop(FrozenDepthsBlocks.SOULFROST_LANTERN.block());
        addDrop(FrozenDepthsBlocks.FROZEN_SOULS.block());
        addDrop(FrozenDepthsBlocks.CRYOTIC_BLOCK.block());
        addDrop(FrozenDepthsBlocks.FROSTFLAME, dropsNothing());
        addDrop(FrozenDepthsBlocks.MONARCHS_CROWN.block());
        addDrop(FrozenDepthsBlocks.FROST_BLOOM.block());
        addPottedPlantDrops(FrozenDepthsBlocks.POTTED_FROST_BLOOM);

        addToolDrop(FrozenDepthsBlocks.SNOWY_BLUE_ICE_BRICKS.block(), ItemTags.PICKAXES);
        addToolDrop(FrozenDepthsBlocks.POLISHED_ICE_BLOCK.block(), ItemTags.PICKAXES);
        addToolDrop(FrozenDepthsBlocks.POLISHED_ICE_BRICKS.block(), ItemTags.PICKAXES);
        addDrop(FrozenDepthsBlocks.ICICLE_BAR.block());
        addDrop(FrozenDepthsBlocks.FRAGILE_ICE.block(), dropsWithSilkTouch(FrozenDepthsBlocks.FRAGILE_ICE.block()));
        addToolDrop(ModBlocks.OAK_HANGING_INN_SIGN.block(), ItemTags.AXES);
        addToolDrop(ModBlocks.ACACIA_HANGING_INN_SIGN.block(), ItemTags.AXES);
        addToolDrop(ModBlocks.SPRUCE_HANGING_INN_SIGN.block(), ItemTags.AXES);
        addDrop(FrozenDepthsBlocks.FROZEN_TORCH.block());
        addBoneDrop(FrozenDepthsBlocks.FROZEN_BONES.block(), 1, 3);
        addBoneDrop(FrozenDepthsBlocks.FROZEN_ADVENTURER.block(), 2, 4);
        addDrop(FrozenDepthsBlocks.DEEPSLATE_BOOKSHELF.block(), block -> drops(block, Items.BOOK, ConstantLootNumberProvider.create(3)));
        addDrop(FrozenDepthsBlocks.ICY_DEEPSLATE_BOOKSHELF.block(), block -> drops(block, FrozenDepthsBlocks.DEEPSLATE_BOOKSHELF.block(), ConstantLootNumberProvider.create(1)));
        addDrop(FrozenDepthsBlocks.SUSPICIOUS_DEEPSLATE_BOOKSHELF.block(), dropsNothing());
        addDrop(ModBlocks.SUSPICIOUS_BOOKSHELF.block(), dropsNothing());
        addDrop(FrozenDepthsBlocks.FROZEN_KINGDOM_FLAG.block());
        addDecoratedPotDrop(FrozenDepthsBlocks.FROZEN_DECORATED_POT.block());
        addDrop(FrozenDepthsBlocks.ICICLE.block(), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(toolCondition(ItemTags.PICKAXES))
                .conditionally(createSilkTouchCondition())
                .with(ItemEntry.builder(FrozenDepthsBlocks.ICICLE.block()))));
    }

    private MatchToolLootCondition.Builder toolCondition(TagKey<Item> tool) {
        return MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                .tag(tool));
    }

    private void addToolDrop(Block block, TagKey<Item> tool) {
        addDrop(block, LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(toolCondition(tool))
                .with(ItemEntry.builder(block))));
    }

    private void addDecoratedPotDrop(Block block) {
        addDrop(block, LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(AlternativeEntry.builder(
                        DynamicEntry.builder(DecoratedPotBlock.SHERDS_DYNAMIC_DROP_ID)
                                .conditionally(BlockStatePropertyLootCondition.builder(block)
                                        .properties(StatePredicate.Builder.create().exactMatch(DecoratedPotBlock.CRACKED, true))),
                        ItemEntry.builder(block)
                                .apply(CopyComponentsLootFunction.builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY)
                                        .include(DataComponentTypes.POT_DECORATIONS))))));
    }

    private void addBoneDrop(Block block, int min, int max) {
        addDrop(block, LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(Items.BONE)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max))))));
    }

}

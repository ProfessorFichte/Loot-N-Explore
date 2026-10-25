package more_rpg_loot.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import more_rpg_loot.blocks.ModBlocks;
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
        addDrop(ModBlocks.BLUE_ICE_BRICKS.block());
        addDrop(ModBlocks.CRACKED_BLUE_ICE_BRICKS.block());
        addDrop(ModBlocks.BLUE_ICE_BRICK_SLAB.block());
        addDrop(ModBlocks.BLUE_ICE_BRICK_WALL.block());
        addDrop(ModBlocks.BLUE_ICE_BRICK_STAIRS.block());
        addDrop(ModBlocks.FROZEN_CHAIN.block());
        addDrop(ModBlocks.SOULFROST_LANTERN.block());
        addDrop(ModBlocks.FROZEN_SOULS.block());
        addDrop(ModBlocks.MONARCHS_CROWN.block());
        addDrop(ModBlocks.FROST_BLOOM.block());
        addPottedPlantDrops(ModBlocks.POTTED_FROST_BLOOM);

        addToolDrop(ModBlocks.SNOWY_BLUE_ICE_BRICKS.block(), ItemTags.PICKAXES);
        addToolDrop(ModBlocks.POLISHED_ICE_BLOCK.block(), ItemTags.PICKAXES);
        addToolDrop(ModBlocks.POLISHED_ICE_BRICKS.block(), ItemTags.PICKAXES);
        addDrop(ModBlocks.ICICLE_BAR.block());
        addToolDrop(ModBlocks.OAK_HANGING_INN_SIGN.block(), ItemTags.AXES);
        addToolDrop(ModBlocks.ACACIA_HANGING_INN_SIGN.block(), ItemTags.AXES);
        addToolDrop(ModBlocks.SPRUCE_HANGING_INN_SIGN.block(), ItemTags.AXES);
        addDrop(ModBlocks.FROZEN_TORCH.block());
        addBoneDrop(ModBlocks.FROZEN_BONES.block(), 1, 3);
        addBoneDrop(ModBlocks.FROZEN_ADVENTURER.block(), 2, 4);
        addDrop(ModBlocks.ICICLE.block(), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(toolCondition(ItemTags.PICKAXES))
                .conditionally(createSilkTouchCondition())
                .with(ItemEntry.builder(ModBlocks.ICICLE.block()))));
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

    private void addBoneDrop(Block block, int min, int max) {
        addDrop(block, LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(Items.BONE)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max))))));
    }

}

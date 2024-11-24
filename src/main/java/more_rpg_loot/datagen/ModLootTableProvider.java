package more_rpg_loot.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import more_rpg_loot.blocks.ModBlocks;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
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
    }

}

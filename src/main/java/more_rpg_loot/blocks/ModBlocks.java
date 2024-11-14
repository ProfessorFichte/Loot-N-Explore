package more_rpg_loot.blocks;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.item.Group;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModBlocks {
    public record Entry(String name, Block block, BlockItem item) {
        public Entry(String name, Block block) {
            this(name, block, new BlockItem(block, new FabricItemSettings()));
        }
    }
    public static final ArrayList<Entry> all = new ArrayList<>();
    private static Entry entry(String name, Block block) {
        var entry = new Entry(name, block);
        all.add(entry);
        return entry;
    }

    public static final Entry INNKEEPER_SHELF = entry("innkeeper_shelf", new InnkeeperShelfBlock(
            FabricBlockSettings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(Instrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
    ));
    public static final Entry BLUE_ICE_BRICKS = entry("blue_ice_bricks", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)
    ));
    public static final Entry CRACKED_BLUE_ICE_BRICKS = entry("cracked_blue_ice_bricks", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)
    ));
    public static final Entry BLUE_ICE_BRICK_SLAB = entry("blue_ice_brick_slab", new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)
    ));
    public static final Entry BLUE_ICE_BRICK_WALL = entry("blue_ice_brick_wall", new WallBlock(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)
    ));
    public static final Entry BLUE_ICE_BRICK_STAIRS = entry("blue_ice_brick_stairs", new StairsBlock(
            ModBlocks.BLUE_ICE_BRICKS.block.getDefaultState(), FabricBlockSettings.copyOf(Blocks.BLUE_ICE)
    ));

    public static final Entry FROZEN_CHAIN = entry("frozen_chain", new ChainBlock(
            FabricBlockSettings.copyOf(Blocks.CHAIN).nonOpaque()
    ));
    public static final Entry SOULFROST_LANTERN = entry("soulfrost_lantern", new LanternBlock(
            FabricBlockSettings.copyOf(Blocks.SOUL_LANTERN).nonOpaque()
    ));

    public static final Entry FROST_BLOOM = entry("frostbloom",
            new FlowerBlock(Effects.FROST_RESISTANCE, 5,
                    FabricBlockSettings.copyOf(Blocks.ALLIUM).nonOpaque().noCollision()));
    public static final Block POTTED_FROST_BLOOM = Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "potted_frostbloom"),
            new FlowerPotBlock(FROST_BLOOM.block, FabricBlockSettings.copyOf(Blocks.POTTED_ALLIUM).nonOpaque()));

    public static void register(){
        for (var entry : all) {
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, entry.name), entry.block);
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, entry.name), entry.item());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_BLOCK_KEY).register((content) -> {
            for (var entry : all) {
                content.add(entry.item());
            }
        });
    }
}

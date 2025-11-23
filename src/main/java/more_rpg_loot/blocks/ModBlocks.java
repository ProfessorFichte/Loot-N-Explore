package more_rpg_loot.blocks;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.item.Group;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModBlocks {
    public record Entry(String name, Block block, BlockItem item, String translation, ModelType modelType) {
        public Entry(String name, Block block, String translation, ModelType modelType) {
            this(name, block, new BlockItem(block, new Item.Settings()), translation, modelType);
        }

        // Convenience constructor that defaults to CubeAll model
        public Entry(String name, Block block, String translation) {
            this(name, block, translation, new ModelType.CubeAll());
        }
    }
    public static final ArrayList<Entry> all = new ArrayList<>();
    private static Entry entry(String name, Block block, String translation) {
        var entry = new Entry(name, block, translation);
        all.add(entry);
        return entry;
    }

    private static Entry entry(String name, Block block, String translation, ModelType modelType) {
        var entry = new Entry(name, block, translation, modelType);
        all.add(entry);
        return entry;
    }

    /// BASE MOD BLOCKS
    public static final Entry INNKEEPER_SHELF = entry("innkeeper_shelf", new InnkeeperShelfBlock(
            FabricBlockSettings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()), "Innkeeper Shelf", new ModelType.CubeBottomTop());
    /// FROZEN DEPTHS 1.0 CONTENT BLOCKS
    public static final Entry BLUE_ICE_BRICKS = entry("blue_ice_bricks", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Blue Ice Bricks");
    public static final Entry CRACKED_BLUE_ICE_BRICKS = entry("cracked_blue_ice_bricks", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Cracked Blue Ice Bricks");
    public static final Entry SNOWY_BLUE_ICE_BRICKS = entry("snowy_blue_ice_bricks", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Snowy Blue Ice Bricks");
    public static final Entry POLISHED_ICE_BLOCK = entry("polished_ice_block", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Polished Ice Block");
    public static final Entry POLISHED_ICE_BRICKS = entry("polished_ice_bricks", new Block(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Polished Ice Bricks");
    public static final Entry BLUE_ICE_BRICK_SLAB = entry("blue_ice_brick_slab", new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Blue Ice Brick Slab", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.SLAB));
    public static final Entry BLUE_ICE_BRICK_WALL = entry("blue_ice_brick_wall", new WallBlock(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Blue Ice Brick Wall", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.WALL));
    public static final Entry BLUE_ICE_BRICK_STAIRS = entry("blue_ice_brick_stairs", new StairsBlock(
            ModBlocks.BLUE_ICE_BRICKS.block.getDefaultState(), FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Blue Ice Brick Stairs", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.STAIRS));
    public static final Entry FROZEN_SOULS = entry("frozen_soul_block", new Block(
            FabricBlockSettings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(-1.0F,3600000.0F)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(5)
                    .nonOpaque()), "Frozen Soul Block");
    public static final Entry MONARCHS_CROWN = entry("monarchs_crown", new MonarchsCrownBlock(
            FabricBlockSettings.copyOf(Blocks.BLUE_ICE)), "Monarch's Crown", new ModelType.Custom());

    public static final Entry FROZEN_CHAIN = entry("frozen_chain", new FrozenChainBlock(
            FabricBlockSettings.copyOf(Blocks.CHAIN).nonOpaque()), "Frozen Chain", new ModelType.Custom());
    public static final Entry SOULFROST_LANTERN = entry("soulfrost_lantern", new LanternBlock(
            FabricBlockSettings.copyOf(Blocks.SOUL_LANTERN).nonOpaque()), "Soulfrost Lantern", new ModelType.Custom());

    // Initialize FROST_BLOOM first without model type, then add POTTED variant, then set model type
    private static final Block FROST_BLOOM_BLOCK = new FrostbloomBlock(Effects.FROST_RESISTANCE.registryEntry, 5,
            FabricBlockSettings.copyOf(Blocks.ALLIUM).nonOpaque().noCollision());
    public static final Block POTTED_FROST_BLOOM = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "potted_frostbloom"),
            new FlowerPotBlock(FROST_BLOOM_BLOCK, FabricBlockSettings.copyOf(Blocks.POTTED_ALLIUM).nonOpaque()));
    public static final Entry FROST_BLOOM = entry("frostbloom", FROST_BLOOM_BLOCK, "Frostbloom",
            new ModelType.FlowerPot(POTTED_FROST_BLOOM, BlockStateModelGenerator.TintType.NOT_TINTED));

    public static final Entry FROZEN_TRIAL_SPAWNER = entry("frozen_trial_spawner", new TrialSpawnerBlock(
            FabricBlockSettings.copyOf(Blocks.TRIAL_SPAWNER).nonOpaque()), "Frozen Trial Spawner", new ModelType.Custom());
    public static final Entry FROZEN_VAULT = entry("frozen_vault", new VaultBlock(
            FabricBlockSettings.copyOf(Blocks.VAULT).nonOpaque()), "Frozen Vault", new ModelType.Custom());


    public static void register(){
        BlockEntityType.TRIAL_SPAWNER.addSupportedBlock(FROZEN_TRIAL_SPAWNER.block);
        BlockEntityType.VAULT.addSupportedBlock(FROZEN_VAULT.block);

        for (var entry : all) {
            Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, entry.name), entry.block);
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, entry.name), entry.item());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_BLOCK_KEY).register((content) -> {
            for (var entry : all) {
                content.add(entry.item());
            }
        });
    }
}

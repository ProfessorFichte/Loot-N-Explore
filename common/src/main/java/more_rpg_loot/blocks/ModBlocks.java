package more_rpg_loot.blocks;

import more_rpg_loot.platform.LNEEvents;

import more_rpg_loot.blocks.frozen_depths.FrostbloomBlock;
import more_rpg_loot.blocks.frozen_depths.FrozenAdventurerBlock;
import more_rpg_loot.blocks.frozen_depths.FrozenAdventurerBlockItem;
import more_rpg_loot.blocks.frozen_depths.FrozenBonesBlock;
import more_rpg_loot.blocks.frozen_depths.FrozenChainBlock;
import more_rpg_loot.blocks.frozen_depths.FrozenSoulBlock;
import more_rpg_loot.blocks.frozen_depths.FrozenSoulBlockItem;
import more_rpg_loot.blocks.frozen_depths.FrozenTorchBlock;
import more_rpg_loot.blocks.frozen_depths.FragileIceBlock;
import more_rpg_loot.blocks.frozen_depths.IcicleBarBlock;
import more_rpg_loot.blocks.frozen_depths.IcicleBlock;
import more_rpg_loot.blocks.frozen_depths.MonarchsCrownBlock;
import more_rpg_loot.blocks.frozen_depths.MonarchsCrownItem;
import more_rpg_loot.blocks.generic.HangingInnSignBlock;
import more_rpg_loot.blocks.generic.InnkeeperShelfBlock;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.item.Group;
import more_rpg_loot.mixin.BlockEntityTypeAccessor;
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

    private static Entry entry(String name, Block block, BlockItem item, String translation, ModelType modelType) {
        var entry = new Entry(name, block, item, translation, modelType);
        all.add(entry);
        return entry;
    }

    private static Entry monarchsCrownEntry() {
        MonarchsCrownBlock block = new MonarchsCrownBlock(AbstractBlock.Settings.copy(Blocks.BLUE_ICE));
        return entry("monarchs_crown", block, new MonarchsCrownItem(block, new Item.Settings()), "Monarch's Crown", new ModelType.Custom());
    }

    private static Entry frozenAdventurerEntry() {
        FrozenAdventurerBlock block = new FrozenAdventurerBlock(
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.LIGHT_BLUE_GRAY)
                        .strength(2.5F)
                        .sounds(BlockSoundGroup.STONE)
                        .nonOpaque());
        return entry("frozen_adventurer", block, new FrozenAdventurerBlockItem(block, new Item.Settings()), "Frozen Adventurer", new ModelType.Custom());
    }

    private static Entry frozenSoulsEntry() {
        FrozenSoulBlock block = new FrozenSoulBlock(
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.PALE_PURPLE)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(-1.0F, 3600000.0F)
                        .sounds(BlockSoundGroup.GLASS)
                        .luminance(state -> 5)
                        .nonOpaque());
        return entry("frozen_soul_block", block, new FrozenSoulBlockItem(block, new Item.Settings()), "Frozen Soul Block", new ModelType.CubeAll());
    }

    /// BASE MOD BLOCKS
    public static final Entry INNKEEPER_SHELF = entry("innkeeper_shelf", new InnkeeperShelfBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()), "Innkeeper Shelf", new ModelType.Custom());
    /// FROZEN DEPTHS 1.0 CONTENT BLOCKS
    public static final Entry BLUE_ICE_BRICKS = entry("blue_ice_bricks", new Block(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Blue Ice Bricks");
    public static final Entry CRACKED_BLUE_ICE_BRICKS = entry("cracked_blue_ice_bricks", new Block(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Cracked Blue Ice Bricks");
    public static final Entry SNOWY_BLUE_ICE_BRICKS = entry("snowy_blue_ice_bricks", new Block(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Snowy Blue Ice Bricks");
    public static final Entry POLISHED_ICE_BLOCK = entry("polished_ice_block", new Block(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Polished Ice Block");
    public static final Entry POLISHED_ICE_BRICKS = entry("polished_ice_bricks", new Block(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Polished Ice Bricks");
    public static final Entry BLUE_ICE_BRICK_SLAB = entry("blue_ice_brick_slab", new SlabBlock(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Blue Ice Brick Slab", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.SLAB));
    public static final Entry BLUE_ICE_BRICK_WALL = entry("blue_ice_brick_wall", new WallBlock(
            AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Blue Ice Brick Wall", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.WALL));
    public static final Entry BLUE_ICE_BRICK_STAIRS = entry("blue_ice_brick_stairs", new StairsBlock(
            ModBlocks.BLUE_ICE_BRICKS.block.getDefaultState(), AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Blue Ice Brick Stairs", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.STAIRS));
    public static final Entry FROZEN_SOULS = frozenSoulsEntry();
    public static final Entry MONARCHS_CROWN = monarchsCrownEntry();

    public static final Entry FROZEN_CHAIN = entry("frozen_chain", new FrozenChainBlock(
            AbstractBlock.Settings.copy(Blocks.CHAIN).nonOpaque()), "Frozen Chain", new ModelType.Custom());
    public static final Entry SOULFROST_LANTERN = entry("soulfrost_lantern", new LanternBlock(
            AbstractBlock.Settings.copy(Blocks.SOUL_LANTERN).nonOpaque()), "Soulfrost Lantern", new ModelType.Custom());

    // Initialize FROST_BLOOM first without model type, then add POTTED variant, then set model type
    private static final Block FROST_BLOOM_BLOCK = new FrostbloomBlock(Effects.FROST_RESISTANCE.registryEntry, 5,
            AbstractBlock.Settings.copy(Blocks.ALLIUM).nonOpaque().noCollision());
    public static final Block POTTED_FROST_BLOOM = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "potted_frostbloom"),
            new FlowerPotBlock(FROST_BLOOM_BLOCK, AbstractBlock.Settings.copy(Blocks.POTTED_ALLIUM).nonOpaque()));
    public static final Entry FROST_BLOOM = entry("frostbloom", FROST_BLOOM_BLOCK, "Frostbloom",
            new ModelType.FlowerPot(POTTED_FROST_BLOOM, BlockStateModelGenerator.TintType.NOT_TINTED));

    public static final Entry FROZEN_TRIAL_SPAWNER = entry("frozen_trial_spawner", new TrialSpawnerBlock(
            AbstractBlock.Settings.copy(Blocks.TRIAL_SPAWNER).nonOpaque()), "Frozen Trial Spawner", new ModelType.Custom());
    public static final Entry FROZEN_VAULT = entry("frozen_vault", new VaultBlock(
            AbstractBlock.Settings.copy(Blocks.VAULT).nonOpaque()), "Frozen Vault", new ModelType.Custom());

    public static final Entry FROZEN_ADVENTURER = frozenAdventurerEntry();
    public static final Entry FROZEN_BONES = entry("frozen_bones", new FrozenBonesBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .strength(1.5F)
                    .sounds(BlockSoundGroup.BONE)
                    .nonOpaque()), "Frozen Bones", new ModelType.Custom());
    public static final Entry FROZEN_TORCH = entry("frozen_torch", new FrozenTorchBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WOOD)
                    .luminance(state -> 10)
                    .nonOpaque()), "Frozen Torch", new ModelType.Custom());
    public static final Entry OAK_HANGING_INN_SIGN = entry("oak_hanging_inn_sign", new HangingInnSignBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()), "Oak Hanging Inn Sign", new ModelType.Custom());
    public static final Entry ACACIA_HANGING_INN_SIGN = entry("acacia_hanging_inn_sign", new HangingInnSignBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()), "Acacia Hanging Inn Sign", new ModelType.Custom());
    public static final Entry SPRUCE_HANGING_INN_SIGN = entry("spruce_hanging_inn_sign", new HangingInnSignBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()), "Spruce Hanging Inn Sign", new ModelType.Custom());
    public static final Entry ICICLE = entry("pointed_icicle", new IcicleBlock(
            AbstractBlock.Settings.copy(Blocks.POINTED_DRIPSTONE)
                    .ticksRandomly()
                    .nonOpaque()), "Icicle", new ModelType.Custom());
    public static final Entry ICICLE_BAR = entry("icicle_bar", new IcicleBarBlock(
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .nonOpaque()), "Icicle Bar", new ModelType.Custom());
    public static final Entry FRAGILE_ICE = entry("fragile_ice", new FragileIceBlock(
            AbstractBlock.Settings.copy(Blocks.ICE)), "Fragile Ice",
            new ModelType.ExistingTexture(Identifier.of("minecraft", "block/ice")));

    public static void register(){
        addBlockEntitySupport(BlockEntityType.TRIAL_SPAWNER, FROZEN_TRIAL_SPAWNER.block);
        addBlockEntitySupport(BlockEntityType.VAULT, FROZEN_VAULT.block);

        for (var entry : all) {
            Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, entry.name), entry.block);
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, entry.name), entry.item());
        }
        java.util.Set<String> generic = java.util.Set.of(
                "innkeeper_shelf", "oak_hanging_inn_sign", "spruce_hanging_inn_sign", "acacia_hanging_inn_sign");
        LNEEvents.get().modifyItemGroup(Group.RPG_GENERIC_KEY, (content) -> {
            for (var entry : all) {
                if (generic.contains(entry.name)) content.add(entry.item());
            }
        });
        LNEEvents.get().modifyItemGroup(Group.RPG_FROZEN_DEPTHS_KEY, (content) -> {
            for (var entry : all) {
                if (!generic.contains(entry.name)) content.add(entry.item());
            }
        });
    }

    private static void addBlockEntitySupport(BlockEntityType<?> type, Block block) {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) type;
        java.util.HashSet<Block> blocks = new java.util.HashSet<>(accessor.lne$getBlocks());
        blocks.add(block);
        accessor.lne$setBlocks(blocks);
    }
}

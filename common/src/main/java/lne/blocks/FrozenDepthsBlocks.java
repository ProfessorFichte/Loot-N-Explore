package lne.blocks;

import lne.blocks.frozen_depths.FrostbloomBlock;
import lne.blocks.frozen_depths.FrozenDecoratedPotBlock;
import lne.blocks.frozen_depths.FrozenKingdomFlagBlock;
import lne.blocks.generic.SuspiciousBlock;
import lne.blocks.generic.VariantBookshelfBlock;
import lne.blocks.frozen_depths.FrostflameBlock;
import lne.blocks.frozen_depths.FrozenAdventurerBlock;
import lne.blocks.frozen_depths.FrozenAdventurerBlockItem;
import lne.blocks.frozen_depths.FrozenBonesBlock;
import lne.blocks.frozen_depths.FrozenChainBlock;
import lne.blocks.frozen_depths.FrozenSoulBlock;
import lne.blocks.frozen_depths.FrozenSoulBlockItem;
import lne.blocks.frozen_depths.FrozenTorchBlock;
import lne.blocks.frozen_depths.FragileIceBlock;
import lne.blocks.frozen_depths.IcicleBarBlock;
import lne.blocks.frozen_depths.IcicleBlock;
import lne.blocks.frozen_depths.MonarchsCrownBlock;
import lne.blocks.frozen_depths.MonarchsCrownItem;
import lne.blocks.themed.ParticleThemes;
import lne.blocks.themed.ThemedTrialSpawnerBlock;
import lne.blocks.themed.ThemedVaultBlock;
import lne.effects.Effects;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.entity.Sherds;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import lne.blocks.ModBlocks.Entry;
import static lne.LneMod.MOD_ID;
import static lne.blocks.ModBlocks.entry;

public class FrozenDepthsBlocks {
    private static Entry monarchsCrownEntry() {
        MonarchsCrownBlock block = new MonarchsCrownBlock(AbstractBlock.Settings.copy(Blocks.BLUE_ICE));
        return entry("monarchs_crown", block, new MonarchsCrownItem(block, new Item.Settings()), "Monarch's Crown", new ModelType.Custom());
    }

    private static Entry frozenAdventurerEntry() {
        FrozenAdventurerBlock block = new FrozenAdventurerBlock(
                AbstractBlock.Settings.create()
                        .mapColor(MapColor.LIGHT_BLUE_GRAY)
                        .strength(2.5F)
                        .sounds(BlockSoundGroup.GLASS)
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
            BLUE_ICE_BRICKS.block().getDefaultState(), AbstractBlock.Settings.copy(Blocks.BLUE_ICE)), "Blue Ice Brick Stairs", new ModelType.PoolVariant(null, ModelType.PoolVariant.VariantType.STAIRS));
    public static final Entry CRYOTIC_BLOCK = entry("cryotic_block", new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.CHIME)
                    .strength(3.0F, 6.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.GLASS)), "Cryotic Block");
    public static final Block FROSTFLAME = new FrostflameBlock(
            AbstractBlock.Settings.copy(Blocks.SOUL_FIRE).mapColor(MapColor.LIGHT_BLUE));
    public static final Entry FROZEN_SOULS = frozenSoulsEntry();
    public static final Entry MONARCHS_CROWN = monarchsCrownEntry();

    public static final Entry FROZEN_CHAIN = entry("frozen_chain", new FrozenChainBlock(
            AbstractBlock.Settings.copy(Blocks.CHAIN).nonOpaque()), "Frozen Chain", new ModelType.Custom());
    public static final Entry SOULFROST_LANTERN = entry("soulfrost_lantern", new LanternBlock(
            AbstractBlock.Settings.copy(Blocks.SOUL_LANTERN).nonOpaque()), "Soulfrost Lantern", new ModelType.Custom());

    private static final Block FROST_BLOOM_BLOCK = new FrostbloomBlock(Effects.FROST_RESISTANCE.registryEntry, 5,
            AbstractBlock.Settings.copy(Blocks.ALLIUM).nonOpaque().noCollision());
    public static final Block POTTED_FROST_BLOOM = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "potted_frostbloom"),
            new FlowerPotBlock(FROST_BLOOM_BLOCK, AbstractBlock.Settings.copy(Blocks.POTTED_ALLIUM).nonOpaque()));
    public static final Entry FROST_BLOOM = entry("frostbloom", FROST_BLOOM_BLOCK, "Frostbloom",
            new ModelType.FlowerPot(POTTED_FROST_BLOOM, BlockStateModelGenerator.TintType.NOT_TINTED));

    public static final Entry FROZEN_TRIAL_SPAWNER = entry("frozen_trial_spawner", new ThemedTrialSpawnerBlock(
            AbstractBlock.Settings.copy(Blocks.TRIAL_SPAWNER).nonOpaque(), ParticleThemes.FROZEN), "Frozen Trial Spawner", new ModelType.Custom());
    public static final Entry FROZEN_VAULT = entry("frozen_vault", new ThemedVaultBlock(
            AbstractBlock.Settings.copy(Blocks.VAULT).nonOpaque(), ParticleThemes.FROZEN), "Frozen Vault", new ModelType.Custom());

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
    public static final Entry ICICLE = entry("pointed_icicle", new IcicleBlock(
            AbstractBlock.Settings.copy(Blocks.POINTED_DRIPSTONE)
                    .sounds(BlockSoundGroup.GLASS)
                    .ticksRandomly()
                    .nonOpaque()), "Icicle", new ModelType.Custom());
    public static final Entry ICICLE_BAR = entry("icicle_bar", new IcicleBarBlock(
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()), "Icicle Bar", new ModelType.Custom());
    public static final Entry FRAGILE_ICE = entry("fragile_ice", new FragileIceBlock(
            AbstractBlock.Settings.copy(Blocks.ICE)), "Fragile Ice",
            new ModelType.ExistingTexture(Identifier.of("minecraft", "block/ice")));

    public static final Entry DEEPSLATE_BOOKSHELF = entry("deepslate_bookshelf", new VariantBookshelfBlock(
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES).strength(2.5F, 6.0F)), "Deepslate Bookshelf",
            new ModelType.ColumnVariants(VariantBookshelfBlock.VARIANT, "frozen_depths/", null));
    public static final Entry ICY_DEEPSLATE_BOOKSHELF = entry("icy_deepslate_bookshelf", new Block(
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES).strength(2.5F, 6.0F)), "Icy Deepslate Bookshelf",
            new ModelType.Column("frozen_depths/", null));
    public static final Entry SUSPICIOUS_DEEPSLATE_BOOKSHELF = entry("suspicious_deepslate_bookshelf", new SuspiciousBlock.Sideways(
            DEEPSLATE_BOOKSHELF.block(), SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, SoundEvents.BLOCK_CHISELED_BOOKSHELF_PICKUP,
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES).strength(0.75F).pistonBehavior(PistonBehavior.DESTROY)),
            "Suspicious Deepslate Bookshelf", new ModelType.Column("frozen_depths/",
            Identifier.of(MOD_ID, "block/frozen_depths/deepslate_bookshelf_top")));
    public static final Entry FROZEN_DECORATED_POT = frozenDecoratedPotEntry();
    public static final Entry FROZEN_KINGDOM_FLAG = entry("frozen_kingdom_flag", new FrozenKingdomFlagBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .noCollision()
                    .nonOpaque()), "Frozen Kingdom Flag", new ModelType.Custom());

    private static Entry frozenDecoratedPotEntry() {
        FrozenDecoratedPotBlock block = new FrozenDecoratedPotBlock(AbstractBlock.Settings.copy(Blocks.DECORATED_POT));
        return entry("frozen_decorated_pot", block, new BlockItem(block, new Item.Settings().component(DataComponentTypes.POT_DECORATIONS, Sherds.DEFAULT)),
                "Frozen Decorated Pot", new ModelType.Custom());
    }

    public static void register() {
        Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "frostflame"), FROSTFLAME);
    }
}

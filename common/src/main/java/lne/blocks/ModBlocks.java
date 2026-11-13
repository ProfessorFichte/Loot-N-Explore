package lne.blocks;

import lne.platform.LNEEvents;

import lne.blocks.frozen_depths.FrozenDecoratedPotBlock;
import lne.blocks.generic.HangingInnSignBlock;
import lne.blocks.generic.SuspiciousBlock;
import lne.blocks.themed.ThemedTrialSpawnerBlock;
import lne.blocks.themed.ThemedVaultBlock;
import lne.blocks.generic.InnkeeperShelfBlock;
import lne.item.Group;
import lne.mixin.BlockEntityTypeAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static lne.LneMod.MOD_ID;

public class ModBlocks {
    public record Entry(String name, Block block, BlockItem item, String translation, ModelType modelType) {
        public Entry(String name, Block block, String translation, ModelType modelType) {
            this(name, block, new BlockItem(block, new Item.Settings()), translation, modelType);
        }

        public Entry(String name, Block block, String translation) {
            this(name, block, translation, new ModelType.CubeAll());
        }
    }
    public static final ArrayList<Entry> all = new ArrayList<>();
    public static Entry entry(String name, Block block, String translation) {
        var entry = new Entry(name, block, translation);
        all.add(entry);
        return entry;
    }

    public static Entry entry(String name, Block block, String translation, ModelType modelType) {
        var entry = new Entry(name, block, translation, modelType);
        all.add(entry);
        return entry;
    }

    public static Entry entry(String name, Block block, BlockItem item, String translation, ModelType modelType) {
        var entry = new Entry(name, block, item, translation, modelType);
        all.add(entry);
        return entry;
    }

    public static final Entry INNKEEPER_SHELF = entry("innkeeper_shelf", new InnkeeperShelfBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()), "Innkeeper Shelf", new ModelType.Custom());
    public static final Entry OAK_HANGING_INN_SIGN = entry("oak_hanging_inn_sign", new HangingInnSignBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.HANGING_SIGN)
                    .nonOpaque()), "Oak Hanging Inn Sign", new ModelType.Custom());
    public static final Entry ACACIA_HANGING_INN_SIGN = entry("acacia_hanging_inn_sign", new HangingInnSignBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.HANGING_SIGN)
                    .nonOpaque()), "Acacia Hanging Inn Sign", new ModelType.Custom());
    public static final Entry SPRUCE_HANGING_INN_SIGN = entry("spruce_hanging_inn_sign", new HangingInnSignBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.HANGING_SIGN)
                    .nonOpaque()), "Spruce Hanging Inn Sign", new ModelType.Custom());

    public static final Entry SUSPICIOUS_BOOKSHELF = entry("suspicious_bookshelf", new SuspiciousBlock.Sideways(Blocks.BOOKSHELF,
            SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, SoundEvents.BLOCK_CHISELED_BOOKSHELF_PICKUP,
            AbstractBlock.Settings.copy(Blocks.BOOKSHELF).strength(0.5F).pistonBehavior(PistonBehavior.DESTROY)),
            "Suspicious Bookshelf", new ModelType.Column("", Identifier.ofVanilla("block/oak_planks")));

    public static void register(){
        FrozenDepthsBlocks.register();

        for (var entry : all) {
            if (entry.block instanceof ThemedTrialSpawnerBlock) addBlockEntitySupport(BlockEntityType.TRIAL_SPAWNER, entry.block);
            if (entry.block instanceof ThemedVaultBlock) addBlockEntitySupport(BlockEntityType.VAULT, entry.block);
            if (entry.block instanceof SuspiciousBlock) addBlockEntitySupport(BlockEntityType.BRUSHABLE_BLOCK, entry.block);
            if (entry.block instanceof FrozenDecoratedPotBlock) addBlockEntitySupport(BlockEntityType.DECORATED_POT, entry.block);
        }

        for (var entry : all) {
            Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, entry.name), entry.block);
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, entry.name), entry.item());
        }
        java.util.Set<String> generic = java.util.Set.of(
                "innkeeper_shelf", "oak_hanging_inn_sign", "spruce_hanging_inn_sign", "acacia_hanging_inn_sign", "suspicious_bookshelf",
                "deepslate_bookshelf", "suspicious_deepslate_bookshelf");
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

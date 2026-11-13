package lne.datagen;

import com.mrpg_lib.compat.spell_engine.loot.SpecificSpellScrollPoolLootFunction;
import com.mrpg_lib.compat.spell_engine.loot.SpellsAvailableLootCondition;
import com.mrpg_lib.util.loot.ConditionalItemEntry;
import lne.LneMod;
import lne.blocks.FrozenDepthsBlocks;
import lne.item.CommonItems;
import lne.util.LneLootTables;
import lne.worldgen.structures.StructureTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.ExplorationMapLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModArchaeologyLootTableProvider extends SimpleFabricLootTableProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;

    public ModArchaeologyLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ARCHAEOLOGY);
        this.registryLookup = registryLookup;
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        exporter.accept(LneLootTables.ARCHAEOLOGY_BOOKSHELF, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.BOOK).weight(4)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))
                        .with(ItemEntry.builder(Items.PAPER).weight(4)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))
                        .with(ItemEntry.builder(Items.WRITABLE_BOOK).weight(2))
                        .with(ItemEntry.builder(Items.INK_SAC).weight(2))));
        exporter.accept(LneLootTables.ARCHAEOLOGY_INN, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(counted(Items.BOOK, 6, 1, 2))
                        .with(counted(Items.PAPER, 5, 1, 3))
                        .with(ItemEntry.builder(Items.WRITABLE_BOOK).weight(3))
                        .with(enchantedBook(1, 5, 10))));
        RegistryWrapper.Impl<Enchantment> enchantments = registryLookup.join().getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        exporter.accept(LneLootTables.ARCHAEOLOGY_STRONGHOLD_LIBRARY, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.BOOK).weight(1)
                                .apply(new SetEnchantmentsLootFunction.Builder()
                                        .enchantment(enchantments.getOrThrow(Enchantments.MENDING), ConstantLootNumberProvider.create(1))))
                        .with(ItemEntry.builder(Items.BOOK).weight(7)
                                .apply(EnchantWithLevelsLootFunction.builder(registryLookup.join(), ConstantLootNumberProvider.create(30))))
                        .with(ConditionalItemEntry.builder(Identifier.of("spell_engine", "spell_scroll"), Optional.empty()).weight(10)
                                .conditionally(SpellsAvailableLootCondition.builder(List.of(), 3, 4))
                                .apply(SpecificSpellScrollPoolLootFunction.builder(List.of(), ConstantLootNumberProvider.create(3),
                                        ConstantLootNumberProvider.create(4), ConstantLootNumberProvider.create(1), List.of())))
                        .with(ItemEntry.builder(Items.BOOK).weight(2))));
        exporter.accept(LneLootTables.ARCHAEOLOGY_WOODLAND_MANSION, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.TOTEM_OF_UNDYING).weight(5))
                        .with(enchantedBook(15, 15, 25))
                        .with(spellScroll(40, 3))
                        .with(ItemEntry.builder(Items.BOOK).weight(40))));
        exporter.accept(LneLootTables.ARCHAEOLOGY_SNOWY_VILLAGE, villageLibrary(StructureTags.ON_FROST_TOWER_MAPS, "frost_tower"));
        exporter.accept(LneLootTables.ARCHAEOLOGY_DESERT_VILLAGE, villageLibrary(StructureTags.ON_DESERT_PYRAMID_MAPS, "desert_pyramid"));
        exporter.accept(LneLootTables.ARCHAEOLOGY_PLAINS_SAVANNA_TAIGA_VILLAGE, villageLibrary(StructureTags.ON_PILLAGER_OUTPOST_MAPS, "pillager_outpost"));

        exporter.accept(frozenDepths("frost_tower"), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(counted(CommonItems.CRYO_ESSENCE.item(), 4, 1, 2))
                .with(counted(Items.IRON_NUGGET, 4, 2, 5))
                .with(counted(Items.BONE, 3, 1, 3))
                .with(counted(CommonItems.FROSTBALL.item(), 2, 1, 2))
                .with(counted(FrozenDepthsBlocks.FROZEN_CHAIN.block(), 2, 1, 1))
                .with(counted(Items.EMERALD, 2, 1, 2))));
        exporter.accept(frozenDepths("glacial_tomb"), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(counted(Items.BONE, 4, 1, 3))
                .with(counted(CommonItems.CRYO_ESSENCE.item(), 4, 1, 3))
                .with(counted(Items.GOLD_NUGGET, 3, 2, 5))
                .with(counted(FrozenDepthsBlocks.FROZEN_TORCH.block(), 2, 1, 2))
                .with(counted(Items.EMERALD, 2, 1, 3))
                .with(counted(Items.DIAMOND, 1, 1, 1))));
        exporter.accept(frozenDepths("outpost"), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(counted(Items.PAPER, 4, 1, 3))
                .with(counted(Items.BONE, 3, 1, 2))
                .with(counted(CommonItems.CRYO_ESSENCE.item(), 3, 1, 1))
                .with(counted(Items.IRON_NUGGET, 3, 2, 4))
                .with(counted(CommonItems.FROSTBALL.item(), 2, 1, 2))
                .with(counted(Items.EMERALD, 1, 1, 1))));
        exporter.accept(frozenDepths("monarchs_castle"), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(counted(CommonItems.CRYO_ESSENCE.item(), 3, 1, 3))
                .with(counted(Items.GOLD_NUGGET, 3, 2, 6))
                .with(counted(Items.EMERALD, 3, 1, 3))
                .with(counted(Items.IRON_INGOT, 2, 1, 2))
                .with(counted(FrozenDepthsBlocks.FROZEN_CHAIN.block(), 2, 1, 1))
                .with(counted(Items.GOLDEN_APPLE, 1, 1, 1))
                .with(counted(Items.DIAMOND, 1, 1, 1))));
        exporter.accept(frozenDepths("monarchs_temple"), LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(counted(CommonItems.CRYO_ESSENCE.item(), 4, 1, 3))
                .with(counted(Items.GOLD_NUGGET, 4, 3, 7))
                .with(counted(CommonItems.FROSTBALL.item(), 3, 1, 2))
                .with(counted(Items.BLUE_ICE, 2, 1, 2))
                .with(counted(Items.GOLD_INGOT, 2, 1, 2))
                .with(counted(Items.EMERALD, 2, 1, 3))
                .with(counted(FrozenDepthsBlocks.FROZEN_CHAIN.block(), 1, 1, 2))
                .with(counted(Items.GOLDEN_APPLE, 1, 1, 1))
                .with(counted(Items.DIAMOND, 1, 1, 1))));
    }

    private LootTable.Builder villageLibrary(TagKey<Structure> mapDestination, String mapName) {
        return LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(enchantedBook(10, 5, 15))
                .with(ItemEntry.builder(Items.MAP).weight(20)
                        .apply(ExplorationMapLootFunction.builder()
                                .withDestination(mapDestination)
                                .withDecoration(MapDecorationTypes.RED_X))
                        .apply(SetNameLootFunction.builder(Text.translatable("filled_map." + LneMod.MOD_ID + "." + mapName),
                                SetNameLootFunction.Target.ITEM_NAME)))
                .with(spellScroll(40, 2))
                .with(ItemEntry.builder(Items.BOOK).weight(30)));
    }

    private LeafEntry.Builder<?> enchantedBook(int weight, int minLevels, int maxLevels) {
        return ItemEntry.builder(Items.BOOK).weight(weight)
                .apply(EnchantWithLevelsLootFunction.builder(registryLookup.join(), UniformLootNumberProvider.create(minLevels, maxLevels)));
    }

    private static LeafEntry.Builder<?> spellScroll(int weight, int tier) {
        return ConditionalItemEntry.builder(Identifier.of("spell_engine", "spell_scroll"), Optional.empty()).weight(weight)
                .conditionally(SpellsAvailableLootCondition.builder(List.of(), tier, tier))
                .apply(SpecificSpellScrollPoolLootFunction.builder(List.of(), ConstantLootNumberProvider.create(tier),
                        ConstantLootNumberProvider.create(tier), ConstantLootNumberProvider.create(1), List.of()));
    }

    private static RegistryKey<LootTable> frozenDepths(String structure) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(LneMod.MOD_ID, "archaeology/frozen_depths/" + structure));
    }

    private static ItemEntry.Builder<?> counted(ItemConvertible item, int weight, int min, int max) {
        var entry = ItemEntry.builder(item).weight(weight);
        if (max > 1) {
            entry.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)));
        }
        return entry;
    }
}

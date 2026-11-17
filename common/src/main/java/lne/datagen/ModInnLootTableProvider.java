package lne.datagen;

import lne.item.CommonItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static lne.LneMod.MOD_ID;

public class ModInnLootTableProvider extends SimpleFabricLootTableProvider {

    public ModInnLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.CHEST);
    }

    @Override
    public String getName() {
        return "Inn Loot Tables";
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        exporter.accept(key("chests/inns/desert"), inn(Items.CACTUS, Items.DEAD_BUSH, true));
        exporter.accept(key("chests/inns/plains"), inn(Items.COBBLESTONE, Items.OAK_SAPLING, true));
        exporter.accept(key("chests/villages/desert/small_inn"), inn(Items.CACTUS, Items.DEAD_BUSH, false));
        exporter.accept(key("chests/villages/plains/small_inn"), inn(Items.OAK_PLANKS, Items.OAK_SAPLING, false));
        exporter.accept(key("chests/villages/savanna/small_inn"), inn(Items.ACACIA_LOG, Items.ACACIA_SAPLING, false));
        exporter.accept(key("chests/villages/snowy/small_inn"), inn(Items.SNOW, Items.ICE, false));
        exporter.accept(key("chests/villages/taiga/small_inn"), inn(Items.SPRUCE_PLANKS, Items.SPRUCE_SAPLING, false));
    }

    private static RegistryKey<LootTable> key(String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, path));
    }

    private static LootTable.Builder inn(ItemConvertible building, ItemConvertible plant, boolean overworldInn) {
        LootPool.Builder main = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(3, 8))
                .with(item(Items.BOWL, 3, 1, 3))
                .with(item(Items.GLASS_BOTTLE, 3, 1, 3))
                .with(item(building, 10, 1, 4))
                .with(item(Items.BREAD, 10, 1, 7))
                .with(ItemEntry.builder(Items.BOOK))
                .with(item(plant, 2, 1, 3));
        if (overworldInn) {
            main.with(item(Items.IRON_INGOT, 5, 2, 4))
                    .with(item(Items.EMERALD, 1, 2, 6));
        } else {
            main.with(item(Items.EMERALD, 1, 1, 3));
        }
        return LootTable.builder()
                .pool(main)
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(item(Items.PAPER, 1, 1, 4)))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05F))
                        .with(ItemEntry.builder(CommonItems.JOLLY_TIME_DISC.item())));
    }

    private static LeafEntry.Builder<?> item(ItemConvertible item, int weight, int min, int max) {
        return ItemEntry.builder(item).weight(weight)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)));
    }
}

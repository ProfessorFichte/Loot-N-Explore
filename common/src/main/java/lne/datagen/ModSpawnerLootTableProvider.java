package lne.datagen;

import lne.item.CommonItems;
import lne.item.ModPotions;
import lne.util.LneLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModSpawnerLootTableProvider extends SimpleFabricLootTableProvider {
    public ModSpawnerLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.EMPTY);
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        exporter.accept(LneLootTables.FROZEN_KEY, LootTable.builder().pool(single(CommonItems.FROZEN_KEY.item())));
        exporter.accept(LneLootTables.FROZEN_OMINOUS_KEY, LootTable.builder().pool(single(CommonItems.MONARCHS_KEY.item())));
        exporter.accept(LneLootTables.FROZEN_TRAPS_ALL, LootTable.builder().pool(frostballs()).pool(slownessArrows()).pool(freezingClouds()));
        exporter.accept(LneLootTables.FROZEN_TRAPS_FROSTBALLS, LootTable.builder().pool(frostballs()));
        exporter.accept(LneLootTables.FROZEN_TRAPS_FROSTBALLS_ARROWS, LootTable.builder().pool(frostballs()).pool(slownessArrows()));
        exporter.accept(LneLootTables.FROZEN_TRAPS_FREEZING_CLOUDS, LootTable.builder().pool(freezingClouds()));
    }

    private static LootPool.Builder single(Item item) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(item));
    }

    private static LootPool.Builder frostballs() {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(CommonItems.FROSTBALL.item())
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4))));
    }

    private static LootPool.Builder slownessArrows() {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(Items.TIPPED_ARROW)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2)))
                        .apply(SetComponentsLootFunction.builder(DataComponentTypes.POTION_CONTENTS,
                                new PotionContentsComponent(Potions.SLOWNESS))));
    }

    private static LootPool.Builder freezingClouds() {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(Items.LINGERING_POTION)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2)))
                        .apply(SetComponentsLootFunction.builder(DataComponentTypes.POTION_CONTENTS,
                                new PotionContentsComponent(ModPotions.FROSTED_POTION.potion()))));
    }
}

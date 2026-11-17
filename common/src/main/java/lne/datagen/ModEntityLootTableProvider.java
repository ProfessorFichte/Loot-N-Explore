package lne.datagen;

import lne.blocks.FrozenDepthsBlocks;
import lne.entity.ModEntities;
import lne.item.CommonItems;
import lne.util.LneLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEntityLootTableProvider extends SimpleFabricLootTableProvider {
    private static final float WEAPON_TROPHY_CHANCE = 0.05F;

    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;

    public ModEntityLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ENTITY);
        this.registryLookup = registryLookup;
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        RegistryWrapper.WrapperLookup lookup = registryLookup.join();
        Item bow = CommonItems.FROZEN_BOW.item();
        Item lance = CommonItems.GUARDS_FROST_LANCE.item();
        add(exporter, ModEntities.FROST_HOUND, cryoEssence(lookup, 0, 1));
        add(exporter, ModEntities.FROZEN_MAGICIAN, cryoEssence(lookup, 0, 2));
        add(exporter, ModEntities.GENERAL_FROZEN_MAGICIAN, cryoEssence(lookup, 1, 3));
        add(exporter, ModEntities.FROZEN_RANGER, cryoEssence(lookup, 0, 2).pool(weaponTrophy(bow)));
        add(exporter, ModEntities.GENERAL_FROZEN_RANGER, cryoEssence(lookup, 1, 3).pool(weaponTrophy(bow)));
        add(exporter, ModEntities.MONARCHS_SOLDIER, cryoEssence(lookup, 0, 2));
        add(exporter, ModEntities.GENERAL_MONARCHS_SOLDIER, cryoEssence(lookup, 1, 3));
        add(exporter, ModEntities.MONARCHS_GUARD, cryoEssence(lookup, 1, 2).pool(weaponTrophy(lance)));
        add(exporter, ModEntities.GENERAL_MONARCHS_GUARD, cryoEssence(lookup, 1, 3).pool(weaponTrophy(lance)));
        add(exporter, ModEntities.GLAZE, playerKillDrop(CommonItems.GLAZE_ROD.item()));
        add(exporter, ModEntities.FROST_MONARCH, playerKillDrop(FrozenDepthsBlocks.MONARCHS_CROWN.item()));
        add(exporter, ModEntities.FROST_HAUNTED, LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .with(ItemEntry.builder(Items.BONE)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))
                        .with(ItemEntry.builder(Items.SNOWBALL)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0, 3)))))
                .pool(cryoEssencePool(lookup, 1, 2))
                .pool(weaponTrophy(CommonItems.FROST_HAUNTS_AXE.item())));
    }

    private static void add(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter, EntityType<?> type, LootTable.Builder table) {
        exporter.accept(LneLootTables.frozenDepthsEntity(type), table);
    }

    private static LootTable.Builder playerKillDrop(Item item) {
        return LootTable.builder().pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(KilledByPlayerLootCondition.builder())
                .with(ItemEntry.builder(item)));
    }

    private static LootTable.Builder cryoEssence(RegistryWrapper.WrapperLookup lookup, int min, int max) {
        return LootTable.builder().pool(cryoEssencePool(lookup, min, max));
    }

    private static LootPool.Builder cryoEssencePool(RegistryWrapper.WrapperLookup lookup, int min, int max) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(KilledByPlayerLootCondition.builder())
                .with(ItemEntry.builder(CommonItems.CRYO_ESSENCE.item())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)))
                        .apply(EnchantedCountIncreaseLootFunction.builder(lookup, UniformLootNumberProvider.create(0, 1))));
    }

    private static LootPool.Builder weaponTrophy(Item weapon) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(KilledByPlayerLootCondition.builder())
                .conditionally(RandomChanceLootCondition.builder(WEAPON_TROPHY_CHANCE))
                .with(ItemEntry.builder(weapon));
    }
}

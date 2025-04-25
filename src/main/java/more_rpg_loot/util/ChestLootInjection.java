package more_rpg_loot.util;

import more_rpg_loot.item.SmithingTemplates;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class ChestLootInjection {
    private static final Identifier UNDER_WATER_RUIN =
            Identifier.of("minecraft", "chests/underwater_ruin_big");
    private static final Identifier NETHER_BRIDGE =
            Identifier.of("minecraft", "chests/nether_bridge");
    private static final Identifier END_CITY_TREASURE =
            Identifier.of("minecraft", "chests/end_city_treasure");
    private static final Identifier GLACIAL_TOMB =
            Identifier.of("loot_n_explore", "chests/dungeons/glacial_tomb/common");


    private static final float smithing_template_drop_chance = 0.2f;

    public static void modifyChestLootTables(){

        net.fabricmc.fabric.api.loot.v3.LootTableEvents.MODIFY.register(new net.fabricmc.fabric.api.loot.v3.LootTableEvents.Modify() {
            @Override
            public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
                if(source.isBuiltin() && UNDER_WATER_RUIN.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                            .with(ItemEntry.builder(SmithingTemplates.ELDER_GUARDIAN_UPGRADE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if(source.isBuiltin() && NETHER_BRIDGE.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                            .with(ItemEntry.builder(SmithingTemplates.WITHER_UPGRADE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if(source.isBuiltin() && END_CITY_TREASURE.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                            .with(ItemEntry.builder(SmithingTemplates.ENDER_DRAGON_UPGRADE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if(source.isBuiltin() && GLACIAL_TOMB.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                            .with(ItemEntry.builder(SmithingTemplates.FROSTMONARCH_UPGRADE))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }
        });
    }
}


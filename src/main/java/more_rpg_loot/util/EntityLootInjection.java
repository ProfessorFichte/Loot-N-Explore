package more_rpg_loot.util;

import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.CommonItems;
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

public class EntityLootInjection {
    private static final Identifier ELDER_GUARDIAN_ID =
            Identifier.of("minecraft", "entities/elder_guardian");
    private static final Identifier ENDER_DRAGON_ID =
            Identifier.of("minecraft", "entities/ender_dragon");


    public static void modifyLootEntityTables(){

        net.fabricmc.fabric.api.loot.v3.LootTableEvents.MODIFY.register(new net.fabricmc.fabric.api.loot.v3.LootTableEvents.Modify() {
            @Override
            public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
                if(source.isBuiltin() && ELDER_GUARDIAN_ID.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(1.0F))
                            .with(ItemEntry.builder(LNE_Relics.ENDER_DRAGON_SCALES.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if(source.isBuiltin() && ENDER_DRAGON_ID.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(1.0F))
                            .with(ItemEntry.builder(LNE_Relics.ENDER_DRAGON_SCALES.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }
        });
    }
}

package more_rpg_loot.util;

import more_rpg_loot.item.SmithingTemplates;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ChestLootInjection {
    private static final Identifier UNDER_WATER_RUIN =
            new Identifier("minecraft", "chests/underwater_ruin_big");
    private static final Identifier NETHER_BRIDGE =
            new Identifier("minecraft", "chests/nether_bridge");
    private static final Identifier END_CITY_TREASURE =
            new Identifier("minecraft", "chests/end_city_treasure");


    private static final float smithing_template_drop_chance = 0.2f;

    public static void modifyChestLootTables(){
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) ->{
            if(UNDER_WATER_RUIN.equals(id)){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                        .with(ItemEntry.builder(SmithingTemplates.ELDER_GUARDIAN_UPGRADE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if(NETHER_BRIDGE.equals(id)){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                        .with(ItemEntry.builder(SmithingTemplates.WITHER_UPGRADE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if(END_CITY_TREASURE.equals(id)){
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(smithing_template_drop_chance))
                        .with(ItemEntry.builder(SmithingTemplates.ENDER_DRAGON_UPGRADE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}

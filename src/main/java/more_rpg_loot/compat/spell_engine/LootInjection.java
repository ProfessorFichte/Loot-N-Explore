package more_rpg_loot.compat.spell_engine;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.ModEntities;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class LootInjection {
    private static final Identifier UNDER_WATER_RUIN_BIG_ID = Identifier.of("minecraft", "underwater_ruin_big");
    public static final RegistryKey<LootTable> UNDER_WATER_RUIN_BIG =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, UNDER_WATER_RUIN_BIG_ID.withPrefixedPath("chests/"));
    private static final Identifier OCEAN_RUIN_WARM_BRUSH_ID = Identifier.of("minecraft", "ocean_ruin_warm");
    public static final RegistryKey<LootTable> OCEAN_RUIN_WARM_BRUSH =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, OCEAN_RUIN_WARM_BRUSH_ID.withPrefixedPath("archaeology/"));
    private static final Identifier SHIPWRECK_TREASURE_ID = Identifier.of("minecraft", "shipwreck_treasure");
    public static final RegistryKey<LootTable> SHIPWRECK_TREASURE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, SHIPWRECK_TREASURE_ID.withPrefixedPath("chests/"));
    private static final Identifier AMETHYST_CLUSTER_ID = Identifier.of("minecraft", "amethyst_cluster");
    public static final RegistryKey<LootTable> AMETHYST_CLUSTER =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, AMETHYST_CLUSTER_ID.withPrefixedPath("blocks/"));
    private static final Identifier END_CITY_TREASURE_ID = Identifier.of("minecraft", "end_city_treasure");
    public static final RegistryKey<LootTable> END_CITY_TREASURE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, END_CITY_TREASURE_ID.withPrefixedPath("chests/"));
    private static final Identifier IGLOO_ID = Identifier.of("minecraft", "igloo_chest");
    public static final RegistryKey<LootTable> IGLOO =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, IGLOO_ID.withPrefixedPath("chests/"));
    private static final Identifier GLAZE_TOWER_ID = Identifier.of("loot_n_explore", "glaze_tower");
    public static final RegistryKey<LootTable> GLAZE_TOWER =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, GLAZE_TOWER_ID.withPrefixedPath("chests/"));
    private static final Identifier FROZEN_TRIAL_REWARD_ID = Identifier.of("loot_n_explore", "reward");
    public static final RegistryKey<LootTable> FROZEN_TRIAL_REWARD =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, FROZEN_TRIAL_REWARD_ID.withPrefixedPath("chests/trials/frozen/"));
    private static final Identifier FROZEN_TRIAL_REWARD_OMINOUS_ID = Identifier.of("loot_n_explore", "reward_ominous");
    public static final RegistryKey<LootTable> FROZEN_TRIAL_REWARD_OMINOUS =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, FROZEN_TRIAL_REWARD_OMINOUS_ID.withPrefixedPath("chests/trials/frozen/"));
    private static final Identifier BASTION_TREASURE_ID = Identifier.of("minecraft", "bastion_treasure");
    public static final RegistryKey<LootTable> BASTION_TREASURE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, BASTION_TREASURE_ID.withPrefixedPath("chests/"));

    private static final float boss_drop = RPGLoot.tweaksConfig.value.boss_relic_dropchance;
    private static final float chest_drop = RPGLoot.tweaksConfig.value.chest_relic_dropchance;
    private static final float archaeology_drop = RPGLoot.tweaksConfig.value.archaeology_relic_dropchance;
    private static final float block_drop = RPGLoot.tweaksConfig.value.block_relic_dropchance;
    private static final float entity_drop = RPGLoot.tweaksConfig.value.entity_relic_dropchance;
    private static final float trial_drop = RPGLoot.tweaksConfig.value.trial_spawner_relic_dropchance;

    public static void modifyChestLootTables(){

        LootTableEvents.MODIFY.register(new LootTableEvents.Modify() {
            @Override
            public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
                ///BOSS DROPS
                if (source.isBuiltin() && EntityType.ENDER_DRAGON.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.ENDER_DRAGON_SCALES.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (source.isBuiltin() && EntityType.ELDER_GUARDIAN.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.ELDER_GUARDIAN_EYE.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (source.isBuiltin() && EntityType.WITHER.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.WITHER_SPINE.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                else if (source.isBuiltin() && ModEntities.FROST_MONARCH.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.FROZEN_SOUL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                ///CHEST INJECTION
                if(source.isBuiltin() && UNDER_WATER_RUIN_BIG.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.POSEIDONS_AMPHORA.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if(source.isBuiltin() && SHIPWRECK_TREASURE.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.AMPHITRITE_DIADEM.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && END_CITY_TREASURE.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.ENDER_DRAGON_TOOTH.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && IGLOO.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.ETERNAL_SNOWFLAKE.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && GLAZE_TOWER.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.GLACIER_SHARD.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && BASTION_TREASURE.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.WITHERED_OBSIDIAN_SHARD.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                ///ARCHAEOLOGY INJECTION
                if (source.isBuiltin() && OCEAN_RUIN_WARM_BRUSH.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(archaeology_drop))
                            .with(ItemEntry.builder(LNE_Relics.RAINBOW_CORAL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                ///BLOCK INJECTION
                if(source.isBuiltin() && AMETHYST_CLUSTER.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(block_drop))
                            .with(ItemEntry.builder(LNE_Relics.CHARGED_AMETHYST.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                ///ENTITY INJECTION
                if (source.isBuiltin() && EntityType.ENDERMAN.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(entity_drop))
                            .with(ItemEntry.builder(LNE_Relics.CORRUPTED_ENDER_PEARL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && EntityType.SKELETON.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(entity_drop))
                            .with(ItemEntry.builder(LNE_Relics.UNKNOWN_REMAINS.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && EntityType.WITHER_SKELETON.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(entity_drop))
                            .with(ItemEntry.builder(LNE_Relics.LOST_SOUL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                ///TRIAL SPAWNER INJECTION
                if (source.isBuiltin() && FROZEN_TRIAL_REWARD.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(trial_drop))
                            .with(ItemEntry.builder(LNE_Relics.FROZEN_RIB.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
                if (source.isBuiltin() && FROZEN_TRIAL_REWARD_OMINOUS.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(trial_drop))
                            .with(ItemEntry.builder(LNE_Relics.FROZEN_RIB.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }
        });
    }
}


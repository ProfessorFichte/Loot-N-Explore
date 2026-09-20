package more_rpg_loot.compat.spell_engine;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.spell_engine.PlatformEvents;

public class LootInjection {
    private static final Identifier UNDER_WATER_RUIN_BIG_ID = new Identifier("minecraft", "underwater_ruin_big");
    public static final Identifier UNDER_WATER_RUIN_BIG = UNDER_WATER_RUIN_BIG_ID.withPrefixedPath("chests/");
    private static final Identifier SHIPWRECK_TREASURE_ID = new Identifier("minecraft", "shipwreck_treasure");
    public static final Identifier SHIPWRECK_TREASURE = SHIPWRECK_TREASURE_ID.withPrefixedPath("chests/");
    private static final Identifier BURIED_TREASURE_ID = new Identifier("minecraft", "buried_treasure");
    public static final Identifier BURIED_TREASURE = BURIED_TREASURE_ID.withPrefixedPath("chests/");
    private static final Identifier AMETHYST_CLUSTER_ID = new Identifier("minecraft", "amethyst_cluster");
    public static final Identifier AMETHYST_CLUSTER = AMETHYST_CLUSTER_ID.withPrefixedPath("blocks/");
    private static final Identifier END_CITY_TREASURE_ID = new Identifier("minecraft", "end_city_treasure");
    public static final Identifier END_CITY_TREASURE = END_CITY_TREASURE_ID.withPrefixedPath("chests/");
    private static final Identifier IGLOO_ID = new Identifier("minecraft", "igloo_chest");
    public static final Identifier IGLOO = IGLOO_ID.withPrefixedPath("chests/");
    private static final Identifier GLAZE_TOWER_ID = new Identifier("loot_n_explore", "glaze_tower");
    public static final Identifier GLAZE_TOWER = GLAZE_TOWER_ID.withPrefixedPath("chests/");
    private static final Identifier GLACIAL_TOMB_COMMON_ID = new Identifier("loot_n_explore", "common");
    public static final Identifier GLACIAL_TOMB_COMMON = GLACIAL_TOMB_COMMON_ID.withPrefixedPath("chests/dungeons/glacial_tomb/");
    private static final Identifier BASTION_TREASURE_ID = new Identifier("minecraft", "bastion_treasure");
    public static final Identifier BASTION_TREASURE = BASTION_TREASURE_ID.withPrefixedPath("chests/");

    private static final float boss_drop = RPGLoot.tweaksConfig.value.boss_relic_dropchance;
    private static final float chest_drop = RPGLoot.tweaksConfig.value.chest_relic_dropchance;
    private static final float archaeology_drop = RPGLoot.tweaksConfig.value.archaeology_relic_dropchance;
    private static final float block_drop = RPGLoot.tweaksConfig.value.block_relic_dropchance;
    private static final float entity_drop = RPGLoot.tweaksConfig.value.entity_relic_dropchance;
    private static final float glaze_tower_drop = RPGLoot.tweaksConfig.value.glaze_tower_relic_dropchance;
    private static final float glacial_tomb_drop = RPGLoot.tweaksConfig.value.glacial_tomb_relic_dropchance;

    public static void modifyChestLootTables(){

        PlatformEvents.onLootTableModify(context -> {
            {
                Identifier key = context.tableId();
                ///BOSS DROPS
                if (EntityType.ENDER_DRAGON.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.ENDER_DRAGON_SCALES.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                else if (EntityType.ELDER_GUARDIAN.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.ELDER_GUARDIAN_EYE.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                else if (EntityType.WITHER.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.WITHER_SPINE.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                else if (ModEntities.FROST_MONARCH.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(boss_drop))
                            .with(ItemEntry.builder(LNE_Relics.FROZEN_SOUL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }

                ///CHEST INJECTION
                if(UNDER_WATER_RUIN_BIG.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.POSEIDONS_AMPHORA.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if(SHIPWRECK_TREASURE.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.AMPHITRITE_DIADEM.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if(BURIED_TREASURE.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.RAINBOW_CORAL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (END_CITY_TREASURE.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.ENDER_DRAGON_TOOTH.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (IGLOO.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.ETERNAL_SNOWFLAKE.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (BASTION_TREASURE.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(chest_drop))
                            .with(ItemEntry.builder(LNE_Relics.WITHERED_OBSIDIAN_SHARD.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                ///BLOCK INJECTION
                if(AMETHYST_CLUSTER.equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(block_drop))
                            .with(ItemEntry.builder(LNE_Relics.CHARGED_AMETHYST.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                ///ENTITY INJECTION
                if (EntityType.ENDERMAN.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(entity_drop))
                            .with(ItemEntry.builder(LNE_Relics.CORRUPTED_ENDER_PEARL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (EntityType.SKELETON.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(entity_drop))
                            .with(ItemEntry.builder(LNE_Relics.UNKNOWN_REMAINS.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (EntityType.WITHER_SKELETON.getLootTableId().equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(entity_drop))
                            .with(ItemEntry.builder(LNE_Relics.LOST_SOUL.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (GLAZE_TOWER.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(glaze_tower_drop))
                            .with(ItemEntry.builder(LNE_Relics.GLACIER_SHARD.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
                if (GLACIAL_TOMB_COMMON.equals(key)) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceLootCondition.builder(glacial_tomb_drop))
                            .with(ItemEntry.builder(LNE_Relics.FROZEN_RIB.item().get()))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                    context.addPool(poolBuilder.build());
                }
            }
        });
    }
}


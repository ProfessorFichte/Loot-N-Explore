package lne.datagen;

import lne.blocks.FrozenDepthsBlocks;
import lne.item.CommonItems;
import lne.item.ModPotions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static lne.LneMod.MOD_ID;

public class ModFrozenDepthsLootTableProvider extends SimpleFabricLootTableProvider {
    private static final String VAULTS = "vaults/frozen_depths/";
    private static final String SPAWNERS = "spawners/frozen_depths/";
    private static final String CHESTS = "chests/frozen_depths/";
    private static final String POTS = "pots/frozen_depths/";

    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
    private RegistryWrapper.WrapperLookup lookup;
    private RegistryWrapper.Impl<Enchantment> enchantments;

    public ModFrozenDepthsLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.CHEST);
        this.registryLookup = registryLookup;
    }

    private static RegistryKey<LootTable> key(String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, path));
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        lookup = registryLookup.join();
        enchantments = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

        for (int tier = 1; tier <= 3; tier++) {
            exporter.accept(key(VAULTS + "t" + tier + "/common"), single(common(tier, false)));
            exporter.accept(key(VAULTS + "t" + tier + "/rare"), single(rare(tier, false)));
            exporter.accept(key(SPAWNERS + "t" + tier + "/consumables"), single(consumables(tier, false)));
        }
        for (int tier = 2; tier <= 3; tier++) {
            exporter.accept(key(VAULTS + "t" + tier + "/ominous_common"), single(common(tier, true)));
            exporter.accept(key(VAULTS + "t" + tier + "/ominous_rare"), single(rare(tier, true)));
        }
        exporter.accept(key(SPAWNERS + "ominous/consumables"), single(consumables(3, true)));

        vault(exporter, "outpost", 1, false, null);
        vault(exporter, "frost_tower", 2, false, CommonItems.FROZEN_DEPTHS_DISC.item());
        vault(exporter, "frost_tower", 2, true, CommonItems.FROZEN_DEPTHS_DISC.item());
        vault(exporter, "glacial_tomb", 3, false, CommonItems.FROZEN_DEPTHS_DISC.item());
        vault(exporter, "glacial_tomb", 3, true, CommonItems.FROZEN_DEPTHS_DISC.item());
        vault(exporter, "monarchs_castle", 2, false, CommonItems.THE_KING_CURSED_BY_WINTER_DISC.item());
        vault(exporter, "monarchs_castle", 3, true, CommonItems.THE_KING_CURSED_BY_WINTER_DISC.item());

        exporter.accept(key(CHESTS + "outpost/supply"), supply());
        exporter.accept(key(CHESTS + "outpost/barrel"), barrel(1));
        exporter.accept(key(CHESTS + "frost_tower/barrel"), barrel(2));
        exporter.accept(key(CHESTS + "glacial_tomb/barrel"), barrel(3));
        exporter.accept(key(CHESTS + "monarchs_castle/barrel"), barrel(3));
        exporter.accept(key(CHESTS + "frost_tower/hidden"), hidden(2));
        exporter.accept(key(CHESTS + "monarchs_temple/hidden"), hidden(3));
        exporter.accept(key(POTS + "outpost"), single(pot(1)));
        exporter.accept(key(POTS + "frost_tower"), single(pot(2)));
        exporter.accept(key(POTS + "glacial_tomb"), single(pot(3)));
        exporter.accept(key(POTS + "monarchs_temple"), single(pot(3)));
    }

    private void vault(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter, String structure, int tier, boolean ominous, Item disc) {
        String prefix = ominous ? "ominous_" : "";
        String tierPath = VAULTS + "t" + tier + "/" + prefix;
        RegistryKey<LootTable> unique = key(VAULTS + structure + "/" + prefix + "unique");
        exporter.accept(unique, single(unique(tier, ominous, disc)));
        RegistryKey<LootTable> vanillaRare = ominous ? LootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE_CHEST : LootTables.TRIAL_CHAMBERS_REWARD_RARE_CHEST;
        exporter.accept(key(VAULTS + structure + "/" + (ominous ? "reward_ominous" : "reward")), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(LootTableEntry.builder(key(tierPath + "rare")).weight(6))
                        .with(LootTableEntry.builder(vanillaRare).weight(2))
                        .with(LootTableEntry.builder(key(tierPath + "common")).weight(2)))
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1, 3))
                        .with(LootTableEntry.builder(key(tierPath + "common"))))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(ominous ? 0.75F : 0.25F))
                        .with(LootTableEntry.builder(unique))));
    }

    private static LootPool.Builder pot(int tier) {
        var pool = one()
                .with(item(Items.SNOWBALL, 6, 4, 12))
                .with(item(Items.BONE, 5, 1, 4))
                .with(item(Items.IRON_NUGGET, 5, 3, 9))
                .with(item(CommonItems.FROSTBALL.item(), 4, 2, 4))
                .with(item(CommonItems.CRYO_ESSENCE.item(), 3 + tier, 1, 1 + tier))
                .with(item(Items.EMERALD, 2 + tier, 1, 2 + tier))
                .with(item(Items.DIAMOND, tier, 1, 1));
        if (tier >= 2) {
            pool.with(item(Items.GOLD_NUGGET, 3, 2, 6));
        }
        return pool;
    }

    private static LootTable.Builder single(LootPool.Builder pool) {
        return LootTable.builder().pool(pool);
    }

    private static LootPool.Builder one() {
        return LootPool.builder().rolls(ConstantLootNumberProvider.create(1));
    }

    private static LeafEntry.Builder<?> item(ItemConvertible item, int weight, int min, int max) {
        var entry = ItemEntry.builder(item).weight(weight);
        if (max > 1) {
            entry.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)));
        }
        return entry;
    }

    private static LeafEntry.Builder<?> potion(Item item, RegistryEntry<Potion> potion, int weight, int min, int max) {
        return item(item, weight, min, max).apply(SetPotionLootFunction.builder(potion));
    }

    private LeafEntry.Builder<?> enchanted(Item item, int weight, int minLevels, int maxLevels) {
        return ItemEntry.builder(item).weight(weight)
                .apply(EnchantWithLevelsLootFunction.builder(lookup, UniformLootNumberProvider.create(minLevels, maxLevels)));
    }

    private LeafEntry.Builder<?> book(RegistryKey<Enchantment> enchantment, int weight, int minLevel, int maxLevel) {
        return ItemEntry.builder(Items.BOOK).weight(weight)
                .apply(new SetEnchantmentsLootFunction.Builder()
                        .enchantment(enchantments.getOrThrow(enchantment), UniformLootNumberProvider.create(minLevel, maxLevel)));
    }

    private static LootPool.Builder common(int tier, boolean ominous) {
        int bonus = tier + (ominous ? 1 : 0);
        var pool = one()
                .with(item(CommonItems.CRYO_ESSENCE.item(), 4, bonus, bonus + 2))
                .with(item(CommonItems.FROSTBALL.item(), 3, 1 + bonus, 3 + bonus))
                .with(potion(Items.TIPPED_ARROW, Potions.SLOWNESS, 3, 2 * bonus, 4 * bonus))
                .with(item(Items.IRON_INGOT, 3, bonus, bonus + 2))
                .with(item(Items.EMERALD, 3, bonus, bonus + 3))
                .with(item(FrozenDepthsBlocks.FROZEN_TORCH.block(), 2, 2, 4 + bonus))
                .with(item(FrozenDepthsBlocks.FROZEN_CHAIN.block(), 1, 1, 3))
                .with(item(FrozenDepthsBlocks.SOULFROST_LANTERN.block(), 1, 1, 1))
                .with(item(Items.PACKED_ICE, 2, 2, 4 + bonus));
        if (ominous) {
            pool.with(item(Items.DIAMOND, 2, 1, 2));
        } else {
            pool.with(item(Items.SNOWBALL, 2, 4, 8))
                    .with(item(Items.BLUE_ICE, tier, 1, 2));
        }
        return pool;
    }

    private LootPool.Builder rare(int tier, boolean ominous) {
        int minLevels = ominous ? 20 : 5 + 5 * (tier - 1);
        int maxLevels = ominous ? 30 : 15 + 5 * (tier - 1);
        int weaponWeight = ominous ? 3 : Math.min(tier, 2);
        RegistryEntry<Potion> frostResistance = tier >= 2 || ominous ? ModPotions.LONG_FROST_RESISTANCE_POTION.potion() : ModPotions.FROST_RESISTANCE_POTION.potion();
        return one()
                .with(item(CommonItems.FROST_HAUNTS_AXE.item(), weaponWeight, 1, 1))
                .with(item(CommonItems.FROZEN_BOW.item(), weaponWeight, 1, 1))
                .with(item(CommonItems.GUARDS_FROST_LANCE.item(), weaponWeight, 1, 1))
                .with(book(Enchantments.FROST_WALKER, 2, 1, 2))
                .with(book(Enchantments.PROTECTION, 2, 1, ominous ? 4 : 1 + tier))
                .with(enchanted(Items.IRON_HELMET, 2, minLevels, maxLevels))
                .with(enchanted(Items.IRON_CHESTPLATE, 2, minLevels, maxLevels))
                .with(enchanted(Items.IRON_LEGGINGS, 2, minLevels, maxLevels))
                .with(enchanted(Items.IRON_BOOTS, 2, minLevels, maxLevels))
                .with(enchanted(Items.IRON_SWORD, 2, minLevels, maxLevels))
                .with(enchanted(Items.IRON_AXE, 2, minLevels, maxLevels))
                .with(item(Items.GOLDEN_APPLE, ominous ? 3 : 1 + tier, 1, 1))
                .with(potion(Items.POTION, frostResistance, 3, 1, 1));
    }

    private LootPool.Builder unique(int tier, boolean ominous, Item disc) {
        int minLevels = ominous ? 10 : 5;
        int maxLevels = ominous ? 20 : 10;
        var pool = one()
                .with(enchanted(Items.DIAMOND_HELMET, 1, minLevels, maxLevels))
                .with(enchanted(Items.DIAMOND_CHESTPLATE, 1, minLevels, maxLevels))
                .with(enchanted(Items.DIAMOND_LEGGINGS, 1, minLevels, maxLevels))
                .with(enchanted(Items.DIAMOND_BOOTS, 1, minLevels, maxLevels))
                .with(enchanted(Items.DIAMOND_SWORD, 1, minLevels, maxLevels))
                .with(item(Items.DIAMOND, 3, tier, tier + 2))
                .with(potion(Items.POTION, ModPotions.LONG_FROST_RESISTANCE_POTION.potion(), 3, 1, 2))
                .with(item(Items.GOLDEN_APPLE, 3, 1, 2));
        if (ominous) {
            pool.with(item(Items.ENCHANTED_GOLDEN_APPLE, 2, 1, 1))
                    .with(item(Items.HEAVY_CORE, 1, 1, 1));
        }
        if (disc != null) {
            pool.with(item(disc, ominous ? 3 : 2, 1, 1));
        }
        return pool;
    }

    private static LootPool.Builder consumables(int tier, boolean ominous) {
        var pool = one()
                .with(item(Items.COOKED_MUTTON, 3, ominous ? 2 : 1, ominous ? 4 : 1 + tier))
                .with(item(Items.BAKED_POTATO, 3, ominous ? 2 : 1, ominous ? 4 : 3))
                .with(item(Items.BREAD, 3, 1, 3))
                .with(item(CommonItems.FROSTBALL.item(), 2, 2, ominous ? 6 : 2 + tier))
                .with(potion(Items.POTION, ModPotions.FROST_RESISTANCE_POTION.potion(), 1, 1, 1))
                .with(potion(Items.POTION, Potions.REGENERATION, 1, 1, 1));
        if (ominous || tier >= 2) {
            pool.with(item(Items.GOLDEN_CARROT, ominous ? 2 : tier - 1, 1, ominous ? 2 : 1));
        }
        if (ominous) {
            pool.with(potion(Items.POTION, Potions.STRENGTH, 1, 1, 1));
        }
        return pool;
    }

    private static LootTable.Builder supply() {
        return LootTable.builder().pool(LootPool.builder()
                .rolls(UniformLootNumberProvider.create(3, 5))
                .with(item(Items.ARROW, 2, 4, 12))
                .with(potion(Items.TIPPED_ARROW, Potions.SLOWNESS, 1, 2, 4))
                .with(item(Items.BAKED_POTATO, 2, 2, 4))
                .with(item(Items.COOKED_MUTTON, 2, 1, 3))
                .with(item(Items.SWEET_BERRIES, 2, 2, 6))
                .with(item(FrozenDepthsBlocks.FROZEN_TORCH.block(), 2, 2, 6))
                .with(item(Items.SNOWBALL, 2, 4, 10))
                .with(item(Items.SPRUCE_PLANKS, 1, 2, 6))
                .with(item(Items.LEATHER, 1, 1, 3))
                .with(item(CommonItems.CRYO_ESSENCE.item(), 1, 1, 2))
                .with(item(Items.STONE_PICKAXE, 1, 1, 1))
                .with(potion(Items.POTION, ModPotions.FROST_RESISTANCE_POTION.potion(), 1, 1, 1)));
    }

    private static LootTable.Builder barrel(int tier) {
        var pool = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(2, 4))
                .with(item(Items.BONE, 4, 1, 4))
                .with(item(Items.SNOWBALL, 4, 2, 8))
                .with(item(Items.ICE, 3, 1, 4))
                .with(item(Items.PACKED_ICE, 2, 1, 3))
                .with(item(Items.BREAD, 3, 1, 3))
                .with(item(Items.COAL, 2, 1, 4))
                .with(item(Items.ARROW, 2, 2, 8))
                .with(item(Items.IRON_NUGGET, 3, 2, 6 + 2 * tier))
                .with(item(FrozenDepthsBlocks.FROZEN_TORCH.block(), 2, 1, 4))
                .with(item(FrozenDepthsBlocks.FROZEN_CHAIN.block(), 1, 1, 2))
                .with(item(CommonItems.CRYO_ESSENCE.item(), tier, 1, 2));
        if (tier >= 2) {
            pool.with(item(Items.IRON_INGOT, tier - 1, 1, 2))
                    .with(item(Items.EMERALD, tier - 1, 1, 2));
        }
        return LootTable.builder().pool(pool);
    }

    private LootTable.Builder hidden(int tier) {
        return LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(2, 4))
                        .with(item(CommonItems.CRYO_ESSENCE.item(), 3, 1, 3))
                        .with(item(Items.IRON_INGOT, 3, 2, 4))
                        .with(item(Items.GOLD_INGOT, 2, 1, 3))
                        .with(item(Items.EMERALD, 3, 2, 5))
                        .with(item(Items.DIAMOND, tier, 1, 2))
                        .with(potion(Items.POTION, ModPotions.LONG_FROST_RESISTANCE_POTION.potion(), 2, 1, 1)))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(LootTableEntry.builder(key(VAULTS + "t" + tier + "/rare"))));
    }
}

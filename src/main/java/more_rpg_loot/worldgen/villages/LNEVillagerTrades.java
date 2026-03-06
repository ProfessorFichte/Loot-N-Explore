package more_rpg_loot.worldgen.villages;

import more_rpg_loot.worldgen.map.ModMapDecorations;
import more_rpg_loot.util.LneItemTags;
import more_rpg_loot.worldgen.structures.LNESellMapFactory;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.ScheduleBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.structure.Structure;

import static more_rpg_loot.RPGLoot.MOD_ID;


public class LNEVillagerTrades {
    public static final String ALWAYS_WORK = "always_work";
    public static final Schedule ALWAYS_WORK_SCHEDULE = new Schedule();

        public static void register() {

            var scheduleBuilder = new ScheduleBuilder(ALWAYS_WORK_SCHEDULE).withActivity(50, Activity.WORK).withActivity(23950, Activity.REST).build();
            Registry.register(Registries.SCHEDULE, Identifier.of(MOD_ID, ALWAYS_WORK), ALWAYS_WORK_SCHEDULE);

            VillagerProfession innkeeper = LNEVillagerProfessions.INNKEEPER;

            int level_1_innkeeper_price_sell = 4;
            int level_1_innkeeper_price_buy = 3;
            int level_1_innkeeper_maxUses = 12;
            int level_1_innkeeper_experience = 10;
            float level_1_innkeeper_priceMultiplier = 0.1F;

            int level_2_innkeeper_price = 12;
            int level_2_innkeeper_maxUses = 5;
            int level_2_innkeeper_experience = 15;
            float level_2_innkeeper_priceMultiplier = 0.01F;

            int level_3_innkeeper_price = 24;
            int level_3_innkeeper_maxUses = 4;
            int level_3_innkeeper_experience = 20;
            float level_3_innkeeper_priceMultiplier = 0.01F;

            int level_4_innkeeper_price = 32;
            int level_4_innkeeper_maxUses = 3;
            int level_4_innkeeper_experience = 20;
            float level_4_innkeeper_priceMultiplier = 0.15F;

            int level_5_innkeeper_price = 55;
            int level_5_innkeeper_maxUses = 2;
            int level_5_innkeeper_experience = 30;
            float level_5_innkeeper_priceMultiplier = 0.15F;

            TradeOfferHelper.registerVillagerOffers(innkeeper, 1,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.GLASS_BOTTLE, level_1_innkeeper_price_buy),
                                new ItemStack(Items.EMERALD, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.BOWL, level_1_innkeeper_price_buy),
                                new ItemStack(Items.EMERALD, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_1_innkeeper_price_sell),
                                new ItemStack(Items.BREAD, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_1_innkeeper_price_sell),
                                new ItemStack(Items.COOKED_BEEF, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_1_innkeeper_price_sell),
                                new ItemStack(Items.COOKED_CHICKEN, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_1_innkeeper_price_sell),
                                new ItemStack(Items.COOKED_MUTTON, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_1_innkeeper_price_sell),
                                new ItemStack(Items.COOKED_PORKCHOP, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_1_innkeeper_price_sell),
                                new ItemStack(Items.COOKED_RABBIT, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                    });

            TradeOfferHelper.registerVillagerOffers(innkeeper, 2, factories -> {
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_0.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_2_innkeeper_maxUses,
                            level_2_innkeeper_experience,
                            level_2_innkeeper_priceMultiplier
                    );
                });
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_0.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_2_innkeeper_maxUses,
                            level_2_innkeeper_experience,
                            level_2_innkeeper_priceMultiplier
                    );
                });
            });
            TradeOfferHelper.registerVillagerOffers(innkeeper, 3, factories -> {
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_1.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_3_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_3_innkeeper_maxUses,
                            level_3_innkeeper_experience,
                            level_3_innkeeper_priceMultiplier
                    );
                });
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_1.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_3_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_3_innkeeper_maxUses,
                            level_3_innkeeper_experience,
                            level_3_innkeeper_priceMultiplier
                    );
                });
            });
            TradeOfferHelper.registerVillagerOffers(innkeeper, 4, factories -> {
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_2.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_4_innkeeper_maxUses,
                            level_4_innkeeper_experience,
                            level_4_innkeeper_priceMultiplier
                    );
                });
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_2.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_4_innkeeper_maxUses,
                            level_4_innkeeper_experience,
                            level_4_innkeeper_priceMultiplier
                    );
                });
            });
            TradeOfferHelper.registerVillagerOffers(innkeeper, 5, factories -> {
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_3.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_5_innkeeper_maxUses,
                            level_5_innkeeper_experience,
                            level_5_innkeeper_priceMultiplier
                    );
                });
                factories.add((entity, random) -> {
                    var registryAccess = entity.getWorld().getRegistryManager();
                    var itemRegistry = registryAccess.get(RegistryKeys.ITEM);
                    var tagKey = TagKey.of(RegistryKeys.ITEM, LneItemTags.INNKEEPER_BUFFS_3.id());
                    var tag = itemRegistry.getEntryList(tagKey);
                    if (tag.isEmpty()) return null;
                    var itemList = tag.get().stream().toList();
                    if (itemList.isEmpty()) return null;
                    var randomItemEntry = itemList.get(random.nextInt(itemList.size()));
                    Item randomItem = randomItemEntry.value();
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                            new ItemStack(randomItem, 1),
                            level_5_innkeeper_maxUses,
                            level_5_innkeeper_experience,
                            level_5_innkeeper_priceMultiplier
                    );
                });
            });

            ///CARTOGRAPHER TRADES
        TagKey<Structure> frostmonarchTempleTag = TagKey.of(RegistryKeys.STRUCTURE, Identifier.of("loot_n_explore", "frostmonarch_temple"));
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 4, factories -> {
            factories.add((entity, random) -> new LNESellMapFactory(
                    12,
                    frostmonarchTempleTag,
                    "filled_map.loot_n_explore.frostmonarch_temple",
                    ModMapDecorations.FROSTMONARCH_TEMPLE,
                    12,
                    5,
                    0x3D9DF3
            ).create(entity, random));
        });
        }

    }

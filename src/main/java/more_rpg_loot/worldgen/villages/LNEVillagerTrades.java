package more_rpg_loot.worldgen.villages;

import more_rpg_loot.util.LneItemTags;
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

            /*
            TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                                new ItemStack(CommonItems.SWEET_BERRY_PUNCH, 1),
                                level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                                new ItemStack(CommonItems.HOT_CHOCOLATE, 1),
                                level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                                new ItemStack(CommonItems.POTATO_SOUP, 1),
                                level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                    });
*/


            /*
            TradeOfferHelper.registerVillagerOffers(innkeeper, 3, factories -> {
                factories.add(((entity, random) -> new LNESellMapFactory(
                        level_3_innkeeper_price, StructureTags.SMALL_MONSTER_QUEST,
                        "filled_map.loot_n_explore.monster_quest",
                        MapDecorationTypes.TARGET_X,
                        level_3_innkeeper_maxUses,
                        level_3_innkeeper_experience).create(entity, random)
                ));
            });
            */
            /*

            TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                new ItemStack(CommonItems.MALT_EXTRACT, 1),
                                level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                new ItemStack(CommonItems.BEET_ROOTBEER, 1),
                                level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                    });



            TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                                new ItemStack(CommonItems.VITAL_DRINK, 1),
                                level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                                new ItemStack(CommonItems.ESPRESSO, 1),
                                level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                    });


            //MORE RPG CLASSES MOD SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {

                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGC_Items.WATERMELON_DRINK, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGC_Items.BLUE_BERRY_PUNCH, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGC_Items.GREEN_CHILLI, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGC_Items.HONEY_MET, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGC_Items.CACTUS_JUICE, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        });
            }

            //RANGED WEAPON API SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("ranged_weapon_api")) {
                TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                                    new ItemStack(RWA_Items.APPLE_JUICE, 1),
                                    level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(RWA_Items.WALDMEISTER, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                                    new ItemStack(RWA_Items.FORREST_SPIRIT, 1),
                                    level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        });
            }

            //SPELL POWER MOD SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("spell_power")) {
                TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                                    new ItemStack(SpellPower_Items.ORANGE_JUICE, 1),
                                    level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPower_Items.FRUIT_ICEWATER, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPower_Items.HOLY_WATER, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPower_Items.CHORUS_EXTRACT, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPower_Items.HOT_CHILLI, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPower_Items.SWEET_CHILLI, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPower_Items.ENCHANTED_ALE, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        }
                        );
                TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                                    new ItemStack(SpellPower_Items.WIZARDS_ELIXIR, 1),
                                    level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        }
                        );
            }

            //WITCHER SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
                TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_2_innkeeper_price),
                                    new ItemStack(Witcher_Items.BEAUCLAIR_WHITE, 1),
                                    level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(Witcher_Items.RIVIAN_KRIEK, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new TradedItem(Items.EMERALD, level_5_innkeeper_price),
                                    new ItemStack(Witcher_Items.BUTCHER_OF_BLAVIKEN, 1),
                                    level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        });
            }

*/
        }

    }

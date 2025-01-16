package more_rpg_loot.worldgen.villages;

import more_rpg_loot.compat.items.MRPGCItems;
import more_rpg_loot.compat.items.RangedWeaponAPIItems;
import more_rpg_loot.compat.items.SpellPowerItems;
import more_rpg_loot.compat.items.WitcherItems;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.worldgen.structures.LNESellMapFactory;
import more_rpg_loot.worldgen.structures.StructureTags;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffer;


public class LNEVillagerTrades {

        public static void register() {
            VillagerProfession innkeeper = LNEVillagerProfessions.INNKEEPER;

            int level_1_innkeeper_price = 3;
            int level_1_innkeeper_maxUses = 12;
            int level_1_innkeeper_experience = 10;
            float level_1_innkeeper_priceMultiplier = 0.1F;

            int level_2_innkeeper_price = 20;
            int level_2_innkeeper_maxUses = 5;
            int level_2_innkeeper_experience = 15;
            float level_2_innkeeper_priceMultiplier = 0.01F;

            int level_3_innkeeper_price = 0;
            int level_3_innkeeper_maxUses = 1;
            int level_3_innkeeper_experience = 20;

            int level_4_innkeeper_price = 30;
            int level_4_innkeeper_maxUses = 6;
            int level_4_innkeeper_experience = 20;
            float level_4_innkeeper_priceMultiplier = 0.15F;

            int level_5_innkeeper_price = 40;
            int level_5_innkeeper_maxUses = 2;
            int level_5_innkeeper_experience = 30;
            float level_5_innkeeper_priceMultiplier = 0.15F;

            TradeOfferHelper.registerVillagerOffers(innkeeper, 1,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.GLASS_BOTTLE, level_1_innkeeper_price),
                                new ItemStack(Items.EMERALD, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.BOWL, level_1_innkeeper_price),
                                new ItemStack(Items.EMERALD, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_1_innkeeper_price),
                                new ItemStack(Items.BREAD, 1),
                                level_1_innkeeper_maxUses, level_1_innkeeper_experience, level_1_innkeeper_priceMultiplier));
                    });

            TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_2_innkeeper_price),
                                new ItemStack(CommonItems.SWEET_BERRY_PUNCH, 1),
                                level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_2_innkeeper_price),
                                new ItemStack(CommonItems.HOT_CHOCOLATE, 1),
                                level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_2_innkeeper_price),
                                new ItemStack(CommonItems.POTATO_SOUP, 1),
                                level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                    });


            TradeOfferHelper.registerVillagerOffers(innkeeper, 3, factories -> {
                factories.add(((entity, random) -> new LNESellMapFactory(
                        level_3_innkeeper_price, StructureTags.SMALL_MONSTER_QUEST,
                        "filled_map.loot_n_explore.monster_quest",
                        MapIcon.Type.TARGET_X,
                        level_3_innkeeper_maxUses,
                        level_3_innkeeper_experience).create(entity, random)
                ));
            });



            TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                new ItemStack(CommonItems.MALT_EXTRACT, 1),
                                level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                new ItemStack(CommonItems.BEET_ROOTBEER, 1),
                                level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                    });



            TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_5_innkeeper_price),
                                new ItemStack(CommonItems.VITAL_DRINK, 1),
                                level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, level_5_innkeeper_price),
                                new ItemStack(CommonItems.ESPRESSO, 1),
                                level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                    });

            //MORE RPG CLASSES MOD SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {

                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGCItems.WATERMELON_DRINK, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGCItems.BLUE_BERRY_PUNCH, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(MRPGCItems.GREEN_CHILLI, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        });
            }

            //RANGED WEAPON API SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("ranged_weapon_api")) {
                TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_2_innkeeper_price),
                                    new ItemStack(RangedWeaponAPIItems.APPLE_JUICE, 1),
                                    level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(RangedWeaponAPIItems.WALDMEISTER, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_5_innkeeper_price),
                                    new ItemStack(RangedWeaponAPIItems.FORREST_SPIRIT, 1),
                                    level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        });
            }

            //SPELL POWER MOD SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("spell_power")) {
                TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_2_innkeeper_price),
                                    new ItemStack(SpellPowerItems.ORANGE_JUICE, 1),
                                    level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPowerItems.FRUIT_ICEWATER, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPowerItems.HOLY_WATER, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPowerItems.CHORUS_EXTRACT, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPowerItems.HOT_CHILLI, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPowerItems.SWEET_CHILLI, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(SpellPowerItems.ENCHANTED_ALE, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        }
                        );
                TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_5_innkeeper_price),
                                    new ItemStack(SpellPowerItems.WIZARDS_ELIXIR, 1),
                                    level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        }
                        );
            }

            //WITCHER SPECIFIC TRADES
            if(FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
                TradeOfferHelper.registerVillagerOffers(innkeeper, 2,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_2_innkeeper_price),
                                    new ItemStack(WitcherItems.BEAUCLAIR_WHITE, 1),
                                    level_2_innkeeper_maxUses, level_2_innkeeper_experience, level_2_innkeeper_priceMultiplier));

                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 4,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_4_innkeeper_price),
                                    new ItemStack(WitcherItems.RIVIAN_KRIEK, 1),
                                    level_4_innkeeper_maxUses, level_4_innkeeper_experience, level_4_innkeeper_priceMultiplier));
                        });
                TradeOfferHelper.registerVillagerOffers(innkeeper, 5,
                        factories -> {
                            factories.add((entity, random) -> new TradeOffer(
                                    new ItemStack(Items.EMERALD, level_5_innkeeper_price),
                                    new ItemStack(WitcherItems.BUTCHER_OF_BLAVIKEN, 1),
                                    level_5_innkeeper_maxUses, level_5_innkeeper_experience, level_5_innkeeper_priceMultiplier));
                        });
            }
        }
    }

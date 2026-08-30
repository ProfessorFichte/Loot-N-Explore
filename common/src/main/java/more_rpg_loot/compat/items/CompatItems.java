package more_rpg_loot.compat.items;

import more_rpg_loot.platform.LNEEvents;
import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.item.Group;
import more_rpg_loot.item.ItemModelType;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.InnkeeperFoodItem;
import more_rpg_loot.item.consumables.ModFoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class CompatItems {

    public static class Entry {
        private final String name;
        private final Supplier<Item> supplier;
        private final String translation;
        private final String loreText;
        private final ItemModelType modelType;
        private final String requiredMod;
        private final int quality;

        // This is null until the item is actually registered
        private Item item;

        public Entry(String name, Supplier<Item> supplier, String translation, String loreText, ItemModelType modelType, String requiredMod, int quality) {
            this.name = name;
            this.supplier = supplier;
            this.translation = translation;
            this.loreText = loreText != null ? loreText : "";
            this.modelType = modelType;
            this.requiredMod = requiredMod;
            this.quality = quality;
        }

        // Constructor with lore text but default model type
        public Entry(String name, Supplier<Item> supplier, String translation, String loreText, String requiredMod, int quality) {
            this(name, supplier, translation, loreText, new ItemModelType.Generated("item/consumables/" + requiredMod + "/"), requiredMod, quality);
        }

        // Constructor without lore text and default model type (original)
        public Entry(String name, Supplier<Item> supplier, String translation, String requiredMod, int quality) {
            this(name, supplier, translation, "", new ItemModelType.Generated("item/consumables/" + requiredMod + "/"), requiredMod, quality);
        }

        public String name() { return name; }
        public String translation() { return translation; }
        public String loreText() { return loreText; } // Getter for lore text (datagen)
        public ItemModelType modelType() { return modelType; }
        public String requiredMod() { return requiredMod; }
        public int getQuality() { return quality; }

        public boolean shouldRegister() {
            return LNEPlatform.isModLoaded(requiredMod);
        }

        public Item getItem() {
            return item;
        }

        public void register() {
            if (!shouldRegister()) return;
            if (item == null) {
                item = Registry.register(
                        Registries.ITEM,
                        Identifier.of(MOD_ID, name),
                        supplier.get()
                );
            }
        }
    }

    public static final List<Entry> ALL = new ArrayList<>();

    // Helper method to create entry without lore
    private static Entry entry(String name, Supplier<Item> supplier, String translation, String requiredMod, int quality) {
        Entry e = new Entry(name, supplier, translation, requiredMod, quality);
        ALL.add(e);
        return e;
    }

    // Helper method to create entry with lore text
    private static Entry entryWithLore(String name, Supplier<Item> supplier, String translation, String loreText, String requiredMod, int quality) {
        Entry e = new Entry(name, supplier, translation, loreText, requiredMod, quality);
        ALL.add(e);
        return e;
    }

    // ----------------------
    // COMPAT ITEMS
    // ----------------------

    // ==================== MORE_RPG_CLASSES ITEMS ====================
    /// T1 BUFF ITEMS
    public static final Entry WATERMELON_DRINK = entry("watermelon_drink",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), MRPGC_Effects.WATERMELON_DRINK.registryEntry, 1),
            "Watermelon Drink", "more_rpg_classes", 1);
    public static final Entry BLUE_BERRY_PUNCH = entry("blue_berry_punch",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), MRPGC_Effects.BLUE_BERRY_PUNCH.registryEntry, 1),
            "Blueberry Punch", "more_rpg_classes", 1);
    public static final Entry GREEN_CHILLI = entry("green_chilli",
            () -> new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON), MRPGC_Effects.GREEN_CHILLI.registryEntry, 1),
            "Green Chilli", "more_rpg_classes", 1);
    public static final Entry HONEY_MET = entry("honey_met",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), MRPGC_Effects.HONEY_MET.registryEntry, 1),
            "Honey Mead", "more_rpg_classes", 1);
    public static final Entry CACTUS_JUICE = entry("cactus_juice",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), MRPGC_Effects.CACTUS_JUICE.registryEntry, 1),
            "Cactus Juice", "more_rpg_classes", 1);
    public static final Entry MOSSFLOWER_TEA = entry("mossflower_tea",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), MRPGC_Effects.MOSSFLOWER_TEA.registryEntry, 1),
            "Mossflower Tea", "more_rpg_classes", 1);
    /// T3 BUFF ITEMS
    public static final Entry DETTLAFFS_BLOOD = entryWithLore("dettlaffs_blood",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), MRPGC_Effects.DETTLAFFS_BLOOD.registryEntry, 3),
            "Dettlaff's Blood", "A crimson draught that ignites bloodlust and grants lifesteal with every furious blow.", "more_rpg_classes", 3);
    public static final Entry SCARLET_ESSENCE = entryWithLore("scarlet_essence",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), MRPGC_Effects.SCARLET_ESSENCE.registryEntry, 3),
            "Scarlet Essence", "A rare, almost sacred substance that blesses spells with magical blood steal.", "more_rpg_classes", 3);
    public static final Entry SVABLODS_BREW = entryWithLore("svablods_brew",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), MRPGC_Effects.SVABLODS_BREW.registryEntry, 3),
            "Svablod's Brew", "Brewed by Northern Cultists with Mardroeme.", "more_rpg_classes", 3);

    // ==================== RANGED_WEAPON_API ITEMS ====================
    /// T0 BUFF ITEMS
    public static final Entry APPLE_JUICE = entry("apple_juice",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON), RWA_Effects.APPLE_JUICE.registryEntry, 0),
            "Apple Juice", "ranged_weapon_api", 0);
    /// T1 BUFF ITEMS
    public static final Entry WALDMEISTER = entry("waldmeister",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), RWA_Effects.WALDMEISTER.registryEntry, 1),
            "Waldmeister", "ranged_weapon_api", 1);
    /// T2 BUFF ITEMS
    public static final Entry FORREST_SPIRIT = entry("forrest_spirit",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE), RWA_Effects.FORREST_SPIRIT.registryEntry, 2),
            "Forrest Spirit", "ranged_weapon_api", 2);
    /// T3 BUFF ITEMS
    public static final Entry WOODSNAKE_POTION = entryWithLore("woodsnake_potion",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), RWA_Effects.WOODSNAKE_POTION.registryEntry, 3),
            "Woodsnake Potion", "Brewed with the skin of a wood snake by a legendary druid.", "ranged_weapon_api", 3);

    // ==================== SPELL_POWER ITEMS ====================
    /// T0 BUFF ITEMS
    public static final Entry ORANGE_JUICE = entry("orange_juice",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON), SpellPower_Effects.ORANGE_JUICE.registryEntry, 0),
            "Orange Juice", "spell_power", 0);
    /// T1 BUFF ITEMS
    public static final Entry SWEET_CHILLI = entry("sweet_chilli",
            () -> new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON), SpellPower_Effects.SWEET_CHILLI.registryEntry, 1),
            "Sweet Chilli", "spell_power", 1);
    public static final Entry FRUIT_ICEWATER = entry("fruit_icewater",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), SpellPower_Effects.FRUIT_ICEWATER.registryEntry, 1),
            "Fruit Icewater", "spell_power", 1);
    public static final Entry CHORUS_EXTRACT = entry("chorus_extract",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), SpellPower_Effects.CHORUS_EXTRACT.registryEntry, 1),
            "Chorus Extract", "spell_power", 1);
    public static final Entry HOT_CHILLI = entry("hot_chilli",
            () -> new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.UNCOMMON), SpellPower_Effects.HOT_CHILLI.registryEntry, 1),
            "Hot Chilli", "spell_power", 1);
    public static final Entry HOLY_WATER = entry("holy_water",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), SpellPower_Effects.HOLY_WATER.registryEntry, 1),
            "Holy Water", "spell_power", 1);
    public static final Entry ENCHANTED_ALE = entry("enchanted_ale",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), SpellPower_Effects.ENCHANTED_ALE.registryEntry, 1),
            "Enchanted Ale", "spell_power", 1);
    /// T2 BUFF ITEMS
    public static final Entry WIZARDS_ELIXIR = entry("wizards_elixir",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE), SpellPower_Effects.WIZARDS_ELIXIR.registryEntry, 2),
            "Wizard's Elixir", "spell_power", 2);
    /// T3 BUFF ITEMS
    public static final Entry MERLINS_FLASK = entryWithLore("merlins_flask",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), SpellPower_Effects.MERLINS_FLASK.registryEntry, 3),
            "Merlin's Flask", "Perfectly assembled by Merlin the Wizard for highest performance.", "spell_power", 3);
    public static final Entry CRUSADERS_REST = entryWithLore("crusaders_rest",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), SpellPower_Effects.CRUSADERS_REST.registryEntry, 3),
            "Crusader's Rest", "Distributed by the Holy Church for the highest Knights Templar.", "spell_power", 3);

    // ==================== WITCHER_RPG ITEMS ====================
    /// T0 BUFF ITEMS
    public static final Entry BEAUCLAIR_WHITE = entry("beauclair_white",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON), Witcher_Effects.BEAUCLAIR_WHITE.registryEntry, 0),
            "Beauclair White", "witcher_rpg", 0);
    /// T1 BUFF ITEMS
    public static final Entry RIVIAN_KRIEK = entry("rivian_kriek",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), Witcher_Effects.RIVIAN_KRIEK.registryEntry, 1),
            "Rivian Kriek", "witcher_rpg", 1);
    /// T2 BUFF ITEMS
    public static final Entry BUTCHER_OF_BLAVIKEN = entry("butcher_of_blaviken",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE), Witcher_Effects.BUTCHER_OF_BLAVIKEN.registryEntry, 2),
            "Butcher of Blaviken", "witcher_rpg", 2);
    /// T3 BUFF ITEMS
    public static final Entry WHITE_WOLF = entryWithLore("white_wolf",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), Witcher_Effects.WHITE_WOLF.registryEntry, 3),
            "White Wolf", "Excellent wine from Toussaint, named after the legendary witcher Geralt of Riva.", "witcher_rpg", 3);

    // ==================== CRITICAL STRIKE MOD ITEMS ====================
    /// T1 BUFF ITEMS
    public static final Entry BLACKPEPPER_BREAD = entry("blackpepper_bread",
            () -> new InnkeeperFoodItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_FOOD).rarity(Rarity.UNCOMMON), CriticalStrike_Effects.BLACKPEPPER_BREAD.registryEntry, 1),
            "Blackpepper Bread", "critical_strike", 1);
    public static final Entry INFERNO_CHILLI_OIL = entry("inferno_chilli_oil",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), CriticalStrike_Effects.INFERNO_CHILLI_OIL.registryEntry, 1),
            "Inferno Chilli Oil", "critical_strike", 1);
    /// T2 BUFF ITEMS
    public static final Entry GRILLED_EEL_SUSHI = entry("grilled_eel_sushi",
            () -> new InnkeeperFoodItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_FOOD).rarity(Rarity.RARE), CriticalStrike_Effects.GRILLED_EEL_SUSHI.registryEntry, 2),
            "Grilled Eel Sushi Roll", "critical_strike", 2);
    /// T3 BUFF ITEMS
    public static final Entry PHANTOMSTRIKE_TONIC = entryWithLore("phantomstrike_tonic",
            () -> new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), CriticalStrike_Effects.PHANTOMSTRIKE_TONIC.registryEntry, 3),
            "Phantom Strike Tonic", "A phantom shadow-infused tonic that sharpens instincts and accuracy.", "critical_strike", 3);

    // ----------------------
    // REGISTRATION
    // ----------------------

    public static void registerAll() {
        for (Entry entry : ALL) {
            entry.register();
        }

        // Add to item groups
        LNEEvents.get().modifyItemGroup(Group.RPG_FOOD_KEY, content -> {
            for (Entry entry : ALL) {
                Item item = entry.getItem();
                if (item instanceof InnkeeperDrinkItem || item instanceof InnkeeperBowlItem || item instanceof InnkeeperFoodItem) {
                    content.add(item);
                }
            }
        });
    }

    public static List<Entry> getAllEntries() {
        return ALL;
    }
}

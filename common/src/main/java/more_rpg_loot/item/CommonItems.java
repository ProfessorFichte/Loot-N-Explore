package more_rpg_loot.item;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.item.consumables.InnkeeperBowlItem;
import more_rpg_loot.item.consumables.InnkeeperDrinkItem;
import more_rpg_loot.item.consumables.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.JukeboxPlayableComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class CommonItems {
    public record Entry(String name, Item item, String translation, String loreText, ItemModelType modelType) {
        public Entry(String name, Item item, String translation, ItemModelType modelType) {
            this(name, item, translation, "", modelType);
        }

        public Entry(String name, Item item, String translation) {
            this(name, item, translation, "", new ItemModelType.Generated());
        }

        public Entry(String name, Item item, String translation, String loreText, ItemModelType modelType) {
            this.name = name;
            this.item = item;
            this.translation = translation;
            this.loreText = loreText != null ? loreText : "";
            this.modelType = modelType;
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Item item, String translation, ItemModelType modelType) {
        var entry = new Entry(name, item, translation, "", modelType);
        all.add(entry);
        return entry;
    }

    private static Entry entry(String name, Item item, String translation) {
        return entry(name, item, translation, new ItemModelType.Generated());
    }

    private static Entry entryWithLore(String name, Item item, String translation, String loreText, ItemModelType modelType) {
        var entry = new Entry(name, item, translation, loreText, modelType);
        all.add(entry);
        return entry;
    }

    private static AttributeModifiersComponent weaponAttributes(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }

    /// T0 BUFF ITEMS
    public static final Entry HOT_CHOCOLATE = entry("hot_chocolate",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON), Effects.HOT_CHOCOLATE.registryEntry, 0),
            "Hot Chocolate", new ItemModelType.Generated("item/drinks/"));
    public static final Entry POTATO_SOUP = entry("potato_soup",
            new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.COMMON), Effects.POTATO_SOUP.registryEntry, 0),
            "Potato Soup", new ItemModelType.Generated("item/drinks/"));
    public static final Entry SWEET_BERRY_PUNCH = entry("sweet_berry_punch",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.COMMON), Effects.SWEET_BERRY_PUNCH.registryEntry, 0),
            "Sweet Berry Punch", new ItemModelType.Generated("item/drinks/"));

    /// T1 BUFF ITEMS
    public static final Entry BEET_ROOTBEER = entry("beet_rootbeer",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), Effects.BEET_ROOTBEER.registryEntry, 1),
            "Beet Rootbeer", new ItemModelType.Generated("item/drinks/"));
    public static final Entry MALT_EXTRACT = entry("malt_extract",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON), Effects.MALT_EXTRACT.registryEntry, 1),
            "Malt Extract", new ItemModelType.Generated("item/drinks/"));

    /// T2 BUFF ITEMS
    public static final Entry VITAL_DRINK = entry("vital_drink",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE), Effects.VITAL_DRINK.registryEntry, 2),
            "Vital Drink", new ItemModelType.Generated("item/drinks/"));
    public static final Entry ESPRESSO = entry("espresso",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE), Effects.ESPRESSO.registryEntry, 2),
            "Espresso", new ItemModelType.Generated("item/drinks/"));

    /// T3 BUFF ITEMS (with lore text)
    public static final Entry KNIGHTS_FAVOURITE = entryWithLore("knights_favourite",
            new InnkeeperBowlItem(new Item.Settings().maxCount(16).food(ModFoodComponents.INN_BOWL).rarity(Rarity.EPIC), Effects.KNIGHTS_FAVOURITE.registryEntry, 3),
            "Knight's Favourite", "A special dish with which no knight has ever lost.", new ItemModelType.Generated("item/drinks/"));
    public static final Entry THE_UNSHAKABLE = entryWithLore("the_unshakable",
            new InnkeeperDrinkItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC), Effects.THE_UNSHAKABLE.registryEntry, 3),
            "The Unshakable", "For the knights of the front line, they cannot be stopped.", new ItemModelType.Generated("item/drinks/"));

    public static final Entry FROSTBALL = entry("frostball",
            new FrostballItem(new Item.Settings().maxCount(16)),
            "Frostball", new ItemModelType.Generated("item/misc/"));
    public static final Entry GLAZE_ROD = entry("glaze_rod",
            new Item(new Item.Settings()),
            "Glaze Rod", new ItemModelType.Handheld("item/misc/"));
    public static final Entry FROZEN_KEY = entry("frozen_key",
            new Item(new Item.Settings()),
            "Frozen Key", new ItemModelType.Generated("item/misc/"));
    public static final Entry MONARCHS_KEY = entry("monarchs_key",
            new Item(new Item.Settings()),
            "Monarch's Key", new ItemModelType.Generated("item/misc/"));

    public static final Entry FROZEN_DEPTHS_DISC = entry("frozen_depths_disc",
            new Item(new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.RARE)
                    .component(DataComponentTypes.JUKEBOX_PLAYABLE,
                            new JukeboxPlayableComponent(new RegistryPair<>(
                                    RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "frozen_depths"))),
                                    true))),
            "Frozen Depths", new ItemModelType.Generated("item/misc/"));

    public static final Entry FROST_HAUNTS_AXE = entry("frost_haunt_axe",
            new AxeItem(ToolMaterials.STONE, new Item.Settings()
                    .attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, 7.0F, -3.2F))),
            "Frost Haunt's Axe", new ItemModelType.Handheld("item/weapons/"));
    public static final Entry MONARCHS_FROST_STAFF = entry("monarchs_frost_staff",
            new StaffItem(new Item.Settings().maxDamage(0)
                    .attributeModifiers(weaponAttributes(4.0F, -3.0F))),
            "Monarch's Frost Staff", new ItemModelType.Custom());
    public static final Entry GUARDS_FROST_LANCE = entry("guards_frost_lance",
            new SwordItem(ToolMaterials.STONE, new Item.Settings()
                    .attributeModifiers(weaponAttributes(5.0F, -2.6F))),
            "Guard's Frost Lance", new ItemModelType.Custom());
    public static final Entry FROZEN_BOW = entry("frozen_bow",
            new BowItem(new Item.Settings().maxDamage(384)),
            "Frozen Bow", new ItemModelType.Bow());


    public static void registerCommonItems(){
        // Register all items
        for (var entry : all) {
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, entry.name()), entry.item());
        }

        ItemGroupEvents.modifyEntriesEvent(Group.RPG_FOOD_KEY).register((content) -> {
            /// T0 BUFF ITEMS
            content.add(SWEET_BERRY_PUNCH.item());
            content.add(HOT_CHOCOLATE.item());
            content.add(POTATO_SOUP.item());
            /// T1 BUFF ITEMS
            content.add(MALT_EXTRACT.item());
            content.add(BEET_ROOTBEER.item());
            /// T2 BUFF ITEMS
            content.add(VITAL_DRINK.item());
            content.add(ESPRESSO.item());
            /// T3 BUFF ITEMS
            content.add(KNIGHTS_FAVOURITE.item());
            content.add(THE_UNSHAKABLE.item());
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((content) -> {
            content.addAfter(Items.SNOWBALL, FROSTBALL.item());
            content.add(FROST_HAUNTS_AXE.item());
            content.add(MONARCHS_FROST_STAFF.item());
            content.add(GUARDS_FROST_LANCE.item());
            content.add(FROZEN_BOW.item());
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((content) -> {
            content.addAfter(Items.BLAZE_ROD, GLAZE_ROD.item());
            content.addAfter(Items.TRIAL_KEY, FROZEN_KEY.item());
            content.addAfter(Items.OMINOUS_TRIAL_KEY, MONARCHS_KEY.item());
        });
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_BLOCK_KEY).register((content) -> {
            content.add(FROSTBALL.item());
            content.add(GLAZE_ROD.item());
            content.addAfter(ModBlocks.FROZEN_VAULT.block(), FROZEN_KEY.item());
            content.addAfter(ModBlocks.FROZEN_VAULT.block(), MONARCHS_KEY.item());
            content.add(FROZEN_DEPTHS_DISC.item());
        });
    }
}

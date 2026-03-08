package more_rpg_loot.compat.spell_engine;

import com.google.common.base.Suppliers;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.RelicItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigUtil;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNE_Relics {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes) { }

    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;
        if (args.attributes != null) {
            settings.attributeModifiers(args.attributes);
        }
        return new RelicItem(settings);
    };
    private static Function<ItemArgs, Item> getFactory() { return factory; }

    public static final class Entry {
        private final int tier;
        private final String name;
        private String translatedName = "";
        private String loreText = "";
        private LNE_RelicsConfig.Entry config;
        public LNE_RelicsConfig.Entry defaults;
        private final Supplier<Item> item;
        private SpellContainer spellContainer;

        public Entry(int tier, String name) {
            this(tier, name, LNE_RelicsConfig.Entry.EMPTY);
        }

        public Entry(int tier, String name, LNE_RelicsConfig.Entry config) {
            this.tier = tier;
            this.name = name;
            this.config = config;
            this.defaults = config;

            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings()
                        .maxCount(1);
                var attributes = (config().attributes != null && !config().attributes.isEmpty())
                        ? ConfigUtil.attributesComponent(Identifier.of(MOD_ID, name), config().attributes).build()
                        : null;
                var spellContainer = spellContainer();
                if (spellContainer != null) {
                    settings = settings.component(SpellDataComponents.SPELL_CONTAINER, spellContainer);
                }
                if (config().durability > 0) {
                    settings = settings.maxDamage(config().durability);
                }

                var rarity = rarityFrom(tier);
                if (rarity != Rarity.COMMON) {
                    settings = settings.rarity(rarity);
                }
                return getFactory().apply(new ItemArgs(settings, attributes));
            });
        }

        private static Rarity rarityFrom(int tier) {
            return switch (tier) {
                case 0, 1 -> Rarity.COMMON;
                case 2 -> Rarity.UNCOMMON;
                case 3 -> Rarity.RARE;
                default -> Rarity.EPIC;
            };
        }

        public int tier() {
            return tier;
        }

        public Identifier id() {
            return Identifier.of(MOD_ID, name);
        }

        public String name() {
            return name;
        }

        public LNE_RelicsConfig.Entry config() {
            return config;
        }

        public Supplier<Item> item() {
            return item;
        }

        @Nullable
        public SpellContainer spellContainer() {
            return spellContainer;
        }

        public Entry config(LNE_RelicsConfig.Entry config) {
            this.config = config;
            return this;
        }

        public Entry spell(SpellContainer spellContainer) {
            this.spellContainer = spellContainer;
            return this;
        }

        // Sets the translated display name for this relic (used by datagen)
        public Entry translatedName(String translatedName) {
            this.translatedName = translatedName;
            return this;
        }

        // Sets the lore text for this relic (shown in tooltip, used by datagen)
        public Entry lore(String loreText) {
            this.loreText = loreText;
            return this;
        }

        public String translatedName() {
            return translatedName;
        }

        public String loreText() {
            return loreText;
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public static float spell_power = 0.05F;
    public static float attack_power = 0.05F;
    public static float haste = 0.05F;
    public static float crit_rate = 0.05F;
    public static float crit_damage = 0.1F;
    public static float armor = 2.0F;
    public static float health = 2.0F;
    public static float armor_toughness = 1.0F;

    public static final String RANGED_WEAPON_HASTE = "ranged_weapon:haste";
    public static final String RANGED_WEAPON_DAMAGE = "ranged_weapon:damage";
    public static final String WATER_SPELL_POWER = "spell_power:water";

    // ENDER DRAGON THEME
    public static final Entry ENDER_DRAGON_TOOTH = add(new Entry(2, "ender_dragon_tooth"))
            .translatedName("Ender Dragon Tooth")
            .lore("The Ender Dragon's Tooth's are scattered across end cities.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), attack_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry CHARGED_AMETHYST = add(new Entry(2, "charged_amethyst"))
            .translatedName("Charged Amethyst")
            .lore("Take a look at every amethyst cluster, there might be some charged crystals.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.ARCANE.id, spell_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry CORRUPTED_ENDER_PEARL = add(new Entry(3, "corrupted_ender_pearl"))
            .translatedName("Corrupted Ender Pearl")
            .lore("Some Enderman carry a corrupted pearl from the end dimension.")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:enderman_teleport")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellPowerMechanics.CRITICAL_CHANCE.id, crit_rate, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry ENDER_DRAGON_SCALES = add(new Entry(4, "ender_dragon_scales"))
            .translatedName("Ender Dragon Scales")
            .lore("Be victorious against the Ender Dragon, use the dragon's scales to craft a powerful weapon!")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:ender_dragon_scales")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(), haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    // WITHER THEME
    public static final Entry UNKNOWN_REMAINS = add(new Entry(2, "unknown_remains"))
            .translatedName("Unknown Remains")
            .lore("Even skeletons once had a name and were someone.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(RANGED_WEAPON_HASTE, haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry LOST_SOUL = add(new Entry(2, "lost_soul"))
            .translatedName("Lost Soul")
            .lore("Wither skeletons collect the souls of lost adventures.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.SOUL.id, spell_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry WITHERED_OBSIDIAN_SHARD = add(new Entry(3, "withered_obsidian_shard"))
            .translatedName("Withered Obsidian Shard")
            .lore("Piglins don't just like gold, they collect these magical shards in their bastion with their treasures.")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:wither_touch")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS.getIdAsString(), armor_toughness, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry WITHER_SPINE = add(new Entry(4, "wither_spine"))
            .translatedName("Wither Spine")
            .lore("Stand strong against the Wither and take his remains to craft a powerful weapon!")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:wither_spine")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ARMOR.getIdAsString(), armor, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    // GLACIAL THEME
    public static final Entry ETERNAL_SNOWFLAKE = add(new Entry(2, "eternal_snowflake"))
            .translatedName("Eternal Snowflake")
            .lore("This snowflake cannot melt, they were collected in igloos.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.FROST.id, spell_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry GLACIER_SHARD = add(new Entry(2, "glacier_shard"))
            .translatedName("Glacier Shard")
            .lore("Glacial Shards are protected by Glaze's in their tower spawners.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ARMOR.getIdAsString(), armor, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry FROZEN_RIB = add(new Entry(3, "frozen_rib"))
            .translatedName("Frozen Rib")
            .lore("Master the obstacles in the glacial tomb to obtain the frozen ribs of Frosthaunt's from their frozen vaults.")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:frozen_touch")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellPowerMechanics.HASTE.id, haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry FROZEN_SOUL = add(new Entry(4, "frozen_soul"))
            .translatedName("Frozen Soul")
            .lore("Take the Frozen Soul from the Frostmonarch to craft a powerful weapon!")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:frozen_soul")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.FROST.id, spell_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );

    // OCEAN THEME
    public static final Entry AMPHITRITE_DIADEM = add(new Entry(2, "amphitrite_diadem"))
            .translatedName("Amphitrite's Diadem")
            .lore("Find this shiny diadem, in buried shipwrecks, plundered by pirates.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(WATER_SPELL_POWER, spell_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry RAINBOW_CORAL = add(new Entry(2, "rainbow_coral"))
            .translatedName("Rainbow Coral")
            .lore("You may find these rare corals in buried treasure chests.")
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellPowerMechanics.HASTE.id, haste, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry POSEIDONS_AMPHORA = add(new Entry(3, "poseidons_amphora"))
            .translatedName("Poseidon's Amphora")
            .lore("In lost underwater ruins, you may find this ancient relic.")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:poseidons_grace")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), health, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry ELDER_GUARDIAN_EYE = add(new Entry(4, "elder_guardian_eye"))
            .translatedName("Elder Guardian Eye")
            .lore("Slay the Elder Guardian to receive his eye and craft a powerful weapon!")
            .spell(SpellContainers.forRelic(Identifier.of("loot_n_explore:elder_guardian_eye")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(RANGED_WEAPON_DAMAGE, spell_power, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );

    public static void register(Map<String, LNE_RelicsConfig.Entry> config) {
        for (var entry : entries) {
            var key = entry.id().toString();
            var configEntry = config.get(key);
            if (configEntry != null) {
                entry.config(configEntry);
            } else {
                config.put(key, entry.config());
            }
        }

        for(var entry: entries) {
            if (entry.isEnabled()) {
                Registry.register(Registries.ITEM, entry.id(), entry.item().get());
            }
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register(content -> {
            for(var entry: entries) {
                if (entry.isEnabled()) {
                    content.add(entry.item().get());
                }
            }
        });
    }
}

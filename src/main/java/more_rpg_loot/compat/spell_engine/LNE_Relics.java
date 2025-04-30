package more_rpg_loot.compat.spell_engine;

import com.google.common.base.Suppliers;
import more_rpg_loot.item.Group;
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
import net.spell_engine.api.spell.container.SpellContainerHelper;
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
        return new Item(settings);
    };
    private static Function<ItemArgs, Item> getFactory() { return factory; }

    public static final class Entry {
        private final int tier;
        private final String name;
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


        public boolean isEnabled() {
            return true;
        }
    }

    public static final String COMBAT_ROLL_COUNT = "combat_roll:count";

    // ENDER DRAGON THEME
    public static final Entry ENDER_DRAGON_SCALES = add(new Entry(1, "ender_dragon_scales"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            //new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry ENDER_DRAGON_TOOTH = add(new Entry(1, "ender_dragon_tooth"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                    ))
            );
    public static final Entry CORRUPTED_ENDER_PEARL = add(new Entry(1, "corrupted_ender_pearl"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.ARCANE.id, 0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry CHARGED_AMETHYST = add(new Entry(1, "charged_amethyst"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                    ))
            );
    // WITHER THEME
    public static final Entry WITHER_SPINE = add(new Entry(1, "wither_spine"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                    ))
            );
    public static final Entry UNKNOWN_REMAINS = add(new Entry(1, "unknown_remains"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                    ))
            );
    public static final Entry LOST_SOUL = add(new Entry(1, "lost_soul"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.SOUL.id, 0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry WITHERED_OBSIDIAN_SHARD = add(new Entry(1, "withered_obsidian_shard"))
            .spell(SpellContainerHelper.createForRelic(Identifier.of("loot_n_explore:wither_touch")))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS.getIdAsString(), 1.0F, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    // GLACIAL THEME
    public static final Entry FROZEN_SOUL = add(new Entry(1, "frozen_soul"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                    ))
            );
    public static final Entry ETERNAL_SNOWFLAKE = add(new Entry(1, "eternal_snowflake"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.FROST.id, 0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry FROZEN_RIB = add(new Entry(1, "frozen_rib"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
                    ))
            );
    public static final Entry GLACIER_SHARD = add(new Entry(1, "glacier_shard"))
            .config(new LNE_RelicsConfig.Entry()
                    .withAttributes(List.of(
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

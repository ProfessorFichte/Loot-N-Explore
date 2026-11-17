package lne.item.relics;

import lne.config.TweaksConfig;
import com.google.common.base.Suppliers;
import lne.item.Group;
import lne.item.RelicItem;
import lne.internal.platform.LNEEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.tiny_config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static lne.LneMod.MOD_ID;

public class LNE_RelicItems {

    public static Function<Item.Settings, Item> factory = RelicItem::new;

    public static final List<Entry> entries = new ArrayList<>();

    public static final ConfigManager<RelicsConfig> config = new ConfigManager<>("relics_v2", new RelicsConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    private static final String ADD = EntityAttributeModifier.Operation.ADD_VALUE.name();
    private static final String MULT = EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE.name();

    static final String ATTACK_DAMAGE = EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString();
    static final String ATTACK_SPEED = EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString();
    static final String ARMOR = EntityAttributes.GENERIC_ARMOR.getIdAsString();
    static final String TOUGHNESS = EntityAttributes.GENERIC_ARMOR_TOUGHNESS.getIdAsString();
    static final String MAX_HEALTH = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
    private static final String LUCK = EntityAttributes.GENERIC_LUCK.getIdAsString();

    private static final String SPELL_ARCANE = "spell_power:arcane";
    static final String SPELL_SOUL = "spell_power:soul";
    static final String SPELL_FROST = "spell_power:frost";
    private static final String SPELL_WATER = "spell_power:water";
    private static final String SPELL_CRIT = "spell_power:critical_chance";
    static final String SPELL_HASTE = "spell_power:haste";
    private static final String DAMAGE_TAKEN = "spell_engine:damage_taken";
    private static final String HEALING_TAKEN = "spell_engine:healing_taken";
    private static final String RANGED_HASTE = "ranged_weapon:haste";
    private static final String RANGED_DAMAGE = "ranged_weapon:damage";
    private static final String CRIT_CHANCE = "critical_strike:chance";
    private static final String CRIT_DAMAGE = "critical_strike:damage";
    private static final String SPELL_GENERIC = "spell_power:generic";
    private static final String DAMAGE_REFLECT = "mrpg_lib:damage_reflect_modifier";

    public static final class Entry {
        private final int tier;
        private final String name;
        private String translatedName = "";
        private String loreText = "";
        private List<RelicsConfig.Bonus> defaults = List.of();
        @Nullable private Identifier spellId;
        private final Supplier<Item> item;

        public Entry(int tier, String name) {
            this.tier = tier;
            this.name = name;
            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings().maxCount(1);
                var rarity = rarityFrom(tier);
                if (rarity != Rarity.COMMON) settings = settings.rarity(rarity);
                return factory.apply(settings);
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

        public Entry title(String translatedName) {
            this.translatedName = translatedName;
            return this;
        }

        public Entry lore(String loreText) {
            this.loreText = loreText;
            return this;
        }

        public Entry bonuses(RelicsConfig.Bonus... bonuses) {
            this.defaults = List.of(bonuses);
            return this;
        }

        public Entry spell(String spellPath) {
            this.spellId = Identifier.of(MOD_ID, spellPath);
            return this;
        }

        public int tier() { return tier; }
        public Identifier id() { return Identifier.of(MOD_ID, name); }
        public String name() { return name; }
        public String translatedName() { return translatedName; }
        public String loreText() { return loreText; }
        public Supplier<Item> item() { return item; }
        @Nullable public Identifier spellId() { return spellId; }
        public List<RelicsConfig.Bonus> defaults() { return defaults; }

        public List<RelicsConfig.Bonus> bonuses() {
            var configured = config.value.entries.get(id().toString());
            return configured != null ? configured.attributes : defaults;
        }
    }

    static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    static RelicsConfig.Bonus flat(String attribute, double amount) {
        return new RelicsConfig.Bonus(attribute, amount, ADD);
    }

    static RelicsConfig.Bonus percent(String attribute, double amount) {
        return new RelicsConfig.Bonus(attribute, amount, MULT);
    }

    public static final Entry ENDER_DRAGON_TOOTH = add(new Entry(2, "ender_dragon_tooth"))
            .title("Ender Dragon Tooth")
            .lore("The Ender Dragon's Tooth's are scattered across end cities.")
            .bonuses(percent(ATTACK_DAMAGE, 0.05), percent(CRIT_DAMAGE, 0.05));
    public static final Entry CHARGED_AMETHYST = add(new Entry(2, "charged_amethyst"))
            .title("Charged Amethyst")
            .lore("Take a look at every amethyst cluster, there might be some charged crystals.")
            .bonuses(flat(LUCK, 1), percent(SPELL_ARCANE, 0.05));
    public static final Entry CORRUPTED_ENDER_PEARL = add(new Entry(3, "corrupted_ender_pearl"))
            .title("Corrupted Ender Pearl")
            .lore("Some Enderman carry a corrupted pearl from the end dimension.")
            .bonuses(percent(ATTACK_DAMAGE, 0.05), percent(SPELL_CRIT, 0.05), percent(CRIT_CHANCE, 0.05));
    public static final Entry ENDER_DRAGON_SCALES = add(new Entry(4, "ender_dragon_scales"))
            .title("Ender Dragon Scales")
            .lore("Be victorious against the Ender Dragon, use the dragon's scales to craft a powerful weapon!")
            .bonuses(percent(ATTACK_SPEED, 0.07), percent(SPELL_HASTE, 0.07), percent(SPELL_GENERIC, 0.10))
            .spell("ender_dragon_scales");

    public static final Entry UNKNOWN_REMAINS = add(new Entry(2, "unknown_remains"))
            .title("Unknown Remains")
            .lore("Even skeletons once had a name and were someone.")
            .bonuses(percent(ATTACK_SPEED, 0.05), percent(RANGED_HASTE, 0.05));
    public static final Entry LOST_SOUL = add(new Entry(2, "lost_soul"))
            .title("Lost Soul")
            .lore("Wither skeletons collect the souls of lost adventures.")
            .bonuses(flat(MAX_HEALTH, 2), percent(SPELL_SOUL, 0.05));
    public static final Entry WITHERED_OBSIDIAN_SHARD = add(new Entry(3, "withered_obsidian_shard"))
            .title("Withered Obsidian Shard")
            .lore("Piglins don't just like gold, they collect these magical shards in their bastion with their treasures.")
            .bonuses(flat(ARMOR, 3), flat(TOUGHNESS, 1), percent(DAMAGE_TAKEN, -0.05));
    public static final Entry WITHER_SPINE = add(new Entry(4, "wither_spine"))
            .title("Wither Spine")
            .lore("Stand strong against the Wither and take his remains to craft a powerful weapon!")
            .bonuses(flat(ARMOR, 3), flat(MAX_HEALTH, 4), percent(SPELL_SOUL, 0.10))
            .spell("wither_spine");

    public static final Entry AMPHITRITE_DIADEM = add(new Entry(2, "amphitrite_diadem"))
            .title("Amphitrite's Diadem")
            .lore("Find this shiny diadem, in buried shipwrecks, plundered by pirates.")
            .bonuses(flat(LUCK, 1), percent(SPELL_WATER, 0.05));
    public static final Entry RAINBOW_CORAL = add(new Entry(2, "rainbow_coral"))
            .title("Rainbow Coral")
            .lore("You may find these rare corals in buried treasure chests.")
            .bonuses(percent(ATTACK_SPEED, 0.05), percent(SPELL_WATER, 0.05));
    public static final Entry POSEIDONS_AMPHORA = add(new Entry(3, "poseidons_amphora"))
            .title("Poseidon's Amphora")
            .lore("In lost underwater ruins, you may find this ancient relic.")
            .bonuses(flat(MAX_HEALTH, 2), percent(HEALING_TAKEN, 0.05), percent(SPELL_WATER, 0.07));
    public static final Entry ELDER_GUARDIAN_EYE = add(new Entry(4, "elder_guardian_eye"))
            .title("Elder Guardian Eye")
            .lore("Slay the Elder Guardian to receive his eye and craft a powerful weapon!")
            .bonuses(percent(ATTACK_DAMAGE, 0.07), percent(RANGED_DAMAGE, 0.07), percent(DAMAGE_REFLECT, 0.10))
            .spell("elder_guardian_eye");

    public static void loadConfig() {
        config.refresh();
        for (var entry : entries) {
            config.value.entries.putIfAbsent(entry.id().toString(), new RelicsConfig.Entry(new ArrayList<>(entry.defaults())));
        }
        config.save();
    }

    public static void registerComponentHooks() {
        LNEEvents.get().modifyItemComponents(context -> {
            for (var entry : entries) {
                context.modify(entry.item().get(), builder -> builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributeComponent(entry)));
            }
        });
    }

    private static AttributeModifiersComponent attributeComponent(Entry entry) {
        var builder = AttributeModifiersComponent.builder();
        if (!TweaksConfig.relicAttributes()) {
            return builder.build();
        }
        for (var bonus : entry.bonuses()) {
            var attribute = Identifier.tryParse(bonus.attribute);
            if (attribute == null) continue;
            var resolved = Registries.ATTRIBUTE.getEntry(attribute);
            if (resolved.isEmpty()) continue;
            var operation = parseOperation(bonus.operation);
            var modifierId = Identifier.of(MOD_ID, "relic/" + entry.name() + "/" + attribute.getNamespace() + "_" + attribute.getPath());
            builder.add(resolved.get(), new EntityAttributeModifier(modifierId, bonus.amount, operation), AttributeModifierSlot.ANY);
        }
        return builder.build().withShowInTooltip(false);
    }

    private static EntityAttributeModifier.Operation parseOperation(String name) {
        try {
            return EntityAttributeModifier.Operation.valueOf(name);
        } catch (IllegalArgumentException e) {
            return EntityAttributeModifier.Operation.ADD_VALUE;
        }
    }

    public static void register() {
        FrozenDepthsRelics.register();
        loadConfig();
        for (var entry : entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item().get());
        }
        LNEEvents.get().modifyItemGroup(Group.RPG_LOOT_KEY, content -> {
            for (var entry : entries) {
                content.add(entry.item().get());
            }
        });
    }
}

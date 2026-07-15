package more_rpg_loot.item.relics;

import com.google.common.base.Suppliers;
import more_rpg_loot.compat.spell_engine.ISpellRelicEnhancer;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.RelicItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNE_RelicItems {

    public static BiFunction<Item.Settings, AttributeModifiersComponent, Item> factory =
            (settings, attrs) -> {
                if (attrs != null) settings = settings.attributeModifiers(attrs);
                return new RelicItem(settings);
            };

    @Nullable
    public static ISpellRelicEnhancer spellEnhancer = null;

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry e) {
        entries.add(e);
        return e;
    }

    public static final class Entry {
        private final int tier;
        private final String name;
        private final AttributeModifiersComponent vanillaAttributes;
        @Nullable private final Identifier spellId;
        private final Supplier<Item> item;

        public Entry(int tier, String name, AttributeModifiersComponent vanillaAttributes, @Nullable Identifier spellId) {
            this.tier = tier;
            this.name = name;
            this.vanillaAttributes = vanillaAttributes;
            this.spellId = spellId;
            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings().maxCount(1);
                var rarity = rarityFrom(tier);
                if (rarity != Rarity.COMMON) settings = settings.rarity(rarity);
                if (spellEnhancer != null) {
                    settings = spellEnhancer.enhance(settings, spellId);
                }
                return factory.apply(settings, vanillaAttributes);
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

        public Identifier id() { return Identifier.of(MOD_ID, name); }
        public String name() { return name; }
        public Supplier<Item> item() { return item; }
        @Nullable public Identifier spellId() { return spellId; }
    }

    private static AttributeModifiersComponent attackDamageBonus(double amount) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "relic_atk"), amount, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        AttributeModifierSlot.ANY)
                .build();
    }

    private static AttributeModifiersComponent attackSpeedBonus(double amount) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "relic_spd"), amount, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        AttributeModifierSlot.ANY)
                .build();
    }

    private static AttributeModifiersComponent armorBonus(double amount) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ARMOR,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "relic_armor"), amount, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.ANY)
                .build();
    }

    private static AttributeModifiersComponent toughnessBonus(double amount) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "relic_tough"), amount, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.ANY)
                .build();
    }

    private static AttributeModifiersComponent healthBonus(double amount) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "relic_hp"), amount, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.ANY)
                .build();
    }

    private static AttributeModifiersComponent luckBonus(double amount) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_LUCK,
                        new EntityAttributeModifier(Identifier.of(MOD_ID, "relic_luck"), amount, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.ANY)
                .build();
    }

    // ENDER DRAGON THEME
    public static final Entry ENDER_DRAGON_TOOTH = add(new Entry(2, "ender_dragon_tooth",
            attackDamageBonus(0.05), null));
    public static final Entry CHARGED_AMETHYST = add(new Entry(2, "charged_amethyst",
            luckBonus(1), null));
    public static final Entry CORRUPTED_ENDER_PEARL = add(new Entry(3, "corrupted_ender_pearl",
            luckBonus(1), Identifier.of(MOD_ID, "enderman_teleport")));
    public static final Entry ENDER_DRAGON_SCALES = add(new Entry(4, "ender_dragon_scales",
            attackSpeedBonus(0.05), Identifier.of(MOD_ID, "ender_dragon_scales")));

    // WITHER THEME
    public static final Entry UNKNOWN_REMAINS = add(new Entry(2, "unknown_remains",
            attackSpeedBonus(0.05), null));
    public static final Entry LOST_SOUL = add(new Entry(2, "lost_soul",
            healthBonus(2), null));
    public static final Entry WITHERED_OBSIDIAN_SHARD = add(new Entry(3, "withered_obsidian_shard",
            toughnessBonus(1), Identifier.of(MOD_ID, "wither_touch")));
    public static final Entry WITHER_SPINE = add(new Entry(4, "wither_spine",
            armorBonus(2), Identifier.of(MOD_ID, "wither_spine")));

    // GLACIAL THEME
    public static final Entry ETERNAL_SNOWFLAKE = add(new Entry(2, "eternal_snowflake",
            luckBonus(1), null));
    public static final Entry GLACIER_SHARD = add(new Entry(2, "glacier_shard",
            armorBonus(2), null));
    public static final Entry FROZEN_RIB = add(new Entry(3, "frozen_rib",
            attackSpeedBonus(0.05), Identifier.of(MOD_ID, "frozen_touch")));
    public static final Entry FROZEN_SOUL = add(new Entry(4, "frozen_soul",
            healthBonus(2), Identifier.of(MOD_ID, "frozen_soul")));

    // OCEAN THEME
    public static final Entry AMPHITRITE_DIADEM = add(new Entry(2, "amphitrite_diadem",
            luckBonus(1), null));
    public static final Entry RAINBOW_CORAL = add(new Entry(2, "rainbow_coral",
            attackSpeedBonus(0.05), null));
    public static final Entry POSEIDONS_AMPHORA = add(new Entry(3, "poseidons_amphora",
            healthBonus(2), Identifier.of(MOD_ID, "poseidons_grace")));
    public static final Entry ELDER_GUARDIAN_EYE = add(new Entry(4, "elder_guardian_eye",
            attackDamageBonus(0.05), Identifier.of(MOD_ID, "elder_guardian_eye")));

    public static void register() {
        for (var entry : entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item().get());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.RPG_LOOT_KEY).register(content -> {
            for (var entry : entries) {
                content.add(entry.item().get());
            }
        });
    }
}

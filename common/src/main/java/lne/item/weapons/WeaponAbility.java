package lne.item.weapons;

import lne.ability.AbilityTheme;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.ability.AbilityDamage;
import lne.ability.RelicText;
import lne.ability.TriggerType;
import lne.item.relics.RelicTooltips;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static lne.LneMod.MOD_ID;

public enum WeaponAbility {
    ENDER_DRAGON("ender_dragon", 300),
    WITHER("wither", 240),
    GLACIAL("glacial", 360),
    ELDER_GUARDIAN("elder_guardian", 200);

    public enum Slot {
        MELEE("melee", TriggerType.MELEE_HIT),
        RANGED("ranged", TriggerType.ARROW_HIT),
        BLOCK("block", TriggerType.BLOCK);

        public final String id;
        public final TriggerType trigger;

        Slot(String id, TriggerType trigger) {
            this.id = id;
            this.trigger = trigger;
        }

        public static Slot of(TriggerType trigger) {
            return trigger == TriggerType.ARROW_HIT ? RANGED : trigger == TriggerType.BLOCK ? BLOCK : MELEE;
        }
    }

    public static final String CHANCE = "chance";
    public static final String COOLDOWN = "cooldown_ticks";
    public static final List<TriggerType> TRIGGERS = List.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.BLOCK);

    private static Map<Item, WeaponAbility> byItem;

    private final String id;
    private final int cooldownTicks;

    WeaponAbility(String id, int cooldownTicks) {
        this.id = id;
        this.cooldownTicks = cooldownTicks;
    }

    public AbilityTheme theme() {
        return switch (this) {
            case ENDER_DRAGON -> AbilityTheme.ENDER_DRAGON;
            case WITHER -> AbilityTheme.WITHER;
            case GLACIAL -> AbilityTheme.GLACIAL;
            case ELDER_GUARDIAN -> AbilityTheme.ELDER_GUARDIAN;
        };
    }

    public String key(Slot slot) {
        return id + "." + slot.id;
    }

    public Map<String, Double> defaultValues(Slot slot) {
        var values = new LinkedHashMap<String, Double>();
        values.put(CHANCE, 0.2);
        values.put(COOLDOWN, (double) cooldownTicks);
        switch (this) {
            case ENDER_DRAGON -> {
                switch (slot) {
                    case MELEE -> damage(values, 6.0, 0.10);
                    case RANGED -> {
                        values.put("radius", 5.0);
                        damage(values, 6.0, 0.10);
                    }
                    case BLOCK -> {
                        values.put("radius", 2.0);
                        damage(values, 2.0, 0.10);
                        values.put("knockback_strength", 1.6);
                    }
                }
            }
            case WITHER -> {
                switch (slot) {
                    case MELEE -> {
                        values.put("radius", 3.0);
                        damage(values, 4.0, 0.10);
                        wither(values);
                    }
                    case RANGED -> {
                        values.put("radius", 7.0);
                        damage(values, 2.0, 0.10);
                        wither(values);
                    }
                    case BLOCK -> {
                        values.put("skulls", 3.0);
                        values.put("orbit_radius", 2.0);
                        values.put("duration_ticks", 160.0);
                        values.put("explosion_radius", 2.0);
                        damage(values, 4.0, 0.10);
                    }
                }
            }
            case GLACIAL -> {
                switch (slot) {
                    case MELEE -> {
                        values.put("radius", 3.0);
                        values.put("duration_ticks", 100.0);
                        values.put("freezing_ticks", 60.0);
                        damage(values, 2.0, 0.10);
                    }
                    case RANGED -> values.put("frostballs", 5.0);
                    case BLOCK -> {
                        values.put("length", 10.0);
                        values.put("spacing", 1.25);
                        damage(values, 4.0, 0.10);
                    }
                }
            }
            case ELDER_GUARDIAN -> {
                switch (slot) {
                    case MELEE -> {
                        values.put("radius", 5.0);
                        values.put("duration_ticks", 100.0);
                        damage(values, 2.0, 0.15);
                    }
                    case RANGED -> {
                        values.put("bubbles", 3.0);
                        values.put("radius", 2.5);
                        values.put("delay_ticks", 8.0);
                        values.put("slowness_ticks", 40.0);
                        damage(values, 3.0, 0.10);
                    }
                    case BLOCK -> {
                        values.put("radius", 4.0);
                        values.put("duration_ticks", 100.0);
                        values.put("push_strength", 0.35);
                        damage(values, 1.0, 0.05);
                    }
                }
            }
        }
        return values;
    }

    private static void damage(Map<String, Double> values, double base, double scaling) {
        values.put("base_damage", base);
        values.put("damage_scaling", scaling);
    }

    private static void wither(Map<String, Double> values) {
        values.put("wither_ticks", 100.0);
        values.put("amplifier_scaling", 0.10);
        values.put("max_amplifier", 3.0);
    }

    public boolean enabled(Slot slot) {
        var entry = WeaponAbilities.config.value.abilities.get(key(slot));
        return entry == null || entry.enabled;
    }

    public double value(Slot slot, String key) {
        var entry = WeaponAbilities.config.value.abilities.get(key(slot));
        if (entry != null && entry.values.containsKey(key)) {
            return entry.values.get(key);
        }
        return defaultValues(slot).getOrDefault(key, 0.0);
    }

    public int ticks(Slot slot, String key) {
        return (int) Math.round(value(slot, key));
    }

    @Nullable
    public static WeaponAbility ofItem(Item item) {
        if (byItem == null) {
            var map = new HashMap<Item, WeaponAbility>();
            Stream.of(LNE_WeaponItems.entries, LNE_WeaponItems.maceEntries, LNE_WeaponItems.rangedEntries)
                    .flatMap(List::stream)
                    .forEach(entry -> putByName(map, entry.name(), entry.item()));
            LNE_ShieldItems.entries.forEach(entry -> putByName(map, entry.name(), entry.item()));
            byItem = map;
        }
        return byItem.get(item);
    }

    private static void putByName(Map<Item, WeaponAbility> map, String name, Item item) {
        for (var ability : values()) {
            if (name.startsWith(ability.id + "_")) map.put(item, ability);
        }
    }

    public int cooldownTicks(Slot slot) {
        return Math.max(1, ticks(slot, COOLDOWN));
    }

    public boolean roll(Slot slot, Random random) {
        return enabled(slot) && random.nextDouble() < value(slot, CHANCE);
    }

    public String langKey(TriggerType trigger) {
        return MOD_ID + ".weapon." + key(Slot.of(trigger));
    }

    public String nameKey(TriggerType trigger) {
        return langKey(trigger) + ".name";
    }

    public String name(TriggerType trigger) {
        var slot = Slot.of(trigger);
        return switch (this) {
            case ENDER_DRAGON -> switch (slot) {
                case MELEE -> "Draconic Drain";
                case RANGED -> "Dragonfire Burst";
                case BLOCK -> "Wingbeat Repulse";
            };
            case WITHER -> switch (slot) {
                case MELEE -> "Withering Aura";
                case RANGED -> "Blight Burst";
                case BLOCK -> "Skull Guard";
            };
            case GLACIAL -> switch (slot) {
                case MELEE -> "Frozen Tempest";
                case RANGED -> "Hailfall";
                case BLOCK -> "Glacial Rampart";
            };
            case ELDER_GUARDIAN -> switch (slot) {
                case MELEE -> "Tidal Downpour";
                case RANGED -> "Bubble Barrage";
                case BLOCK -> "Maelstrom";
            };
        };
    }

    public String description(TriggerType trigger) {
        var slot = Slot.of(trigger);
        return switch (this) {
            case ENDER_DRAGON -> switch (slot) {
                case MELEE -> "%s chance to drain %s magic damage from your target, healing you for the same amount.";
                case RANGED -> "%s chance to unleash a %s block dragon breath explosion at your target, dealing %s magic damage.";
                case BLOCK -> "%s chance to release a %s block dragon breath explosion that deals %s magic damage and knocks nearby enemies back.";
            };
            case WITHER -> switch (slot) {
                case MELEE -> "%s chance to create a %s block withering aura around your target, dealing %s magic damage and inflicting Wither %s.";
                case RANGED -> "%s chance to cause a %s block withering explosion at your target, dealing %s magic damage and stacking Wither up to level %s.";
                case BLOCK -> "%s chance to summon %s wither skulls that circle around you for %s. They explode on contact with enemies for %s magic damage without harming terrain.";
            };
            case GLACIAL -> switch (slot) {
                case MELEE -> "%s chance to summon a %s block freezing storm at your target for %s, dealing %s magic damage every second and inflicting Freezing.";
                case RANGED -> "%s chance to summon a ring of %s frostballs above your target.";
                case BLOCK -> "%s chance to raise a %s block line of icicles from the ground in front of you, dealing %s magic damage and freezing enemies.";
            };
            case ELDER_GUARDIAN -> switch (slot) {
                case MELEE -> "%s chance to conjure a %s block rain storm around your target for %s, dealing %s magic damage about once per second.";
                case RANGED -> "%s chance to release %s bubbles around your target that burst one after another, each dealing %s magic damage within %s blocks and slowing enemies.";
                case BLOCK -> "%s chance to summon a swirling whirlpool around you for %s that pushes enemies away and deals %s magic damage every second.";
            };
        };
    }

    private String damageText(Slot slot, @Nullable LivingEntity viewer) {
        double base = value(slot, "base_damage");
        return RelicText.number(viewer == null ? base : AbilityDamage.scaled(viewer, base, value(slot, "damage_scaling")));
    }

    private Text witherLevel(Slot slot, @Nullable LivingEntity viewer) {
        int amplifier = viewer == null ? 0 : AbilityDamage.scaledAmplifier(viewer, value(slot, "amplifier_scaling"), (int) value(slot, "max_amplifier"));
        return Text.translatable("enchantment.level." + (amplifier + 1));
    }

    public Object[] descriptionArgs(Slot slot, @Nullable LivingEntity viewer) {
        var chance = RelicText.percent(value(slot, CHANCE));
        var damage = damageText(slot, viewer);
        var radius = RelicText.number(value(slot, "radius"));
        var duration = RelicText.seconds(value(slot, "duration_ticks"));
        return switch (this) {
            case ENDER_DRAGON -> slot == Slot.MELEE ? new Object[]{chance, damage} : new Object[]{chance, radius, damage};
            case WITHER -> slot == Slot.BLOCK
                    ? new Object[]{chance, RelicText.number(value(slot, "skulls")), duration, damage}
                    : new Object[]{chance, radius, damage, witherLevel(slot, viewer)};
            case GLACIAL -> switch (slot) {
                case MELEE -> new Object[]{chance, radius, duration, damage};
                case RANGED -> new Object[]{chance, RelicText.number(value(slot, "frostballs"))};
                case BLOCK -> new Object[]{chance, RelicText.number(value(slot, "length")), damage};
            };
            case ELDER_GUARDIAN -> switch (slot) {
                case MELEE -> new Object[]{chance, radius, duration, damage};
                case RANGED -> new Object[]{chance, RelicText.number(value(slot, "bubbles")), damage, radius};
                case BLOCK -> new Object[]{chance, duration, damage};
            };
        };
    }

    public void appendTooltip(TriggerType trigger, List<Text> tooltip) {
        var slot = Slot.of(trigger);
        if (!enabled(slot)) return;
        var description = Text.translatable(langKey(trigger), descriptionArgs(slot, RelicTooltips.viewer()));
        RelicTooltips.appendAbilityBlock(tooltip, false, List.of(trigger), theme().style(Text.translatable(nameKey(trigger))), description, List.of(), cooldownTicks(slot));
    }
}

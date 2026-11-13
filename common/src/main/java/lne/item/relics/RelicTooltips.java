package lne.item.relics;

import lne.platform.LNEPlatform;
import org.jetbrains.annotations.Nullable;
import lne.ability.RelicAbility;
import lne.ability.RelicText;
import lne.ability.RelicTriggers;
import lne.ability.TriggerType;
import lne.ability.impl.DragonToothAbility;
import lne.ability.impl.StackBuffAbility;
import lne.config.TweaksConfig;
import lne.effects.Effects;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static lne.LneMod.MOD_ID;

public final class RelicTooltips {
    public static final String BONUSES_KEY = MOD_ID + ".relic.bonuses";
    public static final String PASSIVE_KEY = MOD_ID + ".relic.passive";
    public static final String ACTIVE_KEY = MOD_ID + ".relic.active";
    public static final String COOLDOWN_KEY = MOD_ID + ".relic.cooldown";
    public static final String STACKS_KEY = MOD_ID + ".relic.stacks";
    public static final String ON_COOLDOWN_KEY = MOD_ID + ".relic.on_cooldown";
    public static final String DEALING_DAMAGE_KEY = MOD_ID + ".relic.trigger.dealing_damage";
    public static final String OFF_HAND_KEY = MOD_ID + ".relic.passive_slot.off_hand";
    public static final String RELIC_SLOT_KEY = MOD_ID + ".relic.passive_slot.relic_slot";
    private static final Set<TriggerType> DAMAGE_DEALT_TRIGGERS = EnumSet.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.SPELL_HIT);
    private static Supplier<LivingEntity> viewer = () -> null;

    private RelicTooltips() {
    }

    public static void setViewer(Supplier<LivingEntity> supplier) {
        viewer = supplier;
    }

    @Nullable
    public static LivingEntity viewer() {
        return viewer.get();
    }

    public static void append(ItemStack stack, List<Text> tooltip, Formatting loreColor) {
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable(stack.getItem().getTranslationKey() + ".lore").formatted(loreColor, Formatting.ITALIC));

        appendBonuses(stack, tooltip);
        appendAbility(stack, tooltip);
        appendSpellStacks(stack, tooltip);
    }

    private static void appendSpellStacks(ItemStack stack, List<Text> tooltip) {
        if (!TweaksConfig.relicAbilities() || !TweaksConfig.spellEngineAbilities()) return;
        if (!(RelicTriggers.abilityOf(stack) instanceof DragonToothAbility)) return;
        var entity = viewer();
        var instance = entity == null ? null : entity.getStatusEffect(Effects.RENDING_FANG.registryEntry);
        int stacks = instance == null ? 0 : instance.getAmplifier() + 1;
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable(STACKS_KEY, stacks, Effects.RENDING_FANG_MAX_STACKS).formatted(Formatting.YELLOW));
    }

    private static void appendBonuses(ItemStack stack, List<Text> tooltip) {
        var component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null || component.modifiers().isEmpty()) return;
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable(BONUSES_KEY).formatted(Formatting.GRAY));
        for (var entry : component.modifiers()) {
            var modifier = entry.modifier();
            double value = modifier.value();
            boolean flat = modifier.operation() == EntityAttributeModifier.Operation.ADD_VALUE;
            double shown = flat ? value : value * 100;
            String prefix = value >= 0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
            var line = Text.translatable(prefix + modifier.operation().getId(), RelicText.number(Math.abs(shown)),
                    Text.translatable(entry.attribute().value().getTranslationKey()));
            tooltip.add(line.formatted(value >= 0 ? Formatting.BLUE : Formatting.RED));
        }
    }

    private static void appendAbility(ItemStack stack, List<Text> tooltip) {
        var ability = RelicTriggers.abilityOf(stack);
        if (ability == null || !ability.enabled()) return;
        var cached = RelicStackCache.get(ability.relic());
        var extra = new ArrayList<Text>();
        if (ability instanceof StackBuffAbility buff) {
            int stacks = cached == null ? 0 : cached.stacks();
            int max = cached == null ? buff.stackLimit() : cached.max();
            extra.add(Text.translatable(STACKS_KEY, stacks, max).formatted(Formatting.YELLOW));
        }
        RelicAbility.Progress progress = ability.progress();
        if (progress != null) {
            int current = cached == null ? 0 : cached.progress();
            int goal = cached == null || cached.goal() <= 0 ? progress.goal() : cached.goal();
            extra.add(Text.translatable(progress.labelKey(), current, goal).formatted(Formatting.YELLOW));
        }
        tooltip.add(ScreenTexts.EMPTY);
        var head = header(ability.isActive(), ability.triggers());
        tooltip.add(ability.isActive() ? head : head.copy().append(passiveSlotHint()));
        if (ability.hybrid()) {
            tooltip.add(header(false, ability.passiveTriggers()).copy().append(passiveSlotHint()));
        }
        tooltip.add(ability.theme().style(Text.translatable(ability.nameKey())));
        tooltip.add(Text.translatable(ability.langKey(), ability.descriptionArgs(viewer.get())).formatted(Formatting.GRAY));
        tooltip.addAll(extra);
        appendCooldown(tooltip, ability.cooldownTicks());
    }

    public static void appendAbilityBlock(List<Text> tooltip, boolean active, Collection<TriggerType> triggers, Text name, Text description,
                                          List<Text> extraLines, int cooldownTicks) {
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(header(active, triggers));
        tooltip.add(name);
        tooltip.add(description.copy().formatted(Formatting.GRAY));
        tooltip.addAll(extraLines);
        appendCooldown(tooltip, cooldownTicks);
    }

    private static Text header(boolean active, Collection<TriggerType> triggers) {
        var header = Text.translatable(active ? ACTIVE_KEY : PASSIVE_KEY).append(Text.literal(" - "));
        boolean collapse = triggers.containsAll(DAMAGE_DEALT_TRIGGERS);
        boolean first = true;
        if (collapse) {
            header.append(Text.translatable(DEALING_DAMAGE_KEY));
            first = false;
        }
        for (var trigger : triggers) {
            if (collapse && DAMAGE_DEALT_TRIGGERS.contains(trigger)) continue;
            if (!first) header.append(Text.literal(" / "));
            header.append(Text.translatable(trigger.langKey()));
            first = false;
        }
        return header.formatted(active ? Formatting.GOLD : Formatting.AQUA);
    }

    private static Text passiveSlotHint() {
        return Text.translatable(LNEPlatform.hasAccessorySlots() ? RELIC_SLOT_KEY : OFF_HAND_KEY).formatted(Formatting.DARK_GRAY);
    }

    private static void appendCooldown(List<Text> tooltip, int cooldownTicks) {
        if (cooldownTicks > 0) {
            tooltip.add(Text.translatable(COOLDOWN_KEY, RelicText.seconds(cooldownTicks)).formatted(Formatting.DARK_GRAY));
        }
    }
}

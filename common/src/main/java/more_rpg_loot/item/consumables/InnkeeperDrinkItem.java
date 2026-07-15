package more_rpg_loot.item.consumables;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class InnkeeperDrinkItem extends Item {
    private final RegistryEntry<StatusEffect>  boost_effect_0;
    private final int quality;

    public InnkeeperDrinkItem(Item.Settings settings,RegistryEntry<StatusEffect>  boostEffect, int quality) {
        super(settings);
        boost_effect_0 = boostEffect;
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        int effectDuration = (60 * 20) * (quality + 1);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        ItemStack resultStack = stack;
        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            resultStack = stack.copy();
            resultStack.decrement(1);

            if (resultStack.isEmpty()) {
                resultStack = new ItemStack(Items.GLASS_BOTTLE);
            } else {
                player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                    effectDuration, 0, false, false, true));
            if (quality == 3) {
                user.heal(user.getMaxHealth());
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION,
                        2400, 3, false, false, true));
            }
        }
        return resultStack;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if(quality == 0){
            return false;
        } else if(quality == 1) {
            return false;
        }
        else if(quality == 2) {
            return true;
        }
        else if(quality == 3) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        Formatting formatting = null;
        if(quality == 0){
            formatting = Formatting.BLUE;
        } else if(quality == 1) {
            formatting = Formatting.DARK_PURPLE;
        } else if(quality == 2) {
            formatting = Formatting.YELLOW;
        }else if(quality == 3) {
            formatting = Formatting.DARK_RED;
        }
        else{
            formatting = Formatting.WHITE;
        }

        String z = "item." + boost_effect_0.getIdAsString() + ".lore";
        z = z.replace(":",".");

        if(quality == 3) {
            tooltip.add(Text.translatable(z).formatted(formatting));
        }
        /// SHOW Attribute Modifiers
        int effectDuration = (60) * (quality +1);
        List<StatusEffectInstance> effects = List.of(new StatusEffectInstance(boost_effect_0, effectDuration, 0));
        if (effects.isEmpty()) {
            return;
        }
        List<com.mojang.datafixers.util.Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> modifiers = new ArrayList<>();
        boolean hasEffect = false;
        for (StatusEffectInstance instance : effects) {
            if (instance == null || instance.getEffectType() == null) continue;

            RegistryEntry<StatusEffect> effectEntry = instance.getEffectType();
            StatusEffect effect = effectEntry.value();
            hasEffect = true;

            MutableText effectText = Text.translatable(instance.getTranslationKey());
            int amplifier = instance.getAmplifier();
            if (amplifier > 0) {
                effectText = Text.translatable("potion.withAmplifier", effectText, Text.translatable("potion.potency." + amplifier));
            }
            if (!instance.isDurationBelow(20)) {
                effectText = Text.translatable("potion.withDuration", effectText, StatusEffectUtil.getDurationText(instance, 1.0f, 1.0f));
            }
            tooltip.add(effectText.formatted(effect.getCategory().getFormatting()));

            effect.forEachAttributeModifier(amplifier, (attribute, modifier) -> {
                modifiers.add(com.mojang.datafixers.util.Pair.of(attribute, modifier));
            });
        }
        if (!hasEffect) {
            tooltip.add(Text.translatable("effect.none").formatted(Formatting.GRAY));
        }
        if (!modifiers.isEmpty()) {
            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

            for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : modifiers) {
                RegistryEntry<EntityAttribute> attrEntry = pair.getFirst();
                EntityAttributeModifier modifier = pair.getSecond();

                double value = modifier.value();
                double displayValue = switch (modifier.operation()) {
                    case ADD_MULTIPLIED_BASE, ADD_MULTIPLIED_TOTAL -> value * 100.0;
                    case ADD_VALUE -> value;
                };
                Text attrText = Text.translatable(attrEntry.value().getTranslationKey());
                Text line = value > 0
                        ? Text.translatable("attribute.modifier.plus." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(displayValue), attrText).formatted(Formatting.BLUE)
                        : Text.translatable("attribute.modifier.take." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(-displayValue), attrText).formatted(Formatting.RED);
                tooltip.add(line);
            }
        }
    }
}

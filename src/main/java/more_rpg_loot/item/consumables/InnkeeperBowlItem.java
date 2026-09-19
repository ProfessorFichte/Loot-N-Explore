package more_rpg_loot.item.consumables;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InnkeeperBowlItem extends Item {
    private final StatusEffect boost_effect_0;
    private final int quality;

    public InnkeeperBowlItem(Item.Settings settings,StatusEffect  boostEffect, int quality) {
        super(settings);
        boost_effect_0 = boostEffect;
        this.quality = quality;
    }



    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        int effectDuration = (60 * 20) * (quality + 1);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        ItemStack resultStack = super.finishUsing(stack, world, user);
        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(boost_effect_0, effectDuration, 0, false, false, true));
            if (quality == 3) {
                user.heal(user.getMaxHealth());
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3, false, false, true));
            }
        }
        if (resultStack.isEmpty()) {
            return new ItemStack(Items.BOWL);
        }
        if (user instanceof PlayerEntity playerEntity) {
            playerEntity.getInventory().insertStack(new ItemStack(Items.BOWL));
        }
        return resultStack;
    }

    // 1.20.1: getMaxUseTime(ItemStack)
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
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
    // 1.20.1: appendTooltip(stack, @Nullable World, tooltip, net.minecraft.client.item.TooltipContext)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
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

        String z = "item." + Registries.STATUS_EFFECT.getId(boost_effect_0).toString() + ".lore";
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
        // 1.20.1: attribute modifiers are keyed by the raw EntityAttribute, not a RegistryEntry.
        List<Pair<EntityAttribute, EntityAttributeModifier>> modifiers = new ArrayList<>();
        boolean hasEffect = false;
        for (StatusEffectInstance instance : effects) {
            if (instance == null || instance.getEffectType() == null) continue;

            StatusEffect effectEntry = instance.getEffectType();
            StatusEffect effect = effectEntry;
            hasEffect = true;

            MutableText effectText = Text.translatable(instance.getTranslationKey());
            int amplifier = instance.getAmplifier();
            if (amplifier > 0) {
                effectText = Text.translatable("potion.withAmplifier", effectText, Text.translatable("potion.potency." + amplifier));
            }
            if (!instance.isDurationBelow(20)) {
                effectText = Text.translatable("potion.withDuration", effectText, StatusEffectUtil.getDurationText(instance, 1.0f));
            }
            tooltip.add(effectText.formatted(effect.getCategory().getFormatting()));

            // 1.20.1: no forEachAttributeModifier -- read the map and scale by amplifier the way
            // vanilla's own PotionUtil#buildTooltip does.
            for (var attributeEntry : effect.getAttributeModifiers().entrySet()) {
                EntityAttributeModifier base = attributeEntry.getValue();
                modifiers.add(Pair.of(attributeEntry.getKey(),
                        new EntityAttributeModifier(base.getName(), effect.adjustModifierAmount(amplifier, base), base.getOperation())));
            }
        }
        if (!hasEffect) {
            tooltip.add(Text.translatable("effect.none").formatted(Formatting.GRAY));
        }
        if (!modifiers.isEmpty()) {
            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

            for (Pair<EntityAttribute, EntityAttributeModifier> pair : modifiers) {
                EntityAttribute attrEntry = pair.getFirst();
                EntityAttributeModifier modifier = pair.getSecond();

                double value = modifier.getValue();
                double displayValue = switch (modifier.getOperation()) {
                    case MULTIPLY_BASE, MULTIPLY_TOTAL -> value * 100.0;
                    case ADDITION -> value;
                };
                Text attrText = Text.translatable(attrEntry.getTranslationKey());
                Text line = value > 0
                        ? Text.translatable("attribute.modifier.plus." + modifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(displayValue), attrText).formatted(Formatting.BLUE)
                        : Text.translatable("attribute.modifier.take." + modifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(-displayValue), attrText).formatted(Formatting.RED);
                tooltip.add(line);
            }
        }
    }
}

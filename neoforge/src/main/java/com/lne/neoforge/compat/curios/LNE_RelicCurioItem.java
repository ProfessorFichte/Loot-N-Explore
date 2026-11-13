package com.lne.neoforge.compat.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lne.ability.RelicTriggers;
import lne.item.relics.RelicTooltips;
import lne.platform.LNEPlatform;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class LNE_RelicCurioItem extends Item implements ICurioItem {
    public LNE_RelicCurioItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getAttributeModifiers(SlotContext slotContext, Identifier id, ItemStack stack) {
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers = LinkedHashMultimap.create();
        modifiers.putAll(ICurioItem.super.getAttributeModifiers(slotContext, id, stack));
        var component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null) return modifiers;
        for (var entry : component.modifiers()) {
            var modifierId = id.withSuffixedPath("/" + entry.modifier().id().getPath().replace('/', '_'));
            modifiers.put(entry.attribute(),
                    new EntityAttributeModifier(modifierId, entry.modifier().value(), entry.modifier().operation()));
        }
        return modifiers;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return RelicTriggers.use(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return RelicTriggers.maxUseTime(stack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return RelicTriggers.maxUseTime(stack) > 0 ? UseAction.BOW : UseAction.NONE;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        RelicTriggers.usageTick(user, stack, remainingUseTicks);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (slotContext.identifier().equals("charm") && LNEPlatform.isModLoaded("spell_engine")) {
            return false;
        }
        return ICurioItem.super.canEquip(slotContext, stack);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return ICurioItem.super.canUnequip(slotContext, stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        RelicTriggers.stopChanneling(user, stack, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        RelicTriggers.stopChanneling(user, stack, 0);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        RelicTooltips.append(stack, tooltip, Formatting.GOLD);
    }
}

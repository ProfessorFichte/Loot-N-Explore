package lne.compat.trinkets;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import lne.ability.RelicTriggers;
import lne.item.relics.RelicTooltips;
import lne.platform.LNEPlatform;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.List;

public class LNETrinketItem extends TrinketItem {
    public LNETrinketItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier slotIdentifier) {
        var modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
        var component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null) return modifiers;
        for (var entry : component.modifiers()) {
            var id = slotIdentifier.withSuffixedPath("/" + entry.modifier().id().getPath().replace('/', '_'));
            modifiers.put(entry.attribute(), new EntityAttributeModifier(id, entry.modifier().value(), entry.modifier().operation()));
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
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var type = slot.inventory().getSlotType();
        boolean necklace = type.getGroup().equals("chest") && type.getName().equals("necklace");
        if (necklace && LNEPlatform.isModLoaded("spell_engine")) {
            return false;
        }
        return super.canEquip(stack, slot, entity);
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return super.canUnequip(stack, slot, entity);
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

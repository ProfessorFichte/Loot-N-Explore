package more_rpg_loot.item.consumables;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class InnkeeperDrinkItem extends Item {
    private final RegistryEntry<StatusEffect>  boost_effect_0;
    private final int quality;

    public InnkeeperDrinkItem(Item.Settings settings,RegistryEntry<StatusEffect>  boostEffect, int quality) {
        super(settings);
        boost_effect_0 = boostEffect;
        this.quality = quality;
    }


    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        int effectDuration = (200 * 20) * (quality +1);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (user instanceof PlayerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            stack.decrement(1);
                if (stack.isEmpty()) {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                if (user instanceof PlayerEntity playerEntity) {
                    playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
                }
        }

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                    effectDuration, 0, false, false, true));
            if(quality == 3){
                user.heal(user.getMaxHealth());
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION,
                        2400, 3, false, false, true));
            }
        }
        return stack;
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

        String y = "effect." + boost_effect_0.getIdAsString() + ".description";
        String z = "item." + boost_effect_0.getIdAsString() + ".lore";
        y = y.replace(":",".");
        z = z.replace(":",".");
        tooltip.add(Text.translatable(y).formatted(formatting));
        if(quality == 3) {
            tooltip.add(Text.translatable(z).formatted(formatting));
        }
    }
}

package more_rpg_loot.item.consumables;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
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

public class InnkeeperBowlItem extends Item {
    private final RegistryEntry<StatusEffect> boost_effect_0;
    private final int quality;

    public InnkeeperBowlItem(Item.Settings settings,RegistryEntry<StatusEffect>  boostEffect, int quality) {
        super(settings);
        boost_effect_0 = boostEffect;
        this.quality = quality;
    }



    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        int effectDuration = (200 * 20) * (quality +1);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        super.finishUsing(stack, world, user);
        if (stack.isEmpty()) {
            return new ItemStack(Items.BOWL);
        }
        if (user instanceof PlayerEntity playerEntity) {
            playerEntity.getInventory().insertStack(new ItemStack(Items.BOWL));
        }

        if (!world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                    effectDuration, 0, false, false, true));
        }
        return stack;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public Rarity rarity(Rarity rarity) {
        if(quality == 0){
            return Rarity.UNCOMMON;
        } else if(quality == 1) {
            return Rarity.RARE;
        }
        else if(quality == 2) {
            return Rarity.EPIC;
        }else{
            return Rarity.COMMON;
        }
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
        }
        else if(quality == 2) {
            formatting = Formatting.YELLOW;
        }else{
            formatting = Formatting.WHITE;
        }

        String y = "effect." + boost_effect_0.getIdAsString() + ".description";
        y = y.replace(":",".");
        tooltip.add(Text.translatable(y).formatted(formatting));
    }
}

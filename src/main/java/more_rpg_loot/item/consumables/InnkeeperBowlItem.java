package more_rpg_loot.item.consumables;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import more_rpg_loot.effects.Effects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class InnkeeperBowlItem extends Item {
    private final StatusEffect boost_effect_0;
    private final StatusEffect boost_effect_1;
    private final StatusEffect boost_effect_2;
    private final int quality;

    public InnkeeperBowlItem(Settings settings, StatusEffect boostEffect0, StatusEffect boostEffect1, StatusEffect boostEffect2, int quality) {
        super(settings);
        boost_effect_0 = boostEffect0;
        boost_effect_1 = boostEffect1;
        boost_effect_2 = boostEffect2;
        this.quality = quality;
    }
    int effectDuration = 0;
    int effectAmplifier = 0;

    int effectDuration_0 = 6000;
    int effectDuration_1 = 6000;
    int effectDuration_2 = 6000;
    int amplifier_0 = 0;
    int amplifier_1 = 0;
    int amplifier_2 = 0;
    int durationNauseaProviantCap = 600;
    int amplifierProviantCap = 2;


    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (user instanceof PlayerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            stack.decrement(1);
        }
        if (!world.isClient) {
            if(quality == 0){
                effectAmplifier = amplifier_0; effectDuration =effectDuration_0;
            } else if(quality == 1) {
                effectAmplifier = amplifier_1; effectDuration =effectDuration_1;
            }
            else if(quality == 2) {
                effectAmplifier = amplifier_2; effectDuration =effectDuration_2;
            }else{
                effectAmplifier = amplifier_0; effectDuration =effectDuration_0;
            }


            if(!user.hasStatusEffect(Effects.INNKEEPERS_PROVIANT)){
                user.addStatusEffect(new StatusEffectInstance(Effects.INNKEEPERS_PROVIANT,
                        effectDuration,0,false,false,true));
                //BOOST_EFFECT_0
                if(!user.hasStatusEffect(boost_effect_0)){
                    user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                            effectDuration,effectAmplifier,false,false,true));
                }else{
                    int currentEffectAmplifier = user.getStatusEffect(boost_effect_0).getAmplifier();
                    user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                            effectDuration,currentEffectAmplifier + 1,false,false,true));
                }
                //BOOST_EFFECT_1
                if(boost_effect_1 != null){
                    if(!user.hasStatusEffect(boost_effect_1)){
                        user.addStatusEffect(new StatusEffectInstance(boost_effect_1,
                                effectDuration,effectAmplifier,false,false,true));
                    }else{
                        int currentEffectAmplifier = user.getStatusEffect(boost_effect_1).getAmplifier();
                        user.addStatusEffect(new StatusEffectInstance(boost_effect_1,
                                effectDuration,currentEffectAmplifier + 1,false,false,true));
                    }
                }
                //BOOST_EFFECT_2
                if(boost_effect_2 != null){
                    if(!user.hasStatusEffect(boost_effect_2)){
                        user.addStatusEffect(new StatusEffectInstance(boost_effect_2,
                                effectDuration,effectAmplifier,false,false,true));
                    }else{
                        int currentEffectAmplifier = user.getStatusEffect(boost_effect_2).getAmplifier();
                        user.addStatusEffect(new StatusEffectInstance(boost_effect_2,
                                effectDuration,currentEffectAmplifier + 1,false,false,true));
                    }
                }
            }
            else{
                int amplifierHydrated = user.getStatusEffect(Effects.INNKEEPERS_PROVIANT).getAmplifier();
                //HYDRATION_CHECK_CAP
                if(amplifierHydrated >= amplifierProviantCap){
                    user.clearStatusEffects();
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,
                            durationNauseaProviantCap,effectAmplifier,false,false,true));
                }else{
                    user.addStatusEffect(new StatusEffectInstance(Effects.INNKEEPERS_PROVIANT,
                            effectDuration,amplifierHydrated + 1,false,false,true));
                    //BOOST_EFFECT_0
                    if(!user.hasStatusEffect(boost_effect_0)){
                        user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                                effectDuration,effectAmplifier,false,false,true));
                    }else{
                        int currentEffectAmplifier = user.getStatusEffect(boost_effect_0).getAmplifier();
                        user.addStatusEffect(new StatusEffectInstance(boost_effect_0,
                                effectDuration,currentEffectAmplifier + 1,false,false,true));
                    }
                    //BOOST_EFFECT_1
                    if(boost_effect_1 != null){
                        if(!user.hasStatusEffect(boost_effect_1)){
                            user.addStatusEffect(new StatusEffectInstance(boost_effect_1,
                                    effectDuration,effectAmplifier,false,false,true));
                        }else{
                            int currentEffectAmplifier = user.getStatusEffect(boost_effect_1).getAmplifier();
                            user.addStatusEffect(new StatusEffectInstance(boost_effect_1,
                                    effectDuration,currentEffectAmplifier + 1,false,false,true));
                        }
                    }
                    //BOOST_EFFECT_2
                    if(boost_effect_2 != null) {
                        if (!user.hasStatusEffect(boost_effect_2)) {
                            user.addStatusEffect(new StatusEffectInstance(boost_effect_2,
                                    effectDuration, effectAmplifier, false, false, true));
                        } else {
                            int currentEffectAmplifier = user.getStatusEffect(boost_effect_2).getAmplifier();
                            user.addStatusEffect(new StatusEffectInstance(boost_effect_2,
                                    effectDuration, currentEffectAmplifier + 1, false, false, true));
                        }
                    }
                }
            }
        }

        return stack.isEmpty() ? new ItemStack(Items.BOWL) : stack;
    }

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
    public Rarity getRarity(ItemStack stack) {
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
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
        if(quality == 0){
            effectAmplifier = amplifier_0; effectDuration =effectDuration_0;
        } else if(quality == 1) {
            effectAmplifier = amplifier_1; effectDuration =effectDuration_1;
        }
        else if(quality == 2) {
            effectAmplifier = amplifier_2; effectDuration =effectDuration_2;
        }else{
            effectAmplifier = amplifier_0; effectDuration =effectDuration_0;
        }
        effectDuration = (effectDuration/20)/60;
        String effect_0 = boost_effect_0.getTranslationKey();


        tooltip.add(Text.of(effectDuration + " minutes Status effect duration for:"));
            tooltip.add(Text.translatable(effect_0).formatted(formatting));
            if(boost_effect_1 != null ){
                String effect_1 = boost_effect_1.getTranslationKey();
                tooltip.add(Text.translatable(effect_1).formatted(formatting));
            }
            if(boost_effect_2 != null ){
                String effect_2 = boost_effect_2.getTranslationKey();
                tooltip.add(Text.translatable(effect_2).formatted(formatting));
            }

    }
}

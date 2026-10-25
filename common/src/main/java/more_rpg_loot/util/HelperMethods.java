package more_rpg_loot.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import com.mrpg_lib.effect.MRPGCEffects;

public class HelperMethods {

    public static RegistryEntry<StatusEffect> getFreezingEffect() {
        return MRPGCEffects.FROSTED.entry;
    }

    public static void applyStatusEffect(LivingEntity target,int effectAmplifier,int effectDurationSeconds,RegistryEntry<StatusEffect> statusEffect,
                                         int maxStackAmplifier, boolean canStackAmplifier, boolean showIcon, boolean increaseDuration,
                                         int increaseEffectDurationSeconds){

        if(target.hasStatusEffect(statusEffect)){
            StatusEffectInstance currentEffect = target.getStatusEffect(statusEffect);
            int currentAmplifier = currentEffect.getAmplifier();
            int currentDuration = currentEffect.getDuration();
            int increaseAmp = 0;
            if(increaseDuration){
                currentDuration = currentDuration + (increaseEffectDurationSeconds*20);
            }
            if(canStackAmplifier){
                increaseAmp = increaseAmp + 1;
            }
            if(currentAmplifier<maxStackAmplifier){
                target.addStatusEffect(new StatusEffectInstance(statusEffect, currentDuration, currentAmplifier + increaseAmp, false, false, showIcon));
            }else{
                target.addStatusEffect(new StatusEffectInstance(statusEffect, currentDuration, maxStackAmplifier, false, false, showIcon));
            }
        }else{
            target.addStatusEffect(new StatusEffectInstance(statusEffect, effectDurationSeconds*20, effectAmplifier, false, false, showIcon));
        }
    }

}

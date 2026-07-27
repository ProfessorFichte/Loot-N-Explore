package more_rpg_loot.util;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.generic.entity.CustomCloudEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.more_rpg_classes.effect.MRPGCEffects;
import org.jetbrains.annotations.Nullable;

public class HelperMethods {

    /** The Frozen Depths freezing effect, swapped for More RPG Classes' Frosted effect when that mod is present. */
    public static RegistryEntry<StatusEffect> getFreezingEffect() {
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            return MRPGCEffects.FROSTED.entry;
        }
        return Effects.FREEZING.registryEntry;
    }

    public static void spawnCloudEntity(
            ParticleEffect particleType,
            Entity owner,
            Entity placementTarget,
            int waitTime,
            float radiusCloud,
            int durationSecondsCloud,
            float radiusGrowthCloud,
            @Nullable RegistryEntry<StatusEffect> statusEffect,
            int durationSecondsStatusEffect,
            int amplifierStatusEffect,
            boolean canStackAmplifier,
            int maxAmplifier,
            boolean canDealDamage,
            float damageAmount,
            @Nullable DamageSource damageSource) {
        if (!owner.getWorld().isClient) {
            CustomCloudEntity cloud = new CustomCloudEntity(owner.getWorld(),placementTarget.getX(),placementTarget.getY(),placementTarget.getZ());

            if (owner instanceof LivingEntity living) {
                cloud.setOwner(living);
            } else if (owner instanceof ProjectileEntity projectile) {
                Entity projectileOwner = projectile.getOwner();
                if (projectileOwner instanceof LivingEntity living) {
                    cloud.setOwner(living);
                }
            }

            cloud.setParticleType(particleType);
            cloud.setRadius(radiusCloud);
            cloud.setDuration(durationSecondsCloud * 20);
            cloud.setWaitTime(waitTime);
            cloud.setRadiusGrowth((radiusGrowthCloud - radiusCloud) / (float)(durationSecondsCloud * 20));

            if (statusEffect != null) {
                cloud.setStatusEffect(statusEffect, durationSecondsStatusEffect * 20, amplifierStatusEffect);
                cloud.setAmplifierStacking(canStackAmplifier, maxAmplifier);
            }

            cloud.setDamageProperties(canDealDamage, damageAmount, damageSource);

            owner.getWorld().spawnEntity(cloud);
        }
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

    public static void stackFreezeStacks(LivingEntity livingEntity,int amount){
        int actualFrozenTicks = livingEntity.getFrozenTicks();
        if(actualFrozenTicks != 160){
            livingEntity.setFrozenTicks(actualFrozenTicks + amount);
        }else{
            livingEntity.setFrozenTicks(160);
        }

    }

}

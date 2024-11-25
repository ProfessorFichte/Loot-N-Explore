package more_rpg_loot.util;

import more_rpg_loot.effects.Effects;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.utils.TargetHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static net.spell_engine.internals.SpellRegistry.getSpell;

public class HelperMethods {

    public static void spawnCloudEntity(
            ParticleEffect particleType, Entity owner, float radiusCloud, int durationSecondsCloud, float radiusGrowthCloud
            , StatusEffect statusEffect, int durationSecondsStatusEffect, int amplifierStatusEffect) {
        if (!owner.getWorld().isClient) {
            List<LivingEntity> list = owner.getWorld().getNonSpectatingEntities(LivingEntity.class, owner.getBoundingBox().expand(4.0, 2.0, 4.0));
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ());
            Entity entity = null;
            if (owner instanceof LivingEntity) {
                entity = owner;
            } else if (owner instanceof ProjectileEntity projectile) {
                projectile.getOwner();
            }
            if (entity instanceof LivingEntity) {
                areaEffectCloudEntity.setOwner((LivingEntity) entity);
            }
            areaEffectCloudEntity.setParticleType(particleType);
            areaEffectCloudEntity.setRadius(radiusCloud);
            areaEffectCloudEntity.setDuration(durationSecondsCloud * 20);
            areaEffectCloudEntity.setRadiusGrowth((radiusGrowthCloud - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffect,
                    durationSecondsStatusEffect * 20, amplifierStatusEffect, false, false, true));
            if (!list.isEmpty()) {
                Iterator var5 = list.iterator();
                while (var5.hasNext()) {
                    LivingEntity livingEntity2 = (LivingEntity) var5.next();
                    double x = owner.squaredDistanceTo(livingEntity2);
                    if (x < 16.0) {
                        areaEffectCloudEntity.setPosition(livingEntity2.getX(), livingEntity2.getY(), livingEntity2.getZ());
                        break;
                    }
                }
            }
            owner.getWorld().spawnEntity(areaEffectCloudEntity);
        }
    }


        public static void executeSpellSpellEngine(PlayerEntity player,LivingEntity target,String modId, String pathSpell,
                                                   SpellCast.Action spellCastAction, boolean aoe){
            if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
                List<Entity> list = new ArrayList<Entity>();
                if (!aoe) {
                    list.add(target);
                } else {
                    float range = getSpell(new Identifier(modId, pathSpell)).range;
                    Predicate<Entity> selectionPredicate = (target2) -> {
                        return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL, player, target2)
                        );
                    };
                    list = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(range), selectionPredicate);
                }
                SpellHelper.performSpell(
                        player.getWorld(),
                        player,
                        new Identifier(modId, pathSpell),
                        list,
                        spellCastAction,
                        1);
            }

        }

    public static void applyStatusEffect(LivingEntity target,int effectAmplifier,int effectDurationSeconds,StatusEffect statusEffect,
                                         int maxStackAmplifier, boolean canStackAmplifier, boolean showIcon, boolean increaseDuration,
                                         int increaseEffectDurationSeconds){
        if(canStackAmplifier){
            if(target.hasStatusEffect(statusEffect)){
                int currentAmplifier = target.getStatusEffect(statusEffect).getAmplifier();
                int currentDuration = target.getStatusEffect(statusEffect).getDuration();
                if(increaseDuration){
                    currentDuration = currentDuration + (increaseEffectDurationSeconds*20);
                }
                if(currentAmplifier>=maxStackAmplifier){
                    target.addStatusEffect(new StatusEffectInstance(statusEffect, currentDuration, effectAmplifier+1, false, false, showIcon));
                }else{
                    target.addStatusEffect(new StatusEffectInstance(statusEffect, currentDuration, maxStackAmplifier, false, false, showIcon));
                }
            }else{
                target.addStatusEffect(new StatusEffectInstance(statusEffect, effectDurationSeconds*20, effectAmplifier, false, false, showIcon));
            }
        }else{
            if(!target.hasStatusEffect(statusEffect)){
                target.addStatusEffect(new StatusEffectInstance(statusEffect, effectDurationSeconds*20, effectAmplifier, false, false, showIcon));
            }else{
                int currentAmplifier = target.getStatusEffect(statusEffect).getAmplifier();
                int currentDuration = target.getStatusEffect(statusEffect).getDuration();
                target.addStatusEffect(new StatusEffectInstance(statusEffect, currentDuration, currentAmplifier, false, false, showIcon));
            }

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

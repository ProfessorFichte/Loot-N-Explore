package more_rpg_loot.api;

import more_rpg_loot.effects.Effects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.spell_engine.internals.casting.SpellCast;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.RPGLoot.tweaksConfig;
import static more_rpg_loot.util.HelperMethods.applyStatusEffect;
import static more_rpg_loot.util.HelperMethods.executeSpellSpellEngine;

public class WeaponPassives {
    public static void ElderGuardianMelee(ItemStack stack, LivingEntity target, LivingEntity attacker){
        float PASSIVE_DAMAGE = tweaksConfig.value.elder_guardian_melee_swimming_damage;
        if (attacker instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("more_rpg_classes")){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_waterbomb_melee", SpellCast.Action.RELEASE,true);
        }
        if(attacker.isSwimming()){
            double gen_atk = attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            target.damage(new DamageSource(target.getDamageSources().generic().getTypeRegistryEntry()), (float) (gen_atk * PASSIVE_DAMAGE));
        }
    }
    public static void EnderDragonMelee(ItemStack stack, LivingEntity target, LivingEntity attacker){
        float PASSIVE = tweaksConfig.value.ender_dragon_melee_regeneration_passive;
        if (attacker instanceof PlayerEntity player){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_dragonclaw", SpellCast.Action.RELEASE,false);

            float player_max_health = (float) player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
            float player_actual_health = player.getHealth() / player_max_health;

            if(player_actual_health < PASSIVE ){
                player.heal(player_max_health * 0.1F);
            }
        }
    }
    public static void WitherMelee(ItemStack stack, LivingEntity target, LivingEntity attacker){
        if (attacker instanceof PlayerEntity player){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_wither_pulse", SpellCast.Action.RELEASE,true);
        }
        int amount_negative = 0;
        List<StatusEffectInstance> list = target.getStatusEffects().stream().toList();
        if (!list.isEmpty()){
            for (StatusEffectInstance statusEffectInstance : list) {
                StatusEffect statusEffect = statusEffectInstance.getEffectType();
                if (!statusEffect.isBeneficial()) {
                    amount_negative++;
                }
            }

            float negative = tweaksConfig.value.wither_melee_damage_per_negative_effect;
            double gen_atk = attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            target.damage(new DamageSource(target.getDamageSources().magic().getTypeRegistryEntry()), (float) (gen_atk * (amount_negative * negative)));
        }
    }
    public static void GlacialMelee(ItemStack stack, LivingEntity target, LivingEntity attacker){
        if (attacker instanceof PlayerEntity player){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_small_avalanche", SpellCast.Action.RELEASE,false);

            applyStatusEffect(target,0,4, Effects.FREEZING,0,
                    true,true,true,1);
        }
    }
}

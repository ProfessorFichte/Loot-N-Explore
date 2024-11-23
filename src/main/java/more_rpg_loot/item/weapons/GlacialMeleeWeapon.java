package more_rpg_loot.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import more_rpg_loot.effects.Effects;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.internals.casting.SpellCast;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.util.HelperMethods.applyStatusEffect;
import static more_rpg_loot.util.HelperMethods.executeSpellSpellEngine;

public class GlacialMeleeWeapon extends SwordItem {
    public GlacialMeleeWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_wither_pulse", SpellCast.Action.RELEASE,true);
        }
        applyStatusEffect(target,0,7, Effects.FREEZING,0,
                true,true,true,1);
        stack.damage(1, attacker, (e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("lore.loot_n_explore.frostmonarch_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.loot_n_explore.frostmonarch_weapon").formatted(Formatting.GOLD));
        super.appendTooltip(stack, world, tooltip, context);
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_wither_pulse.name").formatted(Formatting.BLUE));
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_wither_pulse.description").formatted(Formatting.BLUE));
        }
    }
}

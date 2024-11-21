package more_rpg_loot.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;

import java.util.ArrayList;
import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static more_rpg_loot.util.HelperMethods.executeSpellSpellEngine;

public class DragonMeeleeWeapon extends SwordItem {
    public DragonMeeleeWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player){
            executeSpellSpellEngine(player,target,MOD_ID,"passive_dragonclaw", SpellCast.Action.RELEASE,false);
        }

        double target_max_health = target.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
        target.damage(new DamageSource(target.getDamageSources().magic().getTypeRegistryEntry()), (float) (target_max_health * 0.10F));

        stack.damage(1, attacker, (e)->{
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("lore.loot_n_explore.ender_dragon_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.loot_n_explore.ender_dragon_weapon").formatted(Formatting.GOLD));
        super.appendTooltip(stack, world, tooltip, context);
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_dragonclaw.name").formatted(Formatting.DARK_PURPLE));
            tooltip.add(Text.translatable("spell.loot_n_explore.passive_dragonclaw.description").formatted(Formatting.DARK_PURPLE));
        }
    }
}

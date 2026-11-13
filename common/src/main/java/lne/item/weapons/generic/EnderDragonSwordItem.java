package lne.item.weapons.generic;

import lne.ability.TriggerType;
import lne.item.weapons.LNE_WeaponItems;
import lne.item.weapons.WeaponAbility;
import lne.item.weapons.WeaponProcs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class EnderDragonSwordItem extends SwordItem {
    public EnderDragonSwordItem(float bonus, float speed) { super(ToolMaterials.NETHERITE, LNE_WeaponItems.meleeSettings(bonus, speed)); }
    @Override public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        WeaponProcs.tryMelee(WeaponAbility.ENDER_DRAGON, this, attacker, target);
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.ENDER_DRAGON, TriggerType.MELEE_HIT, tooltip);
    }
}

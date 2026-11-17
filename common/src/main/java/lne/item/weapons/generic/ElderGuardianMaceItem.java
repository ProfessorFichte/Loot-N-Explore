package lne.item.weapons.generic;

import lne.internal.ability.TriggerType;
import lne.item.weapons.LNE_WeaponItems;
import lne.item.weapons.WeaponAbility;
import lne.item.weapons.WeaponProcs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class ElderGuardianMaceItem extends MaceItem {
    public ElderGuardianMaceItem(float bonus, float speed) { super(LNE_WeaponItems.maceSettings(bonus, speed)); }
    @Override public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        WeaponProcs.tryMelee(WeaponAbility.ELDER_GUARDIAN, this, attacker, target);
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.ELDER_GUARDIAN, TriggerType.MELEE_HIT, tooltip);
    }
}

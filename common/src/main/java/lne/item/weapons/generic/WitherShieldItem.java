package lne.item.weapons.generic;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import lne.item.weapons.LNE_ShieldItems;
import lne.internal.ability.TriggerType;
import lne.item.weapons.LNE_WeaponItems;
import lne.item.weapons.WeaponAbility;
import lne.item.weapons.WeaponProcs;
import lne.item.weapons.ShieldBlockAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;


public class WitherShieldItem extends ShieldItem implements ShieldBlockAbility {
    public WitherShieldItem() { super(LNE_ShieldItems.shieldSettings(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)); }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(Items.BONE); }
    @Override public void onShieldBlock(PlayerEntity blocker, LivingEntity attacker) {
        WeaponProcs.tryBlock(WeaponAbility.WITHER, this, blocker, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.WITHER, TriggerType.BLOCK, tooltip);
    }
}

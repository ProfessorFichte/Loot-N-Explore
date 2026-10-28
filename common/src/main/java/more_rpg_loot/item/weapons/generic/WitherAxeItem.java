package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.ability.TriggerType;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.item.weapons.WeaponAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class WitherAxeItem extends AxeItem {
    public WitherAxeItem(float bonus, float speed) { super(ToolMaterials.NETHERITE, LNE_WeaponItems.meleeSettings(bonus, speed)); }
    @Override public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient && LNE_WeaponItems.vanillaAbilitiesActive() && attacker instanceof PlayerEntity player
                && !player.getItemCooldownManager().isCoolingDown(this)
                && WeaponAbility.WITHER.roll(attacker.getRandom())) {
            LNE_WeaponItems.spawnWitherSkulls(attacker.getWorld(), attacker, target);
            player.getItemCooldownManager().set(this, WeaponAbility.WITHER.cooldownTicks());
        }
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.WITHER, TriggerType.MELEE_HIT, tooltip);
    }
}

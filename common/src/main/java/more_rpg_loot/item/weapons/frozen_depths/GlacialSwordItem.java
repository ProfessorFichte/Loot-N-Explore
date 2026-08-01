package more_rpg_loot.item.weapons.frozen_depths;

import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class GlacialSwordItem extends SwordItem {
    private static final int COOLDOWN = 360;
    public GlacialSwordItem(float bonus, float speed) { super(ToolMaterials.NETHERITE, LNE_WeaponItems.meleeSettings(bonus, speed)); }
    @Override public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient && attacker instanceof PlayerEntity player
                && !player.getItemCooldownManager().isCoolingDown(this)
                && attacker.getRandom().nextFloat() < 0.20f) {
            LNE_WeaponItems.spawnFrostballs(attacker.getWorld(), attacker);
            player.getItemCooldownManager().set(this, COOLDOWN);
        }
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(this, tooltip);
    }
}

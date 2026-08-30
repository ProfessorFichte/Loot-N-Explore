package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

// Mace abilities are suppressed when spell_engine is installed.
public class EnderDragonMaceItem extends MaceItem {
    private static final int COOLDOWN = 300;
    public EnderDragonMaceItem(float bonus, float speed) { super(LNE_WeaponItems.maceSettings(bonus, speed)); }
    @Override public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient
                && !LNEPlatform.isModLoaded("spell_engine")
                && attacker instanceof PlayerEntity player
                && !player.getItemCooldownManager().isCoolingDown(this)
                && attacker.getRandom().nextFloat() < 0.20f) {
            float dmg = (float)(attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.15f);
            target.damage(target.getDamageSources().magic(), dmg);
            player.heal(dmg);
            player.getItemCooldownManager().set(this, COOLDOWN);
        }
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(this, tooltip);
    }
}

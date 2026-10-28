package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.ability.TriggerType;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.item.weapons.WeaponAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
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
        if (!attacker.getWorld().isClient && LNE_WeaponItems.vanillaAbilitiesActive() && attacker instanceof PlayerEntity player
                && !player.getItemCooldownManager().isCoolingDown(this)
                && WeaponAbility.ENDER_DRAGON.roll(attacker.getRandom())) {
            float dmg = (float)(attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * WeaponAbility.ENDER_DRAGON.damageMultiplier());
            target.damage(target.getDamageSources().magic(), dmg);
            player.heal(dmg);
            player.getItemCooldownManager().set(this, WeaponAbility.ENDER_DRAGON.cooldownTicks());
            LNE_WeaponItems.spawnDragonParticles(attacker.getWorld(), target);
        }
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.ENDER_DRAGON, TriggerType.MELEE_HIT, tooltip);
    }
}

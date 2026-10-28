package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.item.weapons.LNE_ShieldItems;
import more_rpg_loot.ability.TriggerType;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.item.weapons.WeaponAbility;
import more_rpg_loot.item.weapons.ShieldBlockAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class EnderDragonShieldItem extends ShieldItem implements ShieldBlockAbility {
    public EnderDragonShieldItem() { super(LNE_ShieldItems.shieldSettings(Identifier.of(MOD_ID, "ender_dragon_scales"))); }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(Items.AMETHYST_SHARD); }
    @Override public void onShieldBlock(PlayerEntity blocker, LivingEntity attacker) {
        if (!LNE_WeaponItems.vanillaAbilitiesActive()
                || blocker.getItemCooldownManager().isCoolingDown(this)
                || !WeaponAbility.ENDER_DRAGON.roll(blocker.getRandom())) {
            return;
        }
        float dmg = (float) (blocker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * WeaponAbility.ENDER_DRAGON.damageMultiplier());
        attacker.damage(attacker.getDamageSources().magic(), dmg);
        LNE_WeaponItems.spawnDragonParticles(blocker.getWorld(), attacker);
        blocker.heal(dmg);
        blocker.getItemCooldownManager().set(this, WeaponAbility.ENDER_DRAGON.cooldownTicks());
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.ENDER_DRAGON, TriggerType.BLOCK, tooltip);
    }
}

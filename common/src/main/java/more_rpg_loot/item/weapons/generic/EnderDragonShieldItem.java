package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.item.weapons.LNE_ShieldItems;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
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
    private static final int COOLDOWN = 300;
    public EnderDragonShieldItem() { super(LNE_ShieldItems.shieldSettings(Identifier.of(MOD_ID, "ender_dragon_scales"))); }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(Items.AMETHYST_SHARD); }
    @Override public void onShieldBlock(PlayerEntity blocker, LivingEntity attacker) {
        if (LNEPlatform.isModLoaded("spell_engine")
                || blocker.getItemCooldownManager().isCoolingDown(this)
                || blocker.getRandom().nextFloat() >= 0.20f) {
            return;
        }
        float dmg = (float) (blocker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.15f);
        attacker.damage(attacker.getDamageSources().magic(), dmg);
        LNE_WeaponItems.spawnDragonParticles(blocker.getWorld(), attacker);
        blocker.heal(dmg);
        blocker.getItemCooldownManager().set(this, COOLDOWN);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(this, tooltip);
    }
}

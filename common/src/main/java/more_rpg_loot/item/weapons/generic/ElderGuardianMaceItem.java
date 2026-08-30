package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

// Mace abilities are suppressed when spell_engine or more_rpg_classes is installed.
public class ElderGuardianMaceItem extends MaceItem {
    private static final int COOLDOWN = 200;
    public ElderGuardianMaceItem(float bonus, float speed) { super(LNE_WeaponItems.maceSettings(bonus, speed)); }
    @Override public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient
                && !LNEPlatform.isModLoaded("spell_engine")
                && !LNEPlatform.isModLoaded("more_rpg_classes")
                && attacker instanceof PlayerEntity player
                && !player.getItemCooldownManager().isCoolingDown(this)
                && attacker.getRandom().nextFloat() < 0.20f) {
            LNE_WeaponItems.spawnRainCloud(attacker, target);
            player.getItemCooldownManager().set(this, COOLDOWN);
        }
        return super.postHit(stack, target, attacker);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addElderGuardianTooltip(this, tooltip);
    }
}

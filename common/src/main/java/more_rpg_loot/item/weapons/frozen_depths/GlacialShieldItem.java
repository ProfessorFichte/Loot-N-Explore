package more_rpg_loot.item.weapons.frozen_depths;

import more_rpg_loot.item.weapons.LNE_ShieldItems;
import more_rpg_loot.ability.TriggerType;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.item.weapons.WeaponAbility;
import more_rpg_loot.item.weapons.ShieldBlockAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class GlacialShieldItem extends ShieldItem implements ShieldBlockAbility {
    public GlacialShieldItem() { super(LNE_ShieldItems.shieldSettings(Identifier.of(MOD_ID, "frozen_soul"))); }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(Items.ICE); }
    @Override public void onShieldBlock(PlayerEntity blocker, LivingEntity attacker) {
        if (!LNE_WeaponItems.vanillaAbilitiesActive()
                || blocker.getItemCooldownManager().isCoolingDown(this)
                || !WeaponAbility.GLACIAL.roll(blocker.getRandom())) {
            return;
        }
        LNE_WeaponItems.spawnFrostballs(blocker.getWorld(), blocker);
        blocker.getItemCooldownManager().set(this, WeaponAbility.GLACIAL.cooldownTicks());
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.GLACIAL, TriggerType.BLOCK, tooltip);
    }
}

package more_rpg_loot.item.weapons.generic;

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

public class ElderGuardianShieldItem extends ShieldItem implements ShieldBlockAbility {
    public ElderGuardianShieldItem() { super(LNE_ShieldItems.shieldSettings(Identifier.of(MOD_ID, "elder_guardian_eye"))); }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(Items.PRISMARINE_SHARD); }
    @Override public void onShieldBlock(PlayerEntity blocker, LivingEntity attacker) {
        if (!LNE_WeaponItems.vanillaAbilitiesActive()
                || blocker.getItemCooldownManager().isCoolingDown(this)
                || !WeaponAbility.ELDER_GUARDIAN.roll(blocker.getRandom())) {
            return;
        }
        LNE_WeaponItems.spawnRainCloud(blocker, attacker);
        blocker.getItemCooldownManager().set(this, WeaponAbility.ELDER_GUARDIAN.cooldownTicks());
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addAbilityTooltip(WeaponAbility.ELDER_GUARDIAN, TriggerType.BLOCK, tooltip);
    }
}

package more_rpg_loot.item.weapons.generic;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.item.weapons.LNE_ShieldItems;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
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
    private static final int COOLDOWN = 200;
    public ElderGuardianShieldItem() { super(LNE_ShieldItems.shieldSettings(Identifier.of(MOD_ID, "elder_guardian_eye"))); }
    @Override public boolean canRepair(ItemStack stack, ItemStack ingredient) { return ingredient.isOf(Items.PRISMARINE_SHARD); }
    @Override public void onShieldBlock(PlayerEntity blocker, LivingEntity attacker) {
        if (LNEPlatform.isModLoaded("spell_engine")
                || LNEPlatform.isModLoaded("more_rpg_classes")
                || blocker.getItemCooldownManager().isCoolingDown(this)
                || blocker.getRandom().nextFloat() >= 0.20f) {
            return;
        }
        LNE_WeaponItems.spawnRainCloud(blocker, attacker);
        blocker.getItemCooldownManager().set(this, COOLDOWN);
    }
    @Override public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type); LNE_WeaponItems.addElderGuardianTooltip(this, tooltip);
    }
}

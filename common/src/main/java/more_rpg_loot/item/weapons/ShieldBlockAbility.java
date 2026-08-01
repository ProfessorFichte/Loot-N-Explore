package more_rpg_loot.item.weapons;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface ShieldBlockAbility {
    void onShieldBlock(PlayerEntity blocker, LivingEntity attacker);
}

package more_rpg_loot.item.weapons;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class WitherMeleeWeapon extends SwordItem {
    public WitherMeleeWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
}
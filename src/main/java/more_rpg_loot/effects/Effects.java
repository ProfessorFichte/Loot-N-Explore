package more_rpg_loot.effects;

import more_rpg_loot.RPGLoot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class Effects {
    public static final StatusEffect INNKEEPERS_PROVIANT = registerStatusEffect("innkeepers_proviant",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x00ffff));
    public static final StatusEffect FROST_RESISTANCE = registerStatusEffect("frost_resistance",
            new FrostResistanceEffect(StatusEffectCategory.BENEFICIAL, 0x99ccff));

    private static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, name), statusEffect);
    }
    public static void register(){
        RPGLoot.LOGGER.info("Registering Status Effects for " + MOD_ID);
    }
}

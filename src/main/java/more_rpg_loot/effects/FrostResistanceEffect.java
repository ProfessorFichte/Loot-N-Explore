package more_rpg_loot.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FrostResistanceEffect extends StatusEffect {

    protected FrostResistanceEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        livingEntity.setFrozenTicks(0);
        super.applyUpdateEffect(livingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

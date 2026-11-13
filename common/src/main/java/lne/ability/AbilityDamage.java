package lne.ability;

import com.mrpg_lib.util.CustomMethods;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;

public final class AbilityDamage {
    private AbilityDamage() {
    }

    public static double highest(LivingEntity entity) {
        return CustomMethods.getHighestDamageAttribute(entity);
    }

    public static float scaled(LivingEntity owner, double base, double scaling) {
        return (float) (base + scaling * highest(owner));
    }

    public static int scaledAmplifier(LivingEntity owner, double scaling, int maxAmplifier) {
        return MathHelper.clamp((int) Math.floor(highest(owner) * scaling), 0, Math.max(0, maxAmplifier));
    }

    public static void stackWither(LivingEntity source, LivingEntity target, int durationTicks, int maxAmplifier) {
        var current = target.getStatusEffect(StatusEffects.WITHER);
        int amplifier = current == null ? 0 : Math.min(maxAmplifier, current.getAmplifier() + 1);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, durationTicks, amplifier), source);
    }
}

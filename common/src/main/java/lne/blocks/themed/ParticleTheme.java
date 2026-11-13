package lne.blocks.themed;

import net.minecraft.loot.LootTable;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public record ParticleTheme(
        Supplier<ParticleEffect> flame,
        Supplier<ParticleEffect> smallFlame,
        Supplier<ParticleEffect> vaultConnection,
        Supplier<ParticleEffect> detection,
        Supplier<ParticleEffect> ominousDetection,
        Supplier<ParticleEffect> smoke,
        RegistryKey<LootTable> ominousItems,
        BooleanSupplier ominousItemsEnabled
) {
    public ParticleEffect swap(ParticleEffect effect) {
        if (effect == ParticleTypes.FLAME || effect == ParticleTypes.SOUL_FIRE_FLAME) {
            return resolve(flame, effect);
        }
        if (effect == ParticleTypes.SMALL_FLAME) {
            return resolve(smallFlame, effect);
        }
        if (effect == ParticleTypes.VAULT_CONNECTION) {
            return resolve(vaultConnection, effect);
        }
        if (effect == ParticleTypes.SMOKE || effect == ParticleTypes.LARGE_SMOKE) {
            return resolve(smoke, effect);
        }
        if (effect == ParticleTypes.TRIAL_SPAWNER_DETECTION) {
            return resolve(detection, effect);
        }
        if (effect == ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS) {
            return resolve(ominousDetection, effect);
        }
        return effect;
    }

    private static ParticleEffect resolve(Supplier<ParticleEffect> replacement, ParticleEffect fallback) {
        if (replacement == null) {
            return fallback;
        }
        ParticleEffect effect = replacement.get();
        return effect == null ? fallback : effect;
    }

    public boolean replacesOminousItems() {
        return ominousItems != null && (ominousItemsEnabled == null || ominousItemsEnabled.getAsBoolean());
    }
}

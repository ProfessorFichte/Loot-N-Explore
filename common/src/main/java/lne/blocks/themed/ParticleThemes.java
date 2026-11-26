package lne.blocks.themed;

import lne.LneMod;
import lne.particle.Particles;
import lne.util.LneLootTables;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ParticleThemes {
    private static final Identifier MRPGL_SNOWFLAKE = Identifier.of("mrpg_lib", "freezing_snowflake");

    public static final ParticleTheme FROZEN = new ParticleTheme(
            () -> Particles.FROZEN_FLAME,
            () -> Particles.FROZEN_SMALL_FLAME,
            () -> Particles.FROZEN_VAULT_CONNECTION,
            () -> ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS,
            null,
            ParticleThemes::frozenSnowflake,
            LneLootTables.FROZEN_TRAPS_ALL,
            () -> LneMod.frozenDepthsConfig != null && LneMod.frozenDepthsConfig.value.frozen_trial_spawner_ominous_projectiles
    );

    private static ParticleEffect snowflake;

    private static ParticleEffect frozenSnowflake() {
        if (snowflake == null) {
            snowflake = Registries.PARTICLE_TYPE.get(MRPGL_SNOWFLAKE) instanceof ParticleEffect found ? found : Particles.FREEZING_SNOWFLAKE;
        }
        return snowflake;
    }
}

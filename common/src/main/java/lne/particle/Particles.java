package lne.particle;

import lne.LneMod;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Particles {
    public static final SimpleParticleType DRAGON_CLAW = simple();
    public static final SimpleParticleType FREEZING_SNOWFLAKE = simple();
    public static final SimpleParticleType FROZEN_SOUL_MOTE = simple();
    public static final SimpleParticleType FROZEN_FLAME = simple();
    public static final SimpleParticleType FROZEN_SMALL_FLAME = simple();
    public static final SimpleParticleType FROZEN_VAULT_CONNECTION = simple();
    public static final SimpleParticleType FROST_MIST = simple();
    public static final SimpleParticleType FROZEN_SOUL = simple();
    public static final SimpleParticleType BOOK_DUST = simple();
    public static final SimpleParticleType FROST_SCREECH = new SimpleParticleType(true) {};

    private static SimpleParticleType simple() {
        return new SimpleParticleType(false) {};
    }

    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "dragon_claw"), DRAGON_CLAW);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "freezing_snowflake"), FREEZING_SNOWFLAKE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frozen_soul_mote"), FROZEN_SOUL_MOTE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frozen_flame"), FROZEN_FLAME);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frozen_small_flame"), FROZEN_SMALL_FLAME);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frozen_vault_connection"), FROZEN_VAULT_CONNECTION);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frost_mist"), FROST_MIST);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frozen_soul"), FROZEN_SOUL);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "book_dust"), BOOK_DUST);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(LneMod.MOD_ID, "frost_screech"), FROST_SCREECH);
    }
}

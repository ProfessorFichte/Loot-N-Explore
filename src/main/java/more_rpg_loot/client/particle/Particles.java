package more_rpg_loot.client.particle;

import more_rpg_loot.RPGLoot;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Particles {
    public static final SimpleParticleType DRAGON_CLAW = FabricParticleTypes.simple();
    public static final SimpleParticleType FREEZING_SNOWFLAKE = FabricParticleTypes.simple();

    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(RPGLoot.MOD_ID, "dragon_claw"), DRAGON_CLAW);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(RPGLoot.MOD_ID, "freezing_snowflake"), FREEZING_SNOWFLAKE);
    }
}

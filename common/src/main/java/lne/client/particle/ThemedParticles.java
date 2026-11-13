package lne.client.particle;

import lne.blocks.themed.ParticleTheme;
import lne.blocks.themed.ThemeLookup;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThemedParticles {
    private static ParticleTheme active;

    public static void beginAt(World world, BlockPos pos) {
        active = ThemeLookup.at(world, pos);
    }

    public static void beginNearSpawner(World world, BlockPos pos) {
        active = ThemeLookup.nearestSpawner(world, pos);
    }

    public static void end() {
        active = null;
    }

    public static ParticleEffect swap(ParticleEffect effect) {
        return active == null ? effect : active.swap(effect);
    }
}

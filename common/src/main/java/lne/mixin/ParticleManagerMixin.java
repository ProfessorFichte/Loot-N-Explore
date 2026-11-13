package lne.mixin;

import lne.client.particle.ThemedParticles;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @ModifyVariable(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"), argsOnly = true)
    private ParticleEffect lne$swapThemedParticles(ParticleEffect effect) {
        return ThemedParticles.swap(effect);
    }
}

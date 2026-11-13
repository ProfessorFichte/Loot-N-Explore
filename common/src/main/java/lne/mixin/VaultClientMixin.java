package lne.mixin;

import lne.client.particle.ThemedParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultClientData;
import net.minecraft.block.vault.VaultSharedData;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VaultBlockEntity.Client.class)
public class VaultClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private static void lne$beginTick(World world, BlockPos pos, BlockState state, VaultClientData clientData, VaultSharedData sharedData, CallbackInfo ci) {
        ThemedParticles.beginAt(world, pos);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private static void lne$endTick(World world, BlockPos pos, BlockState state, VaultClientData clientData, VaultSharedData sharedData, CallbackInfo ci) {
        ThemedParticles.end();
    }

    @Inject(method = "spawnActivateParticles", at = @At("HEAD"))
    private static void lne$beginActivate(World world, BlockPos pos, BlockState state, VaultSharedData sharedData, ParticleEffect effect, CallbackInfo ci) {
        ThemedParticles.beginAt(world, pos);
    }

    @Inject(method = "spawnActivateParticles", at = @At("RETURN"))
    private static void lne$endActivate(World world, BlockPos pos, BlockState state, VaultSharedData sharedData, ParticleEffect effect, CallbackInfo ci) {
        ThemedParticles.end();
    }

    @Inject(method = "spawnDeactivateParticles", at = @At("HEAD"))
    private static void lne$beginDeactivate(World world, BlockPos pos, ParticleEffect effect, CallbackInfo ci) {
        ThemedParticles.beginAt(world, pos);
    }

    @Inject(method = "spawnDeactivateParticles", at = @At("RETURN"))
    private static void lne$endDeactivate(World world, BlockPos pos, ParticleEffect effect, CallbackInfo ci) {
        ThemedParticles.end();
    }
}

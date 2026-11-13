package lne.mixin;

import lne.client.particle.ThemedParticles;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrialSpawnerLogic.class)
public class TrialSpawnerLogicMixin {

    @Inject(method = "tickClient", at = @At("HEAD"))
    private void lne$beginTick(World world, BlockPos pos, boolean ominous, CallbackInfo ci) {
        ThemedParticles.beginAt(world, pos);
    }

    @Inject(method = "tickClient", at = @At("RETURN"))
    private void lne$endTick(World world, BlockPos pos, boolean ominous, CallbackInfo ci) {
        ThemedParticles.end();
    }

    @Inject(method = "addMobSpawnParticles", at = @At("HEAD"))
    private static void lne$beginMobSpawn(World world, BlockPos pos, Random random, SimpleParticleType particle, CallbackInfo ci) {
        ThemedParticles.beginNearSpawner(world, pos);
    }

    @Inject(method = "addMobSpawnParticles", at = @At("RETURN"))
    private static void lne$endMobSpawn(World world, BlockPos pos, Random random, SimpleParticleType particle, CallbackInfo ci) {
        ThemedParticles.end();
    }

    @Inject(method = "addEjectItemParticles", at = @At("HEAD"))
    private static void lne$beginEject(World world, BlockPos pos, Random random, CallbackInfo ci) {
        ThemedParticles.beginNearSpawner(world, pos);
    }

    @Inject(method = "addEjectItemParticles", at = @At("RETURN"))
    private static void lne$endEject(World world, BlockPos pos, Random random, CallbackInfo ci) {
        ThemedParticles.end();
    }

    @Inject(method = "addTrialOmenParticles", at = @At("HEAD"))
    private static void lne$beginOmen(World world, BlockPos pos, Random random, CallbackInfo ci) {
        ThemedParticles.beginNearSpawner(world, pos);
    }

    @Inject(method = "addTrialOmenParticles", at = @At("RETURN"))
    private static void lne$endOmen(World world, BlockPos pos, Random random, CallbackInfo ci) {
        ThemedParticles.end();
    }

    @Inject(method = "addDetectionParticles", at = @At("HEAD"))
    private static void lne$beginDetection(World world, BlockPos pos, Random random, int playerCount, ParticleEffect particle, CallbackInfo ci) {
        ThemedParticles.beginNearSpawner(world, pos);
    }

    @Inject(method = "addDetectionParticles", at = @At("RETURN"))
    private static void lne$endDetection(World world, BlockPos pos, Random random, int playerCount, ParticleEffect particle, CallbackInfo ci) {
        ThemedParticles.end();
    }
}

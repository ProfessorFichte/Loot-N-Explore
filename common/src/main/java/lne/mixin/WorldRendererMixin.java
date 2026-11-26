package lne.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lne.client.FrostsiegeClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// During an Eternal Frostsiege it snows everywhere, also in biomes where it would rain or stay dry.
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @WrapOperation(method = "renderWeather", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;hasPrecipitation()Z"))
    private boolean lne_siegeHasPrecipitation(Biome biome, Operation<Boolean> original) {
        return FrostsiegeClient.isActive() || original.call(biome);
    }

    @WrapOperation(method = "renderWeather", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation lne_siegeSnow(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        return FrostsiegeClient.isActive() ? Biome.Precipitation.SNOW : original.call(biome, pos);
    }

    @WrapOperation(method = "tickRainSplashing", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation lne_siegeNoRainSplashes(Biome biome, BlockPos pos, Operation<Biome.Precipitation> original) {
        return FrostsiegeClient.isActive() ? Biome.Precipitation.SNOW : original.call(biome, pos);
    }
}

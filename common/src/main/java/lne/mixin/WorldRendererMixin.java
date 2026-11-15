package lne.mixin;

import lne.client.FrostsiegeClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// During an Eternal Frostsiege it snows everywhere, also in biomes where it would rain or stay dry.
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Redirect(method = "renderWeather", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;hasPrecipitation()Z"))
    private boolean lne_siegeHasPrecipitation(Biome biome) {
        return FrostsiegeClient.isActive() || biome.hasPrecipitation();
    }

    @Redirect(method = "renderWeather", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation lne_siegeSnow(Biome biome, BlockPos pos) {
        return FrostsiegeClient.isActive() ? Biome.Precipitation.SNOW : biome.getPrecipitation(pos);
    }

    @Redirect(method = "tickRainSplashing", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation lne_siegeNoRainSplashes(Biome biome, BlockPos pos) {
        return FrostsiegeClient.isActive() ? Biome.Precipitation.SNOW : biome.getPrecipitation(pos);
    }
}

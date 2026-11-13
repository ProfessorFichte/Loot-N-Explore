package lne.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

import static lne.LneMod.MOD_ID;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {
    private static final Identifier MODDED_ICY = Identifier.of("c", "is_icy");
    private static final Identifier MODDED_SNOWY = Identifier.of("c", "is_snowy");

    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    private static TagKey<Biome> hasStructure(String path) {
        return TagKey.of(RegistryKeys.BIOME, Identifier.of(MOD_ID, "has_structure/" + path));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        surface(hasStructure("frozen_depths/overworld_icy_structures"), true, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.GROVE, BiomeKeys.ICE_SPIKES);
        surface(hasStructure("icy_dungeons"), true, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.GROVE, BiomeKeys.ICE_SPIKES,
                BiomeKeys.SNOWY_SLOPES, BiomeKeys.FROZEN_PEAKS);
        surface(hasStructure("frozen_depths/monarchs_temple"), false, BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_TAIGA, BiomeKeys.ICE_SPIKES);
    }

    @SafeVarargs
    private void surface(TagKey<Biome> tag, boolean moddedSnowy, RegistryKey<Biome>... biomes) {
        var builder = getOrCreateTagBuilder(tag);
        for (var biome : biomes) {
            builder.add(biome);
        }
        builder.addOptionalTag(MODDED_ICY);
        if (moddedSnowy) {
            builder.addOptionalTag(MODDED_SNOWY);
        }
    }
}

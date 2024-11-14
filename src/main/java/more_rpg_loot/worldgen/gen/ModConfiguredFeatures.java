package more_rpg_loot.worldgen.gen;

import more_rpg_loot.blocks.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FROSTBLOOM_KEY = registerKey("frostbloom");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        register(context, FROSTBLOOM_KEY, Feature.FLOWER, ConfiguredFeatures.createRandomPatchFeatureConfig(3,
                PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.FROST_BLOOM.block())))));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> context,
            RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}

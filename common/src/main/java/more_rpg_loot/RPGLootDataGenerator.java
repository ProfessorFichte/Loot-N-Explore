package more_rpg_loot;
import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.compat.spell_engine.LNE_AbilityDatagen;
import more_rpg_loot.datagen.*;
import more_rpg_loot.worldgen.gen.ModConfiguredFeatures;
import more_rpg_loot.worldgen.gen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class RPGLootDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		LNEPlatform.set(new LNEPlatform.Impl() {
			public boolean isModLoaded(String modId) {
				return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
			}
			public boolean isDevelopmentEnvironment() {
				return net.fabricmc.loader.api.FabricLoader.getInstance().isDevelopmentEnvironment();
			}
		});
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEntityTagProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(LootExploreConditionalRecipeProvider::new);
		pack.addProvider(LootExploreConditionalTagProvider::new);
		pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModAdvancementProvider::new);
		pack.addProvider(ModItemTagProvider::new);

		if (LNEPlatform.isModLoaded("spell_engine")) {
			pack.addProvider(LNE_AbilityDatagen::new);
		}

		pack.addProvider(WeaponAttributesGenerator::new);
	}


	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
	}
}

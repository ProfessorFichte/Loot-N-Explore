package lne;
import lne.platform.LNEPlatform;

import lne.compat.spell_engine.LNE_AbilityDatagen;
import lne.datagen.*;
import lne.worldgen.gen.ModConfiguredFeatures;
import lne.worldgen.gen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class LneDataGenerator implements DataGeneratorEntrypoint {
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
		pack.addProvider(ModEntityLootTableProvider::new);
		pack.addProvider(ModSpawnerLootTableProvider::new);
		pack.addProvider(ModFrozenDepthsLootTableProvider::new);
		pack.addProvider(ModArchaeologyLootTableProvider::new);
		pack.addProvider(ModStructureBlockModifierProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEntityTagProvider::new);
		pack.addProvider(ModDamageTypeTagProvider::new);
		pack.addProvider(ModBiomeTagProvider::new);
		pack.addProvider(ModStatusEffectTagProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(LootExploreConditionalRecipeProvider::new);
		pack.addProvider(LootExploreConditionalTagProvider::new);
		pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModAdvancementProvider::new);
		pack.addProvider(ModItemTagProvider::new);

		if (LNEPlatform.isModLoaded("spell_engine")) {
			pack.addProvider(LNE_AbilityDatagen::new);
			pack.addProvider(lne.compat.spell_engine.MobSpellTagGenerator::new);
		}

		pack.addProvider(WeaponAttributesGenerator::new);
	}


	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
	}
}

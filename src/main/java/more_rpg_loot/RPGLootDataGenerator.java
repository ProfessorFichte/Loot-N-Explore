package more_rpg_loot;

import more_rpg_loot.compat.spell_engine.LNE_Weapons;
import more_rpg_loot.worldgen.gen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import more_rpg_loot.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import more_rpg_loot.worldgen.gen.ModConfiguredFeatures;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;

import java.util.concurrent.CompletableFuture;

public class RPGLootDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModEntityTagProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModWorldGenerator::new);
		//pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ItemTagGenerator::new);

	}

	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}
		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateWeaponTags(LNE_Weapons.entries);
		}
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
	}
}

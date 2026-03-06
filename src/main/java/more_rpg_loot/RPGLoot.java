package more_rpg_loot;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.compat.CompatRegistry;
import more_rpg_loot.config.Default;
import more_rpg_loot.config.EffectsConfig;
import more_rpg_loot.config.TweaksConfig;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.ItemsRegistry;
import more_rpg_loot.item.ModSpawnEggs;
import more_rpg_loot.worldgen.map.ModMapDecorations;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.worldgen.gen.ModWorldGen;
import more_rpg_loot.worldgen.processor.ModProcessorTypes;
import more_rpg_loot.worldgen.structure.ModStructureTypes;
import more_rpg_loot.worldgen.villages.LNEVillagerTrades;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPGLoot implements ModInitializer {
	public static final String MOD_ID = "loot_n_explore";
    public static final Logger LOGGER = LoggerFactory.getLogger("loot_n_explore");

	public static ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<>
			("villages", Default.villages)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<TweaksConfig>
			("tweaks_v1", new TweaksConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<EffectsConfig> effectsConfig = new ConfigManager<EffectsConfig>
			("effects_v2", new EffectsConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	@Override
	public void onInitialize() {
		tweaksConfig.refresh();
		effectsConfig.refresh();
		Effects.register();
		if (!FabricLoader.getInstance().isModLoaded("lithostitched")) {
			villageConfig.refresh();
			StructurePoolAPI.injectAll(RPGLoot.villageConfig.value);
		}
		ModBlocks.register();
		ItemsRegistry.registerModItems();
		ModEntities.register();
		ModSpawnEggs.register();
		Group.registerItemGroups();
		Particles.register();
		ModMapDecorations.register();
		LNEVillagerTrades.register();
		ModSounds.register();
		ModProcessorTypes.register();
		ModStructureTypes.register();
		ModWorldGen.generateModWorldGen();
		CompatRegistry.registerModCompat();

		effectsConfig.save();
	}
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
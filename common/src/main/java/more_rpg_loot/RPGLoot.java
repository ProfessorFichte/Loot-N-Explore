package more_rpg_loot;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.compat.CompatRegistry;
import more_rpg_loot.config.Default;
import more_rpg_loot.config.EffectsConfig;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.ItemsRegistry;
import more_rpg_loot.item.ModSpawnEggs;
import more_rpg_loot.server.LNEServerEvents;
import more_rpg_loot.worldgen.map.ModMapDecorations;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.worldgen.processor.ModProcessorTypes;
import more_rpg_loot.worldgen.structure.ModStructureTypes;
import more_rpg_loot.worldgen.villages.LNEVillagerProfessions;
import more_rpg_loot.worldgen.villages.LNEVillagerTrades;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.minecraft.util.Identifier;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPGLoot {
	public static final String MOD_ID = "loot_n_explore";
    public static final Logger LOGGER = LoggerFactory.getLogger("loot_n_explore");

	public static ConfigManager<StructurePoolConfig> villageConfig = null;
	public static ConfigManager<EffectsConfig> effectsConfig = null;

	public static void init() {
		try {
			effectsConfig = new ConfigManager<EffectsConfig>("effects_v2", new EffectsConfig())
					.builder().setDirectory(MOD_ID).sanitize(true).build();
			villageConfig = new ConfigManager<>("villages", Default.villages)
					.builder().setDirectory(MOD_ID).sanitize(true).build();

			effectsConfig.refresh();
			if (!LNEPlatform.isModLoaded("lithostitched")) {
				villageConfig.refresh();
				StructurePoolAPI.injectAll(RPGLoot.villageConfig.value);
			}
		} catch (NoClassDefFoundError e) {
			LOGGER.warn("[LootNExplore] tiny_config not found, configs disabled: {}", e.getMessage());
		}
	}

	public static void registerEffects() {
		Effects.register();
	}

	public static void registerBlocks() {
		ModBlocks.register();
	}

	public static void registerEntities() {
		ModEntities.register();
	}

	public static void registerSounds() {
		ModSounds.register();
	}

	public static void registerItemGroups() {
		Group.registerItemGroups();
	}

	public static void registerItems() {
		ItemsRegistry.registerModItems();
		ModSpawnEggs.register();
		Particles.register();
		ModMapDecorations.register();
		ModProcessorTypes.register();
		ModStructureTypes.register();
		CompatRegistry.registerModCompat();
		if (effectsConfig != null) effectsConfig.save();
	}

	public static void registerVillagePoi() {
		LNEVillagerProfessions.registerPoiTypes();
	}

	public static void registerVillageProfessions() {
		LNEVillagerProfessions.registerProfessions();
		LNEVillagerTrades.registerTrades();
	}

	public static void registerVillagerSchedules() {
		LNEVillagerTrades.registerSchedule();
	}

	public static void registerServerEvents() {
		LNEServerEvents.register();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}

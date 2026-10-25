package more_rpg_loot;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.particle.Particles;
import more_rpg_loot.compat.CompatRegistry;
import more_rpg_loot.compat.items.RWA_Effects;
import more_rpg_loot.config.Default;
import more_rpg_loot.config.EffectsConfig;
import more_rpg_loot.effects.Effects;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.frozen_depths.FrozenDepthsTuning;
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
	public static ConfigManager<EntityAttributeConfig> entityAttributeConfig = null;

	public static void init() {
		effectsConfig = new ConfigManager<EffectsConfig>("effects_v2", new EffectsConfig())
				.builder().setDirectory(MOD_ID).sanitize(true).build();
		villageConfig = new ConfigManager<>("villages", Default.villages)
				.builder().setDirectory(MOD_ID).sanitize(true).build();
		entityAttributeConfig = new ConfigManager<>("entity_attributes_v1", seededEntityAttributeDefaults())
				.builder().setDirectory(MOD_ID).sanitize(true).build();

		effectsConfig.refresh();
		villageConfig.refresh();
		entityAttributeConfig.refresh();
		entityAttributeConfig.save();
		if (!LNEPlatform.isModLoaded("lithostitched")) {
			StructurePoolAPI.injectAll(villageConfig.value);
		}
	}

	private static EntityAttributeConfig seededEntityAttributeDefaults() {
		var config = new EntityAttributeConfig();
		config.entries.put("loot_n_explore:frost_monarch", FrozenDepthsTuning.frostMonarch(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 300.0, 1.25, 1.5)
				.set("minecraft:generic.attack_damage", 6.0, 1.25, 1.5)
				.set("minecraft:generic.armor", 4.0, 1.25, 1.5)
				.set("spell_power:frost", 5.25, 1.25, 1.5)
				.set("spell_power:fire", 1.0, 1.25, 1.5)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:undead_frozen_mage", FrozenDepthsTuning.undeadFrozenMage(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 30.0)
				.set("minecraft:generic.attack_damage", 3.0)
				.set("spell_power:frost", 3.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:general_undead_frozen_mage", FrozenDepthsTuning.generalUndeadFrozenMage(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 50.0)
				.set("minecraft:generic.attack_damage", 5.0)
				.set("spell_power:frost", 4.5)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:frost_hound", new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 20.0)
				.set("minecraft:generic.attack_damage", 5.0));
		config.entries.put("loot_n_explore:frost_haunt", FrozenDepthsTuning.frosthaunt(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 25.0)
				.set("minecraft:generic.attack_damage", 4.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:glaze", FrozenDepthsTuning.glaze(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 20.0)
				.set("minecraft:generic.attack_damage", 6.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:monarchs_guard", FrozenDepthsTuning.monarchsGuard(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 35.0)
				.set("minecraft:generic.attack_damage", 6.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:monarchs_soldier", FrozenDepthsTuning.monarchsSoldier(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 40.0)
				.set("minecraft:generic.attack_damage", 5.0)
				.set("minecraft:generic.armor", 4.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:general_monarchs_soldier", FrozenDepthsTuning.generalMonarchsSoldier(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 65.0)
				.set("minecraft:generic.attack_damage", 8.0)
				.set("minecraft:generic.armor", 6.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:frosted_ranger", FrozenDepthsTuning.frostedRanger(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 28.0)
				.set("minecraft:generic.attack_damage", 2.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:general_frosted_ranger", FrozenDepthsTuning.generalFrostedRanger(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 45.0)
				.set("minecraft:generic.attack_damage", 4.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
			String damageId = RWA_Effects.damageAttributeId();
			config.entries.get("loot_n_explore:frosted_ranger").set(damageId, 1.0);
			config.entries.get("loot_n_explore:general_frosted_ranger").set(damageId, 1.5);
		}
		return config;
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

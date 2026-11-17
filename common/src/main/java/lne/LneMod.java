package lne;

import lne.fx.LNEFx;
import lne.platform.LNEPlatform;
import lne.ability.RelicAbilities;
import lne.item.relics.LNE_RelicItems;

import lne.blocks.ModBlocks;
import lne.particle.Particles;
import lne.compat.CompatRegistry;
import lne.compat.items.RWA_Effects;
import lne.config.StructurePoolAPIDefaults;
import lne.config.EffectsConfig;
import lne.effects.Effects;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.FrozenDepthsTuning;
import lne.item.Group;
import lne.item.ItemsRegistry;
import lne.item.ModSpawnEggs;
import lne.server.LNEServerEvents;
import lne.worldgen.map.ModMapDecorations;
import lne.sounds.ModSounds;
import lne.util.LegacyIds;
import lne.worldgen.processor.ModProcessorTypes;
import lne.worldgen.structure.ModStructureTypes;
import lne.worldgen.villages.LNEVillagerProfessions;
import lne.worldgen.villages.LNEVillagerTrades;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.minecraft.util.Identifier;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;


public class LneMod {
	public static final String MOD_ID = "loot_n_explore";
    public static final Logger LOGGER = LoggerFactory.getLogger("loot_n_explore");

	public static ConfigManager<StructurePoolConfig> villageConfig = null;
	public static ConfigManager<EffectsConfig> effectsConfig = null;
	public static ConfigManager<EntityAttributeConfig> entityAttributeConfig = null;
	public static ConfigManager<lne.config.FrozenDepthsConfig> frozenDepthsConfig = null;
	private static final String FROZEN_DEPTHS_CONFIGS = MOD_ID + "/frozen_depths";
	public static ConfigManager<lne.config.FrostsiegeConfig> frostsiegeConfig = new ConfigManager<>("frostsiege", new lne.config.FrostsiegeConfig())
			.builder().setDirectory(FROZEN_DEPTHS_CONFIGS).sanitize(true).build();
	public static ConfigManager<lne.config.TweaksConfig> tweaksConfig = new ConfigManager<>("tweaks", new lne.config.TweaksConfig())
			.builder().setDirectory(MOD_ID).sanitize(true).build();

	public static void init() {
		tweaksConfig.refresh();
		tweaksConfig.save();
		frostsiegeConfig.refresh();
		frostsiegeConfig.save();
		effectsConfig = new ConfigManager<EffectsConfig>("effects_v2", new EffectsConfig())
				.builder().setDirectory(MOD_ID).sanitize(true).build();
		villageConfig = new ConfigManager<>("villages", StructurePoolAPIDefaults.villages)
				.builder().setDirectory(MOD_ID).sanitize(true).build();
		entityAttributeConfig = new ConfigManager<>("entity_attributes_v1", seededEntityAttributeDefaults())
				.builder().setDirectory(FROZEN_DEPTHS_CONFIGS).sanitize(true).build();

		frozenDepthsConfig = new ConfigManager<>("frozen_depths_v1", new lne.config.FrozenDepthsConfig())
				.builder().setDirectory(FROZEN_DEPTHS_CONFIGS).sanitize(true).build();

		effectsConfig.refresh();
		frozenDepthsConfig.refresh();
		frozenDepthsConfig.save();
		villageConfig.refresh();
		entityAttributeConfig.refresh();
		migrateLegacyEntityIds(entityAttributeConfig.value);
		entityAttributeConfig.save();
		RelicAbilities.loadConfig();
		LNEFx.register();
		lne.item.weapons.WeaponAbilities.loadConfig();
		LNE_RelicItems.registerComponentHooks();
		if (!LNEPlatform.isModLoaded("lithostitched")) {
			StructurePoolAPI.injectAll(villageConfig.value);
		}
	}

	private static void migrateLegacyEntityIds(EntityAttributeConfig config) {
		var migrated = new LinkedHashMap<String, EntityAttributeConfig.Entry>();
		config.entries.forEach((id, entry) -> {
			String renamed = LegacyIds.remapEntityId(id);
			if (renamed.equals(id) || !config.entries.containsKey(renamed)) migrated.put(renamed, entry);
			if (entry.custom != null) {
				for (var value : entry.custom.values()) {
					if (value.string != null) value.string = LegacyIds.remapEntityId(value.string);
					if (value.normal_string != null) value.normal_string = LegacyIds.remapEntityId(value.normal_string);
					if (value.hard_string != null) value.hard_string = LegacyIds.remapEntityId(value.hard_string);
				}
			}
		});
		config.entries = migrated;
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
		config.entries.put("loot_n_explore:frozen_magician", FrozenDepthsTuning.frozenMagician(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 30.0)
				.set("minecraft:generic.attack_damage", 3.0)
				.set("spell_power:frost", 3.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:general_frozen_magician", FrozenDepthsTuning.generalFrozenMagician(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 100.0, 1.25, 1.5)
				.set("minecraft:generic.attack_damage", 5.0)
				.set("spell_power:frost", 4.5)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:frost_hound", FrozenDepthsTuning.frostHound(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 20.0)
				.set("minecraft:generic.attack_damage", 5.0)));
		config.entries.put("loot_n_explore:frost_haunted", FrozenDepthsTuning.frostHaunted(new EntityAttributeConfig.Entry()
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
		config.entries.put("loot_n_explore:general_monarchs_guard", FrozenDepthsTuning.generalMonarchsGuard(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 100.0, 1.25, 1.5)
				.set("minecraft:generic.attack_damage", 9.0)
				.set("minecraft:generic.armor", 4.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:monarchs_soldier", FrozenDepthsTuning.monarchsSoldier(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 40.0)
				.set("minecraft:generic.attack_damage", 5.0)
				.set("minecraft:generic.armor", 4.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:general_monarchs_soldier", FrozenDepthsTuning.generalMonarchsSoldier(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 100.0, 1.25, 1.5)
				.set("minecraft:generic.attack_damage", 8.0)
				.set("minecraft:generic.armor", 6.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:frozen_ranger", FrozenDepthsTuning.frozenRanger(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 28.0)
				.set("minecraft:generic.attack_damage", 2.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		config.entries.put("loot_n_explore:general_frozen_ranger", FrozenDepthsTuning.generalFrozenRanger(new EntityAttributeConfig.Entry()
				.set("minecraft:generic.max_health", 100.0, 1.25, 1.5)
				.set("minecraft:generic.attack_damage", 4.0)
				.set("thermoo:generic.min_temperature", 5.0)
				.set("thermoo:generic.frost_resistance", 10.0)));
		if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
			String damageId = RWA_Effects.damageAttributeId();
			config.entries.get("loot_n_explore:frozen_ranger").set(damageId, 1.0);
			config.entries.get("loot_n_explore:general_frozen_ranger").set(damageId, 1.5);
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

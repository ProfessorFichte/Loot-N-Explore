package more_rpg_loot;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.compat.CompatRegistry;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.ModSpawnEggs;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.ItemsRegistry;
import more_rpg_loot.config.Default;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.util.ChestLootInjection;
import more_rpg_loot.util.EntityLootInjection;
import more_rpg_loot.worldgen.gen.ModWorldGen;
import more_rpg_loot.worldgen.villages.Villages;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.tinyconfig.ConfigManager;
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

	@Override
	public void onInitialize() {
		villageConfig.refresh();
		ModBlocks.register();
		ItemsRegistry.registerModItems();
		ModEntities.register();
		ModSpawnEggs.register();
		Group.registerItemGroups();
		Particles.register();
		Villages.register();
		Effects.register();
		ModSounds.register();
		ModWorldGen.generateModWorldGen();
		CompatRegistry.registerModCompat();
		EntityLootInjection.modifyLootEntityTables();
		ChestLootInjection.modifyChestLootTables();


		/*
		if(FabricLoader.getInstance().isModLoaded("archers")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("loot_n_explore_archers"),
					FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
		}
		if(FabricLoader.getInstance().isModLoaded("wizards")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("loot_n_explore_wizards"),
					FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
		}
		if(FabricLoader.getInstance().isModLoaded("paladins")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("loot_n_explore_paladins"),
					FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
		}
		if(FabricLoader.getInstance().isModLoaded("elemental_wizards_rpg")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("loot_n_explore_elemental_wizards_rpg"),
					FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
		}
		if(!FabricLoader.getInstance().isModLoaded("forcemaster_rpg")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("loot_n_explore_forcemaster_rpg"),
					FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
		}
		if(FabricLoader.getInstance().isModLoaded("jewelry")) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("loot_n_explore_jewelry"),
					FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);
		}*/

	}
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
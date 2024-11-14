package more_rpg_loot;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.mob.ModSpawnEggs;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.Items;
import more_rpg_loot.config.Default;
import more_rpg_loot.item.items.ModPotions;
import more_rpg_loot.item.weapons.WeaponsRegister;
import more_rpg_loot.sounds.ModSounds;
import more_rpg_loot.worldgen.gen.ModWorldGen;
import more_rpg_loot.worldgen.villages.Villages;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import more_rpg_loot.config.ItemConfig;
import net.minecraft.util.Identifier;
import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPGLoot implements ModInitializer {
	public static final String MOD_ID = "loot_n_explore";
    public static final Logger LOGGER = LoggerFactory.getLogger("loot_n_explore");

	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<ItemConfig>
			("items_v1", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<StructurePoolConfig> villageConfig = new ConfigManager<>
			("villages", Default.villages)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	private void registerLootItemGroup() {
		Group.RPG_LOOT = FabricItemGroup.builder()
				.icon(() -> new ItemStack(WeaponsRegister.elderGuardianSword.item()))
				.displayName(Text.translatable("itemGroup." + MOD_ID + ".loot.general"))
				.build();
		Registry.register(Registries.ITEM_GROUP, Group.RPG_LOOT_KEY, Group.RPG_LOOT);
	}
	private void registerFoodItemGroup() {
		Group.RPG_FOOD = FabricItemGroup.builder()
				.icon(() -> new ItemStack(ModBlocks.INNKEEPER_SHELF.block()))
				.displayName(Text.translatable("itemGroup." + MOD_ID + ".food.general"))
				.build();
		Registry.register(Registries.ITEM_GROUP, Group.RPG_FOOD_KEY, Group.RPG_FOOD);
	}
	private void registerBlockItemGroup() {
		Group.RPG_BLOCKS = FabricItemGroup.builder()
				.icon(() -> new ItemStack(ModBlocks.BLUE_ICE_BRICKS.block()))
				.displayName(Text.translatable("itemGroup." + MOD_ID + ".blocks.general"))
				.build();
		Registry.register(Registries.ITEM_GROUP, Group.RPG_BLOCK_KEY, Group.RPG_BLOCKS);
	}
	@Override
	public void onInitialize() {
		itemConfig.refresh();
		villageConfig.refresh();
		ModBlocks.register();
		Items.registerModItems();
		registerLootItemGroup();
		registerFoodItemGroup();
		registerBlockItemGroup();
		ModEntities.register();
		ModSpawnEggs.register();
		Group.registerItemGroups();
		WeaponsRegister.register(itemConfig.value.ranged_weapons, itemConfig.value.melee_weapons);
		itemConfig.save();
		Particles.register();
		Villages.register();
		Effects.register();
		ModSounds.register();
		ModWorldGen.generateModWorldGen();
		ModPotions.registerPotions();
		ModPotions.registerPotionsRecipes();

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
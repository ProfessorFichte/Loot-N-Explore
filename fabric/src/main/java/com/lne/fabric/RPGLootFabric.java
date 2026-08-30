package com.lne.fabric;

import com.lne.fabric.compat.CompatFeatures;
import com.lne.fabric.platform.FabricEventsImpl;
import com.lne.fabric.platform.FabricPlatformImpl;
import com.lne.fabric.worldgen.FabricVegetationGeneration;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.ModPotions;
import more_rpg_loot.network.FrostMonarchSpawnOverlayPayload;
import more_rpg_loot.network.FrozenDepthsMusicPayload;
import more_rpg_loot.platform.LNEEvents;
import more_rpg_loot.platform.LNEPlatform;
import more_rpg_loot.worldgen.villages.LNEVillagerProfessions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;

public final class RPGLootFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LNEPlatform.set(new FabricPlatformImpl());
        LNEEvents.set(new FabricEventsImpl());
        LNEVillagerProfessions.poiRegistrar = (id, block) -> PointOfInterestHelper.register(id, 1, 1, block);
        CompatFeatures.init();
        registerPayloads();
        RPGLoot.init();
        RPGLoot.registerEffects();
        RPGLoot.registerBlocks();
        RPGLoot.registerEntities();
        ModEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
        RPGLoot.registerItems();
        RPGLoot.registerSounds();
        RPGLoot.registerItemGroups();
        RPGLoot.registerVillagePoi();
        RPGLoot.registerVillageProfessions();
        RPGLoot.registerVillagerSchedules();
        RPGLoot.registerServerEvents();
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> ModPotions.registerRecipes(builder::registerPotionRecipe));
        FabricVegetationGeneration.generate();
    }

    private static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(FrozenDepthsMusicPayload.ID, FrozenDepthsMusicPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FrostMonarchSpawnOverlayPayload.ID, FrostMonarchSpawnOverlayPayload.CODEC);
    }
}

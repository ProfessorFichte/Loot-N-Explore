package com.lne.fabric.client;

import more_rpg_loot.client.RPGLootClient;
import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.hud.FrostMonarchSpawnOverlay;
import more_rpg_loot.client.models.CustomModelHelper;
import more_rpg_loot.client.particle.DragonClawParticle;
import more_rpg_loot.client.platform.LNEClientModels;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.network.FrostMonarchSpawnOverlayPayload;
import more_rpg_loot.network.FrozenDepthsMusicPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.render.RenderLayer;

public final class RPGLootFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayers.registerAll((layer, supplier) -> EntityModelLayerRegistry.registerModelLayer(layer, supplier::get));
        RPGLootClient.registerEntityRenderers(EntityRendererRegistry::register);
        ParticleFactoryRegistry.getInstance().register(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> RPGLootClient.onBossEntityLoad(entity));
        ClientPlayNetworking.registerGlobalReceiver(FrozenDepthsMusicPayload.ID,
                (payload, context) -> RPGLootClient.onFrozenDepthsMusic(payload.start()));
        ClientPlayNetworking.registerGlobalReceiver(FrostMonarchSpawnOverlayPayload.ID,
                (payload, context) -> RPGLootClient.onFrostMonarchSpawnOverlay());
        HudRenderCallback.EVENT.register(FrostMonarchSpawnOverlay::render);
        RPGLootClient.registerBlockRenderLayers(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        RPGLootClient.registerModelIds();
        ModelLoadingPlugin.register(pluginContext -> CustomModelHelper.modelIds().forEach(pluginContext::addModels));
        LNEClientModels.lookup = id -> MinecraftClient.getInstance().getBakedModelManager().getModel(id);
        RPGLootClient.registerModelPredicates();
    }
}

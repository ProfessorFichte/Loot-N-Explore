package com.lne.fabric.client;

import more_rpg_loot.client.RPGLootClient;
import net.fabricmc.api.ClientModInitializer;

public final class RPGLootFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RPGLootClient.registerModelLayers();
        RPGLootClient.registerEntityRenderers();
        RPGLootClient.registerParticleFactories();
        RPGLootClient.registerMusicEvents();
        RPGLootClient.init();
        RPGLootClient.registerModelPredicates();
    }
}

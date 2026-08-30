package com.lne.neoforge.client;

import more_rpg_loot.client.RPGLootClient;
import more_rpg_loot.network.FrozenDepthsMusicPayload;

public final class LNEClientPackets {
    private LNEClientPackets() {}

    public static void onMusic(FrozenDepthsMusicPayload payload) {
        RPGLootClient.onFrozenDepthsMusic(payload.start());
    }

    public static void onSpawnOverlay() {
        RPGLootClient.onFrostMonarchSpawnOverlay();
    }
}

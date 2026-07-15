package com.lne.fabric;

import more_rpg_loot.RPGLoot;
import net.fabricmc.api.ModInitializer;

public final class RPGLootFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RPGLoot.registerPayloads();
        RPGLoot.init();
        RPGLoot.registerEffects();
        RPGLoot.registerBlocks();
        RPGLoot.registerEntities();
        RPGLoot.registerItems();
        RPGLoot.registerSounds();
        RPGLoot.registerItemGroups();
        RPGLoot.registerVillagePoi();
        RPGLoot.registerVillageProfessions();
        RPGLoot.registerVillagerSchedules();
        RPGLoot.registerServerEvents();
    }
}

package com.lne.neoforge;

import com.lne.neoforge.compat.CompatFeatures;
import more_rpg_loot.RPGLoot;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(RPGLoot.MOD_ID)
public final class RPGLootNeoForge {
    public RPGLootNeoForge(IEventBus modBus) {
        CompatFeatures.init();
        RPGLoot.registerPayloads();
        RPGLoot.init();
        RPGLoot.registerServerEvents();
        modBus.addListener(RegisterEvent.class, RPGLootNeoForge::register);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.STATUS_EFFECT, reg -> RPGLoot.registerEffects());
        event.register(RegistryKeys.BLOCK, reg -> RPGLoot.registerBlocks());
        event.register(RegistryKeys.ENTITY_TYPE, reg -> RPGLoot.registerEntities());
        event.register(RegistryKeys.ITEM, reg -> RPGLoot.registerItems());
        event.register(RegistryKeys.SOUND_EVENT, reg -> RPGLoot.registerSounds());
        event.register(RegistryKeys.ITEM_GROUP, reg -> RPGLoot.registerItemGroups());
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> RPGLoot.registerVillagePoi());
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> RPGLoot.registerVillageProfessions());
        event.register(RegistryKeys.SCHEDULE, reg -> RPGLoot.registerVillagerSchedules());
    }
}

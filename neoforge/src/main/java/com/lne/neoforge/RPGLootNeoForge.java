package com.lne.neoforge;

import com.lne.neoforge.client.LNEClientPackets;
import com.lne.neoforge.compat.CompatFeatures;
import com.lne.neoforge.platform.NeoForgeEventsImpl;
import com.lne.neoforge.platform.NeoForgePlatformImpl;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.item.ModPotions;
import more_rpg_loot.network.FrostMonarchSpawnOverlayPayload;
import more_rpg_loot.network.FrozenDepthsMusicPayload;
import more_rpg_loot.platform.LNEEvents;
import more_rpg_loot.platform.LNEPlatform;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(RPGLoot.MOD_ID)
public final class RPGLootNeoForge {
    public RPGLootNeoForge(IEventBus modBus) {
        LNEPlatform.set(new NeoForgePlatformImpl());
        LNEEvents.set(new NeoForgeEventsImpl(modBus));
        CompatFeatures.init();
        RPGLoot.init();
        RPGLoot.registerServerEvents();
        modBus.addListener(RegisterEvent.class, RPGLootNeoForge::register);
        modBus.addListener(RegisterPayloadHandlersEvent.class, RPGLootNeoForge::registerPayloads);
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeEventsImpl::dispatchItemGroup);
        modBus.addListener(EntityAttributeCreationEvent.class,
                event -> ModEntities.registerAttributes((type, builder) -> event.put(type, builder.build())));
        NeoForge.EVENT_BUS.addListener(RegisterBrewingRecipesEvent.class,
                event -> ModPotions.registerRecipes(event.getBuilder()::registerPotionRecipe));
        NeoForge.EVENT_BUS.addListener(VillagerTradesEvent.class, NeoForgeEventsImpl::dispatchVillagerTrades);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(RPGLoot.MOD_ID);
        registrar.playToClient(FrozenDepthsMusicPayload.ID, FrozenDepthsMusicPayload.CODEC, RPGLootNeoForge::handleMusic);
        registrar.playToClient(FrostMonarchSpawnOverlayPayload.ID, FrostMonarchSpawnOverlayPayload.CODEC, RPGLootNeoForge::handleSpawnOverlay);
    }

    private static void handleMusic(FrozenDepthsMusicPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> LNEClientPackets.onMusic(payload));
    }

    private static void handleSpawnOverlay(FrostMonarchSpawnOverlayPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> LNEClientPackets.onSpawnOverlay());
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

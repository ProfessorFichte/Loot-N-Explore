package com.lne.neoforge.client;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.client.RPGLootClient;
import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.particle.DragonClawParticle;
import more_rpg_loot.client.particle.Particles;
import net.minecraft.client.particle.SnowflakeParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@EventBusSubscriber(modid = RPGLoot.MOD_ID, value = Dist.CLIENT)
public class RPGLootNeoForgeClient {
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        EntityModelLayers.registerAll(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RPGLootClient.registerEntityRenderers();
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        event.registerSpriteSet(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RPGLootClient.registerMusicEvents();
        RPGLootClient.init();
        event.enqueueWork(RPGLootClient::registerModelPredicates);
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ModelIdentifier(Identifier.of(RPGLoot.MOD_ID, "block/frozen_depths/icicle_straight"), ""));
    }
}

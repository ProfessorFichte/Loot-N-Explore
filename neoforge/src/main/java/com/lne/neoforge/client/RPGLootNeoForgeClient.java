package com.lne.neoforge.client;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.client.RPGLootClient;
import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.hud.FrostMonarchSpawnOverlay;
import more_rpg_loot.client.models.CustomModelHelper;
import more_rpg_loot.client.particle.DragonClawParticle;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.client.platform.LNEClientModels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = RPGLoot.MOD_ID, value = Dist.CLIENT)
public class RPGLootNeoForgeClient {
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        EntityModelLayers.registerAll(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RPGLootClient.registerEntityRenderers(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        event.registerSpriteSet(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RPGLootClient.registerModelIds();
        LNEClientModels.lookup = id -> MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(id, "standalone"));
        event.enqueueWork(() -> {
            RPGLootClient.registerModelPredicates();
            RPGLootClient.registerBlockRenderLayers(block -> RenderLayers.setRenderLayer(block, RenderLayer.getCutout()));
        });
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (Identifier id : CustomModelHelper.modelIds()) {
            event.register(ModelIdentifier.standalone(id));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClient()) {
            RPGLootClient.onBossEntityLoad(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        FrostMonarchSpawnOverlay.render(event.getGuiGraphics(), event.getPartialTick());
    }
}

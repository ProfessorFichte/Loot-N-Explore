package com.lne.neoforge.client;

import lne.LneMod;
import lne.client.LneClient;
import lne.client.entity.models.EntityModelLayers;
import lne.client.hud.EntityHudTextOverlay;
import com.mrpg_lib.client.model.CustomModelHelper;
import lne.client.particle.DragonClawParticle;
import lne.client.particle.FrostMistParticle;
import lne.client.particle.FrozenSoulMoteParticle;
import lne.particle.Particles;
import lne.client.platform.LNEClientModels;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.particle.SonicBoomParticle;
import net.minecraft.client.particle.SoulParticle;
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

@EventBusSubscriber(modid = LneMod.MOD_ID, value = Dist.CLIENT)
public class LneNeoForgeClient {
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        EntityModelLayers.registerAll(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        LneClient.registerEntityRenderers(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        event.registerSpriteSet(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
        event.registerSpriteSet(Particles.FROZEN_SOUL_MOTE, FrozenSoulMoteParticle.Factory::new);
        event.registerSpriteSet(Particles.FROST_MIST, FrostMistParticle.Factory::new);
        event.registerSpriteSet(Particles.FROZEN_SOUL, SoulParticle.Factory::new);
        event.registerSpriteSet(Particles.FROST_SCREECH, SonicBoomParticle.Factory::new);
        event.registerSpriteSet(Particles.FROZEN_FLAME, net.minecraft.client.particle.FlameParticle.Factory::new);
        event.registerSpriteSet(Particles.FROZEN_SMALL_FLAME, net.minecraft.client.particle.FlameParticle.SmallFactory::new);
        event.registerSpriteSet(Particles.FROZEN_VAULT_CONNECTION, net.minecraft.client.particle.ConnectionParticle.VaultConnectionFactory::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LneClient.init();
        LneClient.registerModelIds();
        LNEClientModels.lookup = id -> MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(id, "standalone"));
        event.enqueueWork(() -> {
            LneClient.registerModelPredicates();
            LneClient.registerBlockRenderLayers(
                    block -> RenderLayers.setRenderLayer(block, RenderLayer.getCutout()),
                    block -> RenderLayers.setRenderLayer(block, RenderLayer.getTranslucent()));
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
            LneClient.onBossEntityLoad(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        EntityHudTextOverlay.render(event.getGuiGraphics(), event.getPartialTick());
    }
}

package com.lne.fabric.client;

import lne.client.LneClient;
import lne.client.entity.models.EntityModelLayers;
import lne.client.hud.EntityHudTextOverlay;
import com.mrpg_lib.client.model.CustomModelHelper;
import lne.client.particle.DragonClawParticle;
import lne.client.particle.BookDustParticle;
import lne.client.particle.FrostMistParticle;
import lne.client.particle.FrozenSoulMoteParticle;
import lne.client.platform.LNEClientModels;
import lne.particle.Particles;
import lne.internal.network.EntityHudTextPayload;
import lne.internal.network.StructureMusicPayload;
import lne.internal.network.RelicStacksPayload;
import lne.internal.network.WeaponCooldownPayload;
import lne.client.hud.WeaponReadyCue;
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
import net.minecraft.client.particle.SonicBoomParticle;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.RenderLayer;

public final class LneFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayers.registerAll((layer, supplier) -> EntityModelLayerRegistry.registerModelLayer(layer, supplier::get));
        LneClient.init();
        LneClient.registerEntityRenderers(EntityRendererRegistry::register);
        ParticleFactoryRegistry.getInstance().register(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROZEN_SOUL_MOTE, FrozenSoulMoteParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROST_MIST, FrostMistParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.BOOK_DUST, BookDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROZEN_SOUL, SoulParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROST_SCREECH, SonicBoomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROZEN_FLAME, net.minecraft.client.particle.FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROZEN_SMALL_FLAME, net.minecraft.client.particle.FlameParticle.SmallFactory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FROZEN_VAULT_CONNECTION, net.minecraft.client.particle.ConnectionParticle.VaultConnectionFactory::new);
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> LneClient.onBossEntityLoad(entity));
        ClientPlayNetworking.registerGlobalReceiver(StructureMusicPayload.ID,
                (payload, context) -> LneClient.onStructureMusic(payload));
        ClientPlayNetworking.registerGlobalReceiver(EntityHudTextPayload.ID,
                (payload, context) -> LneClient.onEntityHudText(payload));
        ClientPlayNetworking.registerGlobalReceiver(RelicStacksPayload.ID,
                (payload, context) -> LneClient.onRelicStacks(payload));
        ClientPlayNetworking.registerGlobalReceiver(WeaponCooldownPayload.ID,
                (payload, context) -> WeaponReadyCue.onRangedCooldown(payload.ability(), payload.ticks()));
        HudRenderCallback.EVENT.register(EntityHudTextOverlay::render);
        LneClient.registerBlockRenderLayers(
                block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()),
                block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
        LneClient.registerModelIds();
        ModelLoadingPlugin.register(pluginContext -> CustomModelHelper.modelIds().forEach(pluginContext::addModels));
        LNEClientModels.lookup = id -> MinecraftClient.getInstance().getBakedModelManager().getModel(id);
        LneClient.registerModelPredicates();
    }
}

package more_rpg_loot.client;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.renderers.BarrierIcicleRenderer;
import more_rpg_loot.client.entity.renderers.CustomCloudRenderer;
import more_rpg_loot.client.entity.renderers.ModMobRenderers;
import more_rpg_loot.client.entity.renderers.StraightIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frostmonarch.FrostmonarchEntityRenderer;
import more_rpg_loot.client.models.CustomModelHelper;
import more_rpg_loot.client.particle.DragonClawParticle;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class RPGLootClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
       EntityModelLayers.registerModelLayers();
        EntityRendererRegistry.register(ModEntities.FROST_MONARCH, FrostmonarchEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STRAIGHT_ICICLE, StraightIcicleRenderer::new);
        EntityRendererRegistry.register(ModEntities.BARRIER_ICICLE, BarrierIcicleRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUSTOM_CLOUD, CustomCloudRenderer::new);
        ModMobRenderers.register();
        ParticleFactoryRegistry.getInstance().register(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_CHAIN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOULFROST_LANTERN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROST_BLOOM.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_FROST_BLOOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MONARCHS_CROWN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_TRIAL_SPAWNER.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_VAULT.block(), RenderLayer.getCutout());

        List<Identifier> customModels = List.of(
                Identifier.of(MOD_ID, "block/icicle_straight")
        );
        CustomModelHelper.registerModelIds(customModels);
        CustomModelHelper.initialize();
        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            try {
                net.spell_engine.api.render.CustomModels.registerModelIds(List.of(
                        Identifier.of(MOD_ID, "projectile/small_avalanche"),
                        Identifier.of(MOD_ID, "projectile/wither_skull")
                ));
            } catch (Exception e) {
            }
        }
    }

}

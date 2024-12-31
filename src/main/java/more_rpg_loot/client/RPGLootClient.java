package more_rpg_loot.client;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.effect.FreezingParticles;
import more_rpg_loot.client.entity.renderers.ModMobRenderers;
import more_rpg_loot.client.particle.DragonClawParticle;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.effects.Effects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class RPGLootClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModMobRenderers.register();
        ParticleFactoryRegistry.getInstance().register(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_CHAIN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOULFROST_LANTERN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROST_BLOOM.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_FROST_BLOOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MONARCHS_CROWN.block(), RenderLayer.getCutout());

        if(FabricLoader.getInstance().isModLoaded("spell_engine")){
            CustomModels.registerModelIds(List.of(
                    new Identifier(MOD_ID, "projectile/small_avalanche")
            ));
            CustomParticleStatusEffect.register(Effects.FREEZING, new FreezingParticles(1));
        }
    }

}

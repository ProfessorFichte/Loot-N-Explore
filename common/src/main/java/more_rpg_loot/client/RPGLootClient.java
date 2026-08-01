package more_rpg_loot.client;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.entity.models.EntityModelLayers;
import more_rpg_loot.client.entity.renderers.ModMobRenderers;
import more_rpg_loot.client.entity.renderers.frozen_depths.misc.BarrierIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.misc.StraightIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.misc.TrackingIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_guard.ThrownLanceEntityRenderer;
import more_rpg_loot.client.entity.renderers.generic.CustomCloudRenderer;
import more_rpg_loot.client.models.CustomModelHelper;
import more_rpg_loot.client.hud.FrostMonarchSpawnOverlay;
import more_rpg_loot.client.music.LNEMusicManager;
import more_rpg_loot.client.particle.DragonClawParticle;
import more_rpg_loot.client.particle.Particles;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import more_rpg_loot.network.FrostMonarchSpawnOverlayPayload;
import more_rpg_loot.network.FrozenDepthsMusicPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class RPGLootClient {

    public static void registerModelLayers() {
        EntityModelLayers.registerModelLayers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.STRAIGHT_ICICLE, StraightIcicleRenderer::new);
        EntityRendererRegistry.register(ModEntities.BARRIER_ICICLE, BarrierIcicleRenderer::new);
        EntityRendererRegistry.register(ModEntities.CUSTOM_CLOUD, CustomCloudRenderer::new);
        EntityRendererRegistry.register(ModEntities.LNE_ABILITY_ARROW, ArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROZEN_ARROW, ArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THROWN_LANCE, ThrownLanceEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TRACKING_ICICLE, TrackingIcicleRenderer::new);
        ModMobRenderers.register();
    }

    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(Particles.DRAGON_CLAW, DragonClawParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(Particles.FREEZING_SNOWFLAKE, SnowflakeParticle.Factory::new);
    }

    public static void registerModelPredicates() {
        for (var entry : LNE_WeaponItems.rangedEntries) {
            registerBowOrCrossbowPredicates(entry.item());
        }
        registerBowOrCrossbowPredicates(CommonItems.FROZEN_BOW.item());
    }

    private static void registerBowOrCrossbowPredicates(Item item) {
        if (item instanceof BowItem) {
            ModelPredicateProviderRegistry.register(item, Identifier.of("pulling"),
                (stack, world, entity, seed) ->
                    entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
            ModelPredicateProviderRegistry.register(item, Identifier.of("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null || !entity.isUsingItem() || entity.getActiveItem() != stack) return 0.0F;
                    return BowItem.getPullProgress(entity.getItemUseTime());
                });
        } else if (item instanceof CrossbowItem) {
            ModelPredicateProviderRegistry.register(item, Identifier.of("pulling"),
                (stack, world, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    return CrossbowItem.isCharged(stack) ? 0.0F : (entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
                });
            ModelPredicateProviderRegistry.register(item, Identifier.of("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null || CrossbowItem.isCharged(stack)) return 0.0F;
                    if (!entity.isUsingItem() || entity.getActiveItem() != stack) return 0.0F;
                    return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(stack, entity);
                });
            ModelPredicateProviderRegistry.register(item, Identifier.of("charged"),
                (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
            ModelPredicateProviderRegistry.register(item, Identifier.of("firework"),
                (stack, world, entity, seed) -> {
                    ChargedProjectilesComponent charged = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
                    if (!CrossbowItem.isCharged(stack) || charged == null) return 0.0F;
                    for (ItemStack projectile : charged.getProjectiles()) {
                        if (projectile.isOf(Items.FIREWORK_ROCKET)) return 1.0F;
                    }
                    return 0.0F;
                });
        }
    }

    public static void registerMusicEvents() {
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof FrostMonarchEntity monarch && !monarch.isFakeDeath()) {
                LNEMusicManager.startBossMusic(monarch);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(FrozenDepthsMusicPayload.ID, (payload, context) -> {
            if (payload.start()) {
                LNEMusicManager.startAmbientMusic();
            } else {
                LNEMusicManager.stopAmbientMusic();
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(FrostMonarchSpawnOverlayPayload.ID, (payload, context) ->
                FrostMonarchSpawnOverlay.start());
        HudRenderCallback.EVENT.register(FrostMonarchSpawnOverlay::render);
    }

    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_CHAIN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOULFROST_LANTERN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROST_BLOOM.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_FROST_BLOOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MONARCHS_CROWN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_TRIAL_SPAWNER.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_VAULT.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_BONES.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_TORCH.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROZEN_ADVENTURER.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_HANGING_INN_SIGN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ACACIA_HANGING_INN_SIGN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPRUCE_HANGING_INN_SIGN.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ICICLE_BAR.block(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ICICLE.block(), RenderLayer.getCutout());


        List<Identifier> customModels = List.of(
                Identifier.of(MOD_ID, "block/frozen_depths/icicle_straight")
        );
        CustomModelHelper.registerModelIds(customModels);
        CustomModelHelper.initialize();
    }
}

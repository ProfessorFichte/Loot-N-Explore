package more_rpg_loot.client;

import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.client.entity.renderers.ModMobRenderers;
import more_rpg_loot.client.entity.renderers.frozen_depths.misc.BarrierIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.misc.StraightIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.misc.TrackingIcicleRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_guard.ThrownLanceEntityRenderer;
import more_rpg_loot.client.entity.renderers.generic.CustomCloudRenderer;
import more_rpg_loot.client.models.CustomModelHelper;
import more_rpg_loot.client.hud.FrostMonarchSpawnOverlay;
import more_rpg_loot.client.music.LNEMusicManager;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import more_rpg_loot.mixin.ModelPredicateProviderRegistryInvoker;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

import static more_rpg_loot.RPGLoot.MOD_ID;

@Environment(EnvType.CLIENT)
public class RPGLootClient {

    public interface EntityRendererSink {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory);
    }

    public static void registerEntityRenderers(EntityRendererSink sink) {
        sink.register(ModEntities.STRAIGHT_ICICLE, StraightIcicleRenderer::new);
        sink.register(ModEntities.BARRIER_ICICLE, BarrierIcicleRenderer::new);
        sink.register(ModEntities.CUSTOM_CLOUD, CustomCloudRenderer::new);
        sink.register(ModEntities.LNE_ABILITY_ARROW, ArrowEntityRenderer::new);
        sink.register(ModEntities.FROZEN_ARROW, ArrowEntityRenderer::new);
        sink.register(ModEntities.THROWN_LANCE, ThrownLanceEntityRenderer::new);
        sink.register(ModEntities.TRACKING_ICICLE, TrackingIcicleRenderer::new);
        ModMobRenderers.register(sink);
    }

    public static void registerModelPredicates() {
        for (var entry : LNE_WeaponItems.rangedEntries) {
            registerBowOrCrossbowPredicates(entry.item());
        }
        registerBowOrCrossbowPredicates(CommonItems.FROZEN_BOW.item());
    }

    private static void registerBowOrCrossbowPredicates(Item item) {
        if (item instanceof BowItem) {
            ModelPredicateProviderRegistryInvoker.lne$register(item, Identifier.of("pulling"),
                (stack, world, entity, seed) ->
                    entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
            ModelPredicateProviderRegistryInvoker.lne$register(item, Identifier.of("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null || !entity.isUsingItem() || entity.getActiveItem() != stack) return 0.0F;
                    return BowItem.getPullProgress(entity.getItemUseTime());
                });
        } else if (item instanceof CrossbowItem) {
            ModelPredicateProviderRegistryInvoker.lne$register(item, Identifier.of("pulling"),
                (stack, world, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    return CrossbowItem.isCharged(stack) ? 0.0F : (entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
                });
            ModelPredicateProviderRegistryInvoker.lne$register(item, Identifier.of("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null || CrossbowItem.isCharged(stack)) return 0.0F;
                    if (!entity.isUsingItem() || entity.getActiveItem() != stack) return 0.0F;
                    return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(stack, entity);
                });
            ModelPredicateProviderRegistryInvoker.lne$register(item, Identifier.of("charged"),
                (stack, world, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
            ModelPredicateProviderRegistryInvoker.lne$register(item, Identifier.of("firework"),
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

    public static void onBossEntityLoad(Entity entity) {
        if (entity instanceof FrostMonarchEntity monarch && !monarch.isFakeDeath()) {
            LNEMusicManager.startBossMusic(monarch);
        }
    }

    public static void onFrozenDepthsMusic(boolean start) {
        if (start) {
            LNEMusicManager.startAmbientMusic();
        } else {
            LNEMusicManager.stopAmbientMusic();
        }
    }

    public static void onFrostMonarchSpawnOverlay() {
        FrostMonarchSpawnOverlay.start();
    }

    public static void registerBlockRenderLayers(Consumer<Block> cutout) {
        cutout.accept(ModBlocks.FROZEN_CHAIN.block());
        cutout.accept(ModBlocks.SOULFROST_LANTERN.block());
        cutout.accept(ModBlocks.FROST_BLOOM.block());
        cutout.accept(ModBlocks.POTTED_FROST_BLOOM);
        cutout.accept(ModBlocks.MONARCHS_CROWN.block());
        cutout.accept(ModBlocks.FROZEN_TRIAL_SPAWNER.block());
        cutout.accept(ModBlocks.FROZEN_VAULT.block());
        cutout.accept(ModBlocks.FROZEN_BONES.block());
        cutout.accept(ModBlocks.FROZEN_TORCH.block());
        cutout.accept(ModBlocks.FROZEN_ADVENTURER.block());
        cutout.accept(ModBlocks.OAK_HANGING_INN_SIGN.block());
        cutout.accept(ModBlocks.ACACIA_HANGING_INN_SIGN.block());
        cutout.accept(ModBlocks.SPRUCE_HANGING_INN_SIGN.block());
        cutout.accept(ModBlocks.ICICLE_BAR.block());
        cutout.accept(ModBlocks.ICICLE.block());
    }

    public static void registerModelIds() {
        CustomModelHelper.registerModelIds(List.of(
                Identifier.of(MOD_ID, "block/frozen_depths/icicle_straight")
        ));
    }
}

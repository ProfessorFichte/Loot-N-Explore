package lne.client;

import com.mrpg_lib.compat.player_animator.api.MobAnimations;
import lne.compat.player_animator.LneMobAnimationCompat;
import net.minecraft.client.MinecraftClient;
import lne.blocks.FrozenDepthsBlocks;
import lne.blocks.ModBlocks;
import lne.client.entity.renderers.ModMobRenderers;
import lne.client.entity.renderers.frozen_depths.misc.BarrierIcicleRenderer;
import lne.client.entity.renderers.frozen_depths.misc.IceWallRenderer;
import lne.client.entity.renderers.frozen_depths.frozen_magician.FrostDecoyRenderer;
import lne.client.entity.renderers.frozen_depths.misc.StraightIcicleRenderer;
import lne.client.entity.renderers.frozen_depths.misc.TrackingIcicleRenderer;
import lne.client.entity.renderers.frozen_depths.monarchs_guard.ThrownLanceEntityRenderer;
import lne.client.entity.renderers.generic.CustomCloudRenderer;
import com.mrpg_lib.client.model.CustomModelHelper;
import com.mrpg_lib.entity.MRPGCEntities;
import lne.client.hud.EntityHudTextOverlay;
import lne.client.music.LNEMusicManager;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.item.CommonItems;
import lne.item.relics.RelicTooltips;
import lne.item.weapons.LNE_WeaponItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import lne.mixin.ModelPredicateProviderRegistryInvoker;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
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

import static lne.LneMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class LneClient {

    public interface EntityRendererSink {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory);
    }

    public static void init() {
        RelicTooltips.setViewer(() -> MinecraftClient.getInstance().player);
        if (MobAnimations.isAvailable()) {
            LneMobAnimationCompat.register();
        }
    }

    public static void registerEntityRenderers(EntityRendererSink sink) {
        sink.register(ModEntities.STRAIGHT_ICICLE, StraightIcicleRenderer::new);
        sink.register(ModEntities.BARRIER_ICICLE, BarrierIcicleRenderer::new);
        sink.register(ModEntities.ICE_WALL, IceWallRenderer::new);
        sink.register(ModEntities.ETERNAL_WINTER_PORTAL, EmptyEntityRenderer::new);
        sink.register(ModEntities.FROST_DECOY, FrostDecoyRenderer::new);
        sink.register(MRPGCEntities.CUSTOM_CLOUD, CustomCloudRenderer::new);
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

    public static void onStructureMusic(lne.internal.network.StructureMusicPayload payload) {
        if (payload.start()) {
            LNEMusicManager.startAmbientMusic(payload.track());
        } else {
            LNEMusicManager.stopAmbientMusic(payload.track());
        }
    }

    public static void onRelicStacks(lne.internal.network.RelicStacksPayload payload) {
        lne.item.relics.RelicStackCache.update(payload.entries());
    }

    public static void onEntityHudText(lne.internal.network.EntityHudTextPayload payload) {
        EntityHudTextOverlay.show(payload);
    }

    public static void registerBlockRenderLayers(Consumer<Block> cutout, Consumer<Block> translucent) {
        translucent.accept(FrozenDepthsBlocks.FRAGILE_ICE.block());
        for (var entry : ModBlocks.all) {
            if (entry.block() instanceof lne.blocks.themed.ThemedBlock) cutout.accept(entry.block());
        }
        cutout.accept(FrozenDepthsBlocks.FROZEN_CHAIN.block());
        cutout.accept(FrozenDepthsBlocks.SOULFROST_LANTERN.block());
        cutout.accept(FrozenDepthsBlocks.FROST_BLOOM.block());
        cutout.accept(FrozenDepthsBlocks.POTTED_FROST_BLOOM);
        cutout.accept(FrozenDepthsBlocks.MONARCHS_CROWN.block());
        cutout.accept(FrozenDepthsBlocks.FROZEN_BONES.block());
        cutout.accept(FrozenDepthsBlocks.FROZEN_TORCH.block());
        cutout.accept(FrozenDepthsBlocks.FROZEN_ADVENTURER.block());
        cutout.accept(FrozenDepthsBlocks.SEALED_MONARCH.block());
        ModBlocks.INN_SIGNS.forEach(sign -> cutout.accept(sign.block()));
        cutout.accept(FrozenDepthsBlocks.ICICLE_BAR.block());
        cutout.accept(FrozenDepthsBlocks.ICICLE.block());
        cutout.accept(FrozenDepthsBlocks.FROSTFLAME);
        cutout.accept(FrozenDepthsBlocks.FROZEN_KINGDOM_FLAG.block());
        cutout.accept(ModBlocks.EMPTY_BEER_MUG.block());
    }

    public static void registerModelIds() {
        CustomModelHelper.registerModelIds(List.of(
                Identifier.of(MOD_ID, "block/frozen_depths/icicle_straight"),
                Identifier.of(MOD_ID, "block/frozen_depths/ice_wall"),
                Identifier.of(MOD_ID, "entity/frozen_depths/icicle_projectile")
        ));
    }
}

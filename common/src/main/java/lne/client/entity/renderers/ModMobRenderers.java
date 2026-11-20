package lne.client.entity.renderers;

import lne.client.LneClient;
import lne.client.entity.renderers.relic.GuardianBeamRenderer;
import lne.client.entity.renderers.relic.RelicSkeletonRenderer;
import lne.client.entity.renderers.frozen_depths.frost_haunted.FrostHauntedEntityRenderer;
import lne.client.entity.renderers.frozen_depths.frostmonarch.FrostmonarchEntityRenderer;
import lne.client.entity.renderers.frozen_depths.frost_hound.FrostHoundRenderer;
import lne.client.entity.renderers.frozen_depths.frozen_magician.FrozenMagicianRenderer;
import lne.client.entity.renderers.frozen_depths.frozen_ranger.FrozenRangerRenderer;
import lne.client.entity.renderers.frozen_depths.glaze.GlazeEntityRenderer;
import lne.client.entity.renderers.frozen_depths.misc.GlowingFlyingItemRenderer;
import lne.client.entity.renderers.frozen_depths.monarchs_guard.MonarchsGuardRenderer;
import lne.client.entity.renderers.frozen_depths.monarchs_soldier.MonarchsSoldierRenderer;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.mob.frozen_magician.GeneralFrozenMagicianEntity;
import lne.entity.frozen_depths.mob.frozen_magician.FrozenMagicianEntity;
import lne.entity.frozen_depths.mob.frozen_ranger.FrozenRangerEntity;
import lne.entity.frozen_depths.mob.frozen_ranger.GeneralFrozenRangerEntity;
import lne.entity.frozen_depths.mob.monarchs_soldier.GeneralMonarchsSoldierEntity;
import lne.entity.frozen_depths.mob.monarchs_guard.GeneralMonarchsGuardEntity;
import lne.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import lne.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;

@Environment(EnvType.CLIENT)
public class ModMobRenderers {
    public static void register(LneClient.EntityRendererSink sink){
        sink.register(ModEntities.FROST_HAUNTED, FrostHauntedEntityRenderer::new);
        sink.register(ModEntities.FROST_MONARCH, FrostmonarchEntityRenderer::new);
        sink.register(ModEntities.GLAZE, GlazeEntityRenderer::new);
        sink.register(ModEntities.FROSTBALL, GlowingFlyingItemRenderer::new);
        sink.register(ModEntities.FROSTBALL_LOCATOR, FlyingItemEntityRenderer::new);

        sink.register(ModEntities.FROZEN_MAGICIAN,
                ctx -> new FrozenMagicianRenderer<FrozenMagicianEntity>(ctx));
        sink.register(ModEntities.GENERAL_FROZEN_MAGICIAN,
                ctx -> new FrozenMagicianRenderer<GeneralFrozenMagicianEntity>(ctx));
        sink.register(ModEntities.FROZEN_RANGER,
                ctx -> new FrozenRangerRenderer<FrozenRangerEntity>(ctx));
        sink.register(ModEntities.GENERAL_FROZEN_RANGER,
                ctx -> new FrozenRangerRenderer<GeneralFrozenRangerEntity>(ctx));
        sink.register(ModEntities.FROST_HOUND, FrostHoundRenderer::new);
        sink.register(ModEntities.MONARCHS_SOLDIER,
                ctx -> new MonarchsSoldierRenderer<MonarchsSoldierEntity>(ctx));
        sink.register(ModEntities.GENERAL_MONARCHS_SOLDIER,
                ctx -> new MonarchsSoldierRenderer<GeneralMonarchsSoldierEntity>(ctx));
        sink.register(ModEntities.MONARCHS_GUARD,
                ctx -> new MonarchsGuardRenderer<MonarchsGuardEntity>(ctx));
        sink.register(ModEntities.GENERAL_MONARCHS_GUARD,
                ctx -> new MonarchsGuardRenderer<GeneralMonarchsGuardEntity>(ctx));
        sink.register(ModEntities.CORRUPTED_PEARL, FlyingItemEntityRenderer::new);
        sink.register(ModEntities.RELIC_SKELETON, RelicSkeletonRenderer::new);
        sink.register(ModEntities.RELIC_FROST_HAUNTED, FrostHauntedEntityRenderer::new);
        sink.register(ModEntities.RELIC_WITHER_SKULL, WitherSkullEntityRenderer::new);
        sink.register(ModEntities.ORBITING_WITHER_SKULL, WitherSkullEntityRenderer::new);
        sink.register(ModEntities.GUARDIAN_BEAM, GuardianBeamRenderer::new);
    }
}

package more_rpg_loot.client.entity.renderers;

import more_rpg_loot.client.RPGLootClient;
import more_rpg_loot.client.entity.renderers.frozen_depths.frosthaunt.FrosthauntEntityRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.frostmonarch.FrostmonarchEntityRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.frost_hound.FrostHoundRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.frozen_mage.UndeadFrozenMageRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.frozen_ranger.FrostedRangerRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.glaze.GlazeEntityRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_guard.MonarchsGuardRenderer;
import more_rpg_loot.client.entity.renderers.frozen_depths.monarchs_soldier.MonarchsSoldierRenderer;
import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.GeneralUndeadFrozenMageEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.UndeadFrozenMageEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.FrostedRangerEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.GeneralFrostedRangerEntity;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.GeneralMonarchsSoldierEntity;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class ModMobRenderers {
    public static void register(RPGLootClient.EntityRendererSink sink){
        sink.register(ModEntities.FROST_HAUNT, FrosthauntEntityRenderer::new);
        sink.register(ModEntities.FROST_MONARCH, FrostmonarchEntityRenderer::new);
        sink.register(ModEntities.GLAZE, GlazeEntityRenderer::new);
        sink.register(ModEntities.FROSTBALL, FlyingItemEntityRenderer::new);
        sink.register(ModEntities.FROSTBALL_LOCATOR, FlyingItemEntityRenderer::new);

        sink.register(ModEntities.UNDEAD_FROZEN_MAGE,
                ctx -> new UndeadFrozenMageRenderer<UndeadFrozenMageEntity>(ctx));
        sink.register(ModEntities.GENERAL_UNDEAD_FROZEN_MAGE,
                ctx -> new UndeadFrozenMageRenderer<GeneralUndeadFrozenMageEntity>(ctx));
        sink.register(ModEntities.FROSTED_RANGER,
                ctx -> new FrostedRangerRenderer<FrostedRangerEntity>(ctx));
        sink.register(ModEntities.GENERAL_FROSTED_RANGER,
                ctx -> new FrostedRangerRenderer<GeneralFrostedRangerEntity>(ctx));
        sink.register(ModEntities.FROST_HOUND, FrostHoundRenderer::new);
        sink.register(ModEntities.MONARCHS_SOLDIER,
                ctx -> new MonarchsSoldierRenderer<MonarchsSoldierEntity>(ctx));
        sink.register(ModEntities.GENERAL_MONARCHS_SOLDIER,
                ctx -> new MonarchsSoldierRenderer<GeneralMonarchsSoldierEntity>(ctx));
        sink.register(ModEntities.MONARCHS_GUARD, MonarchsGuardRenderer::new);
    }
}

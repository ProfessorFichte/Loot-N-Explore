package more_rpg_loot.client.entity.renderers;

import more_rpg_loot.entity.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class ModMobRenderers {
    public static void register(){
        EntityRendererRegistry.register(ModEntities.FROST_HAUNT, FrosthauntEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROST_MONARCH, FrostmonarchEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MONARCH_SERVANT, FrostMonarchServantEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.GLAZE, GlazeEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROSTBALL, FlyingItemEntityRenderer::new);
    }
}

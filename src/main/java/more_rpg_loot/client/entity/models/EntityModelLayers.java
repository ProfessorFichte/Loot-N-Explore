package more_rpg_loot.client.entity.models;

import more_rpg_loot.client.entity.models.frosthaunt.FrosthauntModel;
import more_rpg_loot.client.entity.models.frostmonarch.FrostMonarchModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityModelLayers {
    public static final EntityModelLayer FROST_MONARCH = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frost_monarch"),
            "main"
    );
    public static final EntityModelLayer FROST_MONARCH_INNER_ARMOR =
            new EntityModelLayer(Identifier.of("loot_n_explore", "frost_monarch"), "inner_armor");
    public static final EntityModelLayer FROST_MONARCH_OUTER_ARMOR =
            new EntityModelLayer(Identifier.of("loot_n_explore", "frost_monarch"), "outer_armor");
    public static final EntityModelLayer FROST_MONARCH_OUTER =
            new EntityModelLayer(Identifier.of("loot_n_explore", "frost_monarch"), "outer");

    public static final EntityModelLayer FROSTHAUNT = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frosthaunt"),
            "main"
    );

    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(FROST_MONARCH, FrostMonarchModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(FROSTHAUNT, FrosthauntModel::createBodyLayer);
    }
}

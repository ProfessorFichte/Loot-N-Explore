package more_rpg_loot.client.entity.models;

import more_rpg_loot.client.entity.models.frozen_depths.frosthaunt.FrosthauntModel;
import more_rpg_loot.client.entity.models.frozen_depths.frostmonarch.FrostMonarchModel;
import more_rpg_loot.client.entity.models.frozen_depths.frozen_mage.UndeadFrozenMageModel;
import more_rpg_loot.client.entity.models.frozen_depths.frozen_ranger.FrostedRangerModel;
import more_rpg_loot.client.entity.models.frozen_depths.frost_hound.FrostHoundModel;
import more_rpg_loot.client.entity.models.frozen_depths.glaze.GlazeModel;
import more_rpg_loot.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import more_rpg_loot.client.entity.models.frozen_depths.monarchs_guard.MonarchsGuardModel;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.TexturedModelData;
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

    public static final EntityModelLayer UNDEAD_FROZEN_MAGE = new EntityModelLayer(
            Identifier.of("loot_n_explore", "undead_frozen_mage"), "main");
    public static final EntityModelLayer FROSTED_RANGER = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frosted_ranger"), "main");
    public static final EntityModelLayer FROST_HOUND = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frost_hound"), "main");
    public static final EntityModelLayer MONARCHS_SOLDIER = new EntityModelLayer(
            Identifier.of("loot_n_explore", "monarchs_soldier"), "main");
    public static final EntityModelLayer MONARCHS_GUARD = new EntityModelLayer(
            Identifier.of("loot_n_explore", "monarchs_guard"), "main");
    public static final EntityModelLayer GLAZE = new EntityModelLayer(
            Identifier.of("loot_n_explore", "glaze"), "main");

    public static void registerAll(BiConsumer<EntityModelLayer, Supplier<TexturedModelData>> register) {
        register.accept(FROST_MONARCH, FrostMonarchModel::createBodyLayer);
        register.accept(FROSTHAUNT, FrosthauntModel::createBodyLayer);
        register.accept(UNDEAD_FROZEN_MAGE, UndeadFrozenMageModel::createBodyLayer);
        register.accept(FROSTED_RANGER, FrostedRangerModel::createBodyLayer);
        register.accept(FROST_HOUND, FrostHoundModel::createBodyLayer);
        register.accept(MONARCHS_SOLDIER, MonarchsSoldierModel::createBodyLayer);
        register.accept(MONARCHS_GUARD, MonarchsGuardModel::createBodyLayer);
        register.accept(GLAZE, GlazeModel::createBodyLayer);
    }
}

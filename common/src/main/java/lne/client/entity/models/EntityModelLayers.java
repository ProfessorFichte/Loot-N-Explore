package lne.client.entity.models;

import lne.client.entity.models.frozen_depths.frost_haunted.FrostHauntedModel;
import lne.client.entity.models.frozen_depths.frostmonarch.FrostMonarchModel;
import lne.client.entity.models.frozen_depths.frozen_magician.FrozenMagicianModel;
import lne.client.entity.models.frozen_depths.frozen_ranger.FrozenRangerModel;
import lne.client.entity.models.frozen_depths.frost_hound.FrostHoundModel;
import lne.client.entity.models.frozen_depths.glaze.GlazeModel;
import lne.client.entity.models.frozen_depths.monarchs_soldier.MonarchsSoldierModel;
import lne.client.entity.models.frozen_depths.monarchs_guard.MonarchsGuardModel;
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

    public static final EntityModelLayer FROST_HAUNTED = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frost_haunted"),
            "main"
    );

    public static final EntityModelLayer FROZEN_MAGICIAN = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frozen_magician"), "main");
    public static final EntityModelLayer FROZEN_RANGER = new EntityModelLayer(
            Identifier.of("loot_n_explore", "frozen_ranger"), "main");
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
        register.accept(FROST_HAUNTED, FrostHauntedModel::createBodyLayer);
        register.accept(FROZEN_MAGICIAN, FrozenMagicianModel::createBodyLayer);
        register.accept(FROZEN_RANGER, FrozenRangerModel::createBodyLayer);
        register.accept(FROST_HOUND, FrostHoundModel::createBodyLayer);
        register.accept(MONARCHS_SOLDIER, MonarchsSoldierModel::createBodyLayer);
        register.accept(MONARCHS_GUARD, MonarchsGuardModel::createBodyLayer);
        register.accept(GLAZE, GlazeModel::createBodyLayer);
    }
}

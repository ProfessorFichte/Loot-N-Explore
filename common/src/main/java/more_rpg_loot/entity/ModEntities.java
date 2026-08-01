package more_rpg_loot.entity;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.frozen_depths.mob.frost_hound.FrostHoundEntity;
import more_rpg_loot.entity.frozen_depths.mob.frosthaunt.FrosthauntEntity;
import more_rpg_loot.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.GeneralUndeadFrozenMageEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.UndeadFrozenMageEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.FrostedRangerEntity;
import more_rpg_loot.entity.frozen_depths.mob.frozen_ranger.GeneralFrostedRangerEntity;
import more_rpg_loot.entity.frozen_depths.mob.glaze.GlazeEntity;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.GeneralMonarchsSoldierEntity;
import more_rpg_loot.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import more_rpg_loot.entity.frozen_depths.projectile.*;
import more_rpg_loot.entity.generic.entity.CustomCloudEntity;
import more_rpg_loot.entity.generic.projectile.LNEAbilityArrowEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<FrosthauntEntity> FROST_HAUNT = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    FrosthauntEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<FrostMonarchEntity> FROST_MONARCH = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    FrostMonarchEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.8f, 3.3f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<GlazeEntity> GLAZE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    GlazeEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<FrostballEntity> FROSTBALL = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<FrostballEntity>) FrostballEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.8f, 0.8f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<FrostballLocatorEntity> FROSTBALL_LOCATOR = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<FrostballLocatorEntity>) FrostballLocatorEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<StraightIcicleEntity> STRAIGHT_ICICLE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<StraightIcicleEntity>) StraightIcicleEntity::new
            )
            .dimensions(EntityDimensions.fixed(1.2f, 0.8f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<BarrierIcicleEntity> BARRIER_ICICLE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<BarrierIcicleEntity>) BarrierIcicleEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.5f, 0.8f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<LNEAbilityArrowEntity> LNE_ABILITY_ARROW = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<LNEAbilityArrowEntity>) LNEAbilityArrowEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<CustomCloudEntity> CUSTOM_CLOUD = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<CustomCloudEntity>) CustomCloudEntity::new
            )
            .dimensions(EntityDimensions.changing(6.0f, 0.5f))
            .trackRangeChunks(8)
            .build();

    public static final EntityType<TrackingIcicleEntity> TRACKING_ICICLE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<TrackingIcicleEntity>) TrackingIcicleEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<ThrownLanceEntity> THROWN_LANCE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<ThrownLanceEntity>) ThrownLanceEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<FrozenArrowEntity> FROZEN_ARROW = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<FrozenArrowEntity>) FrozenArrowEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
            .trackRangeChunks(8)
            .build();

    public static final EntityType<UndeadFrozenMageEntity> UNDEAD_FROZEN_MAGE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    UndeadFrozenMageEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<GeneralUndeadFrozenMageEntity> GENERAL_UNDEAD_FROZEN_MAGE = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    GeneralUndeadFrozenMageEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.9f, 2.985f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<FrostedRangerEntity> FROSTED_RANGER = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    FrostedRangerEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<GeneralFrostedRangerEntity> GENERAL_FROSTED_RANGER = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    GeneralFrostedRangerEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.9f, 2.985f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<FrostHoundEntity> FROST_HOUND = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    FrostHoundEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.65f, 0.80f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<MonarchsSoldierEntity> MONARCHS_SOLDIER = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    MonarchsSoldierEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<GeneralMonarchsSoldierEntity> GENERAL_MONARCHS_SOLDIER = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    GeneralMonarchsSoldierEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.9f, 2.985f))
            .trackRangeChunks(8)
            .build();
    public static final EntityType<MonarchsGuardEntity> MONARCHS_GUARD = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    MonarchsGuardEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
            .trackRangeChunks(8)
            .build();

    public static void register(){
        register("frost_haunt", FROST_HAUNT);
        register("frost_monarch", FROST_MONARCH);
        register("glaze", GLAZE);
        register("frostball", FROSTBALL);
        register("frostball_locator", FROSTBALL_LOCATOR);
        register("straight_icicle", STRAIGHT_ICICLE);
        register("barrier_icicle", BARRIER_ICICLE);
        register("lne_ability_arrow", LNE_ABILITY_ARROW);
        register("custom_cloud", CUSTOM_CLOUD);
        register("tracking_icicle", TRACKING_ICICLE);
        register("frozen_arrow", FROZEN_ARROW);
        register("thrown_lance", THROWN_LANCE);
        register("undead_frozen_mage", UNDEAD_FROZEN_MAGE);
        register("general_undead_frozen_mage", GENERAL_UNDEAD_FROZEN_MAGE);
        register("frosted_ranger", FROSTED_RANGER);
        register("general_frosted_ranger", GENERAL_FROSTED_RANGER);
        register("frost_hound", FROST_HOUND);
        register("monarchs_soldier", MONARCHS_SOLDIER);
        register("general_monarchs_soldier", GENERAL_MONARCHS_SOLDIER);
        register("monarchs_guard", MONARCHS_GUARD);

        FabricDefaultAttributeRegistry.register(FROST_HAUNT, FrosthauntEntity.createFrosthauntSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(FROST_MONARCH, FrostMonarchEntity.createFrostmonarchAttributes());
        FabricDefaultAttributeRegistry.register(GLAZE, GlazeEntity.createGlazeAttributes());
        FabricDefaultAttributeRegistry.register(UNDEAD_FROZEN_MAGE, UndeadFrozenMageEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GENERAL_UNDEAD_FROZEN_MAGE, GeneralUndeadFrozenMageEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(FROSTED_RANGER, FrostedRangerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GENERAL_FROSTED_RANGER, GeneralFrostedRangerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(FROST_HOUND, FrostHoundEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MONARCHS_SOLDIER, MonarchsSoldierEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GENERAL_MONARCHS_SOLDIER, GeneralMonarchsSoldierEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MONARCHS_GUARD, MonarchsGuardEntity.createAttributes());
    }

    private static <T extends Entity> void register(String id, EntityType<T> type) {
        Registry.register(Registries.ENTITY_TYPE, RPGLoot.id(id), type);
    }
}

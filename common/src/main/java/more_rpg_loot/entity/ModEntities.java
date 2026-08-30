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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.BiConsumer;

public class ModEntities {
    public static final EntityType<FrosthauntEntity> FROST_HAUNT = EntityType.Builder.create(FrosthauntEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("frost_haunt");
    public static final EntityType<FrostMonarchEntity> FROST_MONARCH = EntityType.Builder.create(FrostMonarchEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.8f, 3.3f)
            .maxTrackingRange(8)
            .build("frost_monarch");
    public static final EntityType<GlazeEntity> GLAZE = EntityType.Builder.create(GlazeEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(8)
            .build("glaze");
    public static final EntityType<FrostballEntity> FROSTBALL = EntityType.Builder.<FrostballEntity>create(FrostballEntity::new, SpawnGroup.MISC)
            .dimensions(0.8f, 0.8f)
            .maxTrackingRange(8)
            .build("frostball");
    public static final EntityType<FrostballLocatorEntity> FROSTBALL_LOCATOR = EntityType.Builder.<FrostballLocatorEntity>create(FrostballLocatorEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(8)
            .build("frostball_locator");
    public static final EntityType<StraightIcicleEntity> STRAIGHT_ICICLE = EntityType.Builder.<StraightIcicleEntity>create(StraightIcicleEntity::new, SpawnGroup.MISC)
            .dimensions(1.2f, 0.8f)
            .maxTrackingRange(8)
            .build("straight_icicle");
    public static final EntityType<BarrierIcicleEntity> BARRIER_ICICLE = EntityType.Builder.<BarrierIcicleEntity>create(BarrierIcicleEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.8f)
            .maxTrackingRange(8)
            .build("barrier_icicle");
    public static final EntityType<LNEAbilityArrowEntity> LNE_ABILITY_ARROW = EntityType.Builder.<LNEAbilityArrowEntity>create(LNEAbilityArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(8)
            .build("lne_ability_arrow");
    public static final EntityType<CustomCloudEntity> CUSTOM_CLOUD = EntityType.Builder.<CustomCloudEntity>create(CustomCloudEntity::new, SpawnGroup.MISC)
            .dimensions(6.0f, 0.5f)
            .maxTrackingRange(8)
            .build("custom_cloud");

    public static final EntityType<TrackingIcicleEntity> TRACKING_ICICLE = EntityType.Builder.<TrackingIcicleEntity>create(TrackingIcicleEntity::new, SpawnGroup.MISC)
            .dimensions(0.3f, 0.3f)
            .maxTrackingRange(8)
            .build("tracking_icicle");
    public static final EntityType<ThrownLanceEntity> THROWN_LANCE = EntityType.Builder.<ThrownLanceEntity>create(ThrownLanceEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(8)
            .build("thrown_lance");
    public static final EntityType<FrozenArrowEntity> FROZEN_ARROW = EntityType.Builder.<FrozenArrowEntity>create(FrozenArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(8)
            .build("frozen_arrow");

    public static final EntityType<UndeadFrozenMageEntity> UNDEAD_FROZEN_MAGE = EntityType.Builder.create(UndeadFrozenMageEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("undead_frozen_mage");
    public static final EntityType<GeneralUndeadFrozenMageEntity> GENERAL_UNDEAD_FROZEN_MAGE = EntityType.Builder.create(GeneralUndeadFrozenMageEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.9f, 2.985f)
            .maxTrackingRange(8)
            .build("general_undead_frozen_mage");
    public static final EntityType<FrostedRangerEntity> FROSTED_RANGER = EntityType.Builder.create(FrostedRangerEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("frosted_ranger");
    public static final EntityType<GeneralFrostedRangerEntity> GENERAL_FROSTED_RANGER = EntityType.Builder.create(GeneralFrostedRangerEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.9f, 2.985f)
            .maxTrackingRange(8)
            .build("general_frosted_ranger");
    public static final EntityType<FrostHoundEntity> FROST_HOUND = EntityType.Builder.create(FrostHoundEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.65f, 0.80f)
            .maxTrackingRange(8)
            .build("frost_hound");
    public static final EntityType<MonarchsSoldierEntity> MONARCHS_SOLDIER = EntityType.Builder.create(MonarchsSoldierEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("monarchs_soldier");
    public static final EntityType<GeneralMonarchsSoldierEntity> GENERAL_MONARCHS_SOLDIER = EntityType.Builder.create(GeneralMonarchsSoldierEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.9f, 2.985f)
            .maxTrackingRange(8)
            .build("general_monarchs_soldier");
    public static final EntityType<MonarchsGuardEntity> MONARCHS_GUARD = EntityType.Builder.create(MonarchsGuardEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("monarchs_guard");

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
    }

    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, DefaultAttributeContainer.Builder> sink) {
        sink.accept(FROST_HAUNT, FrosthauntEntity.createFrosthauntSkeletonAttributes());
        sink.accept(FROST_MONARCH, FrostMonarchEntity.createFrostmonarchAttributes());
        sink.accept(GLAZE, GlazeEntity.createGlazeAttributes());
        sink.accept(UNDEAD_FROZEN_MAGE, UndeadFrozenMageEntity.createAttributes());
        sink.accept(GENERAL_UNDEAD_FROZEN_MAGE, GeneralUndeadFrozenMageEntity.createAttributes());
        sink.accept(FROSTED_RANGER, FrostedRangerEntity.createAttributes());
        sink.accept(GENERAL_FROSTED_RANGER, GeneralFrostedRangerEntity.createAttributes());
        sink.accept(FROST_HOUND, FrostHoundEntity.createAttributes());
        sink.accept(MONARCHS_SOLDIER, MonarchsSoldierEntity.createAttributes());
        sink.accept(GENERAL_MONARCHS_SOLDIER, GeneralMonarchsSoldierEntity.createAttributes());
        sink.accept(MONARCHS_GUARD, MonarchsGuardEntity.createAttributes());
    }

    private static <T extends Entity> void register(String id, EntityType<T> type) {
        Registry.register(Registries.ENTITY_TYPE, RPGLoot.id(id), type);
    }
}

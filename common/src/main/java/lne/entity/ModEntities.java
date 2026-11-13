package lne.entity;

import lne.LneMod;
import lne.entity.frozen_depths.mob.frost_hound.FrostHoundEntity;
import lne.entity.frozen_depths.mob.frost_haunted.FrostHauntedEntity;
import lne.entity.frozen_depths.mob.frostmonarch.FrostMonarchEntity;
import lne.entity.frozen_depths.mob.frozen_magician.GeneralFrozenMagicianEntity;
import lne.entity.frozen_depths.mob.frozen_magician.FrozenMagicianEntity;
import lne.entity.frozen_depths.mob.frozen_ranger.FrozenRangerEntity;
import lne.entity.frozen_depths.mob.frozen_ranger.GeneralFrozenRangerEntity;
import lne.entity.frozen_depths.mob.glaze.GlazeEntity;
import lne.entity.frozen_depths.mob.monarchs_guard.MonarchsGuardEntity;
import lne.entity.frozen_depths.mob.monarchs_soldier.GeneralMonarchsSoldierEntity;
import lne.entity.frozen_depths.mob.monarchs_guard.GeneralMonarchsGuardEntity;
import lne.entity.frozen_depths.mob.monarchs_soldier.MonarchsSoldierEntity;
import lne.entity.frozen_depths.projectile.*;
import lne.entity.frozen_depths.conjured.EternalWinterPortalEntity;
import lne.entity.frozen_depths.conjured.IceWallEntity;
import lne.entity.frozen_depths.conjured.FrostDecoyEntity;
import lne.entity.generic.projectile.LNEAbilityArrowEntity;
import lne.entity.generic.projectile.OrbitingWitherSkullEntity;
import lne.entity.relic.GuardianBeamEntity;
import lne.entity.relic.CorruptedPearlEntity;
import lne.entity.relic.RelicFrostHauntedEntity;
import lne.entity.relic.RelicSkeletonEntity;
import lne.entity.relic.RelicWitherSkullEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ModEntities {
    public static final EntityType<FrostHauntedEntity> FROST_HAUNTED = EntityType.Builder.create(FrostHauntedEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("frost_haunted");
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
    public static final EntityType<TrackingIcicleEntity> TRACKING_ICICLE = EntityType.Builder.<TrackingIcicleEntity>create(TrackingIcicleEntity::new, SpawnGroup.MISC)
            .dimensions(0.3f, 0.3f)
            .maxTrackingRange(8)
            .trackingTickInterval(1)
            .build("tracking_icicle");
    public static final EntityType<ThrownLanceEntity> THROWN_LANCE = EntityType.Builder.<ThrownLanceEntity>create(ThrownLanceEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(8)
            .build("thrown_lance");
    public static final EntityType<IceWallEntity> ICE_WALL = EntityType.Builder.<IceWallEntity>create(IceWallEntity::new, SpawnGroup.MISC)
            .dimensions(1.8f, 2.7f)
            .makeFireImmune()
            .maxTrackingRange(8)
            .build("ice_wall");
    public static final EntityType<EternalWinterPortalEntity> ETERNAL_WINTER_PORTAL = EntityType.Builder.<EternalWinterPortalEntity>create(EternalWinterPortalEntity::new, SpawnGroup.MISC)
            .dimensions(2.0f, 2.0f)
            .maxTrackingRange(8)
            .build("eternal_winter_portal");
    public static final EntityType<FrostDecoyEntity> FROST_DECOY = EntityType.Builder.<FrostDecoyEntity>create(FrostDecoyEntity::new, SpawnGroup.MISC)
            .dimensions(0.6f, 1.99f)
            .makeFireImmune()
            .maxTrackingRange(8)
            .build("frost_decoy");
    public static final EntityType<FrozenArrowEntity> FROZEN_ARROW = EntityType.Builder.<FrozenArrowEntity>create(FrozenArrowEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(8)
            .build("frozen_arrow");

    public static final EntityType<CorruptedPearlEntity> CORRUPTED_PEARL = EntityType.Builder.<CorruptedPearlEntity>create(CorruptedPearlEntity::new, SpawnGroup.MISC)
            .dimensions(0.25f, 0.25f)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
            .build("corrupted_pearl");
    public static final EntityType<RelicSkeletonEntity> RELIC_SKELETON = EntityType.Builder.create(RelicSkeletonEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .eyeHeight(1.74f)
            .vehicleAttachment(-0.7f)
            .maxTrackingRange(8)
            .build("relic_skeleton");

    public static final EntityType<RelicFrostHauntedEntity> RELIC_FROST_HAUNTED = EntityType.Builder.create(RelicFrostHauntedEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("relic_frost_haunted");
    public static final EntityType<RelicWitherSkullEntity> RELIC_WITHER_SKULL = EntityType.Builder.<RelicWitherSkullEntity>create(RelicWitherSkullEntity::new, SpawnGroup.MISC)
            .dimensions(0.3125f, 0.3125f)
            .maxTrackingRange(4)
            .trackingTickInterval(10)
            .build("relic_wither_skull");
    public static final EntityType<OrbitingWitherSkullEntity> ORBITING_WITHER_SKULL = EntityType.Builder.<OrbitingWitherSkullEntity>create(OrbitingWitherSkullEntity::new, SpawnGroup.MISC)
            .dimensions(0.3125f, 0.3125f)
            .maxTrackingRange(6)
            .trackingTickInterval(10)
            .build("orbiting_wither_skull");
    public static final EntityType<GuardianBeamEntity> GUARDIAN_BEAM = EntityType.Builder.<GuardianBeamEntity>create(GuardianBeamEntity::new, SpawnGroup.MISC)
            .dimensions(0.1f, 0.1f)
            .maxTrackingRange(8)
            .trackingTickInterval(1)
            .disableSaving()
            .disableSummon()
            .build("guardian_beam");

    public static final EntityType<FrozenMagicianEntity> FROZEN_MAGICIAN = EntityType.Builder.create(FrozenMagicianEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("frozen_magician");
    public static final EntityType<GeneralFrozenMagicianEntity> GENERAL_FROZEN_MAGICIAN = EntityType.Builder.create(GeneralFrozenMagicianEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("general_frozen_magician");
    public static final EntityType<FrozenRangerEntity> FROZEN_RANGER = EntityType.Builder.create(FrozenRangerEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("frozen_ranger");
    public static final EntityType<GeneralFrozenRangerEntity> GENERAL_FROZEN_RANGER = EntityType.Builder.create(GeneralFrozenRangerEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("general_frozen_ranger");
    public static final EntityType<FrostHoundEntity> FROST_HOUND = EntityType.Builder.create(FrostHoundEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.65f, 0.80f)
            .maxTrackingRange(8)
            .build("frost_hound");
    public static final EntityType<MonarchsSoldierEntity> MONARCHS_SOLDIER = EntityType.Builder.create(MonarchsSoldierEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("monarchs_soldier");
    public static final EntityType<GeneralMonarchsSoldierEntity> GENERAL_MONARCHS_SOLDIER = EntityType.Builder.create(GeneralMonarchsSoldierEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("general_monarchs_soldier");
    public static final EntityType<MonarchsGuardEntity> MONARCHS_GUARD = EntityType.Builder.create(MonarchsGuardEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("monarchs_guard");
    public static final EntityType<GeneralMonarchsGuardEntity> GENERAL_MONARCHS_GUARD = EntityType.Builder.create(GeneralMonarchsGuardEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.99f)
            .maxTrackingRange(8)
            .build("general_monarchs_guard");

    public static void register(){
        register("frost_haunted", "Frost Haunted", FROST_HAUNTED);
        register("frost_monarch", "Frost Monarch", FROST_MONARCH);
        register("glaze", "Glaze", GLAZE);
        register("frostball", "Frostball", FROSTBALL);
        register("frostball_locator", "Frostball Locator", FROSTBALL_LOCATOR);
        register("straight_icicle", "Icicle", STRAIGHT_ICICLE);
        register("barrier_icicle", "Barrier Icicle", BARRIER_ICICLE);
        register("lne_ability_arrow", "Arrow", LNE_ABILITY_ARROW);
        register("tracking_icicle", "Icicle", TRACKING_ICICLE);
        register("frozen_arrow", "Frozen Arrow", FROZEN_ARROW);
        register("thrown_lance", "Thrown Lance", THROWN_LANCE);
        register("ice_wall", "Ice Wall", ICE_WALL);
        register("eternal_winter_portal", "Eternal Winter Portal", ETERNAL_WINTER_PORTAL);
        register("frost_decoy", "Frost Decoy", FROST_DECOY);
        register("corrupted_pearl", "Corrupted Ender Pearl", CORRUPTED_PEARL);
        register("relic_skeleton", "Summoned Skeleton", RELIC_SKELETON);
        register("relic_frost_haunted", "Summoned Frost Haunted", RELIC_FROST_HAUNTED);
        register("relic_wither_skull", "Wither Skull", RELIC_WITHER_SKULL);
        register("orbiting_wither_skull", "Wither Skull", ORBITING_WITHER_SKULL);
        register("guardian_beam", "Guardian Beam", GUARDIAN_BEAM);
        register("frozen_magician", "Frozen Magician", FROZEN_MAGICIAN);
        register("general_frozen_magician", "General Ysmerith, the Rimebound Counsel", GENERAL_FROZEN_MAGICIAN);
        register("frozen_ranger", "Frozen Ranger", FROZEN_RANGER);
        register("general_frozen_ranger", "General Kaelthorn, the Wallwatch", GENERAL_FROZEN_RANGER);
        register("frost_hound", "Frost Hound", FROST_HOUND);
        register("monarchs_soldier", "Monarch's Soldier", MONARCHS_SOLDIER);
        register("general_monarchs_soldier", "General Haldrec, the Shield of the Crown", GENERAL_MONARCHS_SOLDIER);
        register("monarchs_guard", "Monarch's Guard", MONARCHS_GUARD);
        register("general_monarchs_guard", "General Dunmarth, the Warden of the City", GENERAL_MONARCHS_GUARD);
    }

    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, DefaultAttributeContainer.Builder> sink) {
        sink.accept(FROST_HAUNTED, FrostHauntedEntity.createFrostHauntedSkeletonAttributes());
        sink.accept(FROST_MONARCH, FrostMonarchEntity.createFrostmonarchAttributes());
        sink.accept(GLAZE, GlazeEntity.createGlazeAttributes());
        sink.accept(FROZEN_MAGICIAN, FrozenMagicianEntity.createAttributes());
        sink.accept(GENERAL_FROZEN_MAGICIAN, GeneralFrozenMagicianEntity.createAttributes());
        sink.accept(FROZEN_RANGER, FrozenRangerEntity.createAttributes());
        sink.accept(GENERAL_FROZEN_RANGER, GeneralFrozenRangerEntity.createAttributes());
        sink.accept(FROST_HOUND, FrostHoundEntity.createAttributes());
        sink.accept(MONARCHS_SOLDIER, MonarchsSoldierEntity.createAttributes());
        sink.accept(GENERAL_MONARCHS_SOLDIER, GeneralMonarchsSoldierEntity.createAttributes());
        sink.accept(MONARCHS_GUARD, MonarchsGuardEntity.createAttributes());
        sink.accept(GENERAL_MONARCHS_GUARD, GeneralMonarchsGuardEntity.createAttributes());
        sink.accept(ETERNAL_WINTER_PORTAL, EternalWinterPortalEntity.createAttributes());
        sink.accept(RELIC_SKELETON, SkeletonEntity.createAbstractSkeletonAttributes());
        sink.accept(RELIC_FROST_HAUNTED, FrostHauntedEntity.createFrostHauntedSkeletonAttributes());
    }

    public record Entry(String id, String translation) {}

    public static final List<Entry> all = new ArrayList<>();

    private static <T extends Entity> void register(String id, String translation, EntityType<T> type) {
        all.add(new Entry(id, translation));
        Registry.register(Registries.ENTITY_TYPE, LneMod.id(id), type);
    }
}

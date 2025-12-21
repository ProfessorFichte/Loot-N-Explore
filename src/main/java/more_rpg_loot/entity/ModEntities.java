package more_rpg_loot.entity;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.mob.FrostMonarchServantEntity;
import more_rpg_loot.entity.mob.FrosthauntEntity;
import more_rpg_loot.entity.mob.GlazeEntity;
import more_rpg_loot.entity.projectile.BarrierIcicleEntity;
import more_rpg_loot.entity.projectile.FrostballEntity;
import more_rpg_loot.entity.projectile.StraightIcicleEntity;
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
    public static final EntityType<FrostMonarchServantEntity> MONARCH_SERVANT = FabricEntityTypeBuilder.create(
                    SpawnGroup.MONSTER,
                    FrostMonarchServantEntity::new
            )
            .dimensions(EntityDimensions.fixed(0.6f, 1.99f))
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
    public static final EntityType<CustomCloudEntity> CUSTOM_CLOUD = FabricEntityTypeBuilder.create(
                    SpawnGroup.MISC,
                    (EntityType.EntityFactory<CustomCloudEntity>) CustomCloudEntity::new
            )
            .dimensions(EntityDimensions.changing(6.0f, 0.5f))
            .trackRangeChunks(8)
            .build();


    public static void register(){
        register("frost_haunt", FROST_HAUNT);
        register("frost_monarch", FROST_MONARCH);
        register("monarchs_servant", MONARCH_SERVANT);
        register("glaze", GLAZE);
        register("frostball", FROSTBALL);
        register("straight_icicle", STRAIGHT_ICICLE);
        register("barrier_icicle", BARRIER_ICICLE);
        register("custom_cloud", CUSTOM_CLOUD);
        FabricDefaultAttributeRegistry.register(FROST_HAUNT, FrosthauntEntity.createFrosthauntSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(FROST_MONARCH, FrostMonarchEntity.createFrostmonarchAttributes());
        FabricDefaultAttributeRegistry.register(GLAZE, GlazeEntity.createGlazeAttributes());
        FabricDefaultAttributeRegistry.register(MONARCH_SERVANT, FrostMonarchServantEntity.createMonarchServantAttributes());


    }

    private static <T extends Entity> void register(String id, EntityType<T> type) {
        Registry.register(Registries.ENTITY_TYPE, RPGLoot.id(id), type);
    }
}

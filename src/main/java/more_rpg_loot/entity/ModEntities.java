package more_rpg_loot.entity;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import more_rpg_loot.entity.mob.FrosthauntEntity;
import more_rpg_loot.entity.mob.GlazeEntity;
import more_rpg_loot.entity.projectile.FrostballEntity;
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
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .trackRangeChunks(8)
            .build();

    public static void register(){
        register("frost_haunt", FROST_HAUNT);
        register("frost_monarch", FROST_MONARCH);
        register("glaze", GLAZE);
        register("frostball", FROSTBALL);
        FabricDefaultAttributeRegistry.register(FROST_HAUNT, FrosthauntEntity.createFrosthauntSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(FROST_MONARCH, FrostMonarchEntity.createFrostmonarchAttributes());
        FabricDefaultAttributeRegistry.register(GLAZE, GlazeEntity.createGlazeAttributes());
    }

    private static <T extends Entity> void register(String id, EntityType<T> type) {
        Registry.register(Registries.ENTITY_TYPE, RPGLoot.id(id), type);
    }
}

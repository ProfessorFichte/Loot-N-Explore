package lne.entity;

import lne.LneMod;
import lne.config.FrozenDepthsConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;

public final class StructureSpawnRules {
    private StructureSpawnRules() {
    }

    public interface Sink {
        <T extends MobEntity> void register(EntityType<T> type, SpawnLocation location, Heightmap.Type heightmap,
                                            SpawnRestriction.SpawnPredicate<T> predicate);
    }

    public static void register(Sink sink) {
        sink.register(ModEntities.FROST_HAUNTED, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
        sink.register(ModEntities.GLAZE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
        sink.register(ModEntities.FROZEN_MAGICIAN, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
        sink.register(ModEntities.FROZEN_RANGER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
        sink.register(ModEntities.FROST_HOUND, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
        sink.register(ModEntities.MONARCHS_SOLDIER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
        sink.register(ModEntities.MONARCHS_GUARD, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StructureSpawnRules::canSpawn);
    }

    // Light is ignored because the structures are full of torches and lanterns.
    // Spawn weights can't lower the spawn rate, so the chance and the nearby limit keep the structures from filling up.
    private static <T extends HostileEntity> boolean canSpawn(EntityType<T> type, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        if (!HostileEntity.canSpawnIgnoreLightLevel(type, world, reason, pos, random)) {
            return false;
        }
        if (reason != SpawnReason.NATURAL) {
            return true;
        }
        FrozenDepthsConfig config = LneMod.frozenDepthsConfig != null ? LneMod.frozenDepthsConfig.value : new FrozenDepthsConfig();
        if (random.nextFloat() >= config.structure_natural_spawn_chance) {
            return false;
        }
        double radius = config.structure_natural_spawn_check_radius;
        return world.getEntitiesByClass(HostileEntity.class, new Box(pos).expand(radius), entity -> true).size()
                < config.structure_natural_spawn_max_nearby;
    }
}

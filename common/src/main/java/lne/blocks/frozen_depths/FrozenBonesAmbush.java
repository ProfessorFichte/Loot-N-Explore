package lne.blocks.frozen_depths;

import lne.entity.ModEntities;
import lne.entity.frozen_depths.mob.frost_haunted.FrostHauntedEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class FrozenBonesAmbush {
    private static final int DELAY_TICKS = 60;
    private static final Map<ServerWorld, List<Pending>> PENDING = new WeakHashMap<>();

    private record Pending(BlockPos pos, long spawnAt, UUID victim) {
    }

    private FrozenBonesAmbush() {
    }

    public static void schedule(ServerWorld world, BlockPos pos, PlayerEntity victim) {
        PENDING.computeIfAbsent(world, key -> new ArrayList<>())
                .add(new Pending(pos.toImmutable(), world.getTime() + DELAY_TICKS, victim.getUuid()));
    }

    public static void tick(ServerWorld world) {
        List<Pending> pending = PENDING.get(world);
        if (pending == null || pending.isEmpty()) {
            return;
        }
        for (Iterator<Pending> iterator = pending.iterator(); iterator.hasNext(); ) {
            Pending ambush = iterator.next();
            if (world.getTime() < ambush.spawnAt()) {
                continue;
            }
            iterator.remove();
            spawn(world, ambush);
        }
    }

    private static void spawn(ServerWorld world, Pending ambush) {
        if (world.getDifficulty() == Difficulty.PEACEFUL || !world.isChunkLoaded(ambush.pos())) {
            return;
        }
        FrostHauntedEntity haunted = ModEntities.FROST_HAUNTED.create(world);
        if (haunted == null) {
            return;
        }
        BlockPos pos = ambush.pos();
        haunted.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.getRandom().nextFloat() * 360.0F, 0.0F);
        haunted.initialize(world, world.getLocalDifficulty(pos), SpawnReason.TRIGGERED, null);
        PlayerEntity victim = world.getPlayerByUuid(ambush.victim());
        if (victim != null && victim.isAlive() && !victim.isCreative() && !victim.isSpectator()) {
            haunted.setTarget(victim);
        }
        world.spawnEntityAndPassengers(haunted);
    }
}

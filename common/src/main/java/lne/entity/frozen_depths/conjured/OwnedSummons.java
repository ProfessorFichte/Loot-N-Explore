package lne.entity.frozen_depths.conjured;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.UUID;

public final class OwnedSummons {
    public static final String NO_LOOT_TAG = "lne_no_loot";
    private static final String OWNER_TAG_PREFIX = "lne_summoned_by_";
    private static final double RANGE = 128.0;

    private OwnedSummons() {
    }

    public static void mark(Entity summon, UUID owner) {
        summon.addCommandTag(OWNER_TAG_PREFIX + owner);
    }

    public static void dismiss(LivingEntity owner) {
        if (!(owner.getWorld() instanceof ServerWorld world)) {
            return;
        }
        UUID id = owner.getUuid();
        String tag = OWNER_TAG_PREFIX + id;
        Box box = owner.getBoundingBox().expand(RANGE);
        for (EternalWinterPortalEntity portal : world.getEntitiesByClass(EternalWinterPortalEntity.class, box, portal -> portal.isOwnedBy(id))) {
            portal.close(false);
        }
        for (LivingEntity summon : world.getEntitiesByClass(LivingEntity.class, box, entity -> entity.isAlive() && entity.getCommandTags().contains(tag))) {
            summon.addCommandTag(NO_LOOT_TAG);
            summon.kill();
        }
    }
}

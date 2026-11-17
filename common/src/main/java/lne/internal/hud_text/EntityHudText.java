package lne.internal.hud_text;

import lne.internal.network.EntityHudTextPayload;
import lne.internal.platform.LNEEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public final class EntityHudText {
    public static final int FROST_COLOR = 0x9FE8FF;

    private EntityHudText() {}

    public static void send(Entity speaker, double radius, EntityHudTextPayload payload) {
        if (!(speaker.getWorld() instanceof ServerWorld world)) {
            return;
        }
        double radiusSq = radius * radius;
        for (ServerPlayerEntity player : world.getPlayers(p -> p.squaredDistanceTo(speaker) <= radiusSq)) {
            LNEEvents.get().sendToClient(player, payload);
        }
    }
}

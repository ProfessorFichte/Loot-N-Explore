package more_rpg_loot.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public record FrostMonarchSpawnOverlayPayload() implements CustomPayload {
    public static final CustomPayload.Id<FrostMonarchSpawnOverlayPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "frostmonarch_spawn_overlay"));
    public static final PacketCodec<PacketByteBuf, FrostMonarchSpawnOverlayPayload> CODEC =
            PacketCodec.unit(new FrostMonarchSpawnOverlayPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

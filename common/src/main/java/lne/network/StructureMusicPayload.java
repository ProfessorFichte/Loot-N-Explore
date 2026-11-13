package lne.network;

import lne.sounds.StructureMusicTrack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public record StructureMusicPayload(StructureMusicTrack track, boolean start) implements CustomPayload {
    public static final CustomPayload.Id<StructureMusicPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "structure_music"));
    public static final PacketCodec<PacketByteBuf, StructureMusicPayload> CODEC = PacketCodec.tuple(
            StructureMusicTrack.PACKET_CODEC, StructureMusicPayload::track,
            PacketCodecs.BOOL, StructureMusicPayload::start,
            StructureMusicPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

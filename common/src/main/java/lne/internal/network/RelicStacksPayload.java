package lne.internal.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

import static lne.LneMod.MOD_ID;

public record RelicStacksPayload(List<Entry> entries) implements CustomPayload {
    public static final CustomPayload.Id<RelicStacksPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "relic_stacks"));

    public static final PacketCodec<PacketByteBuf, Entry> ENTRY_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, Entry::relic,
            PacketCodecs.VAR_INT, Entry::stacks,
            PacketCodecs.VAR_INT, Entry::max,
            PacketCodecs.VAR_INT, Entry::progress,
            PacketCodecs.VAR_INT, Entry::goal,
            Entry::new);

    public static final PacketCodec<PacketByteBuf, RelicStacksPayload> CODEC =
            PacketCodec.tuple(ENTRY_CODEC.collect(PacketCodecs.toList()), RelicStacksPayload::entries, RelicStacksPayload::new);

    public record Entry(String relic, int stacks, int max, int progress, int goal) {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

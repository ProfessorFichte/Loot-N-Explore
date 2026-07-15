package more_rpg_loot.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public record FrozenDepthsMusicPayload(boolean start) implements CustomPayload {
    public static final CustomPayload.Id<FrozenDepthsMusicPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "frozen_depths_music"));
    public static final PacketCodec<PacketByteBuf, FrozenDepthsMusicPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.BOOL, FrozenDepthsMusicPayload::start, FrozenDepthsMusicPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

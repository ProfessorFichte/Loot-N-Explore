package lne.sounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public enum StructureMusicTrack {
    FROZEN_DEPTHS(ModSounds.MUSIC_FROZEN_DEPTHS),
    INN(ModSounds.MUSIC_INN);

    public static final PacketCodec<ByteBuf, StructureMusicTrack> PACKET_CODEC =
            PacketCodecs.indexed(i -> values()[i], Enum::ordinal);

    private final ModSounds.Entry sound;

    StructureMusicTrack(ModSounds.Entry sound) {
        this.sound = sound;
    }

    public ModSounds.Entry sound() {
        return sound;
    }
}

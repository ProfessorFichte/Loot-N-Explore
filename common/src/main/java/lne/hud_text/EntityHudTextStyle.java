package lne.hud_text;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public enum EntityHudTextStyle {
    BANNER(14),
    TYPEWRITER(10),
    TYPEWRITER_ICICLE_FALL(0);

    public static final int ICICLE_FALL_STAGGER_TICKS = 3;
    public static final int ICICLE_FALL_TICKS = 22;

    public static final PacketCodec<ByteBuf, EntityHudTextStyle> PACKET_CODEC =
            PacketCodecs.indexed(i -> values()[i], Enum::ordinal);

    private final int fadeOutTicks;

    EntityHudTextStyle(int fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
    }

    public int fadeOutTicks() {
        return fadeOutTicks;
    }
}

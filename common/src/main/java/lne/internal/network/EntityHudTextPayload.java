package lne.internal.network;

import lne.internal.hud_text.EntityHudTextStyle;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static lne.LneMod.MOD_ID;

public record EntityHudTextPayload(String translationKey, EntityHudTextStyle style, int color,
                                   int typeTicks, int holdTicks, Optional<Identifier> icon) implements CustomPayload {
    public static final CustomPayload.Id<EntityHudTextPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "entity_hud_text"));
    public static final PacketCodec<PacketByteBuf, EntityHudTextPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, EntityHudTextPayload::translationKey,
            EntityHudTextStyle.PACKET_CODEC, EntityHudTextPayload::style,
            PacketCodecs.INTEGER, EntityHudTextPayload::color,
            PacketCodecs.VAR_INT, EntityHudTextPayload::typeTicks,
            PacketCodecs.VAR_INT, EntityHudTextPayload::holdTicks,
            PacketCodecs.optional(Identifier.PACKET_CODEC), EntityHudTextPayload::icon,
            EntityHudTextPayload::new);

    public EntityHudTextPayload(String translationKey, EntityHudTextStyle style, int color, int typeTicks, int holdTicks) {
        this(translationKey, style, color, typeTicks, holdTicks, Optional.empty());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

package lne.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public record WeaponCooldownPayload(int ability, int ticks) implements CustomPayload {
    public static final CustomPayload.Id<WeaponCooldownPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "weapon_cooldown"));
    public static final PacketCodec<PacketByteBuf, WeaponCooldownPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.VAR_INT, WeaponCooldownPayload::ability, PacketCodecs.VAR_INT, WeaponCooldownPayload::ticks,
                    WeaponCooldownPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

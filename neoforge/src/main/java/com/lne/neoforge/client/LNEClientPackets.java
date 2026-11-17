package com.lne.neoforge.client;

import lne.client.LneClient;
import lne.internal.network.EntityHudTextPayload;
import lne.internal.network.StructureMusicPayload;

public final class LNEClientPackets {
    private LNEClientPackets() {}

    public static void onMusic(StructureMusicPayload payload) {
        LneClient.onStructureMusic(payload);
    }

    public static void onRelicStacks(lne.internal.network.RelicStacksPayload payload) {
        LneClient.onRelicStacks(payload);
    }

    public static void onWeaponCooldown(lne.internal.network.WeaponCooldownPayload payload) {
        lne.client.hud.WeaponReadyCue.onRangedCooldown(payload.ability(), payload.ticks());
    }

    public static void onEntityHudText(EntityHudTextPayload payload) {
        LneClient.onEntityHudText(payload);
    }
}

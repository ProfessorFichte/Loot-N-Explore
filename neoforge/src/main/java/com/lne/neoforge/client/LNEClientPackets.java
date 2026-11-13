package com.lne.neoforge.client;

import lne.client.LneClient;
import lne.network.EntityHudTextPayload;
import lne.network.StructureMusicPayload;

public final class LNEClientPackets {
    private LNEClientPackets() {}

    public static void onMusic(StructureMusicPayload payload) {
        LneClient.onStructureMusic(payload);
    }

    public static void onRelicStacks(lne.network.RelicStacksPayload payload) {
        LneClient.onRelicStacks(payload);
    }

    public static void onWeaponCooldown(lne.network.WeaponCooldownPayload payload) {
        lne.client.hud.WeaponReadyCue.onRangedCooldown(payload.ability(), payload.ticks());
    }

    public static void onEntityHudText(EntityHudTextPayload payload) {
        LneClient.onEntityHudText(payload);
    }
}

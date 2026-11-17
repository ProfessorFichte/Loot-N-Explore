package lne.entity.frozen_depths.mob.frostmonarch;

import lne.internal.hud_text.EntityHudText;
import lne.internal.hud_text.EntityHudTextStyle;
import lne.internal.network.EntityHudTextPayload;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

import static lne.LneMod.MOD_ID;

public final class FrostMonarchLines {
    public static final String INTRO_KEY = "entity.loot_n_explore.frost_monarch.spawn_message";
    public static final String FAKE_DEATH_KEY = "entity.loot_n_explore.frost_monarch.fake_death_message";
    public static final List<String> BARRIER_SUMMON_KEYS = List.of(
            "entity.loot_n_explore.frost_monarch.barrier_summon_1",
            "entity.loot_n_explore.frost_monarch.barrier_summon_2",
            "entity.loot_n_explore.frost_monarch.barrier_summon_3",
            "entity.loot_n_explore.frost_monarch.barrier_summon_4");

    private static final double RANGE = 64.0;
    private static final Identifier INTRO_ICON = Identifier.of(MOD_ID, "textures/gui/frozen_depths/frostmonarch_spawn_overlay.png");
    private static final int INTRO_TYPE_TICKS = 55;
    private static final int FAKE_DEATH_TYPE_TICKS = 20;
    private static final int FAKE_DEATH_HOLD_TICKS = 30;
    private static final int BARRIER_TYPE_TICKS = 30;
    private static final int BARRIER_HOLD_TICKS = 50;

    private FrostMonarchLines() {}

    public static void intro(FrostMonarchEntity monarch, int introTicks) {
        int visible = Math.round(introTicks * 0.7F);
        int hold = Math.max(0, visible - INTRO_TYPE_TICKS - EntityHudTextStyle.BANNER.fadeOutTicks());
        EntityHudText.send(monarch, RANGE, new EntityHudTextPayload(INTRO_KEY, EntityHudTextStyle.BANNER,
                EntityHudText.FROST_COLOR, INTRO_TYPE_TICKS, hold, Optional.of(INTRO_ICON)));
    }

    public static void fakeDeath(FrostMonarchEntity monarch) {
        EntityHudText.send(monarch, RANGE, new EntityHudTextPayload(FAKE_DEATH_KEY,
                EntityHudTextStyle.TYPEWRITER_ICICLE_FALL, EntityHudText.FROST_COLOR,
                FAKE_DEATH_TYPE_TICKS, FAKE_DEATH_HOLD_TICKS));
    }

    public static void barrierSummon(FrostMonarchEntity monarch) {
        String key = BARRIER_SUMMON_KEYS.get(monarch.getRandom().nextInt(BARRIER_SUMMON_KEYS.size()));
        EntityHudText.send(monarch, RANGE, new EntityHudTextPayload(key, EntityHudTextStyle.TYPEWRITER,
                EntityHudText.FROST_COLOR, BARRIER_TYPE_TICKS, BARRIER_HOLD_TICKS));
    }
}

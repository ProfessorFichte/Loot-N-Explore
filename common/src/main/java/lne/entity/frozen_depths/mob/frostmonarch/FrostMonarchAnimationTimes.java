package lne.entity.frozen_depths.mob.frostmonarch;

public final class FrostMonarchAnimationTimes {
    public static final int SCREECH_LENGTH_TICKS = 25;
    public static final int SCREECH_RELEASE_TICK = 13;
    public static final int SUMMON_LENGTH_TICKS = 75;
    public static final int SUMMON_RELEASE_TICK = 18;
    public static final int QUICK_CAST_LENGTH_TICKS = 22;
    public static final int GROUND_ICICLE_RELEASE_TICK = 10;
    public static final int WALL_CAST_LENGTH_TICKS = 9;
    public static final int WALL_RELEASE_TICK = 6;
    public static final int DEATH_LENGTH_TICKS = 211;
    public static final int ATTACK1_LENGTH_TICKS = 18;
    public static final int ATTACK1_WINDUP_TICK = 4;
    public static final int ATTACK1_IMPACT_TICK = 9;
    public static final int ATTACK2_LENGTH_TICKS = 35;
    public static final int ATTACK2_WINDUP_TICK = 8;
    public static final int ATTACK2_IMPACT_TICK = 16;
    public static final int ATTACK3_LENGTH_TICKS = 30;
    public static final int ATTACK3_WINDUP_TICK = 7;
    public static final int ATTACK3_IMPACT_TICK = 17;

    public enum Cast {
        NONE(0),
        SUMMON(SUMMON_LENGTH_TICKS),
        QUICK(QUICK_CAST_LENGTH_TICKS),
        WALL(WALL_CAST_LENGTH_TICKS);

        public final int lengthTicks;

        Cast(int lengthTicks) {
            this.lengthTicks = lengthTicks;
        }

        public static Cast byId(int id) {
            Cast[] values = values();
            return id >= 0 && id < values.length ? values[id] : NONE;
        }
    }

    private FrostMonarchAnimationTimes() {}
}

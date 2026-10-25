package more_rpg_loot.entity.frozen_depths.projectile;

public final class IcicleFormation {
    private static final float[][] FIVE = {
            {0.0F, 2.7F},
            {-1.5F, 3.3F},
            {1.5F, 3.3F},
            {-2.1F, 1.7F},
            {2.1F, 1.7F}
    };

    private static final float[][] EIGHT = {
            {0.0F, 2.7F},
            {0.0F, 3.8F},
            {-1.5F, 3.3F},
            {1.5F, 3.3F},
            {-2.5F, 2.6F},
            {2.5F, 2.6F},
            {-2.1F, 1.7F},
            {2.1F, 1.7F}
    };

    private IcicleFormation() {
    }

    public static float[][] slots(int count) {
        if (count == FIVE.length) return FIVE;
        if (count == EIGHT.length) return EIGHT;
        float[][] slots = new float[count][2];
        for (int i = 0; i < count; i++) {
            double angle = Math.PI * (count == 1 ? 0.5 : (double) i / (count - 1));
            slots[i][0] = (float) (Math.cos(angle) * 2.4);
            slots[i][1] = 1.7F + (float) (Math.sin(angle) * 2.0);
        }
        return slots;
    }
}

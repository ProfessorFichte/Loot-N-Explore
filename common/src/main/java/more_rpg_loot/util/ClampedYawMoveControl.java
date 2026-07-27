package more_rpg_loot.util;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

/** Shared move control for the frozen_depths bosses/elites: clamps yaw turn speed to +-30 deg/tick for smoother turning. */
public class ClampedYawMoveControl extends MoveControl {
    public ClampedYawMoveControl(MobEntity entity) {
        super(entity);
    }

    @Override
    protected float wrapDegrees(float from, float to, float max) {
        float f = MathHelper.wrapDegrees(to - from);
        if (f > 30.0F) f = 30.0F;
        if (f < -30.0F) f = -30.0F;
        float g = from + f;
        if (g < 0.0F) g += 360.0F;
        else if (g > 360.0F) g -= 360.0F;
        return g;
    }
}

package lne.internal.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import lne.entity.frozen_depths.projectile.StraightIcicleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class FrozenSoulAbility extends RelicAbility {
    public FrozenSoulAbility() {
        super("frozen_soul");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.ACTIVE_USE);
    }

    @Override
    public Set<TriggerType> passiveTriggers() {
        return java.util.EnumSet.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.SPELL_HIT);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 1200.0);
        values.put("icicles_per_direction", 5.0);
        values.put("base_damage", 4.0);
        values.put("damage_scaling", 0.10);
        values.put("icicle_spacing", 1.6);
    }

    @Override
    public String name() {
        return "Frozen Eruption";
    }

    @Override
    public String description() {
        return "%s icicles erupt from the ground around you in an X pattern, freezing enemies and dealing %s magic damage.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.number(Math.max(1, (int) value("icicles_per_direction")) * 4), scaledNumber(viewer, "base_damage", "damage_scaling")};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var player = context.player();
        if (!context.ready()) {
            return false;
        }
        var world = context.world();
        int icicles = Math.max(1, (int) value("icicles_per_direction")) * 4;
        double spacing = value("icicle_spacing");
        float baseYaw = player.getYaw() * MathHelper.RADIANS_PER_DEGREE;
        boolean sounded = false;
        for (int i = 0; i < icicles; i++) {
            int direction = i % 4;
            int step = i / 4 + 1;
            double angle = baseYaw + Math.PI / 4 + direction * Math.PI / 2;
            double x = player.getX() - Math.sin(angle) * spacing * step;
            double z = player.getZ() + Math.cos(angle) * spacing * step;
            double y = groundY(world, x, z, player.getY());
            if (Double.isNaN(y)) continue;
            var icicle = new StraightIcicleEntity(world, x, y, z, (float) (angle + Math.PI / 2), step * 2, player);
            icicle.configureFixedDamage((float) value("base_damage"), (float) value("damage_scaling"));
            if (!sounded) {
                icicle.withEruptSound();
                sounded = true;
            }
            world.spawnEntity(icicle);
        }
        var pos = player.getPos();
        MrpgFx.play(world, LNEFx.FROZEN_SOUL, FxParams.at(pos).radius((float) (spacing * 2)));
        context.startCooldown();
        return true;
    }

    public static double groundY(World world, double x, double z, double fromY) {
        BlockPos pos = BlockPos.ofFloored(x, fromY + 1.5, z);
        int floor = MathHelper.floor(fromY) - 3;
        while (pos.getY() >= floor) {
            BlockPos below = pos.down();
            BlockState state = world.getBlockState(below);
            if (state.isSideSolidFullSquare(world, below, Direction.UP)) {
                double offset = 0.0;
                if (!world.isAir(pos)) {
                    var shape = world.getBlockState(pos).getCollisionShape(world, pos);
                    if (!shape.isEmpty()) offset = shape.getMax(Direction.Axis.Y);
                }
                return pos.getY() + offset;
            }
            pos = below;
        }
        return Double.NaN;
    }
}

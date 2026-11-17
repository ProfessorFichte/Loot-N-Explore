package lne.internal.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.internal.fx.LNEFx;
import lne.internal.ability.AbilityDamage;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import lne.entity.relic.RelicWitherSkullEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.Set;

public class WitherSkullVolleyAbility extends RelicAbility {
    public WitherSkullVolleyAbility() {
        super("wither_spine");
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
        values.put(COOLDOWN, 1800.0);
        values.put("skulls", 3.0);
        values.put("spread_degrees", 9.0);
        values.put("base_damage", 6.0);
        values.put("damage_scaling", 0.10);
        values.put("radius", 5.0);
        values.put("wither_ticks", 100.0);
        values.put("wither_amplifier", 1.0);
    }

    @Override
    public String name() {
        return "Skull Barrage";
    }

    @Override
    public String description() {
        return "Fire %s wither skulls where you look. Each explodes without destroying terrain, dealing %s magic damage and inflicting Wither.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.number(value("skulls")), scaledNumber(viewer, "base_damage", "damage_scaling")};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        if (!context.ready()) {
            return false;
        }
        var player = context.player();
        var world = context.world();
        int skulls = Math.max(1, (int) value("skulls"));
        float damage = AbilityDamage.scaled(player, value("base_damage"), value("damage_scaling"));
        double spread = Math.toRadians(value("spread_degrees"));
        var target = context.type() == TriggerType.ACTIVE_USE ? null : context.target();
        Vec3d look = target != null
                ? target.getPos().add(0, target.getHeight() * 0.5, 0).subtract(player.getEyePos()).normalize()
                : player.getRotationVec(1.0F);
        for (int i = 0; i < skulls; i++) {
            double offset = skulls == 1 ? 0 : (i - (skulls - 1) / 2.0) * spread;
            double cos = Math.cos(offset);
            double sin = Math.sin(offset);
            Vec3d direction = new Vec3d(look.x * cos - look.z * sin, look.y, look.x * sin + look.z * cos);
            world.spawnEntity(new RelicWitherSkullEntity(world, player, direction, damage, value("radius"),
                    ticks("wither_ticks"), (int) value("wither_amplifier")));
        }
        var pos = player.getEyePos();
        LNEFx.play(world, LNEFx.WITHER_CAST, pos.add(look));
        context.startCooldown();
        return true;
    }
}

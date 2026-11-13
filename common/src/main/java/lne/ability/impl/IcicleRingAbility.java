package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.fx.LNEFx;
import lne.ability.RelicAbility;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;
import lne.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class IcicleRingAbility extends RelicAbility {
    public IcicleRingAbility() {
        super("glacier_shard");
    }

    @Override
    public Set<TriggerType> triggers() {
        return EnumSet.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.SPELL_HIT);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put("hits_required", 10.0);
        values.put("icicles", 5.0);
        values.put("base_damage", 3.0);
        values.put("damage_scaling", 0.05);
    }

    @Override
    public String name() {
        return "Glacial Shards";
    }

    @Override
    public String description() {
        return "Every %s time you deal damage, %s icicles shoot outward in a circle, each dealing %s magic damage.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{AmethystBurstAbility.ordinal((int) value("hits_required")), RelicText.number(value("icicles")), scaledNumber(viewer, "base_damage", "damage_scaling")};
    }

    @Override
    public Progress progress() {
        return new Progress(relic() + ".hits", (int) value("hits_required"), PROGRESS_HITS);
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        if (context.target() == null) return false;
        if (!context.countUp(relic() + ".hits", (int) value("hits_required"))) return false;
        var player = context.player();
        var world = context.world();
        int icicles = Math.max(1, (int) value("icicles"));
        double offset = player.getRandom().nextDouble() * Math.PI * 2;
        Vec3d center = player.getPos().add(0, player.getHeight() * 0.6, 0);
        for (int i = 0; i < icicles; i++) {
            double angle = offset + Math.PI * 2 * i / icicles;
            Vec3d direction = new Vec3d(Math.cos(angle), 0.02, Math.sin(angle));
            Vec3d spawn = center.add(direction.multiply(0.9));
            var icicle = new TrackingIcicleEntity(world, player, direction, spawn.x, spawn.y, spawn.z, TrackingIcicleEntity.Shot.RING);
            icicle.configureFixedDamage((float) value("base_damage"), (float) value("damage_scaling"));
            world.spawnEntity(icicle);
        }
        LNEFx.play(world, LNEFx.ICICLE_RING, center);
        return true;
    }
}

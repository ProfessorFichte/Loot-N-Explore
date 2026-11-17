package lne.internal.ability.impl;

import org.jetbrains.annotations.Nullable;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import com.mrpg_lib.util.AllyHelper;
import lne.internal.ability.AbilityDamage;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class SoothingWaterAbility extends RelicAbility {
    public SoothingWaterAbility() {
        super("amphitrite_diadem");
    }

    @Override
    public Set<TriggerType> triggers() {
        return EnumSet.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.SPELL_HIT);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put("hits_required", 10.0);
        values.put("radius", 5.0);
        values.put("heal_base", 2.0);
        values.put("heal_scaling", 0.10);
    }

    @Override
    public String name() {
        return "Soothing Tide";
    }

    @Override
    public String description() {
        return "Every %s time you deal damage, a %s block soothing water area heals you and your allies for %s hearts.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{AmethystBurstAbility.ordinal((int) value("hits_required")), RelicText.number(value("radius")), healHearts(viewer)};
    }

    @Override
    public Progress progress() {
        return new Progress(relic() + ".hits", (int) value("hits_required"), PROGRESS_HITS);
    }

    private String healHearts(@Nullable LivingEntity viewer) {
        double heal = viewer == null ? value("heal_base") : AbilityDamage.scaled(viewer, value("heal_base"), value("heal_scaling"));
        return RelicText.number(heal / 2.0);
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        if (context.target() == null) return false;
        if (!context.countUp(relic() + ".hits", (int) value("hits_required"))) return false;
        var player = context.player();
        var world = context.world();
        double radius = value("radius");
        float heal = AbilityDamage.scaled(player, value("heal_base"), value("heal_scaling"));
        var center = player.getPos();
        var box = new Box(center.x - radius, center.y - 2, center.z - radius, center.x + radius, center.y + 3, center.z + radius);
        double radiusSquared = radius * radius;
        for (var entity : world.getEntitiesByClass(LivingEntity.class, box,
                entity -> entity.isAlive() && entity.squaredDistanceTo(center.x, entity.getY(), center.z) <= radiusSquared)) {
            if (entity == player || AllyHelper.canHelp(player, entity)) {
                entity.heal(heal);
                LNEFx.play(world, LNEFx.HEAL, entity.getPos().add(0, entity.getHeight() * 0.6, 0));
            }
        }
        MrpgFx.play(world, LNEFx.SOOTHING_WATER, FxParams.at(center).radius((float) radius));
        return true;
    }
}

package lne.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.fx.LNEFx;
import lne.ability.AbilityDamage;
import lne.ability.RelicAbility;
import lne.ability.RelicTargets;
import lne.ability.RelicText;
import lne.ability.TriggerContext;
import lne.ability.TriggerType;

import java.util.Map;
import java.util.Set;

public class AmethystBurstAbility extends RelicAbility {
    public AmethystBurstAbility() {
        super("charged_amethyst");
    }

    @Override
    public Set<TriggerType> triggers() {
        return java.util.EnumSet.of(TriggerType.MELEE_HIT, TriggerType.ARROW_HIT, TriggerType.SPELL_HIT);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put("hits_required", 5.0);
        values.put("radius", 5.0);
        values.put("base_damage", 2.0);
        values.put("damage_scaling", 0.05);
    }

    @Override
    public String name() {
        return "Resonant Discharge";
    }

    @Override
    public String description() {
        return "Every %s time you deal damage, a %s block explosion deals %s magic damage to enemies.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{ordinal((int) value("hits_required")), RelicText.number(value("radius")), scaledNumber(viewer, "base_damage", "damage_scaling")};
    }

    @Override
    public Progress progress() {
        return new Progress(relic() + ".hits", (int) value("hits_required"), PROGRESS_HITS);
    }

    public static String ordinal(int number) {
        return number + switch (number % 10) {
            case 1 -> number % 100 == 11 ? "th" : "st";
            case 2 -> number % 100 == 12 ? "th" : "nd";
            case 3 -> number % 100 == 13 ? "th" : "rd";
            default -> "th";
        };
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var target = context.target();
        if (target == null) return false;
        if (!context.countUp(relic() + ".hits", (int) value("hits_required"))) return false;
        var player = context.player();
        var world = context.world();
        var center = target.getPos().add(0, target.getHeight() * 0.5, 0);
        double radius = value("radius");
        float damage = AbilityDamage.scaled(player, value("base_damage"), value("damage_scaling"));
        for (var enemy : RelicTargets.enemiesAround(world, player, center, radius)) {
            RelicTargets.magicDamage(world, player, enemy, damage);
        }
        MrpgFx.play(world, LNEFx.AMETHYST_BURST, FxParams.at(center).radius((float) radius));
        return true;
    }
}

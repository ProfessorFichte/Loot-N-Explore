package lne.internal.ability.impl;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import lne.internal.ability.AbilityDamage;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicTargets;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Map;
import java.util.Set;

public class WitherAuraAbility extends RelicAbility {
    public WitherAuraAbility() {
        super("withered_obsidian_shard");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.DAMAGE_TAKEN);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put("hits_required", 10.0);
        values.put("radius", 6.0);
        values.put("duration_ticks", 100.0);
        values.put("amplifier_scaling", 0.10);
        values.put("max_amplifier", 3.0);
    }

    @Override
    public String name() {
        return "Withering Spite";
    }

    @Override
    public String description() {
        return "Every %s time you take damage, a %s block withering aura inflicts Wither %s on enemies for %s.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{AmethystBurstAbility.ordinal((int) value("hits_required")), RelicText.number(value("radius")), witherLevel(viewer), RelicText.seconds(value("duration_ticks"))};
    }

    @Override
    public Progress progress() {
        return new Progress(relic() + ".hits", (int) value("hits_required"), PROGRESS_HITS_TAKEN);
    }

    private Text witherLevel(@Nullable LivingEntity viewer) {
        int amplifier = viewer == null ? 0 : AbilityDamage.scaledAmplifier(viewer, value("amplifier_scaling"), (int) value("max_amplifier"));
        return Text.translatable("enchantment.level." + (amplifier + 1));
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        if (!context.countUp(relic() + ".hits", (int) value("hits_required"))) return false;
        var player = context.player();
        var world = context.world();
        int amplifier = AbilityDamage.scaledAmplifier(player, value("amplifier_scaling"), (int) value("max_amplifier"));
        double radius = value("radius");
        for (var enemy : RelicTargets.enemiesAround(world, player, player.getPos(), radius)) {
            enemy.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, ticks("duration_ticks"), amplifier), player);
            LNEFx.play(world, LNEFx.SOUL_HIT, enemy.getPos().add(0, enemy.getHeight() * 0.5, 0));
        }
        var base = player.getPos();
        MrpgFx.play(world, LNEFx.WITHER_AURA, FxParams.at(base).radius((float) radius));
        return true;
    }
}

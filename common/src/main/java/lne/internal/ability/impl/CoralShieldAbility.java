package lne.internal.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import lne.internal.fx.LNEFx;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicPlayerData;
import net.minecraft.server.network.ServerPlayerEntity;
import lne.internal.ability.RelicText;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import net.minecraft.entity.effect.StatusEffectInstance;
import lne.effects.Effects;
import lne.effects.generic.RainbowCoralEffect;


import java.util.Map;
import java.util.Set;

public class CoralShieldAbility extends RelicAbility {
    public CoralShieldAbility() {
        super("rainbow_coral");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.HEALED);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 600.0);
        values.put("roll_interval_ticks", 20.0);
        values.put("chance", 0.05);
        values.put("absorption_percent", 0.20);
        values.put("duration_ticks", 1200.0);
    }

    @Override
    public String name() {
        return "Prismatic Shell";
    }

    @Override
    public String description() {
        return "When you receive healing, %s chance (one roll every %s) to gain absorption hearts worth %s of your max health for %s.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.percent(value("chance")), RelicText.seconds(value("roll_interval_ticks")), RelicText.percent(value("absorption_percent")), RelicText.seconds(value("duration_ticks"))};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var player = context.player();
        if (!context.ready() || !context.forced() && !rollAllowed(player, context.data()) || !context.roll(value("chance"))) {
            return false;
        }
        grant(player);
        context.startCooldown();
        return true;
    }

    public boolean rollAllowed(ServerPlayerEntity player, RelicPlayerData data) {
        String key = relic() + ".roll";
        long now = player.getServerWorld().getTime();
        if (!data.isReady(key, now)) return false;
        data.startCooldown(key, now, ticks("roll_interval_ticks"));
        return true;
    }

    public void grant(ServerPlayerEntity player) {
        double shield = player.getMaxHealth() * value("absorption_percent");
        player.addStatusEffect(new StatusEffectInstance(Effects.RAINBOW_CORAL.registryEntry, ticks("duration_ticks"), 0, false, false, true));
        RainbowCoralEffect.grant(player, shield);
        LNEFx.play(player.getServerWorld(), LNEFx.CORAL_SHIELD, player.getPos().add(0, 1.0, 0));
    }
}

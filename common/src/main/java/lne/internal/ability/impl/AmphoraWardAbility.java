package lne.internal.ability.impl;

import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.LivingEntity;
import com.mrpg_lib.fx.FxHandle;
import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.internal.fx.LNEFx;
import lne.internal.ability.RelicAbility;
import lne.internal.ability.RelicPlayerData;
import lne.internal.ability.RelicText;
import lne.internal.ability.RelicTriggers;
import lne.internal.ability.TriggerContext;
import lne.internal.ability.TriggerType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class AmphoraWardAbility extends RelicAbility {
    private static final String WARD_END = "ward_end";
    private static final String STORED = "ward_stored";
    private static final Map<ServerPlayerEntity, WardFx> WARD_FX = new WeakHashMap<>();

    private record WardFx(FxHandle handle, boolean lethal) {
    }

    public AmphoraWardAbility() {
        super("poseidons_amphora");
    }

    @Override
    public Set<TriggerType> triggers() {
        return Set.of(TriggerType.LOW_HEALTH);
    }

    @Override
    protected void defaults(Map<String, Double> values) {
        values.put(COOLDOWN, 1200.0);
        values.put("health_threshold", 0.10);
        values.put("heal_percent", 0.50);
        values.put("ward_ticks", 100.0);
        values.put("release_percent", 0.5);
    }

    @Override
    public String name() {
        return "Tidal Ward";
    }

    @Override
    public String description() {
        return "When you fall below %s health you are healed to %s health and take no damage for %s, but the damage is stored. When the ward ends you take %s of the stored damage.";
    }

    @Override
    public Object[] descriptionArgs(@Nullable LivingEntity viewer) {
        return new Object[]{RelicText.percent(value("health_threshold")), RelicText.percent(value("heal_percent")), RelicText.seconds(value("ward_ticks")), RelicText.percent(value("release_percent"))};
    }

    @Override
    public boolean onTrigger(TriggerContext context) {
        var result = intercept(context.player(), context.data(), context.amount(), context.forced(), context.ready(), context::startCooldown);
        if (result != WardResult.NONE) {
            context.cancel();
        }
        return result == WardResult.STARTED;
    }

    public enum WardResult { NONE, ABSORBED, STARTED }

    public WardResult intercept(ServerPlayerEntity player, RelicPlayerData data, float amount, boolean forced, boolean ready, Runnable startCooldown) {
        var world = player.getServerWorld();
        if (data.value(WARD_END) > 0) {
            data.setValue(STORED, data.value(STORED) + amount);
            LNEFx.play(world, LNEFx.AMPHORA_ABSORB, player.getPos());
            updateWardFx(player, data);
            return WardResult.ABSORBED;
        }
        boolean fallsLow = player.getHealth() - amount < player.getMaxHealth() * value("health_threshold");
        if (!(fallsLow || forced) || !ready) {
            return WardResult.NONE;
        }
        data.setValue(WARD_END, world.getTime() + ticks("ward_ticks"));
        data.setValue(STORED, amount);
        player.setHealth(Math.max(player.getHealth(), (float) (player.getMaxHealth() * value("heal_percent"))));
        startCooldown.run();
        LNEFx.play(world, LNEFx.AMPHORA_START, player.getPos());
        stopWardFx(player);
        updateWardFx(player, data);
        return WardResult.STARTED;
    }

    @Override
    public void onServerTick(ServerPlayerEntity player, RelicPlayerData data) {
        double end = data.value(WARD_END);
        if (end <= 0) return;
        var world = player.getServerWorld();
        if (world.getTime() < end) {
            updateWardFx(player, data);
            return;
        }
        stopWardFx(player);
        double stored = data.value(STORED);
        data.setValue(WARD_END, 0);
        data.setValue(STORED, 0);
        LNEFx.play(world, LNEFx.AMPHORA_RELEASE, player.getPos());
        float release = (float) (stored * value("release_percent"));
        if (release > 0) {
            RelicTriggers.runSuppressed(() -> player.damage(player.getDamageSources().magic(), release));
        }
    }

    private boolean lethal(ServerPlayerEntity player, RelicPlayerData data) {
        return data.value(STORED) * value("release_percent") >= player.getHealth() + player.getAbsorptionAmount();
    }

    private void updateWardFx(ServerPlayerEntity player, RelicPlayerData data) {
        boolean lethal = lethal(player, data);
        var current = WARD_FX.get(player);
        if (current != null && current.lethal() == lethal) return;
        if (current != null) current.handle().stop();
        int total = ticks("ward_ticks");
        int age = (int) Math.max(0, player.getServerWorld().getTime() - (data.value(WARD_END) - total));
        var handle = MrpgFx.play(player.getServerWorld(), LNEFx.AMPHORA_WARD, FxParams.on(player)
                .duration(total).startAge(age).color(lethal ? LNEFx.WARD_LETHAL : LNEFx.WARD_SAFE));
        WARD_FX.put(player, new WardFx(handle, lethal));
    }

    private static void stopWardFx(ServerPlayerEntity player) {
        var current = WARD_FX.remove(player);
        if (current != null) current.handle().stop();
    }
}

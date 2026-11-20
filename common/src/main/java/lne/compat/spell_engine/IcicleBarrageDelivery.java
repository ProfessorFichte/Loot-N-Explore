package lne.compat.spell_engine;

import lne.entity.frozen_depths.projectile.IcicleFormation;
import lne.entity.frozen_depths.projectile.TrackingIcicleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.SpellModifiers;
import net.spell_engine.internals.SpellParameters;
import net.spell_engine.internals.impact.SpellImpacts;

import java.util.List;

public final class IcicleBarrageDelivery {
    private static final int HOVER_TICKS = 12;
    private static final int LAUNCH_INTERVAL = 5;
    private static final int MAX_ICICLES = 12;

    private IcicleBarrageDelivery() {
    }

    static boolean deliver(World world, RegistryEntry<Spell> entry, LivingEntity caster, List<SpellExecution.DeliveryTarget> targets,
                           SpellExecution.ImpactContext context, Vec3d position) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return false;
        }
        int count = Math.min(MAX_ICICLES, LNE_Abilities.ICICLE_BARRAGE_COUNT + extraIcicles(caster, entry));
        double range = SpellParameters.getRange(caster, entry);
        float[][] slots = IcicleFormation.slots(count);
        for (int i = 0; i < slots.length; i++) {
            var icicle = new TrackingIcicleEntity(serverWorld, caster, slots[i][0], slots[i][1]);
            icicle.setHitHandler(target -> SpellImpacts.performImpacts(serverWorld, caster, target, caster, entry, entry.value().impacts, context));
            icicle.autoLaunch(HOVER_TICKS + i * LAUNCH_INTERVAL, range);
            serverWorld.spawnEntity(icicle);
        }
        serverWorld.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 0.6F, 1.5F);
        return true;
    }

    private static int extraIcicles(LivingEntity caster, RegistryEntry<Spell> entry) {
        if (!(caster instanceof PlayerEntity player)) {
            return 0;
        }
        int extra = 0;
        for (Spell.Modifier modifier : SpellModifiers.of(player, entry)) {
            if (modifier.projectile_launch != null) {
                extra += modifier.projectile_launch.extra_launch_count;
            }
        }
        return extra;
    }
}

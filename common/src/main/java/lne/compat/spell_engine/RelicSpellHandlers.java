package lne.compat.spell_engine;

import lne.ability.RelicAbilities;
import lne.ability.RelicTriggers;
import lne.ability.impl.AmphoraWardAbility;
import lne.ability.impl.CoralShieldAbility;
import lne.ability.impl.FrostHauntedSummonAbility;
import lne.ability.impl.SkeletonSummonAbility;
import lne.entity.relic.CorruptedPearlEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.internals.impact.SpellImpacts;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RelicSpellHandlers {
    private static final int SUMMON_TICKS = 400;

    public static void register() {
        SpellHandlers.registerCustomDelivery(LNE_Abilities.VOID_RIFT, RelicSpellHandlers::voidRift);
        SpellHandlers.registerCustomImpact(LNE_Abilities.SUMMON_SKELETON, (entry, power, caster, target, context) -> {
            if (!(caster instanceof ServerPlayerEntity player)) return new SpellHandlers.ImpactResult(false, false);
            double value = power.nonCriticalValue();
            var skeleton = SkeletonSummonAbility.summon(player.getServerWorld(), player, SUMMON_TICKS, value, value * 0.25);
            return new SpellHandlers.ImpactResult(skeleton != null, false);
        });
        SpellHandlers.registerCustomImpact(LNE_Abilities.SUMMON_FROST_HAUNTED, (entry, power, caster, target, context) -> {
            if (!(caster instanceof ServerPlayerEntity player)) return new SpellHandlers.ImpactResult(false, false);
            double value = power.nonCriticalValue();
            FrostHauntedSummonAbility.summon(player.getServerWorld(), player, 3, SUMMON_TICKS, value * 0.5, value * 0.15);
            return new SpellHandlers.ImpactResult(true, false);
        });
        RelicTriggers.spellBridge = new Bridge();
    }

    private static List<Entity> around(ServerWorld world, Vec3d center, float radius, ServerPlayerEntity player, boolean includePlayer) {
        var box = Box.of(center, radius * 2, radius * 2, radius * 2);
        return world.getEntitiesByClass(LivingEntity.class, box, living -> living.isAlive()
                        && (includePlayer || living != player)
                        && living.squaredDistanceTo(center) <= radius * radius)
                .stream().map(living -> (Entity) living).toList();
    }

    private static boolean voidRift(World world, RegistryEntry<Spell> entry, LivingEntity caster, List<SpellExecution.DeliveryTarget> targets,
                                    SpellExecution.ImpactContext context, Vec3d position) {
        if (!(caster instanceof ServerPlayerEntity player) || !(world instanceof ServerWorld serverWorld)) return false;
        var pearl = new CorruptedPearlEntity(serverWorld, player);
        pearl.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
        pearl.onLanding((owner, center) -> riftBurst(serverWorld, owner, entry, center));
        serverWorld.spawnEntity(pearl);
        return true;
    }

    private static void riftBurst(ServerWorld world, ServerPlayerEntity owner, RegistryEntry<Spell> entry, Vec3d center) {
        var spell = entry.value();
        float radius = spell.area_impact != null ? spell.area_impact.radius : 7.0F;
        var context = new SpellExecution.ImpactContext()
                .power(SpellPower.getSpellPower(spell.school, owner))
                .position(center);
        for (var target : around(world, center, radius, owner, false)) {
            SpellImpacts.performImpacts(world, owner, target, owner, entry, spell.impacts, context);
        }
        if (spell.area_impact != null) {
            if (spell.area_impact.visuals != null && spell.area_impact.visuals.particles != null) {
                ParticleHelper.sendBatches(center, owner, spell.area_impact.visuals.particles);
            }
            if (spell.area_impact.sound != null) {
                var sound = Registries.SOUND_EVENT.get(Identifier.of(spell.area_impact.sound.id()));
                if (sound != null) {
                    world.playSound(null, center.x, center.y, center.z, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Nullable
    private static RegistryEntry<Spell> equipped(ServerPlayerEntity player, Identifier id) {
        for (var entry : SpellContainerSource.passiveSpellsOf(player)) {
            if (entry.matchesId(id)) return entry;
        }
        return null;
    }

    private static int cooldownTicks(RegistryEntry<Spell> entry) {
        var cost = entry.value().cost;
        return cost != null && cost.cooldown != null ? Math.round(cost.cooldown.duration * 20) : 0;
    }

    private static void startCooldown(ServerPlayerEntity player, RegistryEntry<Spell> entry, Identifier relic) {
        int ticks = cooldownTicks(entry);
        ((SpellCasterEntity) player).getCooldownManager().set(entry, ticks);
        player.getItemCooldownManager().set(Registries.ITEM.get(relic), ticks);
    }

    private static class Bridge implements RelicTriggers.SpellBridge {
        @Override
        public boolean beforeDamage(ServerPlayerEntity player, float amount) {
            if (!(RelicAbilities.get("poseidons_amphora") instanceof AmphoraWardAbility ward)) return false;
            var entry = equipped(player, LNE_Abilities.POSEIDONS_AMPHORA);
            var cooldowns = ((SpellCasterEntity) player).getCooldownManager();
            boolean ready = entry != null && !cooldowns.isCoolingDown(entry);
            var result = ward.intercept(player, RelicTriggers.dataOf(player), amount, false, ready,
                    () -> startCooldown(player, entry, LNE_Abilities.POSEIDONS_AMPHORA));
            return result != AmphoraWardAbility.WardResult.NONE;
        }

        @Override
        public void onHeal(ServerPlayerEntity player) {
            if (!(RelicAbilities.get("rainbow_coral") instanceof CoralShieldAbility coral)) return;
            var entry = equipped(player, LNE_Abilities.RAINBOW_CORAL);
            if (entry == null) return;
            var cooldowns = ((SpellCasterEntity) player).getCooldownManager();
            if (cooldowns.isCoolingDown(entry) || !coral.rollAllowed(player, RelicTriggers.dataOf(player))) return;
            if (player.getRandom().nextDouble() >= coral.value("chance")) return;
            coral.grant(player);
            startCooldown(player, entry, LNE_Abilities.RAINBOW_CORAL);
        }
    }
}

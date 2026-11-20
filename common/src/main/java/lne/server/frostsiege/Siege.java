package lne.server.frostsiege;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.config.FrostsiegeConfig;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.conjured.EternalWinterPortalEntity;
import lne.entity.frozen_depths.conjured.PortalPlacement;
import lne.internal.fx.LNEFx;
import lne.util.LneLootTables;
import lne.util.LneTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


final class Siege {
    static final String NAME_KEY = "event.loot_n_explore.frostsiege";
    private static final String WAVE_KEY = NAME_KEY + ".wave";
    private static final String VICTORY_KEY = NAME_KEY + ".victory";
    private static final double BAR_RANGE = 64.0;
    private static final double LEAVE_RANGE = 96.0;
    private static final double MOB_SCAN_RANGE = 128.0;
    private static final int MARKING_TICKS = 60;
    private static final int MARKER_INTERVAL = 20;
    private static final int PORTAL_FIRST_SPAWN_DELAY = 20;
    private static final int GENERAL_PORTAL_LIFETIME = 100;
    private static final int GENERAL_SPAWN_DELAY = 60;
    private static final int PLACEMENT_RETRY_TICKS = 600;
    private static final int STRAGGLER_TICKS = 600;
    private static final int WAVE_TIMEOUT_TICKS = 2400;
    private static final int VICTORY_TICKS = 100;
    private static final double FLANK_DISTANCE = 8.0;
    private static final double FLANK_RADIUS = 5.0;

    private enum Phase { WARMUP, PLACING, MARKING, FIGHTING, PAUSE, VICTORY }

    final UUID playerId;
    private final FrostsiegeConfig config;
    private final ServerBossBar bar = new ServerBossBar(Text.translatable(NAME_KEY), BossBar.Color.BLUE, BossBar.Style.PROGRESS);
    private final String mobTag;
    private final int totalWaves;
    private final int portalsPerWave;
    private final boolean ominous;
    private final Set<UUID> portals = new HashSet<>();
    private final Set<UUID> seenMobs = new HashSet<>();
    private List<Vec3d> spots = List.of();
    private final List<Integer> openDelays = new ArrayList<>();
    private List<EntityType<?>> spotTypes = List.of();
    private int opened;
    private int flankAt = -1;
    private Vec3d center;
    private Phase phase = Phase.WARMUP;
    private int phaseTicks;
    private int wave;
    private int expectedMobs;
    private int portalLifetime;
    boolean startedSnow;

    Siege(ServerPlayerEntity player, FrostsiegeConfig config, boolean ominous) {
        this.playerId = player.getUuid();
        this.config = config;
        this.ominous = ominous;
        this.center = player.getPos();
        this.mobTag = "lne_frostsiege_" + playerId;
        Difficulty difficulty = player.getWorld().getDifficulty();
        this.totalWaves = Math.max(1, switch (difficulty) {
            case HARD -> config.waves_hard;
            case NORMAL -> config.waves_normal;
            default -> config.waves_easy;
        });
        this.portalsPerWave = Math.max(1, switch (difficulty) {
            case HARD -> config.portals_hard;
            case NORMAL -> config.portals_normal;
            default -> config.portals_easy;
        });
    }

    Vec3d center() {
        return center;
    }

    int estimatedTicks() {
        return config.warmup_ticks + totalWaves * (config.portal_lifetime_ticks + config.wave_pause_ticks + MARKING_TICKS + STRAGGLER_TICKS);
    }

    boolean tick(ServerWorld world) {
        ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerId);
        if (player == null || player.getWorld() != world || player.getPos().squaredDistanceTo(center) > LEAVE_RANGE * LEAVE_RANGE) {
            return false;
        }
        if (world.getTime() % 20 == 0) {
            syncBarPlayers(world, player);
        }
        phaseTicks++;
        return switch (phase) {
            case WARMUP -> {
                bar.setPercent(Math.min(1.0F, phaseTicks / (float) Math.max(1, config.warmup_ticks)));
                if (phaseTicks >= config.warmup_ticks) {
                    enter(Phase.PLACING);
                }
                yield true;
            }
            case PLACING -> placePortals(world, player);
            case MARKING -> {
                if (phaseTicks % MARKER_INTERVAL == 0) {
                    markSpots(world);
                }
                openDuePortals(world);
                yield true;
            }
            case FIGHTING -> {
                if (phaseTicks == flankAt) {
                    openFlankPortal(world);
                }
                if (phaseTicks % 10 == 0) {
                    updateWave(world);
                }
                yield true;
            }
            case PAUSE -> {
                if (phaseTicks >= config.wave_pause_ticks) {
                    wave++;
                    enter(Phase.PLACING);
                }
                yield true;
            }
            case VICTORY -> {
                bar.setPercent(Math.min(1.0F, phaseTicks / (float) VICTORY_TICKS));
                yield phaseTicks < VICTORY_TICKS;
            }
        };
    }

    private void enter(Phase next) {
        phase = next;
        phaseTicks = 0;
    }

    private boolean isGeneralWave() {
        return config.wave_mobs == null || wave >= config.wave_mobs.size();
    }

    private boolean placePortals(ServerWorld world, ServerPlayerEntity player) {
        if (phaseTicks % 20 != 1) {
            return phaseTicks < PLACEMENT_RETRY_TICKS;
        }
        center = player.getPos();
        int extraPlayers = Math.max(0, bar.getPlayers().size() - 1);
        int portalCount = portalsPerWave + Math.min(config.max_extra_portals, extraPlayers * config.extra_portals_per_player);
        Vec3d anchor = randomParticipant(world, player).getPos();
        double radius = config.portal_radius * (0.7 + world.getRandom().nextDouble() * 0.6);
        spots = PortalPlacement.findSpots(world, anchor, world.getRandom(), isGeneralWave() ? 1 : portalCount, radius, null, true, true);
        if (spots.isEmpty()) {
            return true;
        }
        prepareOpenings(world);
        bar.setName(Text.translatable(NAME_KEY).append(" - ").append(Text.translatable(WAVE_KEY, wave + 1, totalWaves)));
        bar.setPercent(1.0F);
        markSpots(world);
        enter(Phase.MARKING);
        return true;
    }

    private void markSpots(ServerWorld world) {
        for (int i = opened; i < spots.size(); i++) {
            MrpgFx.play(world, LNEFx.PORTAL_MARKER, FxParams.at(spots.get(i)));
        }
    }

    // Portals open one after another at random times, so a wave doesn't pop up all at once.
    private void prepareOpenings(ServerWorld world) {
        portals.clear();
        seenMobs.clear();
        expectedMobs = 0;
        opened = 0;
        boolean general = isGeneralWave();
        portalLifetime = general ? GENERAL_PORTAL_LIFETIME : config.portal_lifetime_ticks;
        openDelays.clear();
        int stagger = general ? 0 : Math.max(0, config.portal_open_stagger_ticks);
        for (int i = 0; i < spots.size(); i++) {
            openDelays.add(i == 0 || stagger == 0 ? 0 : world.getRandom().nextInt(stagger + 1));
        }
        openDelays.sort(Integer::compareTo);
        List<EntityType<?>> types = new ArrayList<>(general ? List.of(randomGeneral(world)) : waveTypes());
        Collections.rotate(types, world.getRandom().nextInt(types.size()));
        spotTypes = types;
        boolean flank = !general && world.getRandom().nextFloat() < config.flank_portal_chance;
        flankAt = flank ? portalLifetime / 3 + world.getRandom().nextInt(Math.max(1, portalLifetime / 3)) : -1;
    }

    private void openDuePortals(ServerWorld world) {
        boolean general = isGeneralWave();
        while (opened < spots.size() && phaseTicks >= MARKING_TICKS + openDelays.get(opened)) {
            openPortal(world, spots.get(opened), spotTypes.get(opened % spotTypes.size()), portalLifetime, general);
            opened++;
        }
        if (opened >= spots.size()) {
            enter(Phase.FIGHTING);
        }
    }

    private void openPortal(ServerWorld world, Vec3d spot, EntityType<?> type, int lifetime, boolean general) {
        float jitter = 0.85F + world.getRandom().nextFloat() * 0.3F;
        int interval = Math.max(40, Math.round(config.portal_spawn_interval_ticks * jitter * (ominous ? config.ominous_spawn_interval_multiplier : 1.0F)));
        int firstDelay = general ? GENERAL_SPAWN_DELAY : PORTAL_FIRST_SPAWN_DELAY;
        EternalWinterPortalEntity portal = PortalPlacement.openAt(world, null, spot, type,
                lifetime, firstDelay, general ? lifetime : interval, true);
        portal.shielded(general ? lifetime : Math.min(lifetime, config.portal_shield_ticks))
                .empowering(ominous ? config.ominous_strength_bonus : 0.0, mobTag);
        portals.add(portal.getUuid());
        expectedMobs += general ? 1 : 1 + Math.max(0, lifetime - 1 - firstDelay) / interval;
    }

    // Sometimes an extra portal opens behind a player in the middle of a wave.
    private void openFlankPortal(ServerWorld world) {
        ServerPlayerEntity target = randomParticipant(world, null);
        if (target == null || spotTypes.isEmpty()) {
            return;
        }
        Vec3d facing = target.getRotationVector().multiply(1.0, 0.0, 1.0);
        Vec3d back = facing.lengthSquared() < 1.0E-4 ? Vec3d.ZERO : facing.normalize().multiply(FLANK_DISTANCE);
        List<Vec3d> flank = PortalPlacement.findSpots(world, target.getPos().subtract(back), world.getRandom(), 1, FLANK_RADIUS, null, true, true);
        if (!flank.isEmpty()) {
            EntityType<?> type = spotTypes.get(world.getRandom().nextInt(spotTypes.size()));
            openPortal(world, flank.get(0), type, Math.max(PORTAL_FIRST_SPAWN_DELAY + 1, portalLifetime - flankAt), false);
        }
    }

    private ServerPlayerEntity randomParticipant(ServerWorld world, ServerPlayerEntity fallback) {
        List<ServerPlayerEntity> players = bar.getPlayers().stream()
                .filter(player -> player.isAlive() && !player.isSpectator() && player.getWorld() == world)
                .toList();
        return players.isEmpty() ? fallback : players.get(world.getRandom().nextInt(players.size()));
    }

    private List<EntityType<?>> waveTypes() {
        List<EntityType<?>> types = new ArrayList<>();
        List<String> ids = config.wave_mobs.get(wave);
        for (String id : ids != null ? ids : List.<String>of()) {
            if (id != null) types.add(PortalPlacement.resolveType(id, ModEntities.FROST_HAUNTED));
        }
        return types.isEmpty() ? List.of(ModEntities.FROST_HAUNTED) : types;
    }

    private static EntityType<?> randomGeneral(ServerWorld world) {
        List<EntityType<?>> generals = Registries.ENTITY_TYPE.getEntryList(LneTags.GENERALS)
                .map(list -> list.stream().<EntityType<?>>map(entry -> entry.value()).toList())
                .orElse(List.of());
        return generals.isEmpty() ? ModEntities.GENERAL_MONARCHS_SOLDIER : generals.get(world.getRandom().nextInt(generals.size()));
    }

    private void updateWave(ServerWorld world) {
        portals.removeIf(id -> !(world.getEntity(id) instanceof EternalWinterPortalEntity portal) || !portal.isAlive());
        List<MobEntity> alive = world.getEntitiesByClass(MobEntity.class, Box.of(center, MOB_SCAN_RANGE * 2, MOB_SCAN_RANGE * 2, MOB_SCAN_RANGE * 2),
                mob -> mob.isAlive() && mob.getCommandTags().contains(mobTag));
        for (MobEntity mob : alive) {
            if (seenMobs.add(mob.getUuid())) {
                widenFollowRange(mob);
            }
            huntNearestPlayer(world, mob);
        }
        if (portals.isEmpty()) {
            expectedMobs = seenMobs.size();
        }
        int remaining = Math.max(0, expectedMobs - seenMobs.size()) + alive.size();
        bar.setPercent(expectedMobs <= 0 ? 0.0F : Math.min(1.0F, remaining / (float) expectedMobs));

        boolean cleared = portals.isEmpty() && alive.isEmpty();
        if (!cleared && phaseTicks > portalLifetime + STRAGGLER_TICKS) {
            alive.forEach(mob -> mob.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0, false, false)));
        }
        if (cleared || phaseTicks > portalLifetime + WAVE_TIMEOUT_TICKS) {
            if (wave + 1 >= totalWaves) {
                bar.setName(Text.translatable(NAME_KEY).append(" - ").append(Text.translatable(VICTORY_KEY)));
                enter(Phase.VICTORY);
                rewardPlayers(world);
            } else {
                enter(Phase.PAUSE);
            }
        }
    }

    private void widenFollowRange(MobEntity mob) {
        var followRange = mob.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE);
        if (followRange != null && followRange.getBaseValue() < config.target_range) {
            followRange.setBaseValue(config.target_range);
        }
    }

    private void huntNearestPlayer(ServerWorld world, MobEntity mob) {
        LivingEntity current = mob.getTarget();
        double range = config.target_range;
        if (current instanceof PlayerEntity player && player.isAlive() && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(player)
                && mob.squaredDistanceTo(player) <= range * range) {
            return;
        }
        PlayerEntity nearest = world.getClosestPlayer(mob.getX(), mob.getY(), mob.getZ(), range, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
        if (nearest != null) {
            mob.setTarget(nearest);
        }
    }

    private void rewardPlayers(ServerWorld world) {
        LootTable table = world.getServer().getReloadableRegistries().getLootTable(ominous ? LneLootTables.FROSTSIEGE_REWARD_OMINOUS : LneLootTables.FROSTSIEGE_REWARD);
        int experience = Math.max(0, config.reward_experience_per_wave) * totalWaves;
        for (ServerPlayerEntity player : List.copyOf(bar.getPlayers())) {
            if (!player.isAlive() || player.isSpectator()) {
                continue;
            }
            LootContextParameterSet parameters = new LootContextParameterSet.Builder(world)
                    .add(LootContextParameters.ORIGIN, player.getPos())
                    .add(LootContextParameters.THIS_ENTITY, player)
                    .luck(player.getLuck())
                    .build(LootContextTypes.CHEST);
            for (ItemStack stack : table.generateLoot(parameters)) {
                player.getInventory().offerOrDrop(stack);
            }
            if (experience > 0) {
                ExperienceOrbEntity.spawn(world, player.getPos(), experience);
            }
            player.playSoundToPlayer(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 0.8F, 1.0F);
        }
    }

    private void syncBarPlayers(ServerWorld world, ServerPlayerEntity owner) {
        Set<ServerPlayerEntity> nearby = new HashSet<>(world.getPlayers(player -> player.getPos().squaredDistanceTo(center) <= BAR_RANGE * BAR_RANGE));
        nearby.add(owner);
        for (ServerPlayerEntity player : List.copyOf(bar.getPlayers())) {
            if (!nearby.contains(player)) {
                bar.removePlayer(player);
            }
        }
        nearby.forEach(bar::addPlayer);
    }

    boolean includes(ServerPlayerEntity player) {
        return player.getUuid().equals(playerId) || bar.getPlayers().contains(player);
    }

    void end(ServerWorld world) {
        bar.clearPlayers();
        for (UUID id : portals) {
            Entity entity = world.getEntity(id);
            if (entity instanceof EternalWinterPortalEntity portal && portal.isAlive()) {
                portal.close(false);
            }
        }
        portals.clear();
    }
}

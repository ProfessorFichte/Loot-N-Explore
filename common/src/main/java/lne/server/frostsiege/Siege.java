package lne.server.frostsiege;

import com.mrpg_lib.fx.FxParams;
import com.mrpg_lib.fx.MrpgFx;
import lne.config.FrostsiegeConfig;
import lne.entity.ModEntities;
import lne.entity.frozen_depths.conjured.EternalWinterPortalEntity;
import lne.entity.frozen_depths.conjured.PortalPlacement;
import lne.internal.fx.LNEFx;
import lne.util.LneTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;

import java.util.ArrayList;
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
                if (phaseTicks >= MARKING_TICKS) {
                    openPortals(world);
                }
                yield true;
            }
            case FIGHTING -> {
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
        spots = PortalPlacement.findSpots(world, center, world.getRandom(), isGeneralWave() ? 1 : portalCount, config.portal_radius, null, true, true);
        if (spots.isEmpty()) {
            return true;
        }
        bar.setName(Text.translatable(NAME_KEY).append(" - ").append(Text.translatable(WAVE_KEY, wave + 1, totalWaves)));
        bar.setPercent(1.0F);
        markSpots(world);
        enter(Phase.MARKING);
        return true;
    }

    private void markSpots(ServerWorld world) {
        for (Vec3d spot : spots) {
            MrpgFx.play(world, LNEFx.PORTAL_MARKER, FxParams.at(spot));
        }
    }

    private void openPortals(ServerWorld world) {
        portals.clear();
        seenMobs.clear();
        boolean general = isGeneralWave();
        List<EntityType<?>> types = general ? List.of(randomGeneral(world)) : waveTypes();
        int interval = Math.max(40, Math.round(config.portal_spawn_interval_ticks * (ominous ? config.ominous_spawn_interval_multiplier : 1.0F)));
        portalLifetime = general ? GENERAL_PORTAL_LIFETIME : config.portal_lifetime_ticks;
        int firstDelay = general ? GENERAL_SPAWN_DELAY : PORTAL_FIRST_SPAWN_DELAY;
        int spawnsPerPortal = general ? 1 : 1 + Math.max(0, portalLifetime - 1 - firstDelay) / interval;
        for (int i = 0; i < spots.size(); i++) {
            EternalWinterPortalEntity portal = PortalPlacement.openAt(world, null, spots.get(i), types.get(i % types.size()),
                    portalLifetime, firstDelay, general ? portalLifetime : interval, true);
            portal.shielded(general ? portalLifetime : config.portal_shield_ticks)
                    .empowering(ominous ? config.ominous_strength_bonus : 0.0, mobTag);
            portals.add(portal.getUuid());
        }
        expectedMobs = spots.size() * spawnsPerPortal;
        enter(Phase.FIGHTING);
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
        alive.forEach(mob -> seenMobs.add(mob.getUuid()));
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
            } else {
                enter(Phase.PAUSE);
            }
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

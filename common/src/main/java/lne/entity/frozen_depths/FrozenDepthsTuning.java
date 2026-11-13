package lne.entity.frozen_depths;

import com.mrpg_lib.entity.config.EntityAttributeConfig.Entry;
import com.mrpg_lib.entity.config.EntityAttributeConfig.EquipmentValue;
import net.minecraft.entity.EquipmentSlot;

public final class FrozenDepthsTuning {
    public static final String MELEE_ENABLED = "melee_enabled";
    public static final String BETTER_COMBAT_ENABLED = "better_combat_enabled";
    public static final String WEAPON_SKILLS_ENABLED = "weapon_skills_enabled";
    public static final String SPELL_CASTING_ENABLED = "spell_casting_enabled";
    public static final String SPELL_CASTING_COOLDOWN = "spell_casting_cooldown_ticks";
    public static final String PRIMARY_SPELL_ENABLED = "primary_spell_enabled";
    public static final String PRIMARY_SPELL_COOLDOWN = "primary_spell_cooldown_ticks";
    public static final String COMBAT_ROLL_ENABLED = "combat_roll_enabled";

    public static final String SCREECH_ENABLED = "screech_enabled";
    public static final String SCREECH_COOLDOWN = "screech_cooldown_ticks";
    public static final String SCREECH_DAMAGE_MULTIPLIER = "screech_damage_multiplier";
    public static final String SCREECH_HEALTH_THRESHOLD = "screech_health_threshold";
    public static final String SCREECH_RANGE = "screech_range";
    public static final String ABILITY_PAUSE = "ability_pause_ticks";
    public static final String HAILSTORM_ENABLED = "hailstorm_enabled";
    public static final String HAILSTORM_COOLDOWN = "hailstorm_cooldown_ticks";
    public static final String HAILSTORM_HAILSTONES_PER_TARGET = "hailstorm_hailstones_per_target";
    public static final String HAILSTORM_RANDOM_HAILSTONES = "hailstorm_random_hailstones";
    public static final String ICICLE_ATTACK_ENABLED = "icicle_attack_enabled";
    public static final String ICICLE_ATTACK_COOLDOWN = "icicle_attack_cooldown_ticks";
    public static final String DISTANCE_ICICLE_ENABLED = "distance_icicle_enabled";
    public static final String DISTANCE_ICICLE_COOLDOWN = "distance_icicle_cooldown_ticks";
    public static final String GROUND_ICICLE_TELEGRAPH = "ground_icicle_telegraph_ticks";
    public static final String BASIC_ICICLE_ENABLED = "basic_icicle_enabled";
    public static final String BASIC_ICICLE_COOLDOWN = "basic_icicle_cooldown_ticks";
    public static final String ICE_WALL_ENABLED = "ice_wall_enabled";
    public static final String ICE_WALL_COOLDOWN = "ice_wall_cooldown_ticks";
    public static final String ICE_WALL_DURATION = "ice_wall_duration_ticks";
    public static final String ICE_WALL_SEGMENTS = "ice_wall_segments";
    public static final String ICE_WALL_DISTANCE = "ice_wall_distance";
    public static final String ICE_WALL_DETECTION_RANGE = "ice_wall_detection_range";
    public static final String BARRIER_ENABLED = "barrier_enabled";
    public static final String BARRIER_USES = "barrier_uses";
    public static final String PORTAL_SPAWN_RADIUS = "portal_spawn_radius";
    public static final String PORTAL_FIRST_SPAWN_DELAY = "portal_first_spawn_delay_ticks";
    public static final String PORTAL_SPAWN_INTERVAL = "portal_spawn_interval_ticks";

    public static final String PORTAL_ENABLED = "portal_enabled";
    public static final String PORTAL_COOLDOWN = "portal_cooldown_ticks";
    public static final String PORTAL_HEALTH_THRESHOLD = "portal_health_threshold";
    public static final String PORTAL_COUNT = "portal_count";
    public static final String PORTAL_ENTITY = "portal_entity";
    public static final String PORTAL_LIFETIME = "portal_lifetime_ticks";

    public static final String FREEZE_TICKS_ON_HIT = "freeze_ticks_on_hit";

    public static final String FROSTBALL_VOLLEY_ENABLED = "frostball_volley_enabled";
    public static final String FROSTBALL_VOLLEY_COOLDOWN = "frostball_volley_cooldown_ticks";
    public static final String FROST_STORM_ENABLED = "frost_storm_enabled";
    public static final String FROST_STORM_COOLDOWN = "frost_storm_cooldown_ticks";
    public static final String FROST_STORM_DAMAGE_MULTIPLIER = "frost_storm_damage_multiplier";

    public static final String DASH_ENABLED = "dash_enabled";
    public static final String DASH_COOLDOWN = "dash_cooldown_ticks";
    public static final String DASH_RANGE = "dash_range";
    public static final String SPEAR_THROW_ENABLED = "spear_throw_enabled";
    public static final String SPEAR_THROW_COOLDOWN = "spear_throw_cooldown_ticks";
    public static final String SPEAR_THROW_RANGE = "spear_throw_range";
    public static final String STAB_ENABLED = "stab_enabled";
    public static final String STAB_COOLDOWN = "stab_cooldown_ticks";

    public static final String AOE_SWING_ENABLED = "aoe_swing_enabled";
    public static final String AOE_SWING_RANGE = "aoe_swing_range";
    public static final String AOE_SWING_COOLDOWN = "aoe_swing_cooldown_ticks";
    public static final String SHIELD_BLOCK_ENABLED = "shield_block_enabled";
    public static final String SHIELD_RAISE_CHANCE = "shield_raise_chance_per_tick";
    public static final String SHIELD_BLOCK_DURATION = "shield_block_duration_ticks";
    public static final String SHIELD_BLOCK_COOLDOWN = "shield_block_cooldown_ticks";

    public static final String SALVO_ENABLED = "salvo_enabled";
    public static final String SALVO_ARROW_COUNT = "salvo_arrow_count";
    public static final String SALVO_COOLDOWN = "salvo_cooldown_ticks";
    public static final String SHOT_ENABLED = "shot_enabled";
    public static final String SHOT_COOLDOWN = "shot_cooldown_ticks";
    public static final String ATTACK_RANGE = "attack_range";
    public static final String DODGE_ENABLED = "dodge_enabled";
    public static final String DODGE_CHANCE = "dodge_chance";

    public static final String CLOSE_ICICLE_ENABLED = "close_icicle_enabled";
    public static final String GROUND_ICICLE_COUNT = "ground_icicle_count";
    public static final String AIR_ICICLE_COUNT = "air_icicle_count";
    public static final String CLOSE_ICICLE_COOLDOWN = "close_icicle_cooldown_ticks";
    public static final String ICICLE_FORMATION_ENABLED = "icicle_formation_enabled";
    public static final String ICICLE_FORMATION_COOLDOWN = "icicle_formation_cooldown_ticks";
    public static final String ICICLE_FORMATION_BREAK_TICKS = "icicle_formation_break_ticks";
    public static final String SINGLE_ICICLE_ENABLED = "single_icicle_enabled";
    public static final String SINGLE_ICICLE_COOLDOWN = "single_icicle_cooldown_ticks";
    public static final String RETREAT_SPEED_MULTIPLIER = "retreat_speed_multiplier";
    public static final String FROST_BLINK_ENABLED = "frost_blink_enabled";
    public static final String FROST_BLINK_COOLDOWN = "frost_blink_cooldown_ticks";
    public static final String FROST_DECOY_FUSE = "frost_decoy_fuse_ticks";
    public static final String FROST_DECOY_RADIUS = "frost_decoy_radius";
    public static final String FROST_DECOY_DAMAGE_MULTIPLIER = "frost_decoy_damage_multiplier";

    public static final String FROST_HOUND = "loot_n_explore:frost_hound";
    public static final String FROST_HAUNTED = "loot_n_explore:frost_haunted";
    public static final String MONARCHS_SOLDIER = "loot_n_explore:monarchs_soldier";
    public static final String MONARCHS_GUARD = "loot_n_explore:monarchs_guard";
    public static final String GENERAL_MONARCHS_GUARD = "loot_n_explore:general_monarchs_guard";

    public static final String FROST_HAUNT_AXE = "loot_n_explore:frost_haunt_axe";
    public static final String GUARDS_FROST_LANCE = "loot_n_explore:guards_frost_lance";
    public static final String FROZEN_BOW = "loot_n_explore:frozen_bow";

    private FrozenDepthsTuning() {
    }

    public static String barrierPortalCount(int phase) {
        return "barrier_" + (phase + 1) + "_portal_count";
    }

    public static String barrierPortalEntity(int phase) {
        return "barrier_" + (phase + 1) + "_portal_entity";
    }

    public static Entry frostMonarch(Entry entry) {
        return entry
                .setCustom(MELEE_ENABLED, 1)
                .setCustom(BETTER_COMBAT_ENABLED, 1)
                .setCustom(PRIMARY_SPELL_ENABLED, 1)
                .setCustom(SPELL_CASTING_ENABLED, 1)
                .setCustom(SPELL_CASTING_COOLDOWN, 240)
                .setCustom(ABILITY_PAUSE, 40)
                .setCustom(SCREECH_ENABLED, 1)
                .setCustom(SCREECH_COOLDOWN, 400)
                .setCustom(SCREECH_DAMAGE_MULTIPLIER, 0.1)
                .setCustom(SCREECH_HEALTH_THRESHOLD, 0.5)
                .setCustom(SCREECH_RANGE, 16.0)
                .setCustom(HAILSTORM_ENABLED, 1)
                .setCustom(HAILSTORM_COOLDOWN, 400)
                .setCustom(HAILSTORM_HAILSTONES_PER_TARGET, 8)
                .setCustom(HAILSTORM_RANDOM_HAILSTONES, 40)
                .setCustom(ICICLE_ATTACK_ENABLED, 1)
                .setCustom(ICICLE_ATTACK_COOLDOWN, 200)
                .setCustom(DISTANCE_ICICLE_ENABLED, 1)
                .setCustom(DISTANCE_ICICLE_COOLDOWN, 250)
                .setCustom(GROUND_ICICLE_TELEGRAPH, 8)
                .setCustom(BASIC_ICICLE_ENABLED, 1)
                .setCustom(BASIC_ICICLE_COOLDOWN, 160)
                .setCustom(ICE_WALL_ENABLED, 1)
                .setCustom(ICE_WALL_COOLDOWN, 300)
                .setCustom(ICE_WALL_DURATION, 100)
                .setCustom(ICE_WALL_SEGMENTS, 5)
                .setCustom(ICE_WALL_DISTANCE, 5.0)
                .setCustom(ICE_WALL_DETECTION_RANGE, 32.0)
                .setCustom(BARRIER_ENABLED, 1)
                .setCustom(BARRIER_USES, 3)
                .setCustom(PORTAL_SPAWN_RADIUS, 25.0)
                .setCustom(PORTAL_LIFETIME, 400)
                .setCustom(PORTAL_FIRST_SPAWN_DELAY, 20)
                .setCustom(PORTAL_SPAWN_INTERVAL, 100)
                .setCustom(barrierPortalCount(0), 1)
                .setCustomString(barrierPortalEntity(0), FROST_HOUND, FROST_HAUNTED, MONARCHS_SOLDIER)
                .setCustom(barrierPortalCount(1), 2)
                .setCustomString(barrierPortalEntity(1), FROST_HAUNTED, MONARCHS_SOLDIER, MONARCHS_GUARD)
                .setCustom(barrierPortalCount(2), 2, 1.5, 1.5)
                .setCustomString(barrierPortalEntity(2), MONARCHS_GUARD, MONARCHS_GUARD, GENERAL_MONARCHS_GUARD);
    }

    public static Entry frostHaunted(Entry entry) {
        return entry
                .setCustom(MELEE_ENABLED, 1)
                .setCustom(BETTER_COMBAT_ENABLED, 1)
                .setCustom(WEAPON_SKILLS_ENABLED, 1)
                .setCustom(FREEZE_TICKS_ON_HIT, 20)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem(FROST_HAUNT_AXE));
    }

    public static Entry frostHound(Entry entry) {
        return entry
                .setCustom(MELEE_ENABLED, 1);
    }

    public static Entry glaze(Entry entry) {
        return entry
                .setCustom(MELEE_ENABLED, 1)
                .setCustom(FROSTBALL_VOLLEY_ENABLED, 1)
                .setCustom(FROSTBALL_VOLLEY_COOLDOWN, 100)
                .setCustom(FROST_STORM_ENABLED, 1)
                .setCustom(FROST_STORM_COOLDOWN, 600)
                .setCustom(FROST_STORM_DAMAGE_MULTIPLIER, 0.3)
                .setCustom(FREEZE_TICKS_ON_HIT, 20);
    }

    public static Entry monarchsGuard(Entry entry) {
        return guard(entry, 60, 120, 30);
    }

    public static Entry generalMonarchsGuard(Entry entry) {
        return guard(entry, 45, 90, 24)
                .setCustom(SPELL_CASTING_ENABLED, 1)
                .setCustom(SPELL_CASTING_COOLDOWN, 140);
    }

    private static Entry guard(Entry entry, int dashCooldown, int spearThrowCooldown, int stabCooldown) {
        return entry
                .setCustom(BETTER_COMBAT_ENABLED, 1)
                .setCustom(WEAPON_SKILLS_ENABLED, 1)
                .setCustom(COMBAT_ROLL_ENABLED, 1)
                .setCustom(DASH_ENABLED, 1)
                .setCustom(DASH_COOLDOWN, dashCooldown)
                .setCustom(DASH_RANGE, 12)
                .setCustom(SPEAR_THROW_ENABLED, 1)
                .setCustom(SPEAR_THROW_COOLDOWN, spearThrowCooldown)
                .setCustom(SPEAR_THROW_RANGE, 32)
                .setCustom(STAB_ENABLED, 1)
                .setCustom(STAB_COOLDOWN, stabCooldown)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem(GUARDS_FROST_LANCE));
    }

    public static Entry monarchsSoldier(Entry entry) {
        return soldier(entry, 3, 200);
    }

    public static Entry generalMonarchsSoldier(Entry entry) {
        return soldier(entry, 4, 120);
    }

    private static Entry soldier(Entry entry, double aoeSwingRange, int spellCooldown) {
        return entry
                .setCustom(MELEE_ENABLED, 1)
                .setCustom(SPELL_CASTING_ENABLED, 1)
                .setCustom(SPELL_CASTING_COOLDOWN, spellCooldown)
                .setCustom(BETTER_COMBAT_ENABLED, 1)
                .setCustom(WEAPON_SKILLS_ENABLED, 1)
                .setCustom(AOE_SWING_ENABLED, 1)
                .setCustom(AOE_SWING_RANGE, aoeSwingRange)
                .setCustom(AOE_SWING_COOLDOWN, 80)
                .setCustom(SHIELD_BLOCK_ENABLED, 1)
                .setCustom(SHIELD_RAISE_CHANCE, 0.08)
                .setCustom(SHIELD_BLOCK_DURATION, 40)
                .setCustom(SHIELD_BLOCK_COOLDOWN, 60)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem("minecraft:iron_sword"))
                .setEquipment(EquipmentSlot.OFFHAND, keptItem("minecraft:shield"));
    }

    public static Entry frozenRanger(Entry entry) {
        return ranger(entry, 5, 200);
    }

    public static Entry generalFrozenRanger(Entry entry) {
        return ranger(entry, 7, 140);
    }

    private static Entry ranger(Entry entry, int salvoArrowCount, int spellCooldown) {
        return entry
                .setCustom(COMBAT_ROLL_ENABLED, 1)
                .setCustom(SPELL_CASTING_ENABLED, 1)
                .setCustom(SPELL_CASTING_COOLDOWN, spellCooldown)
                .setCustom(DODGE_ENABLED, 1)
                .setCustom(DODGE_CHANCE, 0.05)
                .setCustom(SALVO_ENABLED, 1)
                .setCustom(SALVO_ARROW_COUNT, salvoArrowCount)
                .setCustom(SALVO_COOLDOWN, 100)
                .setCustom(SHOT_ENABLED, 1)
                .setCustom(SHOT_COOLDOWN, 20)
                .setCustom(ATTACK_RANGE, 20)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem(FROZEN_BOW));
    }

    public static Entry frozenMagician(Entry entry) {
        return mage(entry, 5, 3, 160);
    }

    public static Entry generalFrozenMagician(Entry entry) {
        return mage(entry, 7, 5, 140)
                .setCustom(PORTAL_ENABLED, 1)
                .setCustom(PORTAL_COOLDOWN, 1200)
                .setCustom(PORTAL_HEALTH_THRESHOLD, 0.3)
                .setCustom(PORTAL_COUNT, 1)
                .setCustomString(PORTAL_ENTITY, FROST_HOUND, FROST_HOUND, FROST_HAUNTED)
                .setCustom(PORTAL_LIFETIME, 400)
                .setCustom(PORTAL_FIRST_SPAWN_DELAY, 20)
                .setCustom(PORTAL_SPAWN_INTERVAL, 100)
                .setCustom(PORTAL_SPAWN_RADIUS, 10.0)
                .setCustom(FROST_BLINK_ENABLED, 1)
                .setCustom(FROST_BLINK_COOLDOWN, 240)
                .setCustom(FROST_DECOY_FUSE, 40)
                .setCustom(FROST_DECOY_RADIUS, 3.0)
                .setCustom(FROST_DECOY_DAMAGE_MULTIPLIER, 1.25);
    }

    private static Entry mage(Entry entry, int groundIcicles, int airIcicles, int formationCooldown) {
        return entry
                .setCustom(SPELL_CASTING_ENABLED, 1)
                .setCustom(SPELL_CASTING_COOLDOWN, 100)
                .setCustom(PRIMARY_SPELL_ENABLED, 1)
                .setCustom(PRIMARY_SPELL_COOLDOWN, 40)
                .setCustom(CLOSE_ICICLE_ENABLED, 1)
                .setCustom(GROUND_ICICLE_COUNT, groundIcicles)
                .setCustom(CLOSE_ICICLE_COOLDOWN, 60)
                .setCustom(GROUND_ICICLE_TELEGRAPH, 5)
                .setCustom(ICICLE_FORMATION_ENABLED, 1)
                .setCustom(AIR_ICICLE_COUNT, airIcicles)
                .setCustom(ICICLE_FORMATION_COOLDOWN, formationCooldown)
                .setCustom(ICICLE_FORMATION_BREAK_TICKS, 60)
                .setCustom(SINGLE_ICICLE_ENABLED, 1)
                .setCustom(SINGLE_ICICLE_COOLDOWN, 25)
                .setCustom(RETREAT_SPEED_MULTIPLIER, 1.6)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem("minecraft:snowball"));
    }

    private static EquipmentValue keptItem(String itemId) {
        return EquipmentValue.of(itemId).dropChance(0.0);
    }
}

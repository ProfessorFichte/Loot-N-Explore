package more_rpg_loot.entity.frozen_depths;

import com.mrpg_lib.entity.config.EntityAttributeConfig.Entry;
import com.mrpg_lib.entity.config.EntityAttributeConfig.EquipmentValue;
import net.minecraft.entity.EquipmentSlot;

public final class FrozenDepthsTuning {
    public static final String SCREECH_COOLDOWN = "screech_cooldown_ticks";
    public static final String SCREECH_DAMAGE_MULTIPLIER = "screech_damage_multiplier";
    public static final String HAILSTORM_COOLDOWN = "hailstorm_cooldown_ticks";
    public static final String HAILSTORM_HAILSTONES_PER_TARGET = "hailstorm_hailstones_per_target";
    public static final String HAILSTORM_RANDOM_HAILSTONES = "hailstorm_random_hailstones";
    public static final String ICICLE_ATTACK_COOLDOWN = "icicle_attack_cooldown_ticks";
    public static final String DISTANCE_ICICLE_COOLDOWN = "distance_icicle_cooldown_ticks";
    public static final String BARRIER_USES = "barrier_uses";
    public static final String BARRIER_SUMMON_COUNT = "barrier_summon_count";
    public static final String BARRIER_SUMMON_ENTITY = "barrier_summon_entity";

    public static final String FREEZE_TICKS_ON_HIT = "freeze_ticks_on_hit";

    public static final String FROSTBALL_VOLLEY_COOLDOWN = "frostball_volley_cooldown_ticks";
    public static final String FROST_STORM_COOLDOWN = "frost_storm_cooldown_ticks";
    public static final String FROST_STORM_DAMAGE_MULTIPLIER = "frost_storm_damage_multiplier";

    public static final String DASH_COOLDOWN = "dash_cooldown_ticks";
    public static final String DASH_RANGE = "dash_range";
    public static final String SPEAR_THROW_COOLDOWN = "spear_throw_cooldown_ticks";
    public static final String SPEAR_THROW_RANGE = "spear_throw_range";
    public static final String STAB_COOLDOWN = "stab_cooldown_ticks";

    public static final String AOE_SWING_RANGE = "aoe_swing_range";
    public static final String AOE_SWING_COOLDOWN = "aoe_swing_cooldown_ticks";
    public static final String SHIELD_RAISE_CHANCE = "shield_raise_chance_per_tick";
    public static final String SHIELD_BLOCK_DURATION = "shield_block_duration_ticks";
    public static final String SHIELD_BLOCK_COOLDOWN = "shield_block_cooldown_ticks";

    public static final String SALVO_ARROW_COUNT = "salvo_arrow_count";
    public static final String SALVO_COOLDOWN = "salvo_cooldown_ticks";
    public static final String SHOT_COOLDOWN = "shot_cooldown_ticks";
    public static final String ATTACK_RANGE = "attack_range";

    public static final String GROUND_ICICLE_COUNT = "ground_icicle_count";
    public static final String AIR_ICICLE_COUNT = "air_icicle_count";
    public static final String CLOSE_ICICLE_COOLDOWN = "close_icicle_cooldown_ticks";
    public static final String ICICLE_FORMATION_COOLDOWN = "icicle_formation_cooldown_ticks";
    public static final String SINGLE_ICICLE_COOLDOWN = "single_icicle_cooldown_ticks";

    public static final String FROST_HAUNT_AXE = "loot_n_explore:frost_haunt_axe";
    public static final String GUARDS_FROST_LANCE = "loot_n_explore:guards_frost_lance";
    public static final String FROZEN_BOW = "loot_n_explore:frozen_bow";

    private FrozenDepthsTuning() {
    }

    public static Entry frostMonarch(Entry entry) {
        return entry
                .setCustom(SCREECH_COOLDOWN, 300)
                .setCustom(SCREECH_DAMAGE_MULTIPLIER, 0.1)
                .setCustom(HAILSTORM_COOLDOWN, 400)
                .setCustom(HAILSTORM_HAILSTONES_PER_TARGET, 8)
                .setCustom(HAILSTORM_RANDOM_HAILSTONES, 40)
                .setCustom(ICICLE_ATTACK_COOLDOWN, 200)
                .setCustom(DISTANCE_ICICLE_COOLDOWN, 250)
                .setCustom(BARRIER_USES, 2, 1.5, 2.0)
                .setCustom(BARRIER_SUMMON_COUNT, 1, 2.0, 3.0)
                .setCustomString(BARRIER_SUMMON_ENTITY, "loot_n_explore:frost_haunt");
    }

    public static Entry frosthaunt(Entry entry) {
        return entry
                .setCustom(FREEZE_TICKS_ON_HIT, 20)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem(FROST_HAUNT_AXE));
    }

    public static Entry glaze(Entry entry) {
        return entry
                .setCustom(FROSTBALL_VOLLEY_COOLDOWN, 100)
                .setCustom(FROST_STORM_COOLDOWN, 600)
                .setCustom(FROST_STORM_DAMAGE_MULTIPLIER, 0.3)
                .setCustom(FREEZE_TICKS_ON_HIT, 20);
    }

    public static Entry monarchsGuard(Entry entry) {
        return entry
                .setCustom(DASH_COOLDOWN, 60)
                .setCustom(DASH_RANGE, 12)
                .setCustom(SPEAR_THROW_COOLDOWN, 120)
                .setCustom(SPEAR_THROW_RANGE, 32)
                .setCustom(STAB_COOLDOWN, 30)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem(GUARDS_FROST_LANCE));
    }

    public static Entry monarchsSoldier(Entry entry) {
        return soldier(entry, 3);
    }

    public static Entry generalMonarchsSoldier(Entry entry) {
        return soldier(entry, 4);
    }

    private static Entry soldier(Entry entry, double aoeSwingRange) {
        return entry
                .setCustom(AOE_SWING_RANGE, aoeSwingRange)
                .setCustom(AOE_SWING_COOLDOWN, 80)
                .setCustom(SHIELD_RAISE_CHANCE, 0.08)
                .setCustom(SHIELD_BLOCK_DURATION, 40)
                .setCustom(SHIELD_BLOCK_COOLDOWN, 60)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem("minecraft:stone_sword"))
                .setEquipment(EquipmentSlot.OFFHAND, keptItem("minecraft:shield"));
    }

    public static Entry frostedRanger(Entry entry) {
        return ranger(entry, 5);
    }

    public static Entry generalFrostedRanger(Entry entry) {
        return ranger(entry, 7);
    }

    private static Entry ranger(Entry entry, int salvoArrowCount) {
        return entry
                .setCustom(SALVO_ARROW_COUNT, salvoArrowCount)
                .setCustom(SALVO_COOLDOWN, 100)
                .setCustom(SHOT_COOLDOWN, 20)
                .setCustom(ATTACK_RANGE, 20)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem(FROZEN_BOW));
    }

    public static Entry undeadFrozenMage(Entry entry) {
        return mage(entry, 5, 5);
    }

    public static Entry generalUndeadFrozenMage(Entry entry) {
        return mage(entry, 7, 8);
    }

    private static Entry mage(Entry entry, int groundIcicles, int airIcicles) {
        return entry
                .setCustom(GROUND_ICICLE_COUNT, groundIcicles)
                .setCustom(AIR_ICICLE_COUNT, airIcicles)
                .setCustom(CLOSE_ICICLE_COOLDOWN, 60)
                .setCustom(ICICLE_FORMATION_COOLDOWN, 80)
                .setCustom(SINGLE_ICICLE_COOLDOWN, 25)
                .setEquipment(EquipmentSlot.MAINHAND, keptItem("minecraft:snowball"));
    }

    private static EquipmentValue keptItem(String itemId) {
        return EquipmentValue.of(itemId).dropChance(0.0);
    }
}

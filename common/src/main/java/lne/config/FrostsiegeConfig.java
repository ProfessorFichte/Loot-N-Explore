package lne.config;

import java.util.ArrayList;
import java.util.List;

public class FrostsiegeConfig {
    public boolean enabled = true;
    public int check_interval_ticks = 1200;
    public float chance_easy = 0.01F;
    public float chance_normal = 0.02F;
    public float chance_hard = 0.03F;
    public float crown_ability_chance_bonus = 0.05F;
    public float max_chance = 0.5F;
    public int cooldown_ticks = 72000;
    public int warmup_ticks = 200;
    public int waves_easy = 3;
    public int waves_normal = 4;
    public int waves_hard = 5;
    public int portals_easy = 2;
    public int portals_normal = 3;
    public int portals_hard = 4;
    public int extra_portals_per_player = 1;
    public int max_extra_portals = 4;
    public double portal_radius = 16.0;
    public int portal_lifetime_ticks = 800;
    public int portal_shield_ticks = 400;
    public int portal_spawn_interval_ticks = 220;
    public int wave_pause_ticks = 100;
    public int portal_open_stagger_ticks = 80;
    public float flank_portal_chance = 0.35F;
    public double target_range = 40.0;
    public int reward_experience_per_wave = 10;
    public float ominous_spawn_interval_multiplier = 0.75F;
    public float ominous_strength_bonus = 0.25F;
    public boolean change_weather = true;
    public List<List<String>> wave_mobs = new ArrayList<>(List.of(
            new ArrayList<>(List.of("loot_n_explore:frost_hound")),
            new ArrayList<>(List.of("loot_n_explore:frost_haunted")),
            new ArrayList<>(List.of("loot_n_explore:frozen_ranger", "loot_n_explore:monarchs_soldier")),
            new ArrayList<>(List.of("loot_n_explore:frozen_magician", "loot_n_explore:monarchs_soldier"))));
}

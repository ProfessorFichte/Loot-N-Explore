package lne.item.weapons;

import java.util.LinkedHashMap;

public class WeaponAbilityConfig {
    public boolean ready_particles = true;
    public LinkedHashMap<String, Entry> abilities = new LinkedHashMap<>();
    public StaffIcicle monarchs_frost_staff = new StaffIcicle();
    public FrozenWeapons frozen_weapons = new FrozenWeapons();

    public static class Entry {
        public boolean enabled = true;
        public LinkedHashMap<String, Double> values = new LinkedHashMap<>();
    }

    public static class StaffIcicle {
        public boolean enabled = true;
        public int cooldown_ticks = 100;
        public double damage_multiplier = 1.0;
        public int freezing_ticks = 100;
    }

    public static class FrozenWeapons {
        public boolean enabled = true;
        public int freezing_ticks = 60;
        public int freezing_amplifier = 0;
    }
}

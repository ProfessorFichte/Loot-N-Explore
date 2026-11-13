package lne.item.weapons;

import net.tiny_config.ConfigManager;

import static lne.LneMod.MOD_ID;

public class WeaponAbilities {
    public static final ConfigManager<WeaponAbilityConfig> config = new ConfigManager<>("weapon_abilities_v2", new WeaponAbilityConfig())
            .builder()
            .setDirectory(MOD_ID)
            .sanitize(true)
            .build();

    public static void loadConfig() {
        config.refresh();
        for (var ability : WeaponAbility.values()) {
            for (var slot : WeaponAbility.Slot.values()) {
                var entry = config.value.abilities.computeIfAbsent(ability.key(slot), key -> new WeaponAbilityConfig.Entry());
                var defaults = ability.defaultValues(slot);
                entry.values.keySet().retainAll(defaults.keySet());
                defaults.forEach(entry.values::putIfAbsent);
            }
        }
        config.save();
    }
}

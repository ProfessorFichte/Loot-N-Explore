package more_rpg_loot.config;

import net.fabric_extras.ranged_weapon.api.RangedConfig;

import java.util.HashMap;
import java.util.Map;

public class ItemConfig { public ItemConfig() {}
    public Map<String, RangedConfig> ranged_weapons = new HashMap();
    public Map<String, net.spell_engine.api.item.ItemConfig.Weapon> melee_weapons = new HashMap();
    public Map<String, net.spell_engine.api.item.ItemConfig.ArmorSet> armor_sets = new HashMap();
}

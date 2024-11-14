package more_rpg_loot.item.weapons;

import net.minecraft.item.Item;

import java.util.HashMap;

public class ItemConfig {
    public static final HashMap<String, Item> entries;
    static {
        entries = new HashMap<>();
        for(var weaponEntry: WeaponsRegister.rangedEntries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
        for(var weaponEntry: WeaponsRegister.meleeEntries) {
            entries.put(weaponEntry.id().toString(), weaponEntry.item());
        }
    }
}
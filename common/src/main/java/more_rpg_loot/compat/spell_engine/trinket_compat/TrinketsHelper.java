package more_rpg_loot.compat.spell_engine.trinket_compat;

import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.relics.LNE_RelicItems;

public class TrinketsHelper {
    public static void register() {
        LNE_Relics.factory = args -> new LNETrinketItem(args.settings(), args.attributes());
        LNE_RelicItems.factory = (settings, attrs) -> new LNETrinketItem(settings, attrs);
    }
}

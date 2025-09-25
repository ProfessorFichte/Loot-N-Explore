package more_rpg_loot.compat.spell_engine.trinket_compat;

import more_rpg_loot.compat.spell_engine.LNE_Relics;

public class TrinketsHelper {
    public static void register() {
        LNE_Relics.factory = args -> new LNETrinketItem(args.settings(), args.attributes());
    }
}

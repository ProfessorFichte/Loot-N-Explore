package com.lne.neoforge.compat.curios;

import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.relics.LNE_RelicItems;

public class CuriosHelper {
    public static void register() {
        LNE_Relics.factory = args -> new LNE_RelicCurioItem(args.settings(), args.attributes());
        LNE_RelicItems.factory = LNE_RelicCurioItem::new;
    }
}

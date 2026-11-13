package com.lne.neoforge.compat.curios;

import lne.item.relics.LNE_RelicItems;

public class CuriosHelper {
    public static void register() {
        LNE_RelicItems.factory = LNE_RelicCurioItem::new;
    }
}

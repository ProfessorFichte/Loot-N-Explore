package lne.compat.trinkets;

import lne.item.relics.LNE_RelicItems;

public class TrinketsHelper {
    public static void register() {
        LNE_RelicItems.factory = LNETrinketItem::new;
    }
}

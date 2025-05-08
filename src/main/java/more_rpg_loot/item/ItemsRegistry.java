package more_rpg_loot.item;

import more_rpg_loot.compat.spell_engine.SmithingTemplates;

public class ItemsRegistry {

    public static void registerModItems() {
        ModPotions.registerPotions();
        ModPotions.registerPotionsRecipes();
        CommonItems.registerCommonItems();
    }

}

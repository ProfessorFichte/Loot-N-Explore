package more_rpg_loot.item;

public class ItemsRegistry {

    public static void registerModItems() {
        ModPotions.registerPotions();
        ModPotions.registerPotionsRecipes();
        SmithingTemplates.registerSmithingUpgrades();
        CommonItems.registerCommonItems();
    }

}

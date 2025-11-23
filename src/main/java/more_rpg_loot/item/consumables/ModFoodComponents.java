package more_rpg_loot.item.consumables;

import net.minecraft.component.type.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent INN_BOWL = new FoodComponent.Builder().nutrition(6).saturationModifier(0.5f).build();
    public static final FoodComponent INN_FOOD = new FoodComponent.Builder().nutrition(10).saturationModifier(0.75f).build();
}

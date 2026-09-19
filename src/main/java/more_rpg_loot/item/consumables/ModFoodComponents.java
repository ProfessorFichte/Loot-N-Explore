package more_rpg_loot.item.consumables;

// 1.20.1: FoodComponent lives in net.minecraft.item and its builder uses hunger/saturationModifier.
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent INN_BOWL = new FoodComponent.Builder().hunger(6).saturationModifier(0.5f).build();
}

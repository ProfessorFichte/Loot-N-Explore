package more_rpg_loot.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/// 1.20.1: there is no `FabricBrewingRecipeRegistryBuilder`, and
/// `BrewingRecipeRegistry#registerPotionRecipe` is private. Reach it with an invoker, exactly as
/// this mod's own legacy 1.20.1 branch did.
@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryMixin {

    @Invoker("registerPotionRecipe")
    static void invokeRegisterPotionRecipe(Potion input, Item item, Potion output){
    }
}

package more_rpg_loot.item.items;

import more_rpg_loot.effects.Effects;
import more_rpg_loot.mixin.BrewingRecipeRegistryMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static net.minecraft.registry.Registries.POTION;

public class ModPotions {
    public static final Potion FROST_RESISTANCE_POTION =
            Registry.register(POTION, new Identifier(MOD_ID, "frost_resistance_potion"),
                    new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE, 3600, 0)));
    public static final Potion LONG_FROST_RESISTANCE_POTION =
            Registry.register(POTION, new Identifier(MOD_ID, "long_frost_resistance_potion"),
                    new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE, 9600, 0)));

    public static void registerPotions(){

    }

    public static void registerPotionsRecipes(){
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, more_rpg_loot.item.Items.GLAZE_ROD, ModPotions.FROST_RESISTANCE_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.FROST_RESISTANCE_POTION, Items.REDSTONE, ModPotions.LONG_FROST_RESISTANCE_POTION);
    }
}

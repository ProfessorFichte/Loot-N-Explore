package more_rpg_loot.compat.items;

import more_rpg_loot.item.CommonItems;
import more_rpg_loot.mixin.BrewingRecipeRegistryMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.effect.MRPGCEffects;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static net.minecraft.registry.Registries.POTION;

public class MRPGCPotions {
    public static final Potion FROSTED_POTION =
            Registry.register(POTION, new Identifier(MOD_ID, "frosted_potion"),
                    new Potion(new StatusEffectInstance(MRPGCEffects.FROSTED, 900, 0)));
    public static final Potion LONG_FROSTED_POTION =
            Registry.register(POTION, new Identifier(MOD_ID, "long_frosted_potion"),
                    new Potion(new StatusEffectInstance(MRPGCEffects.FROSTED, 1800, 1)));

    public static void registerPotions(){

    }

    public static void registerPotionsRecipes(){
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, CommonItems.GLAZE_ROD, FROSTED_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(FROSTED_POTION, Items.REDSTONE, LONG_FROSTED_POTION);
    }
}

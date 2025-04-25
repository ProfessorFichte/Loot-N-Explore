package more_rpg_loot.item;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.effects.Effects;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModPotions {

    public static final RegistryEntry<Potion> FROST_RESISTANCE_POTION = registerPotion("frost_resistance_potion",
            new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE.registryEntry, 180, 0)));
    public static final RegistryEntry<Potion> LONG_FROST_RESISTANCE_POTION = registerPotion("long_frost_resistance_potion",
            new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE.registryEntry, 480, 0)));
    public static final RegistryEntry<Potion> FROSTED_POTION = registerPotion("frosted_potion",
            new Potion(new StatusEffectInstance(Effects.FREEZING.registryEntry, 45, 0)));
    public static final RegistryEntry<Potion> LONG_FROSTED_POTION = registerPotion("long_frosted_potion",
            new Potion(new StatusEffectInstance(Effects.FREEZING.registryEntry, 90, 1)));

    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(MOD_ID, name), potion);
    }

    public static void registerPotions(){
        RPGLoot.LOGGER.info("Registering Potions for " + MOD_ID);
    }


    public static void registerPotionsRecipes(){
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ModBlocks.FROST_BLOOM.item(), ModPotions.FROST_RESISTANCE_POTION);
            builder.registerPotionRecipe(ModPotions.FROST_RESISTANCE_POTION, Items.REDSTONE, ModPotions.LONG_FROST_RESISTANCE_POTION);
            builder.registerPotionRecipe(Potions.AWKWARD, CommonItems.GLAZE_ROD, ModPotions.FROSTED_POTION);
            builder.registerPotionRecipe(FROSTED_POTION, Items.REDSTONE, ModPotions.LONG_FROSTED_POTION);
        });
    }
}

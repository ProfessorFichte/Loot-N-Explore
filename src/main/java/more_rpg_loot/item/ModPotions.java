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

import java.util.ArrayList;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModPotions {
    // Potions don't need item models - they use vanilla potion bottle models
    // But they do need translations
    public record Entry(String name, RegistryEntry<Potion> potion, String translation) {
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Potion potion, String translation) {
        RegistryEntry<Potion> registryEntry = Registry.registerReference(Registries.POTION, Identifier.of(MOD_ID, name), potion);
        var entry = new Entry(name, registryEntry, translation);
        all.add(entry);
        return entry;
    }

    public static final Entry FROST_RESISTANCE_POTION = entry("frost_resistance_potion",
            new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE.registryEntry, 180, 0)),
            "Frost Resistance");
    public static final Entry LONG_FROST_RESISTANCE_POTION = entry("long_frost_resistance_potion",
            new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE.registryEntry, 480, 0)),
            "Frost Resistance");
    public static final Entry FROSTED_POTION = entry("frosted_potion",
            new Potion(new StatusEffectInstance(Effects.FREEZING.registryEntry, 45, 0)),
            "Frosted");
    public static final Entry LONG_FROSTED_POTION = entry("long_frosted_potion",
            new Potion(new StatusEffectInstance(Effects.FREEZING.registryEntry, 90, 1)),
            "Frosted");

    public static void registerPotions(){
        RPGLoot.LOGGER.info("Registering Potions for " + MOD_ID);
    }


    public static void registerPotionsRecipes(){
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ModBlocks.FROST_BLOOM.item(), ModPotions.FROST_RESISTANCE_POTION.potion());
            builder.registerPotionRecipe(ModPotions.FROST_RESISTANCE_POTION.potion(), Items.REDSTONE, ModPotions.LONG_FROST_RESISTANCE_POTION.potion());
            builder.registerPotionRecipe(Potions.AWKWARD, CommonItems.GLAZE_ROD.item(), ModPotions.FROSTED_POTION.potion());
            builder.registerPotionRecipe(FROSTED_POTION.potion(), Items.REDSTONE, ModPotions.LONG_FROSTED_POTION.potion());
        });
    }
}

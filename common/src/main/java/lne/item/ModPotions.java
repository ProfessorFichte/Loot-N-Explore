package lne.item;

import lne.blocks.FrozenDepthsBlocks;
import lne.effects.Effects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static lne.LneMod.MOD_ID;

public class ModPotions {
    public interface BrewingSink {
        void potionRecipe(RegistryEntry<Potion> input, Item ingredient, RegistryEntry<Potion> output);
    }

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
            new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE.registryEntry, 180 * 20, 0)),
            "Frost Resistance");
    public static final Entry LONG_FROST_RESISTANCE_POTION = entry("long_frost_resistance_potion",
            new Potion(new StatusEffectInstance(Effects.FROST_RESISTANCE.registryEntry, 480 * 20, 0)),
            "Frost Resistance");
    public static final Entry FROSTED_POTION = entry("frosted_potion",
            new Potion(new StatusEffectInstance(Effects.FREEZING.registryEntry, 45 * 20, 0)),
            "Frosted");
    public static final Entry LONG_FROSTED_POTION = entry("long_frosted_potion",
            new Potion(new StatusEffectInstance(Effects.FREEZING.registryEntry, 90 * 20, 1)),
            "Frosted");

    public static void registerPotions(){
    }

    public static void registerRecipes(BrewingSink sink) {
        sink.potionRecipe(Potions.AWKWARD, FrozenDepthsBlocks.FROST_BLOOM.item(), FROST_RESISTANCE_POTION.potion());
        sink.potionRecipe(FROST_RESISTANCE_POTION.potion(), Items.REDSTONE, LONG_FROST_RESISTANCE_POTION.potion());
        sink.potionRecipe(Potions.AWKWARD, CommonItems.GLAZE_ROD.item(), FROSTED_POTION.potion());
        sink.potionRecipe(FROSTED_POTION.potion(), Items.REDSTONE, LONG_FROSTED_POTION.potion());
    }
}

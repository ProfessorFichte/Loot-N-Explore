package more_rpg_loot.item;

import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

/**
 * Defines how an item's model should be generated in datagen.
 */
public sealed interface ItemModelType {

    /**
     * Generates the item model using the specified strategy.
     */
    void generate(ItemModelGenerator generator, Item item, String itemName);

    /**
     * Standard generated item model (2D texture).
     * @param texturePath Path to texture relative to textures/ (e.g., "item/drinks/", "item/misc/")
     */
    record Generated(String texturePath) implements ItemModelType {
        public Generated() {
            this("item/");
        }

        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            Models.GENERATED.upload(
                Identifier.of(MOD_ID, "item/" + itemName),
                net.minecraft.data.client.TextureMap.layer0(
                    Identifier.of(MOD_ID, texturePath + itemName)
                ),
                generator.writer
            );
        }
    }

    /**
     * Handheld item model (for tools, weapons).
     * @param texturePath Path to texture relative to textures/ (e.g., "item/weapons/")
     */
    record Handheld(String texturePath) implements ItemModelType {
        public Handheld() {
            this("item/");
        }

        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            Models.HANDHELD.upload(
                Identifier.of(MOD_ID, "item/" + itemName),
                net.minecraft.data.client.TextureMap.layer0(
                    Identifier.of(MOD_ID, texturePath + itemName)
                ),
                generator.writer
            );
        }
    }

    /**
     * Spawn egg model (uses minecraft:item/template_spawn_egg as parent).
     * Since spawn eggs are so simple, we skip datagen - keep manual JSON files.
     */
    record SpawnEgg() implements ItemModelType {
        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            // Spawn eggs have very simple models: {"parent": "item/template_spawn_egg"}
            // Keep the manual JSON files - they're already correct and very simple
            // Skip generation to avoid complexity
        }
    }

    /**
     * Custom or manual model (skip datagen).
     * Use this when you have a manually created model JSON.
     */
    record Custom() implements ItemModelType {
        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            // Skip generation - model file exists manually
        }
    }

    /**
     * No model needed (for items that don't appear in inventory, like potions base types).
     */
    record None() implements ItemModelType {
        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            // No model generation
        }
    }
}

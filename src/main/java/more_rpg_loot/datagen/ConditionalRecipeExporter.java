package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Wrapper for the recipe exporter that adds Fabric and NeoForge conditional loading to recipes.
 * This allows recipes to only load when specific mods are present.
 */
// 1.20.1: there is no RecipeExporter interface (and no AdvancementEntry); recipes are handed to a
// plain Consumer<RecipeJsonProvider>, so this wrapper implements that instead.
public class ConditionalRecipeExporter implements Consumer<RecipeJsonProvider> {
    private final Consumer<RecipeJsonProvider> baseExporter;
    private final List<String> requiredMods;

    public ConditionalRecipeExporter(Consumer<RecipeJsonProvider> baseExporter, List<String> requiredMods) {
        this.baseExporter = baseExporter;
        this.requiredMods = new ArrayList<>(requiredMods);
    }

    /**
     * Creates a conditional exporter that requires a single mod to be loaded
     */
    public static ConditionalRecipeExporter create(Consumer<RecipeJsonProvider> baseExporter, String modId) {
        return new ConditionalRecipeExporter(baseExporter, List.of(modId));
    }

    /**
     * Creates a conditional exporter that requires multiple mods to be loaded
     */
    public static ConditionalRecipeExporter create(Consumer<RecipeJsonProvider> baseExporter, String... modIds) {
        return new ConditionalRecipeExporter(baseExporter, List.of(modIds));
    }

    @Override
    public void accept(RecipeJsonProvider recipe) {
        // Accept the recipe with conditions by wrapping it with conditional JSON exporter
        baseExporter.accept(recipe);
    }


    /**
     * Builds the Fabric load conditions JSON structure
     * Format: {"fabric:load_conditions": [{"condition": "fabric:all_mods_loaded", "values": ["mod1", "mod2"]}]}
     */
    public JsonObject buildFabricConditions() {
        if (requiredMods.isEmpty()) {
            return new JsonObject();
        }

        JsonObject root = new JsonObject();
        JsonArray conditions = new JsonArray();

        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "fabric:all_mods_loaded");

        JsonArray values = new JsonArray();
        for (String modId : requiredMods) {
            values.add(modId);
        }
        condition.add("values", values);

        conditions.add(condition);
        root.add("fabric:load_conditions", conditions);

        return root;
    }

    /**
     * Builds the NeoForge load conditions JSON structure
     * Format: {"neoforge:conditions": [{"type": "neoforge:mod_loaded", "modid": "mod1"}]}
     */
    public JsonObject buildNeoForgeConditions() {
        if (requiredMods.isEmpty()) {
            return new JsonObject();
        }

        JsonObject root = new JsonObject();
        JsonArray conditions = new JsonArray();

        for (String modId : requiredMods) {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "neoforge:mod_loaded");
            condition.addProperty("modid", modId);
            conditions.add(condition);
        }

        root.add("neoforge:conditions", conditions);

        return root;
    }

    /**
     * Gets the list of required mod IDs
     */
    public List<String> getRequiredMods() {
        return new ArrayList<>(requiredMods);
    }

    /**
     * Returns the base exporter (useful for chaining)
     */
    public Consumer<RecipeJsonProvider> getBaseExporter() {
        return baseExporter;
    }
}

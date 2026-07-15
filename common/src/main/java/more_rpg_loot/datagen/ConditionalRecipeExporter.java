package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ConditionalRecipeExporter implements RecipeExporter {
    private final RecipeExporter baseExporter;
    private final List<String> requiredMods;

    public ConditionalRecipeExporter(RecipeExporter baseExporter, List<String> requiredMods) {
        this.baseExporter = baseExporter;
        this.requiredMods = new ArrayList<>(requiredMods);
    }

    public static ConditionalRecipeExporter create(RecipeExporter baseExporter, String modId) {
        return new ConditionalRecipeExporter(baseExporter, List.of(modId));
    }

    public static ConditionalRecipeExporter create(RecipeExporter baseExporter, String... modIds) {
        return new ConditionalRecipeExporter(baseExporter, List.of(modIds));
    }

    @Override
    public void accept(Identifier id, Recipe<?> recipe, AdvancementEntry advancement) {
        baseExporter.accept(id, recipe, advancement);
    }

    @Override
    public Advancement.Builder getAdvancementBuilder() {
        return null;
    }


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

    public List<String> getRequiredMods() {
        return new ArrayList<>(requiredMods);
    }

    public RecipeExporter getBaseExporter() {
        return baseExporter;
    }
}

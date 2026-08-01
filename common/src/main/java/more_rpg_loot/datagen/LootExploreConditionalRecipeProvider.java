package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import more_rpg_loot.RPGLoot;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootExploreConditionalRecipeProvider implements DataProvider {

    private final FabricDataOutput output;
    private final List<UnlessModLoadedRecipe> recipes = new ArrayList<>();

    public LootExploreConditionalRecipeProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generateRecipes();

        return CompletableFuture.allOf(recipes.stream().map(recipe -> {
            JsonObject json = buildRecipeJson(recipe);
            Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "recipe")
                    .resolveJson(Identifier.of(RPGLoot.MOD_ID, recipe.name()));
            return DataProvider.writeToPath(writer, json, path);
        }).toArray(CompletableFuture[]::new));
    }

    private void generateRecipes() {
        Item dragonTemplate = getItem("loot_n_explore:dragon_upgrade_smithing_template");
        Item guardianTemplate = getItem("loot_n_explore:guardian_upgrade_smithing_template");
        Item witherTemplate = getItem("loot_n_explore:wither_upgrade_smithing_template");
        Item frostTemplate = getItem("loot_n_explore:frostmonarch_upgrade_smithing_template");

        Item dragonScales = getItem("loot_n_explore:ender_dragon_scales");
        Item guardianEye = getItem("loot_n_explore:elder_guardian_eye");
        Item witherSpine = getItem("loot_n_explore:wither_spine");
        Item frozenSoul = getItem("loot_n_explore:frozen_soul");

        unlessModLoaded("generic/ender_dragon_bow", dragonTemplate, Items.BOW, dragonScales, getItem("loot_n_explore:ender_dragon_bow"), "lne_archers");
        unlessModLoaded("generic/ender_dragon_crossbow", dragonTemplate, Items.CROSSBOW, dragonScales, getItem("loot_n_explore:ender_dragon_crossbow"), "lne_archers");
        unlessModLoaded("generic/elder_guardian_bow", guardianTemplate, Items.BOW, guardianEye, getItem("loot_n_explore:elder_guardian_bow"), "lne_archers");
        unlessModLoaded("generic/elder_guardian_crossbow", guardianTemplate, Items.CROSSBOW, guardianEye, getItem("loot_n_explore:elder_guardian_crossbow"), "lne_archers");
        unlessModLoaded("generic/wither_bow", witherTemplate, Items.BOW, witherSpine, getItem("loot_n_explore:wither_bow"), "lne_archers");
        unlessModLoaded("generic/wither_crossbow", witherTemplate, Items.CROSSBOW, witherSpine, getItem("loot_n_explore:wither_crossbow"), "lne_archers");
        unlessModLoaded("frozen_depths/glacial_bow", frostTemplate, Items.BOW, frozenSoul, getItem("loot_n_explore:glacial_bow"), "lne_archers");
        unlessModLoaded("frozen_depths/glacial_crossbow", frostTemplate, Items.CROSSBOW, frozenSoul, getItem("loot_n_explore:glacial_crossbow"), "lne_archers");

        unlessModLoaded("generic/ender_dragon_mace", dragonTemplate, Items.MACE, dragonScales, getItem("loot_n_explore:ender_dragon_mace"), "lne_paladins");
        unlessModLoaded("generic/elder_guardian_mace", guardianTemplate, Items.MACE, guardianEye, getItem("loot_n_explore:elder_guardian_mace"), "lne_paladins");
        unlessModLoaded("generic/wither_mace", witherTemplate, Items.MACE, witherSpine, getItem("loot_n_explore:wither_mace"), "lne_paladins");
        unlessModLoaded("frozen_depths/glacial_mace", frostTemplate, Items.MACE, frozenSoul, getItem("loot_n_explore:glacial_mace"), "lne_paladins");
    }

    private void unlessModLoaded(String recipeName, Item template, Item base, Item addition, Item result, String excludedMod) {
        recipes.add(new UnlessModLoadedRecipe(recipeName, template, base, addition, result, excludedMod));
    }

    private JsonObject buildRecipeJson(UnlessModLoadedRecipe recipe) {
        JsonObject json = new JsonObject();

        JsonArray fabricConditions = new JsonArray();
        JsonObject fabricNot = new JsonObject();
        fabricNot.addProperty("condition", "fabric:not");
        JsonObject fabricAllModsLoaded = new JsonObject();
        fabricAllModsLoaded.addProperty("condition", "fabric:all_mods_loaded");
        JsonArray fabricModValues = new JsonArray();
        fabricModValues.add(recipe.excludedMod());
        fabricAllModsLoaded.add("values", fabricModValues);
        fabricNot.add("value", fabricAllModsLoaded);
        fabricConditions.add(fabricNot);
        json.add("fabric:load_conditions", fabricConditions);

        JsonArray neoforgeConditions = new JsonArray();
        JsonObject neoforgeNot = new JsonObject();
        neoforgeNot.addProperty("type", "neoforge:not");
        JsonObject neoforgeModLoaded = new JsonObject();
        neoforgeModLoaded.addProperty("type", "neoforge:mod_loaded");
        neoforgeModLoaded.addProperty("modid", recipe.excludedMod());
        neoforgeNot.add("value", neoforgeModLoaded);
        neoforgeConditions.add(neoforgeNot);
        json.add("neoforge:conditions", neoforgeConditions);

        json.addProperty("type", "minecraft:smithing_transform");

        JsonObject template = new JsonObject();
        template.addProperty("item", Registries.ITEM.getId(recipe.template()).toString());
        json.add("template", template);

        JsonObject base = new JsonObject();
        base.addProperty("item", Registries.ITEM.getId(recipe.base()).toString());
        json.add("base", base);

        JsonObject addition = new JsonObject();
        addition.addProperty("item", Registries.ITEM.getId(recipe.addition()).toString());
        json.add("addition", addition);

        JsonObject result = new JsonObject();
        result.addProperty("id", Registries.ITEM.getId(recipe.result()).toString());
        result.addProperty("count", 1);
        json.add("result", result);

        return json;
    }

    private Item getItem(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null) {
            throw new IllegalArgumentException("Invalid item ID: " + itemId);
        }
        Item item = Registries.ITEM.get(id);
        if (item == Items.AIR) {
            System.out.println("WARNING: Item not found: " + itemId + ", using BARRIER as placeholder");
            return Items.BARRIER;
        }
        return item;
    }

    @Override
    public String getName() {
        return "Loot & Explore Conditional Smithing Recipes";
    }

    private record UnlessModLoadedRecipe(String name, Item template, Item base, Item addition, Item result, String excludedMod) {}
}

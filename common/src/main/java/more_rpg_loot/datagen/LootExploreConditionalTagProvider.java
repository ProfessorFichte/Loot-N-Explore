package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootExploreConditionalTagProvider implements DataProvider {

    private final FabricDataOutput output;

    public LootExploreConditionalTagProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<String> relicIds = LNE_Relics.entries.stream().map(entry -> entry.id().toString()).toList();

        CompletableFuture<?> spellTrinketTag = writeTag(
                writer, "spell_engine", "spell_trinket", relicIds,
                requireMod("more_rpg_classes")
        );
        CompletableFuture<?> trinketsCharmTag = writeTag(
                writer, "trinkets", "charm/trinket", relicIds,
                and(requireMod("trinkets"), not(requireMod("more_rpg_classes")))
        );
        CompletableFuture<?> accessoriesNecklaceTag = writeTag(
                writer, "accessories", "necklace", relicIds,
                and(requireMod("accessories"), not(requireMod("more_rpg_classes")))
        );

        return CompletableFuture.allOf(spellTrinketTag, trinketsCharmTag, accessoriesNecklaceTag);
    }

    private CompletableFuture<?> writeTag(DataWriter writer, String namespace, String tagPath, List<String> itemIds, Conditions conditions) {
        JsonObject json = new JsonObject();
        json.add("fabric:load_conditions", conditions.fabric());
        json.add("neoforge:conditions", conditions.neoforge());

        json.addProperty("replace", false);
        JsonArray values = new JsonArray();
        itemIds.forEach(values::add);
        json.add("values", values);

        Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "tags/item")
                .resolveJson(Identifier.of(namespace, tagPath));
        return DataProvider.writeToPath(writer, json, path);
    }

    private record Conditions(JsonArray fabric, JsonArray neoforge) {}

    private static Conditions requireMod(String modId) {
        JsonObject fabricCondition = new JsonObject();
        fabricCondition.addProperty("condition", "fabric:all_mods_loaded");
        JsonArray fabricValues = new JsonArray();
        fabricValues.add(modId);
        fabricCondition.add("values", fabricValues);
        JsonArray fabric = new JsonArray();
        fabric.add(fabricCondition);

        JsonObject neoforgeCondition = new JsonObject();
        neoforgeCondition.addProperty("type", "neoforge:mod_loaded");
        neoforgeCondition.addProperty("modid", modId);
        JsonArray neoforge = new JsonArray();
        neoforge.add(neoforgeCondition);

        return new Conditions(fabric, neoforge);
    }

    private static Conditions not(Conditions condition) {
        JsonObject fabricNot = new JsonObject();
        fabricNot.addProperty("condition", "fabric:not");
        fabricNot.add("value", condition.fabric().get(0));
        JsonArray fabric = new JsonArray();
        fabric.add(fabricNot);

        JsonObject neoforgeNot = new JsonObject();
        neoforgeNot.addProperty("type", "neoforge:not");
        neoforgeNot.add("value", condition.neoforge().get(0));
        JsonArray neoforge = new JsonArray();
        neoforge.add(neoforgeNot);

        return new Conditions(fabric, neoforge);
    }

    private static Conditions and(Conditions a, Conditions b) {
        JsonObject fabricAnd = new JsonObject();
        fabricAnd.addProperty("condition", "fabric:and");
        JsonArray fabricConditions = new JsonArray();
        fabricConditions.add(a.fabric().get(0));
        fabricConditions.add(b.fabric().get(0));
        fabricAnd.add("conditions", fabricConditions);
        JsonArray fabric = new JsonArray();
        fabric.add(fabricAnd);

        JsonObject neoforgeAnd = new JsonObject();
        neoforgeAnd.addProperty("type", "neoforge:and");
        JsonArray neoforgeConditions = new JsonArray();
        neoforgeConditions.add(a.neoforge().get(0));
        neoforgeConditions.add(b.neoforge().get(0));
        neoforgeAnd.add("conditions", neoforgeConditions);
        JsonArray neoforge = new JsonArray();
        neoforge.add(neoforgeAnd);

        return new Conditions(fabric, neoforge);
    }

    @Override
    public String getName() {
        return "Loot & Explore Conditional Relic Slot Tags";
    }
}

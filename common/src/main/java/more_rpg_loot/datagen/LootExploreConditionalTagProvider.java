package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.relics.LNE_RelicItems;
import more_rpg_loot.util.LneItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootExploreConditionalTagProvider implements DataProvider {

    private final FabricDataOutput output;

    public LootExploreConditionalTagProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<String> relicIds = new ArrayList<>();
        LNE_Relics.entries.forEach(entry -> relicIds.add(entry.id().toString()));
        LNE_RelicItems.entries.forEach(entry -> relicIds.add(entry.id().toString()));

        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        JsonArray values = new JsonArray();
        relicIds.forEach(id -> values.add(optionalEntry(id)));
        json.add("values", values);

        return CompletableFuture.allOf(
                DataProvider.writeToPath(writer, json, tagPath("spell_engine", "spell_trinket")),
                DataProvider.writeToPath(writer, trinketsNecklaceTag(), tagPath("trinkets", "chest/necklace")));
    }

    private static JsonObject optionalEntry(String id) {
        JsonObject entry = new JsonObject();
        entry.addProperty("id", id);
        entry.addProperty("required", false);
        return entry;
    }

    private Path tagPath(String namespace, String path) {
        return output.getResolver(DataOutput.OutputType.DATA_PACK, "tags/item")
                .resolveJson(Identifier.of(namespace, path));
    }

    private static JsonObject trinketsNecklaceTag() {
        JsonObject spellEngineLoaded = new JsonObject();
        spellEngineLoaded.addProperty("condition", "fabric:all_mods_loaded");
        JsonArray mods = new JsonArray();
        mods.add("spell_engine");
        spellEngineLoaded.add("values", mods);

        JsonObject not = new JsonObject();
        not.addProperty("condition", "fabric:not");
        not.add("value", spellEngineLoaded);

        JsonArray conditions = new JsonArray();
        conditions.add(not);

        JsonObject json = new JsonObject();
        json.add("fabric:load_conditions", conditions);
        json.addProperty("replace", false);
        JsonArray values = new JsonArray();
        values.add(optionalEntry("#" + LneItemTags.RELICS.id()));
        json.add("values", values);
        return json;
    }

    @Override
    public String getName() {
        return "Loot & Explore Relic Slot Tags";
    }
}

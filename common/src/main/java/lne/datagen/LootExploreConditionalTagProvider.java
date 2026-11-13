package lne.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lne.item.relics.LNE_RelicItems;
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
        LNE_RelicItems.entries.forEach(entry -> relicIds.add(entry.id().toString()));

        JsonObject json = new JsonObject();
        json.addProperty("replace", false);
        JsonArray values = new JsonArray();
        relicIds.forEach(id -> values.add(optionalEntry(id)));
        json.add("values", values);

        return DataProvider.writeToPath(writer, json, tagPath("spell_engine", "spell_trinket"));
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

    @Override
    public String getName() {
        return "Loot & Explore Spell Engine Relic Tag";
    }
}

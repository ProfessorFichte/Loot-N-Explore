package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.relics.LNE_RelicItems;
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
        relicIds.forEach(values::add);
        json.add("values", values);

        Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "tags/item")
                .resolveJson(Identifier.of("spell_engine", "spell_trinket"));
        return DataProvider.writeToPath(writer, json, path);
    }

    @Override
    public String getName() {
        return "Loot & Explore Relic Slot Tags";
    }
}

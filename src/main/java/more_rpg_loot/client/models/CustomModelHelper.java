package more_rpg_loot.client.models;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CustomModelHelper {
    private static final List<Identifier> MODEL_IDS = new ArrayList<>();

    public static void registerModelIds(List<Identifier> ids) {
        MODEL_IDS.addAll(ids);
    }

    public static void initialize() {
        ModelLoadingPlugin.register(pluginContext -> {
            for (Identifier modelId : MODEL_IDS) {
                pluginContext.addModels(modelId);
            }
        });
    }
}

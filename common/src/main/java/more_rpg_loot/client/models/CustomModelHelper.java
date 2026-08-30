package more_rpg_loot.client.models;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CustomModelHelper {
    private static final List<Identifier> MODEL_IDS = new ArrayList<>();

    public static void registerModelIds(List<Identifier> ids) {
        MODEL_IDS.addAll(ids);
    }

    public static List<Identifier> modelIds() {
        return MODEL_IDS;
    }
}

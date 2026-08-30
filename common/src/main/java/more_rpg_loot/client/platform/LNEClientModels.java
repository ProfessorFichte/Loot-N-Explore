package more_rpg_loot.client.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class LNEClientModels {
    public static Function<Identifier, BakedModel> lookup = id -> null;

    private LNEClientModels() {}
}

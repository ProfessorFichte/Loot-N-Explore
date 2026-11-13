package lne.client.entity.renderers.relic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public record OptionalTexture(Identifier preferred, Identifier fallback) {
    public Identifier get() {
        return MinecraftClient.getInstance().getResourceManager().getResource(preferred).isPresent() ? preferred : fallback;
    }
}

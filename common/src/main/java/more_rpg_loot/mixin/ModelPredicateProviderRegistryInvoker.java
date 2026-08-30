package more_rpg_loot.mixin;

import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelPredicateProviderRegistry.class)
public interface ModelPredicateProviderRegistryInvoker {
    @Invoker("register")
    static void lne$register(Item item, Identifier id, ClampedModelPredicateProvider provider) {
        throw new AssertionError();
    }
}

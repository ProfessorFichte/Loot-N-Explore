package lne.mixin;

import lne.util.LegacyIds;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> {
    @Shadow
    @Final
    RegistryKey<? extends Registry<T>> key;

    @ModifyVariable(method = {
            "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;",
            "getEntry(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;",
            "containsId(Lnet/minecraft/util/Identifier;)Z"
    }, at = @At("HEAD"), argsOnly = true)
    private Identifier lne$legacyId(Identifier id) {
        return LegacyIds.remap(key, id);
    }
}

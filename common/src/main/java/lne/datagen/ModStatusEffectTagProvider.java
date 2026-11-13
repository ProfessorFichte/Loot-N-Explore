package lne.datagen;

import lne.effects.Effects;
import lne.util.LneTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModStatusEffectTagProvider extends FabricTagProvider<StatusEffect> {
    public ModStatusEffectTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STATUS_EFFECT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(LneTags.BLOCKED_BY_FROST_RESISTANCE)
                .addOptional(Effects.FREEZING.id)
                .addOptional(Identifier.of("mrpg_lib", "frosted"))
                .addOptional(Identifier.of("mrpg_lib", "frozen_solid"));
    }
}

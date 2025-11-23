package more_rpg_loot.compat.spell_engine;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.spell_engine.api.datagen.SpellGenerator;

import java.util.concurrent.CompletableFuture;

public class LNE_AbilityDatagen extends SpellGenerator {
    public LNE_AbilityDatagen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateSpells(Builder builder) {
        // Register all spells from the LNE_Abilities entries list
        for (var entry : LNE_Abilities.entries) {
            builder.add(entry.id(), entry.spell());
        }
    }
}
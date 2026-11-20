package lne.compat.spell_engine;

import lne.LneMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.registry.SpellRegistry;

import java.util.concurrent.CompletableFuture;

public class MobSpellTagGenerator extends FabricTagProvider<Spell> {
    public MobSpellTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, SpellRegistry.KEY, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        var staff = getOrCreateTagBuilder(TagKey.of(SpellRegistry.KEY, LNE_Abilities.MONARCHS_STAFF_SPELLS));
        staff.addOptional(Identifier.of("wizards", "frostbolt"));
        staff.addOptional(LNE_Abilities.ICICLE_BARRAGE);
        tag("frost_monarch", "secondary", "wizards:frost_lance");
        tag("frozen_magician", "primary", "wizards:frost_shard");
        tag("frozen_magician", "secondary", "wizards:frost_nova", "wizards:frost_spikes");
        tag("general_frozen_magician", "primary", "wizards:frostbolt");
        tag("general_frozen_magician", "secondary", "wizards:frost_nova", "wizards:frost_spikes", "wizards:frost_shield");
        tag("monarchs_soldier", "secondary", "rogues:charge");
        tag("general_monarchs_soldier", "secondary", "rogues:charge", "rogues:shout", "rogues:mortal_strike");
        tag("general_monarchs_guard", "secondary", "rogues:charge", "rogues:throw_net");
        tag("frozen_ranger", "secondary", "archers_expansion:arctic_volley");
        tag("general_frozen_ranger", "secondary", "archers_expansion:arctic_volley", "archers_expansion:enchanted_crystal_arrow", "archers:rain_of_arrows");
    }

    private void tag(String entity, String slot, String... spells) {
        var builder = getOrCreateTagBuilder(TagKey.of(SpellRegistry.KEY, LneMod.id("mob/" + entity + "/" + slot)));
        for (String spell : spells) {
            builder.addOptional(Identifier.of(spell));
        }
    }
}

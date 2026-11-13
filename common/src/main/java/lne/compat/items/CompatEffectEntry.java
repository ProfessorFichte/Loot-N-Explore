package lne.compat.items;

import com.mrpg_lib.config.AttributeModifier;
import lne.config.EffectAttributeConfigs;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

import static lne.LneMod.MOD_ID;
import static lne.LneMod.effectsConfig;

public final class CompatEffectEntry {
    public final Identifier id;
    public final String title;
    public final String description;
    public final StatusEffect effect;
    public RegistryEntry<StatusEffect> registryEntry;

    private CompatEffectEntry(String name, String title, String description, StatusEffect effect) {
        this.id = Identifier.of(MOD_ID, name);
        this.title = title;
        this.description = description;
        this.effect = effect;
    }

    public static CompatEffectEntry add(List<CompatEffectEntry> owner, String name, String title, String description, StatusEffect effect) {
        CompatEffectEntry entry = new CompatEffectEntry(name, title, description, effect);
        owner.add(entry);
        return entry;
    }

    public Identifier modifierId() {
        return Identifier.of(MOD_ID, "effect." + id.getPath());
    }

    public void applyAttributes(AttributeModifier... defaults) {
        EffectAttributeConfigs.apply(effectsConfig != null ? effectsConfig.value.compat_effects : null, id, modifierId(), effect, List.of(defaults));
    }

    public void register() {
        registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
    }

    public static void registerAll(List<CompatEffectEntry> entries) {
        for (CompatEffectEntry entry : entries) {
            entry.register();
        }
    }
}

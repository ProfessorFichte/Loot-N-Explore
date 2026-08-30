package more_rpg_loot.compat.items;

import more_rpg_loot.RPGLoot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

import static more_rpg_loot.RPGLoot.MOD_ID;

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

    public void register() {
        registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
    }

    public static void registerAll(String label, List<CompatEffectEntry> entries) {
        RPGLoot.LOGGER.info("Registering " + label + " compat effects for " + MOD_ID);
        for (CompatEffectEntry entry : entries) {
            entry.register();
        }
    }
}

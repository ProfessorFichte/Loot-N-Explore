package lne.config;

import com.mrpg_lib.config.AttributeModifier;
import com.mrpg_lib.config.EffectConfig;
import lne.LneMod;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EffectAttributeConfigs {
    // typed defaults may be holders that are not in the registry yet, so they resolve without a registry lookup
    private static final Map<String, RegistryEntry<EntityAttribute>> KNOWN_ATTRIBUTES = new HashMap<>();

    private EffectAttributeConfigs() {
    }

    public static AttributeModifier modifier(RegistryEntry<EntityAttribute> attribute, float value, EntityAttributeModifier.Operation operation) {
        String id = attribute.getKey().orElseThrow().getValue().toString();
        KNOWN_ATTRIBUTES.put(id, attribute);
        return new AttributeModifier(id, value, operation);
    }

    public static AttributeModifier modifier(String attribute, float value, EntityAttributeModifier.Operation operation) {
        return new AttributeModifier(attribute, value, operation);
    }

    public static void apply(@Nullable Map<String, EffectConfig> configs, Identifier effectId, Identifier modifierId, StatusEffect effect, List<AttributeModifier> defaults) {
        EffectConfig config = configs == null
                ? new EffectConfig(defaults)
                : configs.computeIfAbsent(effectId.toString(), key -> new EffectConfig(defaults));
        for (AttributeModifier modifier : config.selectedAttributes()) {
            RegistryEntry<EntityAttribute> attribute = resolve(modifier.attribute);
            if (attribute == null) {
                LneMod.LOGGER.debug("Skipping unregistered attribute '{}' on effect {}", modifier.attribute, effectId);
                continue;
            }
            Identifier id = modifier.id == null || modifier.id.isEmpty() ? modifierId : Identifier.of(modifier.id);
            effect.addAttributeModifier(attribute, id, modifier.value, modifier.operation);
        }
    }

    @Nullable
    private static RegistryEntry<EntityAttribute> resolve(String attribute) {
        RegistryEntry<EntityAttribute> known = KNOWN_ATTRIBUTES.get(attribute);
        if (known != null) {
            return known;
        }
        Identifier attributeId = Identifier.tryParse(attribute);
        return attributeId == null ? null : Registries.ATTRIBUTE.getEntry(attributeId).orElse(null);
    }
}

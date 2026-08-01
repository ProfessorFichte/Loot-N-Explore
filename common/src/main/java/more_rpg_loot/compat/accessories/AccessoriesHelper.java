package more_rpg_loot.compat.accessories;

import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.item.relics.LNE_RelicItems;

public class AccessoriesHelper {
    public static void registerFactory() {
        LNE_Relics.factory = args -> {
            var settings = args.settings();
            if (args.attributes() != null) {
                var builder = AccessoryItemAttributeModifiers.builder();
                for (var entry : args.attributes().modifiers()) {
                    builder = builder.addForSlot(entry.attribute(), entry.modifier(), "charm", true);
                }
                settings = settings.component(AccessoriesDataComponents.ATTRIBUTES, builder.build());
            }
            return new RelicAccessoryItem(settings);
        };
        LNE_RelicItems.factory = (settings, attrs) -> {
            if (attrs != null) {
                var builder = AccessoryItemAttributeModifiers.builder();
                for (var entry : attrs.modifiers()) {
                    builder = builder.addForSlot(entry.attribute(), entry.modifier(), "necklace", true);
                }
                settings = settings.component(AccessoriesDataComponents.ATTRIBUTES, builder.build());
            }
            return new RelicAccessoryItem(settings);
        };
    }
}

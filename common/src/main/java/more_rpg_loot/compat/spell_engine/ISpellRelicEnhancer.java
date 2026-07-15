package more_rpg_loot.compat.spell_engine;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface ISpellRelicEnhancer {
    Item.Settings enhance(Item.Settings settings, @Nullable Identifier spellId);
}

package more_rpg_loot.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class RelicItem extends Item {

    public RelicItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        String loreKey = this.getTranslationKey() + ".lore";

        tooltip.add(ScreenTexts.EMPTY);

        tooltip.add(Text.translatable(loreKey).formatted(Formatting.GRAY, Formatting.ITALIC));
    }
}

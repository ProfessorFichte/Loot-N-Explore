package more_rpg_loot.blocks.frozen_depths;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FrozenSoulBlockItem extends BlockItem {
    public FrozenSoulBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".lore").formatted(Formatting.GRAY, Formatting.ITALIC));
    }
}

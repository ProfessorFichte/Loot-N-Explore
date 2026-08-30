package more_rpg_loot.blocks.frozen_depths;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FrozenAdventurerBlockItem extends BlockItem {
    public FrozenAdventurerBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (type.isCreative()) {
            tooltip.add(Text.translatable(getTranslationKey() + ".hint").formatted(Formatting.DARK_AQUA));
        }
    }
}

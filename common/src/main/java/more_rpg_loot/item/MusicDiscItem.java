package more_rpg_loot.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class MusicDiscItem extends Item {
    public static final String ARTIST = "KaktusDoesMusic";

    private final String title;

    public MusicDiscItem(Settings settings, String title) {
        super(settings);
        this.title = title;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(title + " - " + ARTIST).formatted(Formatting.GRAY));
    }
}

package lne.blocks.generic;

import lne.item.relics.RelicTooltips;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public final class VariantControls {
    public static final String TOOLTIP = "tooltip.loot_n_explore.change_variant";

    private VariantControls() {
    }

    public static boolean canChangeVariant(PlayerEntity player) {
        return player.getAbilities().creativeMode && player.isSneaking() && player.getMainHandStack().isEmpty();
    }

    public static void appendTooltip(List<Text> tooltip) {
        if (RelicTooltips.viewer() instanceof PlayerEntity player && player.getAbilities().creativeMode) {
            tooltip.add(Text.translatable(TOOLTIP).formatted(Formatting.GRAY));
        }
    }
}

package lne.ability;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.Map;

public enum AbilityTheme {
    ENDER_DRAGON(0xB050FF),
    WITHER(0x6A4C8A),
    GLACIAL(0x8FE6FF),
    ELDER_GUARDIAN(0x1E90D6),
    NONE(0xFFFFFF);

    private static final Map<String, AbilityTheme> RELICS = Map.ofEntries(
            Map.entry("ender_dragon_tooth", ENDER_DRAGON),
            Map.entry("charged_amethyst", ENDER_DRAGON),
            Map.entry("corrupted_ender_pearl", ENDER_DRAGON),
            Map.entry("ender_dragon_scales", ENDER_DRAGON),
            Map.entry("unknown_remains", WITHER),
            Map.entry("lost_soul", WITHER),
            Map.entry("withered_obsidian_shard", WITHER),
            Map.entry("wither_spine", WITHER),
            Map.entry("eternal_snowflake", GLACIAL),
            Map.entry("glacier_shard", GLACIAL),
            Map.entry("frozen_rib", GLACIAL),
            Map.entry("frozen_soul", GLACIAL),
            Map.entry("amphitrite_diadem", ELDER_GUARDIAN),
            Map.entry("rainbow_coral", ELDER_GUARDIAN),
            Map.entry("poseidons_amphora", ELDER_GUARDIAN),
            Map.entry("elder_guardian_eye", ELDER_GUARDIAN));

    private final int color;

    AbilityTheme(int color) {
        this.color = color;
    }

    public int color() {
        return color;
    }

    public static AbilityTheme ofRelic(String relic) {
        return RELICS.getOrDefault(relic, NONE);
    }

    public MutableText style(Text name) {
        return name.copy().styled(style -> style.withColor(TextColor.fromRgb(color)).withBold(true));
    }
}

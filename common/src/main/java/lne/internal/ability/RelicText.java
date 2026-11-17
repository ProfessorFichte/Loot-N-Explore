package lne.internal.ability;

import java.text.DecimalFormat;

public final class RelicText {
    private static final DecimalFormat NUMBER = new DecimalFormat("0.##");

    private RelicText() {
    }

    public static String percent(double fraction) {
        return NUMBER.format(fraction * 100) + "%";
    }

    public static String number(double value) {
        return NUMBER.format(value);
    }

    public static String seconds(double ticks) {
        return NUMBER.format(ticks / 20.0) + "s";
    }
}

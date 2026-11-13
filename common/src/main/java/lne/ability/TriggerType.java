package lne.ability;

import static lne.LneMod.MOD_ID;

public enum TriggerType {
    MELEE_HIT("melee_hit", "On melee hit"),
    ARROW_HIT("arrow_hit", "On arrow hit"),
    ARROW_SHOT("arrow_shot", "On arrow shot"),
    SPELL_HIT("spell_hit", "On spell hit"),
    KILL("kill", "On kill"),
    DAMAGE_TAKEN("damage_taken", "When taking damage"),
    LOW_HEALTH("low_health", "When falling to low health"),
    HEALED("healed", "When healed"),
    BLOCK("block", "On block"),
    ACTIVE_USE("active_use", "Right click");

    private final String id;
    private final String label;

    TriggerType(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String langKey() {
        return MOD_ID + ".relic.trigger." + id;
    }

    public String label() {
        return label;
    }
}

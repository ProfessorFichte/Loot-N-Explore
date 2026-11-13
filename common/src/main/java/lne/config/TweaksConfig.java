package lne.config;

import com.mrpg_lib.compat.player_animator.api.MobAnimations;
import lne.LneMod;
import lne.platform.LNEPlatform;

public class TweaksConfig {
    public boolean disable_relic_attributes = false;
    public boolean disable_spell_engine_abilities = false;
    public boolean disable_relic_abilities = false;
    public boolean disable_weapon_abilities = false;
    public boolean disable_custom_mod_music = false;
    public boolean disable_entity_hud_text = false;
    public boolean disable_mob_spell_casting = false;
    public boolean disable_mob_better_combat = false;
    public boolean disable_mob_combat_roll = false;

    private static TweaksConfig get() {
        return LneMod.tweaksConfig.value;
    }

    public static boolean relicAttributes() {
        return !get().disable_relic_attributes;
    }

    public static boolean relicAbilities() {
        return !get().disable_relic_abilities;
    }

    public static boolean weaponAbilities() {
        return !get().disable_weapon_abilities;
    }

    public static boolean customMusic() {
        return !get().disable_custom_mod_music;
    }

    public static boolean entityHudText() {
        return !get().disable_entity_hud_text;
    }

    public static boolean spellEngineAbilities() {
        return LNEPlatform.isModLoaded("spell_engine") && !get().disable_spell_engine_abilities;
    }

    public static boolean mobSpellCasting() {
        return spellEngineAbilities() && !get().disable_mob_spell_casting && MobAnimations.isAvailable();
    }

    public static boolean mobBetterCombat() {
        return LNEPlatform.isModLoaded("bettercombat") && !get().disable_mob_better_combat && MobAnimations.isAvailable();
    }

    public static boolean mobCombatRoll() {
        return LNEPlatform.isModLoaded("combat_roll") && !get().disable_mob_combat_roll && MobAnimations.isAvailable();
    }
}

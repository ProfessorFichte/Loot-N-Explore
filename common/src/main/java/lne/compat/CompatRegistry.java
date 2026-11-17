package lne.compat;

import lne.internal.platform.LNEPlatform;

import lne.compat.items.*;
import lne.compat.spell_engine.SmithingTemplates;
import lne.compat.spell_engine.SpellEngine_LNE;
import lne.item.Group;
import lne.item.relics.LNE_RelicItems;
import lne.item.relics.RelicLootInjection;
import lne.item.weapons.LNE_ShieldItems;
import lne.item.weapons.LNE_WeaponItems;


public class CompatRegistry {
    public static void registerModCompat() {
        if (LNEPlatform.isModLoaded("spell_power")) {
            SpellPower_Effects.register();
            MRPGC_Effects.register();
            SpellPower_Effects.applyMonarchsFrostStaffPower();
        }
        if (LNEPlatform.isModLoaded("ranged_weapon_api")) {
            RWA_Effects.register();
        }
        if (LNEPlatform.isModLoaded("witcher_rpg")) {
            Witcher_Effects.register();
        }
        if (LNEPlatform.isModLoaded("critical_strike")) {
            CriticalStrike_Effects.register();
        }

        CompatItems.registerAll();

        LNE_WeaponItems.register();

        Group.registerLootItemGroup(() -> LNE_WeaponItems.ENDER_DRAGON_SWORD.item());

        if (LNEPlatform.isModLoaded("spell_engine")) {
            SpellEngine_LNE.initialize();
        }

        SmithingTemplates.registerSmithingUpgrades();

        LNE_RelicItems.register();
        RelicLootInjection.init();

        if (!LNEPlatform.isModLoaded("lne_archers")) {
            LNE_WeaponItems.registerRanged();
        }

        if (!LNEPlatform.isModLoaded("lne_paladins")) {
            LNE_WeaponItems.registerMaces();
            LNE_ShieldItems.register();
        }
    }
}

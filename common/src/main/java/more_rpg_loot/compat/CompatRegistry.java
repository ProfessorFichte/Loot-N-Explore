package more_rpg_loot.compat;

import more_rpg_loot.platform.LNEPlatform;

import more_rpg_loot.compat.items.*;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.compat.spell_engine.SmithingTemplates;
import more_rpg_loot.compat.spell_engine.SpellEngine_LNE;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.relics.LNE_RelicItems;
import more_rpg_loot.item.relics.RelicLootInjection;
import more_rpg_loot.item.relics.VanillaRelicAbilities;
import more_rpg_loot.item.weapons.LNE_ShieldItems;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.minecraft.item.Items;


public class CompatRegistry {
    public static void registerModCompat() {
        if (LNEPlatform.isModLoaded("spell_power")) {
            SpellPower_Effects.register();
            SpellPower_Effects.applyMonarchsFrostStaffPower();
        }
        if (LNEPlatform.isModLoaded("more_rpg_classes")) {
            MRPGC_Effects.register();
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

        if (LNEPlatform.isModLoaded("spell_engine")) {
            SpellEngine_LNE.initialize();
        } else {
            Group.registerLootItemGroup(() -> Items.ENDER_EYE);
        }

        SmithingTemplates.registerSmithingUpgrades();

        if (LNEPlatform.isModLoaded("more_rpg_classes")) {
            LNE_Relics.register(SpellEngine_LNE.relicsConfig.value.entries);
        } else {
            LNE_RelicItems.spellEnhancer = new VanillaRelicAbilities();
            LNE_RelicItems.register();
        }
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

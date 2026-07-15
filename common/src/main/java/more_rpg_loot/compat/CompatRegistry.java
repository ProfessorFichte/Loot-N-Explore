package more_rpg_loot.compat;

import more_rpg_loot.compat.accessories.AccessoriesCompat;
import more_rpg_loot.compat.items.*;
import more_rpg_loot.compat.spell_engine.SpellEngine_LNE;
import more_rpg_loot.compat.spell_engine.trinket_compat.TrinketsCompat;
import more_rpg_loot.item.Group;
import more_rpg_loot.item.relics.LNE_RelicItems;
import more_rpg_loot.item.relics.RelicLootInjection;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;


public class CompatRegistry {
    public static void registerModCompat() {
        TrinketsCompat.init();
        AccessoriesCompat.init();

        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            SpellPower_Effects.register();
        }
        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            MRPGC_Effects.register();
        }
        if (FabricLoader.getInstance().isModLoaded("ranged_weapon_api")) {
            RWA_Effects.register();
        }
        if (FabricLoader.getInstance().isModLoaded("witcher_rpg")) {
            Witcher_Effects.register();
        }
        if (FabricLoader.getInstance().isModLoaded("critical_strike")) {
            CriticalStrike_Effects.register();
        }

        CompatItems.registerAll();

        // Weapons always use LNE_WeaponItems regardless of spell_engine
        LNE_WeaponItems.register();

        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            // Handles item group, spell relics, loot injection, smithing templates, scroll config
            SpellEngine_LNE.initialize();
        } else {
            Group.registerLootItemGroup(() -> Items.ENDER_EYE);
            LNE_RelicItems.register();
            RelicLootInjection.inject();
        }

        // Ranged weapons only if lne_archers is not installed (it registers its own bows/crossbows).
        // registerRanged() already includes frozen_bow, so only fall back to registerFrozenBow() alone
        // when registerRanged() is skipped - frozen_bow must never be registered by both.
        if (!FabricLoader.getInstance().isModLoaded("lne_archers")) {
            LNE_WeaponItems.registerRanged();
        } else {
            LNE_WeaponItems.registerFrozenBow();
        }

        // Maces only if lne_paladins is not installed (it registers its own maces)
        if (!FabricLoader.getInstance().isModLoaded("lne_paladins")) {
            LNE_WeaponItems.registerMaces();
        }
    }
}

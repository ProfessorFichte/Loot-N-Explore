package more_rpg_loot.compat;

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
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;


public class CompatRegistry {
    public static void registerModCompat() {
        if (FabricLoader.getInstance().isModLoaded("spell_power")) {
            SpellPower_Effects.register();
            SpellPower_Effects.applyMonarchsFrostStaffPower();
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
            // Handles item group, loot injection, scroll config, weapon spells
            SpellEngine_LNE.initialize();
        } else {
            Group.registerLootItemGroup(() -> Items.ENDER_EYE);
        }

        SmithingTemplates.registerSmithingUpgrades();

        if (FabricLoader.getInstance().isModLoaded("more_rpg_classes")) {
            LNE_Relics.register(SpellEngine_LNE.relicsConfig.value.entries);
        } else {
            LNE_RelicItems.spellEnhancer = new VanillaRelicAbilities();
            LNE_RelicItems.register();
            RelicLootInjection.inject();
        }

        // Ranged weapons only if lne_archers is not installed (it registers its own bows/crossbows).
        // Frozen Bow is a plain vanilla-style weapon registered via CommonItems, independent of this.
        if (!FabricLoader.getInstance().isModLoaded("lne_archers")) {
            LNE_WeaponItems.registerRanged();
        }

        // Maces and Shields only if lne_paladins is not installed (it registers its own themed variants)
        if (!FabricLoader.getInstance().isModLoaded("lne_paladins")) {
            LNE_WeaponItems.registerMaces();
            LNE_ShieldItems.register();
        }
    }
}

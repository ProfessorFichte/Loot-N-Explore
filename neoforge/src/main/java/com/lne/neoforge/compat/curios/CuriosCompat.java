package com.lne.neoforge.compat.curios;

import more_rpg_loot.RPGLoot;
import more_rpg_loot.platform.LNEPlatform;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public class CuriosCompat {
    private static final String CHARM_SLOT_PACK = "resourcepacks/curios_charm_relics";

    public static void init() {
        if (LNEPlatform.isModLoaded("curios")) {
            CuriosHelper.register();
        }
    }

    public static void addPacks(AddPackFindersEvent event) {
        if (event.getPackType() != ResourceType.SERVER_DATA
                || !LNEPlatform.isModLoaded("curios")
                || LNEPlatform.isModLoaded("spell_engine")) {
            return;
        }
        event.addPackFinders(Identifier.of(RPGLoot.MOD_ID, CHARM_SLOT_PACK), ResourceType.SERVER_DATA,
                Text.literal("Loot & Explore Curios Relics"), ResourcePackSource.BUILTIN, true,
                ResourcePackProfile.InsertionPosition.TOP);
    }
}

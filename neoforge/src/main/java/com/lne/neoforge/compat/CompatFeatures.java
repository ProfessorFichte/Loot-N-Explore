package com.lne.neoforge.compat;

import com.google.common.collect.ImmutableSet;
import more_rpg_loot.compat.accessories.AccessoriesCompat;
import more_rpg_loot.worldgen.villages.LNEVillagerProfessions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

public class CompatFeatures {
    public static void init() {
        AccessoriesCompat.init();
        LNEVillagerProfessions.poiRegistrar = (id, block) -> {
            var blockStates = ImmutableSet.copyOf(block.getStateManager().getStates());
            var poiType = new PointOfInterestType(blockStates, 1, 1);
            Registry.register(Registries.POINT_OF_INTEREST_TYPE, id, poiType);
            return poiType;
        };
    }
}

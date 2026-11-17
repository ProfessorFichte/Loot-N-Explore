package lne.worldgen.structures;

import lne.LneMod;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.structure.Structure;

public interface StructureTags {
    TagKey<Structure> MONARCHS_TEMPLE_LOCATED = register("frozen_depths/monarchs_temple");
    TagKey<Structure> MONARCHS_CASTLE_LOCATED = register("frozen_depths/monarchs_castle");
    TagKey<Structure> GLACIAL_TOMB_LOCATED = register("frozen_depths/glacial_tomb");
    TagKey<Structure> ON_FROST_TOWER_MAPS = register("on_frost_tower_maps");
    TagKey<Structure> ON_DESERT_PYRAMID_MAPS = register("on_desert_pyramid_maps");
    TagKey<Structure> ON_PILLAGER_OUTPOST_MAPS = register("on_pillager_outpost_maps");

    private static TagKey<Structure> register(String id) {
        return TagKey.of(RegistryKeys.STRUCTURE,  LneMod.id(id));
    }
}

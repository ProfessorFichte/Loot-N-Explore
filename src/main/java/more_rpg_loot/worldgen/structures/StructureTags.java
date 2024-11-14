package more_rpg_loot.worldgen.structures;

import more_rpg_loot.RPGLoot;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.structure.Structure;

public interface StructureTags {
    TagKey<Structure> SMALL_MONSTER_QUEST = register("monster_quest");
    TagKey<Structure> SMALL_TREASURE= register("small_treasure");

    private static TagKey<Structure> register(String id) {
        return TagKey.of(RegistryKeys.STRUCTURE,  RPGLoot.id(id));
    }
}

package more_rpg_loot.worldgen.structure;

import more_rpg_loot.RPGLoot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructureTypes {

    public static final StructureType<GlacialTombStructure> GLACIAL_TOMB =
            () -> GlacialTombStructure.CODEC;

    public static void register() {
        Registry.register(
                Registries.STRUCTURE_TYPE,
                RPGLoot.id("glacial_tomb"),
                GLACIAL_TOMB
        );
    }
}

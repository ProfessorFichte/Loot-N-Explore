package lne.worldgen.structure;

import lne.LneMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructureTypes {

    public static final StructureType<GlacialTombStructure> GLACIAL_TOMB =
            () -> GlacialTombStructure.CODEC;

    public static void register() {
        Registry.register(
                Registries.STRUCTURE_TYPE,
                LneMod.id("glacial_tomb"),
                GLACIAL_TOMB
        );
    }
}

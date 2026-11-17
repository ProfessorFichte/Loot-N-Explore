package lne.worldgen.structure;

import lne.LneMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructureTypes {

    public static final StructureType<AnchoredJigsawStructure> ANCHORED_JIGSAW =
            () -> AnchoredJigsawStructure.CODEC;
    public static final StructurePoolElementType<GroundedPoolElement> GROUNDED_SINGLE =
            () -> GroundedPoolElement.CODEC;
    public static final StructurePoolElementType<UnadaptedPoolElement> UNADAPTED =
            () -> UnadaptedPoolElement.CODEC;

    public static void register() {
        Registry.register(
                Registries.STRUCTURE_TYPE,
                LneMod.id("anchored_jigsaw"),
                ANCHORED_JIGSAW
        );
        Registry.register(
                Registries.STRUCTURE_POOL_ELEMENT,
                LneMod.id("grounded_single_pool_element"),
                GROUNDED_SINGLE
        );
        Registry.register(
                Registries.STRUCTURE_POOL_ELEMENT,
                LneMod.id("unadapted"),
                UNADAPTED
        );
    }
}

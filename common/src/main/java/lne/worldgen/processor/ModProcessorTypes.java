package lne.worldgen.processor;

import lne.LneMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.processor.StructureProcessorType;

public class ModProcessorTypes {

    public static final StructureProcessorType<GlacialTombProcessor> GLACIAL_TOMB =
            () -> GlacialTombProcessor.CODEC;
    public static final StructureProcessorType<IcicleGrowthProcessor> ICICLE_GROWTH =
            () -> IcicleGrowthProcessor.CODEC;

    public static void register() {
        Registry.register(
                Registries.STRUCTURE_PROCESSOR,
                LneMod.id("glacial_tomb"),
                GLACIAL_TOMB
        );
        Registry.register(
                Registries.STRUCTURE_PROCESSOR,
                LneMod.id("icicle_growth"),
                ICICLE_GROWTH
        );
    }
}

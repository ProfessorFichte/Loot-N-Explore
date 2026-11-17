package lne.worldgen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class GroundedPoolElement extends SinglePoolElement {
    public static final MapCodec<GroundedPoolElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            locationGetter(),
            processorsGetter(),
            projectionGetter(),
            overrideLiquidSettingsGetter(),
            Codec.INT.fieldOf("ground_level_delta").forGetter(element -> element.groundLevelDelta)
    ).apply(instance, GroundedPoolElement::new));

    private final int groundLevelDelta;

    public GroundedPoolElement(Either<Identifier, StructureTemplate> location,
                               RegistryEntry<StructureProcessorList> processors,
                               StructurePool.Projection projection,
                               Optional<StructureLiquidSettings> overrideLiquidSettings,
                               int groundLevelDelta) {
        super(location, processors, projection, overrideLiquidSettings);
        this.groundLevelDelta = groundLevelDelta;
    }

    @Override
    public int getGroundLevelDelta() {
        return this.groundLevelDelta;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return ModStructureTypes.GROUNDED_SINGLE;
    }

    @Override
    public String toString() {
        return "Grounded[" + this.location + "]";
    }
}

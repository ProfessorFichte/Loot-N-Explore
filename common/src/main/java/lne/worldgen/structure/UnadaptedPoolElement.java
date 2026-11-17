package lne.worldgen.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;

// Reports a non-rigid projection so the terrain adaptation skips this piece; the wrapped element still places rigidly.
public class UnadaptedPoolElement extends StructurePoolElement {
    public static final MapCodec<UnadaptedPoolElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            StructurePoolElement.CODEC.fieldOf("element").forGetter(element -> element.element)
    ).apply(instance, UnadaptedPoolElement::new));

    private final StructurePoolElement element;

    public UnadaptedPoolElement(StructurePoolElement element) {
        super(StructurePool.Projection.TERRAIN_MATCHING);
        this.element = element;
    }

    @Override
    public Vec3i getStart(StructureTemplateManager manager, BlockRotation rotation) {
        return this.element.getStart(manager, rotation);
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getStructureBlockInfos(StructureTemplateManager manager, BlockPos pos,
                                                                             BlockRotation rotation, Random random) {
        return this.element.getStructureBlockInfos(manager, pos, rotation, random);
    }

    @Override
    public BlockBox getBoundingBox(StructureTemplateManager manager, BlockPos pos, BlockRotation rotation) {
        return this.element.getBoundingBox(manager, pos, rotation);
    }

    @Override
    public boolean generate(StructureTemplateManager manager, StructureWorldAccess world, StructureAccessor structureAccessor,
                            ChunkGenerator chunkGenerator, BlockPos pos, BlockPos pivot, BlockRotation rotation, BlockBox box,
                            Random random, StructureLiquidSettings liquidSettings, boolean keepJigsaws) {
        return this.element.generate(manager, world, structureAccessor, chunkGenerator, pos, pivot, rotation, box, random,
                liquidSettings, keepJigsaws);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return ModStructureTypes.UNADAPTED;
    }

    @Override
    public String toString() {
        return "Unadapted[" + this.element + "]";
    }
}

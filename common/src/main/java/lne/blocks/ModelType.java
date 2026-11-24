package lne.blocks;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.IntProperty;
import org.jetbrains.annotations.Nullable;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import static lne.LneMod.MOD_ID;

public sealed interface ModelType {

    void generate(BlockStateModelGenerator generator, Block block);

    default void generateItemModel(ItemModelGenerator generator, Item item, String blockName) {
    }

    record CubeAll() implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerCubeAllModelTexturePool(block);
        }
    }

    record ExistingTexture(Identifier textureId) implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
        }
    }

    record CubeBottomTop() implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerSingleton(block, net.minecraft.data.client.TexturedModel.CUBE_BOTTOM_TOP);
        }
    }

    record Lantern() implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerLantern(block);
        }

    }

    record FlowerPot(Block pottedBlock) implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerFlowerPotPlant(block, pottedBlock, BlockStateModelGenerator.TintType.NOT_TINTED);
        }
    }

    record ParentReference(String parentModel) implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
        }
    }

    record PoolVariant(Block baseBlock, VariantType variantType) implements ModelType {
        public enum VariantType { STAIRS, SLAB, WALL }

        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
        }
    }

    record Custom(boolean generateItemModel) implements ModelType {
        public Custom() {
            this(false);
        }

        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
        }

        @Override
        public void generateItemModel(ItemModelGenerator generator, Item item, String blockName) {
            if (generateItemModel) {
                Models.GENERATED.upload(
                    Identifier.of(MOD_ID, "item/" + blockName),
                    net.minecraft.data.client.TextureMap.layer0(
                        Identifier.of(MOD_ID, "item/blocks/" + blockName)
                    ),
                    generator.writer
                );
            }
        }
    }

    record ColumnVariants(IntProperty property, String folder, @Nullable Identifier end) implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            var id = Registries.BLOCK.getId(block);
            String base = "block/" + folder + id.getPath();
            Identifier endTexture = end != null ? end : Identifier.of(id.getNamespace(), base + "_top");
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                    .coordinate(BlockStateVariantMap.create(property).register(value -> {
                        Identifier side = Identifier.of(id.getNamespace(), base + "_" + value);
                        Identifier model = Models.CUBE_COLUMN.upload(side, TextureMap.sideEnd(side, endTexture), generator.modelCollector);
                        return BlockStateVariant.create().put(VariantSettings.MODEL, model);
                    })));
            int first = property.getValues().iterator().next();
            generator.registerParentedItemModel(block, Identifier.of(id.getNamespace(), base + "_" + first));
        }
    }

    record Column(String folder, @Nullable Identifier end) implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            var id = Registries.BLOCK.getId(block);
            Identifier side = Identifier.of(id.getNamespace(), "block/" + folder + id.getPath());
            Identifier endTexture = end != null ? end : side.withSuffixedPath("_top");
            Identifier model = Models.CUBE_COLUMN.upload(side, TextureMap.sideEnd(side, endTexture), generator.modelCollector);
            generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, model));
            generator.registerParentedItemModel(block, model);
        }
    }

    record HorizontalFacing(String folder, @Nullable String itemTextureFolder) implements ModelType {
        public HorizontalFacing(String folder) {
            this(folder, null);
        }

        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            var id = Registries.BLOCK.getId(block);
            Identifier model = Identifier.of(id.getNamespace(), "block/" + folder + id.getPath());
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, model))
                    .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
            if (itemTextureFolder == null) {
                generator.registerParentedItemModel(block, model);
            }
        }

        @Override
        public void generateItemModel(ItemModelGenerator generator, Item item, String blockName) {
            if (itemTextureFolder != null) {
                Models.GENERATED.upload(Identifier.of(MOD_ID, "item/" + blockName),
                        TextureMap.layer0(Identifier.of(MOD_ID, "item/" + itemTextureFolder + blockName)), generator.writer);
            }
        }
    }

    record SimpleBlockItem() implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerSimpleState(block);
        }
    }
}

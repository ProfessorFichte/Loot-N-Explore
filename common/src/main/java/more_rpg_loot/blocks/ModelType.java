package more_rpg_loot.blocks;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

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

    record FlowerPot(Block pottedBlock, BlockStateModelGenerator.TintType tintType) implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerFlowerPotPlant(block, pottedBlock, tintType);
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

    record SimpleBlockItem() implements ModelType {
        @Override
        public void generate(BlockStateModelGenerator generator, Block block) {
            generator.registerSimpleState(block);
        }
    }
}

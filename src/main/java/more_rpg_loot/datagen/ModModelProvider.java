package more_rpg_loot.datagen;

import com.google.gson.JsonObject;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.blocks.ModelType;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.compat.spell_engine.LNE_Weapons;
import more_rpg_loot.compat.spell_engine.SmithingTemplates;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Track texture pools for handling variants (stairs, slab, wall)
        Map<Block, BlockStateModelGenerator.BlockTexturePool> texturePools = new HashMap<>();

        // First pass: Generate base blocks and create texture pools
        for (var entry : ModBlocks.all) {
            ModelType modelType = entry.modelType();

            // Handle pool variants specially - they need the base block's pool
            if (modelType instanceof ModelType.PoolVariant poolVariant) {
                // Find the base block by name pattern (e.g., "blue_ice_brick_slab" -> "blue_ice_bricks")
                String baseName = entry.name()
                        .replace("_slab", "s")
                        .replace("_stairs", "s")
                        .replace("_wall", "s");

                ModBlocks.Entry baseEntry = ModBlocks.all.stream()
                        .filter(e -> e.name().equals(baseName))
                        .findFirst()
                        .orElse(null);

                if (baseEntry != null) {
                    Block baseBlock = baseEntry.block();
                    // Create pool if it doesn't exist
                    texturePools.computeIfAbsent(baseBlock, b ->
                        blockStateModelGenerator.registerCubeAllModelTexturePool(b));

                    // Generate the variant using the pool
                    BlockStateModelGenerator.BlockTexturePool pool = texturePools.get(baseBlock);
                    switch (poolVariant.variantType()) {
                        case STAIRS -> pool.stairs(entry.block());
                        case SLAB -> pool.slab(entry.block());
                        case WALL -> pool.wall(entry.block());
                    }
                }
            } else if (modelType instanceof ModelType.CubeAll) {
                // For CubeAll blocks, just create the pool (this registers the block)
                // Don't call modelType.generate() separately to avoid duplicate registration
                texturePools.computeIfAbsent(entry.block(), b ->
                    blockStateModelGenerator.registerCubeAllModelTexturePool(b));
            } else if (!(modelType instanceof ModelType.Custom)) {
                // Generate other non-custom blocks (Lantern, FlowerPot, CubeBottomTop, etc.)
                modelType.generate(blockStateModelGenerator, entry.block());
            }
            // Custom models are skipped - they're handled manually via JSON files
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Generate item models for blocks that need custom item models
        for (var entry : ModBlocks.all) {
            entry.modelType().generateItemModel(itemModelGenerator, entry.item(), entry.name());
        }

        // Generate item models for all items
        for (var entry : more_rpg_loot.item.CommonItems.all) {
            entry.modelType().generate(itemModelGenerator, entry.item(), entry.name());
        }

        for (var entry : more_rpg_loot.item.ModSpawnEggs.all) {
            entry.modelType().generate(itemModelGenerator, entry.item(), entry.name());
        }

        // Generate item models for ALL compatibility items (regardless of mod loading)
        // This ensures models exist even if the compat mods aren't loaded during datagen
        // NOTE: We pass null for the item since we only need the name for model generation
        for (var entry : more_rpg_loot.compat.items.CompatItems.getAllEntries()) {
            entry.modelType().generate(itemModelGenerator, null, entry.name());
        }

        // SPELL ENGINE ITEMS (Relics, Weapons, Smithing Templates)
        // Generate models when spell_engine is loaded
        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {

            // RELICS - Generate "item/generated" models with texture path: item/relics/{name}
            for (var entry : LNE_Relics.entries) {
                Item item = entry.item().get();
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());

                // Create JSON model with "item/generated" parent
                JsonObject json = new JsonObject();
                json.addProperty("parent", "item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", MOD_ID + ":item/relics/" + entry.name());
                json.add("textures", textures);

                // Write the model JSON file
                itemModelGenerator.writer.accept(modelId, () -> json);
            }

            for (var entry : LNE_Weapons.entries) {
                Item item = entry.item();
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());
                JsonObject json = new JsonObject();
                json.addProperty("parent", "item/handheld");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", MOD_ID + ":item/weapons/" + entry.name());
                json.add("textures", textures);

                itemModelGenerator.writer.accept(modelId, () -> json);
            }

            // SMITHING TEMPLATES - Generate "item/generated" models with texture path: item/smithing_templates/{key}_upgrade
            for (var entry : SmithingTemplates.ENTRIES) {
                Item item = entry.item();
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());

                JsonObject json = new JsonObject();
                json.addProperty("parent", "item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", MOD_ID + ":item/template/" + entry.templateKey() + "_upgrade");
                json.add("textures", textures);

                itemModelGenerator.writer.accept(modelId, () -> json);
            }
        }
    }
}




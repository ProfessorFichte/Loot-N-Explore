package more_rpg_loot.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.blocks.ModelType;
import more_rpg_loot.compat.spell_engine.LNE_Relics;
import more_rpg_loot.compat.spell_engine.SmithingTemplates;
import more_rpg_loot.item.ItemModelType;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
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
        Map<Block, BlockStateModelGenerator.BlockTexturePool> texturePools = new HashMap<>();
        Map<String, ModBlocks.Entry> entriesByName = new HashMap<>();
        for (var entry : ModBlocks.all) {
            entriesByName.put(entry.name(), entry);
        }

        for (var entry : ModBlocks.all) {
            ModelType modelType = entry.modelType();

            if (modelType instanceof ModelType.PoolVariant poolVariant) {
                String baseName = entry.name()
                        .replace("_slab", "s")
                        .replace("_stairs", "s")
                        .replace("_wall", "s");

                ModBlocks.Entry baseEntry = entriesByName.get(baseName);

                if (baseEntry != null) {
                    Block baseBlock = baseEntry.block();
                    texturePools.computeIfAbsent(baseBlock, b ->
                        blockStateModelGenerator.registerCubeAllModelTexturePool(b));

                    BlockStateModelGenerator.BlockTexturePool pool = texturePools.get(baseBlock);
                    switch (poolVariant.variantType()) {
                        case STAIRS -> pool.stairs(entry.block());
                        case SLAB -> pool.slab(entry.block());
                        case WALL -> pool.wall(entry.block());
                    }
                }
            } else if (modelType instanceof ModelType.CubeAll) {
                texturePools.computeIfAbsent(entry.block(), b ->
                    blockStateModelGenerator.registerCubeAllModelTexturePool(b));
            } else if (!(modelType instanceof ModelType.Custom)) {
                modelType.generate(blockStateModelGenerator, entry.block());
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (var entry : ModBlocks.all) {
            entry.modelType().generateItemModel(itemModelGenerator, entry.item(), entry.name());
        }

        for (var entry : more_rpg_loot.item.CommonItems.all) {
            entry.modelType().generate(itemModelGenerator, entry.item(), entry.name());
        }

        for (var entry : more_rpg_loot.item.ModSpawnEggs.all) {
            entry.modelType().generate(itemModelGenerator, entry.item(), entry.name());
        }

        for (var entry : more_rpg_loot.compat.items.CompatItems.getAllEntries()) {
            entry.modelType().generate(itemModelGenerator, null, entry.name());
        }

        for (var entry : LNE_WeaponItems.entries) {
            Identifier modelId = Identifier.of(MOD_ID, "item/" + entry.name());
            JsonObject json = new JsonObject();
            json.addProperty("parent", "minecraft:item/handheld");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", MOD_ID + ":item/weapons/" + entry.name());
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        }

        for (var entry : LNE_WeaponItems.rangedEntries) {
            Identifier itemId = Identifier.of(MOD_ID, entry.name());
            if (entry.name().endsWith("_bow")) {
                new ItemModelType.Bow().generate(itemModelGenerator, entry.item(), entry.name());
            } else if (entry.name().endsWith("_crossbow")) {
                generateCrossbowModel(itemModelGenerator, itemId, entry.name());
            }
        }

        if (FabricLoader.getInstance().isModLoaded("spell_engine")) {
            for (var entry : LNE_Relics.entries) {
                Item item = entry.item().get();
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());

                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", MOD_ID + ":item/relics/" + entry.name());
                json.add("textures", textures);
                itemModelGenerator.writer.accept(modelId, () -> json);
            }

            for (var entry : SmithingTemplates.ENTRIES) {
                Item item = entry.item();
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());

                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", MOD_ID + ":item/template/" + entry.templateKey() + "_upgrade");
                json.add("textures", textures);
                itemModelGenerator.writer.accept(modelId, () -> json);
            }
        }
    }



    private void generateCrossbowModel(ItemModelGenerator itemModelGenerator, Identifier itemId, String name) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);

        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/crossbow");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":item/weapons/" + name);
        json.add("textures", textures);

        JsonArray overrides = new JsonArray();

        JsonObject pullingOverride = new JsonObject();
        JsonObject pullingPredicate = new JsonObject();
        pullingPredicate.addProperty("pulling", 1);
        pullingOverride.add("predicate", pullingPredicate);
        pullingOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_0");
        overrides.add(pullingOverride);

        for (int i = 0; i <= 2; i++) {
            JsonObject pullOverride = new JsonObject();
            JsonObject pullPredicate = new JsonObject();
            pullPredicate.addProperty("pulling", 1);
            pullPredicate.addProperty("pull", (i + 1) * 0.333);
            pullOverride.add("predicate", pullPredicate);
            pullOverride.addProperty("model", MOD_ID + ":item/" + name + "_pulling_" + i);
            overrides.add(pullOverride);
        }

        JsonObject chargedOverride = new JsonObject();
        JsonObject chargedPredicate = new JsonObject();
        chargedPredicate.addProperty("charged", 1);
        chargedOverride.add("predicate", chargedPredicate);
        chargedOverride.addProperty("model", MOD_ID + ":item/" + name + "_arrow");
        overrides.add(chargedOverride);

        JsonObject fireworkOverride = new JsonObject();
        JsonObject fireworkPredicate = new JsonObject();
        fireworkPredicate.addProperty("charged", 1);
        fireworkPredicate.addProperty("firework", 1);
        fireworkOverride.add("predicate", fireworkPredicate);
        fireworkOverride.addProperty("model", MOD_ID + ":item/" + name + "_firework");
        overrides.add(fireworkOverride);

        json.add("overrides", overrides);
        itemModelGenerator.writer.accept(modelId, () -> json);

        for (int i = 0; i <= 2; i++) {
            Identifier pullingModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_pulling_" + i);
            JsonObject pullingJson = new JsonObject();
            pullingJson.addProperty("parent", MOD_ID + ":item/" + name);
            JsonObject pullingTextures = new JsonObject();
            pullingTextures.addProperty("layer0", MOD_ID + ":item/weapons/bow_pulling/" + name + "_pulling_" + i);
            pullingJson.add("textures", pullingTextures);
            itemModelGenerator.writer.accept(pullingModelId, () -> pullingJson);
        }

        Identifier arrowModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_arrow");
        JsonObject arrowJson = new JsonObject();
        arrowJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject arrowTextures = new JsonObject();
        arrowTextures.addProperty("layer0", MOD_ID + ":item/weapons/" + name + "_arrow");
        arrowJson.add("textures", arrowTextures);
        itemModelGenerator.writer.accept(arrowModelId, () -> arrowJson);

        Identifier fireworkModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_firework");
        JsonObject fireworkJson = new JsonObject();
        fireworkJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject fireworkTextures = new JsonObject();
        fireworkTextures.addProperty("layer0", MOD_ID + ":item/weapons/" + name + "_firework");
        fireworkJson.add("textures", fireworkTextures);
        itemModelGenerator.writer.accept(fireworkModelId, () -> fireworkJson);
    }
}




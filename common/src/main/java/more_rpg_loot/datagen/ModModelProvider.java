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
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
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
                        createCubeAllTexturePool(blockStateModelGenerator, b, "block/frozen_depths/" + baseEntry.name()));

                    BlockStateModelGenerator.BlockTexturePool pool = texturePools.get(baseBlock);
                    switch (poolVariant.variantType()) {
                        case STAIRS -> pool.stairs(entry.block());
                        case SLAB -> pool.slab(entry.block());
                        case WALL -> pool.wall(entry.block());
                    }
                }
            } else if (modelType instanceof ModelType.CubeAll) {
                texturePools.computeIfAbsent(entry.block(), b ->
                    createCubeAllTexturePool(blockStateModelGenerator, b, "block/frozen_depths/" + entry.name()));
            } else if (modelType instanceof ModelType.ExistingTexture existingTexture) {
                texturePools.computeIfAbsent(entry.block(), b ->
                    blockStateModelGenerator.new BlockTexturePool(TextureMap.all(existingTexture.textureId()))
                            .base(b, Models.CUBE_ALL));
            } else if (!(modelType instanceof ModelType.Custom)) {
                modelType.generate(blockStateModelGenerator, entry.block());
            }
        }
    }

    // registerCubeAllModelTexturePool always derives the texture id from the block's own registry name with
    // no override, so a nested (theme-sorted) texture path needs a manually built pool instead.
    private BlockStateModelGenerator.BlockTexturePool createCubeAllTexturePool(BlockStateModelGenerator generator, Block block, String textureId) {
        return generator.new BlockTexturePool(TextureMap.all(Identifier.of(MOD_ID, textureId)))
                .base(block, Models.CUBE_ALL);
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
            textures.addProperty("layer0", MOD_ID + ":item/weapons/" + weaponTheme(entry.name()) + "/" + entry.name());
            json.add("textures", textures);
            itemModelGenerator.writer.accept(modelId, () -> json);
        }

        for (var entry : LNE_WeaponItems.rangedEntries) {
            Identifier itemId = Identifier.of(MOD_ID, entry.name());
            String theme = weaponTheme(entry.name());
            if (entry.name().endsWith("_bow")) {
                new ItemModelType.Bow("item/weapons/" + theme + "/").generate(itemModelGenerator, entry.item(), entry.name());
            } else if (entry.name().endsWith("_crossbow")) {
                generateCrossbowModel(itemModelGenerator, itemId, entry.name(), theme);
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
                textures.addProperty("layer0", MOD_ID + ":item/relics/" + relicTheme(entry.name()) + "/" + entry.name());
                json.add("textures", textures);
                itemModelGenerator.writer.accept(modelId, () -> json);
            }

            for (var entry : SmithingTemplates.ENTRIES) {
                Item item = entry.item();
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + itemId.getPath());

                String theme = entry.templateKey().equals("frostmonarch") ? "frozen_depths" : "generic";
                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", MOD_ID + ":item/template/" + theme + "/" + entry.templateKey() + "_upgrade");
                json.add("textures", textures);
                itemModelGenerator.writer.accept(modelId, () -> json);
            }
        }
    }

    // Only "glacial" weapons belong to the Frozen Depths theme - matches the ModItemTagProvider weapon-theme tag convention.
    private static String weaponTheme(String name) {
        return name.contains("glacial") || name.equals("frozen_bow") ? "frozen_depths" : "generic";
    }

    private static final java.util.Set<String> FROZEN_DEPTHS_RELICS = java.util.Set.of(
            "eternal_snowflake", "glacier_shard", "frozen_rib", "frozen_soul");

    private static String relicTheme(String name) {
        return FROZEN_DEPTHS_RELICS.contains(name) ? "frozen_depths" : "generic";
    }



    private void generateCrossbowModel(ItemModelGenerator itemModelGenerator, Identifier itemId, String name, String theme) {
        Identifier modelId = Identifier.of(itemId.getNamespace(), "item/" + name);
        String texturePath = "item/weapons/" + theme + "/";

        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/crossbow");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", MOD_ID + ":" + texturePath + name);
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
            pullingTextures.addProperty("layer0", MOD_ID + ":" + texturePath + "bow_pulling/" + name + "_pulling_" + i);
            pullingJson.add("textures", pullingTextures);
            itemModelGenerator.writer.accept(pullingModelId, () -> pullingJson);
        }

        Identifier arrowModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_arrow");
        JsonObject arrowJson = new JsonObject();
        arrowJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject arrowTextures = new JsonObject();
        arrowTextures.addProperty("layer0", MOD_ID + ":" + texturePath + name + "_arrow");
        arrowJson.add("textures", arrowTextures);
        itemModelGenerator.writer.accept(arrowModelId, () -> arrowJson);

        Identifier fireworkModelId = Identifier.of(itemId.getNamespace(), "item/" + name + "_firework");
        JsonObject fireworkJson = new JsonObject();
        fireworkJson.addProperty("parent", MOD_ID + ":item/" + name);
        JsonObject fireworkTextures = new JsonObject();
        fireworkTextures.addProperty("layer0", MOD_ID + ":" + texturePath + name + "_firework");
        fireworkJson.add("textures", fireworkTextures);
        itemModelGenerator.writer.accept(fireworkModelId, () -> fireworkJson);
    }
}




package more_rpg_loot.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public sealed interface ItemModelType {

    void generate(ItemModelGenerator generator, Item item, String itemName);

    record Generated(String texturePath) implements ItemModelType {
        public Generated() {
            this("item/");
        }

        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            Models.GENERATED.upload(
                Identifier.of(MOD_ID, "item/" + itemName),
                net.minecraft.data.client.TextureMap.layer0(
                    Identifier.of(MOD_ID, texturePath + itemName)
                ),
                generator.writer
            );
        }
    }

    record Handheld(String texturePath) implements ItemModelType {
        public Handheld() {
            this("item/");
        }

        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            Models.HANDHELD.upload(
                Identifier.of(MOD_ID, "item/" + itemName),
                net.minecraft.data.client.TextureMap.layer0(
                    Identifier.of(MOD_ID, texturePath + itemName)
                ),
                generator.writer
            );
        }
    }

    record Bow(String texturePath) implements ItemModelType {
        public Bow() {
            this("item/weapons/");
        }

        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
            Identifier modelId = Identifier.of(MOD_ID, "item/" + itemName);

            JsonObject json = new JsonObject();
            json.addProperty("parent", "minecraft:item/bow");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", MOD_ID + ":" + texturePath + itemName);
            json.add("textures", textures);

            JsonArray overrides = new JsonArray();

            JsonObject pullingOverride = new JsonObject();
            JsonObject pullingPredicate = new JsonObject();
            pullingPredicate.addProperty("pulling", 1);
            pullingOverride.add("predicate", pullingPredicate);
            pullingOverride.addProperty("model", MOD_ID + ":item/" + itemName + "_pulling_0");
            overrides.add(pullingOverride);

            for (int i = 0; i <= 2; i++) {
                JsonObject pullOverride = new JsonObject();
                JsonObject pullPredicate = new JsonObject();
                pullPredicate.addProperty("pulling", 1);
                pullPredicate.addProperty("pull", (i + 1) * 0.333);
                pullOverride.add("predicate", pullPredicate);
                pullOverride.addProperty("model", MOD_ID + ":item/" + itemName + "_pulling_" + i);
                overrides.add(pullOverride);
            }

            json.add("overrides", overrides);
            generator.writer.accept(modelId, () -> json);

            for (int i = 0; i <= 2; i++) {
                Identifier pullingModelId = Identifier.of(MOD_ID, "item/" + itemName + "_pulling_" + i);
                JsonObject pullingJson = new JsonObject();
                pullingJson.addProperty("parent", MOD_ID + ":item/" + itemName);
                JsonObject pullingTextures = new JsonObject();
                pullingTextures.addProperty("layer0", MOD_ID + ":" + texturePath + "bow_pulling/" + itemName + "_pulling_" + i);
                pullingJson.add("textures", pullingTextures);
                generator.writer.accept(pullingModelId, () -> pullingJson);
            }
        }
    }

    record SpawnEgg() implements ItemModelType {
        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
        }
    }

    record Custom() implements ItemModelType {
        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
        }
    }

    record None() implements ItemModelType {
        @Override
        public void generate(ItemModelGenerator generator, Item item, String itemName) {
        }
    }
}

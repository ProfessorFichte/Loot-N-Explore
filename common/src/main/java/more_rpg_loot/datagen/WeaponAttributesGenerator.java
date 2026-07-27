package more_rpg_loot.datagen;

import com.google.gson.JsonObject;
import more_rpg_loot.RPGLoot;
import more_rpg_loot.item.CommonItems;
import more_rpg_loot.item.weapons.LNE_WeaponItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class WeaponAttributesGenerator implements DataProvider {
    private final FabricDataOutput output;

    public WeaponAttributesGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (var entry : LNE_WeaponItems.maceEntries) {
            futures.add(generateWeaponAttribute(writer, entry.id(), "bettercombat:mace"));
        }
        for (var entry : LNE_WeaponItems.rangedEntries) {
            String parent = entry.name().contains("crossbow") ? "bettercombat:crossbow" : "bettercombat:bow";
            futures.add(generateWeaponAttribute(writer, entry.id(), parent));
        }

        futures.add(generateWeaponAttribute(writer, RPGLoot.id(CommonItems.MONARCHS_FROST_STAFF.name()), "bettercombat:staff"));
        futures.add(generateWeaponAttribute(writer, RPGLoot.id(CommonItems.GUARDS_FROST_LANCE.name()), "bettercombat:spear"));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<?> generateWeaponAttribute(DataWriter writer, Identifier id, String parent) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", parent);
        Path path = output.getResolver(DataOutput.OutputType.DATA_PACK, "weapon_attributes").resolveJson(id);
        return DataProvider.writeToPath(writer, json, path);
    }

    @Override
    public String getName() {
        return "Weapon Attributes - " + MOD_ID;
    }
}

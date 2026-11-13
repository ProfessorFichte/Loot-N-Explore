package lne.datagen;

import com.mrpg_lib.datagen.StructureBlockModifierProvider;
import com.mrpg_lib.worldgen.modifier.StructureBlockModifier;
import lne.blocks.ModBlocks;
import lne.util.LneLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static lne.LneMod.MOD_ID;

public class ModStructureBlockModifierProvider extends StructureBlockModifierProvider {
    public ModStructureBlockModifierProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate(BiConsumer<Identifier, StructureBlockModifier> exporter) {
        exporter.accept(Identifier.of(MOD_ID, "stronghold_library_suspicious_bookshelves"), new StructureBlockModifier(
                List.of("minecraft:stronghold"), List.of("minecraft:shli"), "minecraft:bookshelf", 0.03F, 4,
                ModBlocks.SUSPICIOUS_BOOKSHELF.block().getDefaultState(),
                Optional.of(LneLootTables.ARCHAEOLOGY_STRONGHOLD_LIBRARY.getValue()), List.of()));
        bookshelves(exporter, "woodland_mansion", List.of("minecraft:mansion"), List.of("minecraft:wmp"), 0.05F, 2,
                LneLootTables.ARCHAEOLOGY_WOODLAND_MANSION);
        bookshelves(exporter, "snowy_village", List.of("minecraft:village_snowy"), List.of(), 0.15F, 3,
                LneLootTables.ARCHAEOLOGY_SNOWY_VILLAGE);
        bookshelves(exporter, "desert_village", List.of("minecraft:village_desert"), List.of(), 0.15F, 3,
                LneLootTables.ARCHAEOLOGY_DESERT_VILLAGE);
        bookshelves(exporter, "plains_savanna_taiga_village",
                List.of("minecraft:village_plains", "minecraft:village_savanna", "minecraft:village_taiga"), List.of(), 0.15F, 3,
                LneLootTables.ARCHAEOLOGY_PLAINS_SAVANNA_TAIGA_VILLAGE);
    }

    private static void bookshelves(BiConsumer<Identifier, StructureBlockModifier> exporter, String name, List<String> structures,
                                    List<String> pieces, float chance, int maxPerPiece, RegistryKey<LootTable> lootTable) {
        exporter.accept(Identifier.of(MOD_ID, name + "_suspicious_bookshelves"), new StructureBlockModifier(
                structures, pieces, "minecraft:bookshelf", chance, maxPerPiece,
                ModBlocks.SUSPICIOUS_BOOKSHELF.block().getDefaultState(), Optional.of(lootTable.getValue()), List.of()));
    }
}

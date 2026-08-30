package more_rpg_loot.worldgen.villages;

import com.google.common.collect.ImmutableSet;
import java.util.function.BiFunction;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.sounds.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class LNEVillagerProfessions {
    public static BiFunction<Identifier, Block, PointOfInterestType> poiRegistrar;
    public static final RegistryKey<PointOfInterestType> INNKEEPER_POI_KEY = registerKey("innkeeper");
    public static PointOfInterestType INNKEEPER_POI;
    public static VillagerProfession INNKEEPER;

    public static void registerPoiTypes() {
        INNKEEPER_POI = registerPoi("innkeeper", ModBlocks.INNKEEPER_SHELF.block());
    }

    public static void registerProfessions() {
        INNKEEPER = registerProfession("innkeeper", INNKEEPER_POI_KEY, ModSounds.VILLAGER_INNKEEPER.soundEvent());
    }

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type, SoundEvent sound) {
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(MOD_ID, name),
                new VillagerProfession(name,
                        entry -> entry.matchesKey(type),
                        entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(),
                        sound));
    }


    private static PointOfInterestType registerPoi(String name, Block block) {
        return poiRegistrar.apply(Identifier.of(MOD_ID, name), block);
    }

    public static RegistryKey<PointOfInterestType> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(MOD_ID, name));
    }
}

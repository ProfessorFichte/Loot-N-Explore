package more_rpg_loot.worldgen.villages;

import com.google.common.collect.ImmutableSet;
import more_rpg_loot.blocks.ModBlocks;
import more_rpg_loot.sounds.ModSounds;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
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
    public static final RegistryKey<PointOfInterestType> INNKEEPER_POI_KEY = registerKey("innkeeper");
    public static final PointOfInterestType INNKEEPER_POI = registerPoi("innkeeper", ModBlocks.INNKEEPER_SHELF.block());
    public static final VillagerProfession INNKEEPER = registerProfession("innkeeper", INNKEEPER_POI_KEY, ModSounds.VILLAGER_INNKEEPER_EVENT);

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type, SoundEvent sound) {
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(MOD_ID, name),
                new VillagerProfession(name,
                        entry -> entry.matchesKey(type),
                        entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(),
                        sound));
    }


    private static PointOfInterestType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(MOD_ID, name), 1, 1, block);
    }

    public static RegistryKey<PointOfInterestType> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(MOD_ID, name));
    }

    public static void registerVillagers() {
    }
}

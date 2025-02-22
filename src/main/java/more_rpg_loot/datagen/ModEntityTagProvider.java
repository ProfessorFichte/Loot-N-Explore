package more_rpg_loot.datagen;

import more_rpg_loot.entity.ModEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(EntityTypeTags.SKELETONS)
                .add(ModEntities.FROST_HAUNT)
                .add(ModEntities.FROST_MONARCH)
        ;

        getOrCreateTagBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                .add(ModEntities.GLAZE)
                .add(ModEntities.FROST_HAUNT)
                .add(ModEntities.FROST_MONARCH)
        ;

        getOrCreateTagBuilder(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(ModEntities.GLAZE)
                .add(ModEntities.FROST_HAUNT)
                .add(ModEntities.FROST_MONARCH)
        ;

        getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(ModEntities.GLAZE)
        ;

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("c", "bosses")))
                .add(ModEntities.FROST_MONARCH)
        ;

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("frostiful", "benefits_from_cold")))
                .add(ModEntities.FROST_MONARCH)
                .add(ModEntities.FROST_HAUNT)
                .add(ModEntities.GLAZE)
        ;


    }
}

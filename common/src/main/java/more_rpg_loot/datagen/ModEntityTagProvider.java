package more_rpg_loot.datagen;

import more_rpg_loot.entity.ModEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
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
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HAUNT))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_MONARCH))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_GUARD))
        ;

        getOrCreateTagBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GLAZE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HAUNT))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_MONARCH))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HOUND))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_GUARD))
        ;

        getOrCreateTagBuilder(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GLAZE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HAUNT))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_MONARCH))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HOUND))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_GUARD))
        ;

        getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GLAZE))
        ;

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("c", "bosses")))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_MONARCH))
        ;

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("frostiful", "benefits_from_cold")))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_MONARCH))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HAUNT))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GLAZE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_UNDEAD_FROZEN_MAGE))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_FROSTED_RANGER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.FROST_HOUND))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.GENERAL_MONARCHS_SOLDIER))
                .addOptional(Registries.ENTITY_TYPE.getId(ModEntities.MONARCHS_GUARD))
        ;
    }
}

package more_rpg_loot.datagen;

import more_rpg_loot.item.WeaponRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "axes")))
                .add(WeaponRegister.ELDER_GUARDIAN_AXE)
                .add(WeaponRegister.ENDER_DRAGON_AXE)
                .add(WeaponRegister.WITHER_AXE)
                .add(WeaponRegister.GLACIAL_AXE)
        ;
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "swords")))
                .add(WeaponRegister.ELDER_GUARDIAN_SWORD)
                .add(WeaponRegister.ENDER_DRAGON_SWORD)
                .add(WeaponRegister.WITHER_SWORD)
                .add(WeaponRegister.GLACIAL_SWORD)
        ;
        /*
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("dehydration", "hydrating_drinks")))
                .add(CommonItems.BEET_ROOTBEER)
                .add(CommonItems.HOT_CHOCOLATE)
                .add(CommonItems.MALT_EXTRACT)
                .add(CommonItems.SWEET_BERRY_PUNCH);
        ;
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("dehydration", "stronger_hydrating_drinks")))
                .add(CommonItems.ESPRESSO)
                .add(CommonItems.VITAL_DRINK)
        ;
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("dehydration", "hydrating_stew")))
                .add(CommonItems.POTATO_SOUP)
        ;*/
    }
}

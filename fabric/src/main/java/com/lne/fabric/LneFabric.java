package com.lne.fabric;

import lne.command.FrostsiegeCommand;
import lne.command.RelicCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.lne.fabric.compat.CompatFeatures;
import com.lne.fabric.platform.FabricEventsImpl;
import com.lne.fabric.platform.FabricPlatformImpl;
import com.lne.fabric.worldgen.FabricVegetationGeneration;
import lne.LneMod;
import lne.entity.ModEntities;
import lne.item.ModPotions;
import lne.network.EntityHudTextPayload;
import lne.network.StructureMusicPayload;
import lne.network.RelicStacksPayload;
import lne.network.WeaponCooldownPayload;
import lne.platform.LNEEvents;
import lne.platform.LNEPlatform;
import lne.worldgen.villages.LNEVillagerProfessions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;

public final class LneFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LNEPlatform.set(new FabricPlatformImpl());
        LNEEvents.set(new FabricEventsImpl());
        LNEVillagerProfessions.poiRegistrar = (id, block) -> PointOfInterestHelper.register(id, 1, 1, block);
        CompatFeatures.init();
        registerRelicSlotPack();
        registerPayloads();
        LneMod.init();
        LneMod.registerEffects();
        LneMod.registerBlocks();
        LneMod.registerEntities();
        ModEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
        LneMod.registerItems();
        LneMod.registerSounds();
        LneMod.registerItemGroups();
        LneMod.registerVillagePoi();
        LneMod.registerVillageProfessions();
        LneMod.registerVillagerSchedules();
        LneMod.registerServerEvents();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RelicCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> FrostsiegeCommand.register(dispatcher));
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> ModPotions.registerRecipes(builder::registerPotionRecipe));
        FabricVegetationGeneration.generate();
    }

    private static void registerRelicSlotPack() {
        if (LNEPlatform.isModLoaded("spell_engine") || !LNEPlatform.isModLoaded("trinkets")) {
            return;
        }
        FabricLoader.getInstance().getModContainer(LneMod.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(LneMod.id("trinkets_necklace_relics"), container,
                        Text.literal("Loot & Explore Trinkets Relics"), ResourcePackActivationType.ALWAYS_ENABLED));
    }

    private static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(StructureMusicPayload.ID, StructureMusicPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EntityHudTextPayload.ID, EntityHudTextPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RelicStacksPayload.ID, RelicStacksPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(WeaponCooldownPayload.ID, WeaponCooldownPayload.CODEC);
    }
}

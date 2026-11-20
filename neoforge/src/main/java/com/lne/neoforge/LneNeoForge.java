package com.lne.neoforge;

import lne.internal.command.FrostsiegeCommand;
import lne.internal.command.RelicCommand;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import com.lne.neoforge.client.LNEClientPackets;
import com.lne.neoforge.compat.CompatFeatures;
import com.lne.neoforge.compat.curios.CuriosCompat;
import com.lne.neoforge.platform.NeoForgeEventsImpl;
import com.lne.neoforge.platform.NeoForgePlatformImpl;
import lne.LneMod;
import lne.entity.ModEntities;
import lne.item.ModPotions;
import lne.internal.network.EntityHudTextPayload;
import lne.internal.network.StructureMusicPayload;
import lne.internal.network.RelicStacksPayload;
import lne.internal.network.WeaponCooldownPayload;
import lne.internal.platform.LNEEvents;
import lne.internal.platform.LNEPlatform;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import lne.entity.StructureSpawnRules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(LneMod.MOD_ID)
public final class LneNeoForge {
    public LneNeoForge(IEventBus modBus) {
        LNEPlatform.set(new NeoForgePlatformImpl());
        LNEEvents.set(new NeoForgeEventsImpl(modBus));
        CompatFeatures.init();
        LneMod.init();
        LneMod.registerServerEvents();
        modBus.addListener(AddPackFindersEvent.class, CuriosCompat::addPacks);
        modBus.addListener(RegisterEvent.class, LneNeoForge::register);
        modBus.addListener(RegisterPayloadHandlersEvent.class, LneNeoForge::registerPayloads);
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeEventsImpl::dispatchItemGroup);
        modBus.addListener(EntityAttributeCreationEvent.class,
                event -> ModEntities.registerAttributes((type, builder) -> event.put(type, builder.build())));
        modBus.addListener(RegisterSpawnPlacementsEvent.class, LneNeoForge::registerSpawnPlacements);
        NeoForge.EVENT_BUS.addListener(RegisterBrewingRecipesEvent.class,
                event -> ModPotions.registerRecipes(event.getBuilder()::registerPotionRecipe));
        NeoForge.EVENT_BUS.addListener(VillagerTradesEvent.class, NeoForgeEventsImpl::dispatchVillagerTrades);
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> RelicCommand.register(event.getDispatcher()));
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> FrostsiegeCommand.register(event.getDispatcher()));
    }

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        StructureSpawnRules.register(new StructureSpawnRules.Sink() {
            @Override
            public <T extends MobEntity> void register(EntityType<T> type, SpawnLocation location, Heightmap.Type heightmap,
                                                       SpawnRestriction.SpawnPredicate<T> predicate) {
                event.register(type, location, heightmap, predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
            }
        });
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(LneMod.MOD_ID);
        registrar.playToClient(StructureMusicPayload.ID, StructureMusicPayload.CODEC, LneNeoForge::handleMusic);
        registrar.playToClient(EntityHudTextPayload.ID, EntityHudTextPayload.CODEC, LneNeoForge::handleEntityHudText);
        registrar.playToClient(RelicStacksPayload.ID, RelicStacksPayload.CODEC, LneNeoForge::handleRelicStacks);
        registrar.playToClient(WeaponCooldownPayload.ID, WeaponCooldownPayload.CODEC, LneNeoForge::handleWeaponCooldown);
    }

    private static void handleMusic(StructureMusicPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> LNEClientPackets.onMusic(payload));
    }

    private static void handleRelicStacks(RelicStacksPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> LNEClientPackets.onRelicStacks(payload));
    }

    private static void handleWeaponCooldown(WeaponCooldownPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> LNEClientPackets.onWeaponCooldown(payload));
    }

    private static void handleEntityHudText(EntityHudTextPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> LNEClientPackets.onEntityHudText(payload));
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.STATUS_EFFECT, reg -> LneMod.registerEffects());
        event.register(RegistryKeys.BLOCK, reg -> LneMod.registerBlocks());
        event.register(RegistryKeys.ENTITY_TYPE, reg -> LneMod.registerEntities());
        event.register(RegistryKeys.ITEM, reg -> LneMod.registerItems());
        event.register(RegistryKeys.SOUND_EVENT, reg -> LneMod.registerSounds());
        event.register(RegistryKeys.ITEM_GROUP, reg -> LneMod.registerItemGroups());
        event.register(RegistryKeys.POINT_OF_INTEREST_TYPE, reg -> LneMod.registerVillagePoi());
        event.register(RegistryKeys.VILLAGER_PROFESSION, reg -> LneMod.registerVillageProfessions());
        event.register(RegistryKeys.SCHEDULE, reg -> LneMod.registerVillagerSchedules());
    }
}

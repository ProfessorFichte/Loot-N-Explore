package com.lne.neoforge.platform;

import more_rpg_loot.platform.LNEEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NeoForgeEventsImpl implements LNEEvents.Impl {
    private static final Map<RegistryKey<ItemGroup>, List<Consumer<LNEEvents.Entries>>> ITEM_GROUP_FILLERS = new HashMap<>();
    private static final List<TradeRegistration> VILLAGER_TRADES = new ArrayList<>();

    private record TradeRegistration(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factories) {}

    private final IEventBus modBus;

    public NeoForgeEventsImpl(IEventBus modBus) {
        this.modBus = modBus;
    }

    @Override
    public void registerServerWorldTick(Consumer<ServerWorld> listener) {
        NeoForge.EVENT_BUS.addListener(LevelTickEvent.Post.class, event -> {
            if (event.getLevel() instanceof ServerWorld serverWorld) {
                listener.accept(serverWorld);
            }
        });
    }

    @Override
    public void sendToClient(ServerPlayerEntity player, CustomPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void modifyItemGroup(RegistryKey<ItemGroup> group, Consumer<LNEEvents.Entries> filler) {
        ITEM_GROUP_FILLERS.computeIfAbsent(group, k -> new ArrayList<>()).add(filler);
    }

    @Override
    public ItemGroup createItemGroup(Supplier<ItemStack> icon, Text displayName) {
        return ItemGroup.builder().icon(icon).displayName(displayName).build();
    }

    @Override
    public void registerVillagerTrades(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factories) {
        VILLAGER_TRADES.add(new TradeRegistration(profession, level, factories));
    }

    @Override
    public void onLootTableModify(Consumer<LNEEvents.LootTableModifyContext> consumer) {
        NeoForge.EVENT_BUS.addListener(LootTableLoadEvent.class, event -> consumer.accept(new LNEEvents.LootTableModifyContext() {
            @Override
            public RegistryWrapper.WrapperLookup registries() {
                return event.getRegistries();
            }

            @Override
            public Identifier tableId() {
                return event.getName();
            }

            @Override
            public RegistryKey<LootTable> tableKey() {
                return event.getKey();
            }

            @Override
            public boolean isBuiltin() {
                return true;
            }

            @Override
            public void addPool(LootPool pool) {
                event.getTable().addPool(pool);
            }
        }));
    }

    @Override
    public void onServerStarted(Consumer<MinecraftServer> consumer) {
        NeoForge.EVENT_BUS.addListener(ServerStartedEvent.class, event -> consumer.accept(event.getServer()));
    }

    @Override
    public void onDataPackReload(Runnable runnable) {
        NeoForge.EVENT_BUS.addListener(OnDatapackSyncEvent.class, event -> runnable.run());
    }

    @Override
    public void modifyItemComponents(Consumer<LNEEvents.ItemComponentContext> consumer) {
        modBus.addListener(ModifyDefaultComponentsEvent.class, event -> consumer.accept((item, mutator) ->
                event.modify(item, builder -> mutator.accept(new LNEEvents.ComponentSink() {
                    @Override
                    public <T> void add(ComponentType<T> type, T value) {
                        builder.add(type, value);
                    }
                }))));
    }

    public static void dispatchItemGroup(BuildCreativeModeTabContentsEvent event) {
        List<Consumer<LNEEvents.Entries>> fillers = ITEM_GROUP_FILLERS.get(event.getTabKey());
        if (fillers == null) return;
        LNEEvents.Entries adapter = new LNEEvents.Entries() {
            @Override
            public void add(ItemConvertible item) {
                event.add(item);
            }

            @Override
            public void addAfter(ItemConvertible anchor, ItemConvertible item) {
                event.insertAfter(new ItemStack(anchor), new ItemStack(item), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
            }
        };
        fillers.forEach(filler -> filler.accept(adapter));
    }

    public static void dispatchVillagerTrades(VillagerTradesEvent event) {
        for (TradeRegistration reg : VILLAGER_TRADES) {
            if (!reg.profession().equals(event.getType())) continue;
            List<TradeOffers.Factory> list = event.getTrades().get(reg.level());
            if (list != null) {
                reg.factories().accept(list);
            }
        }
    }
}

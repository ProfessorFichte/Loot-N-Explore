package com.lne.fabric.platform;

import more_rpg_loot.platform.LNEEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FabricEventsImpl implements LNEEvents.Impl {
    @Override
    public void registerServerWorldTick(Consumer<ServerWorld> listener) {
        ServerTickEvents.END_WORLD_TICK.register(listener::accept);
    }

    @Override
    public void sendToClient(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void modifyItemGroup(RegistryKey<ItemGroup> group, Consumer<LNEEvents.Entries> filler) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> filler.accept(new LNEEvents.Entries() {
            @Override
            public void add(ItemConvertible item) {
                entries.add(item);
            }

            @Override
            public void addAfter(ItemConvertible anchor, ItemConvertible item) {
                entries.addAfter(anchor, item);
            }
        }));
    }

    @Override
    public ItemGroup createItemGroup(Supplier<ItemStack> icon, Text displayName) {
        return FabricItemGroup.builder().icon(icon).displayName(displayName).build();
    }

    @Override
    public void registerVillagerTrades(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factories) {
        TradeOfferHelper.registerVillagerOffers(profession, level, factories::accept);
    }

    @Override
    public void onLootTableModify(Consumer<LNEEvents.LootTableModifyContext> consumer) {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> consumer.accept(new LNEEvents.LootTableModifyContext() {
            @Override
            public net.minecraft.registry.RegistryWrapper.WrapperLookup registries() {
                return registries;
            }

            @Override
            public net.minecraft.util.Identifier tableId() {
                return key.getValue();
            }

            @Override
            public RegistryKey<net.minecraft.loot.LootTable> tableKey() {
                return key;
            }

            @Override
            public boolean isBuiltin() {
                return source.isBuiltin();
            }

            @Override
            public void addPool(net.minecraft.loot.LootPool pool) {
                tableBuilder.pool(pool);
            }
        }));
    }

    @Override
    public void onServerStarted(Consumer<MinecraftServer> consumer) {
        ServerLifecycleEvents.SERVER_STARTED.register(consumer::accept);
    }

    @Override
    public void onDataPackReload(Runnable runnable) {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> runnable.run());
    }

    @Override
    public void modifyItemComponents(Consumer<LNEEvents.ItemComponentContext> consumer) {
        DefaultItemComponentEvents.MODIFY.register(context -> consumer.accept((item, mutator) ->
                context.modify(item, builder -> mutator.accept(new LNEEvents.ComponentSink() {
                    @Override
                    public <T> void add(ComponentType<T> type, T value) {
                        builder.add(type, value);
                    }
                }))));
    }
}

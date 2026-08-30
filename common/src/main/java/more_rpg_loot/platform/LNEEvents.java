package more_rpg_loot.platform;

import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class LNEEvents {
    public interface Impl {
        void registerServerWorldTick(Consumer<ServerWorld> listener);

        void sendToClient(ServerPlayerEntity player, CustomPayload payload);

        void modifyItemGroup(RegistryKey<ItemGroup> group, Consumer<Entries> filler);

        ItemGroup createItemGroup(Supplier<ItemStack> icon, Text displayName);

        void registerVillagerTrades(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factories);

        void onLootTableModify(Consumer<LootTableModifyContext> consumer);

        void onServerStarted(Consumer<MinecraftServer> consumer);

        void onDataPackReload(Runnable runnable);

        void modifyItemComponents(Consumer<ItemComponentContext> consumer);
    }

    public interface Entries {
        void add(ItemConvertible item);

        void addAfter(ItemConvertible anchor, ItemConvertible item);
    }

    public interface LootTableModifyContext {
        RegistryWrapper.WrapperLookup registries();

        Identifier tableId();

        RegistryKey<LootTable> tableKey();

        boolean isBuiltin();

        void addPool(LootPool pool);
    }

    public interface ItemComponentContext {
        void modify(Item item, Consumer<ComponentSink> mutator);
    }

    public interface ComponentSink {
        <T> void add(ComponentType<T> type, T value);
    }

    private static Impl impl;

    private LNEEvents() {}

    public static void set(Impl value) {
        impl = value;
    }

    public static Impl get() {
        return impl;
    }
}

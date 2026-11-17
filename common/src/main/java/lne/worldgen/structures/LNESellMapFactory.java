package lne.worldgen.structures;

import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapColorComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;

public class LNESellMapFactory implements TradeOffers.Factory {
    private static final int SEARCH_BLOCKS = 10_000;
    private static final int FALLBACK_RADIUS = 100;

    private final int price;
    private final TagKey<Structure> structure;
    private final String nameKey;
    private final RegistryEntry<MapDecorationType> decoration;
    private final int maxUses;
    private final int experience;
    private final int mapColor;

    public LNESellMapFactory(int price, TagKey<Structure> structure, String nameKey, RegistryEntry<MapDecorationType> decoration, int maxUses, int experience, int mapColor) {
        this.price = price;
        this.structure = structure;
        this.decoration = decoration;
        this.nameKey = nameKey;
        this.maxUses = maxUses;
        this.experience = experience;
        this.mapColor = mapColor;
    }

    @Override
    @Nullable
    public TradeOffer create(Entity entity, Random random) {

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) {
            return null;
        }


        BlockPos blockPos = serverWorld.locateStructure(this.structure, entity.getBlockPos(), searchRadius(serverWorld), false);
        if (blockPos != null) {
            ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
            FilledMapItem.fillExplorationMap(serverWorld, itemStack);
            MapState.addDecorationsNbt(itemStack, blockPos, "+", this.decoration);
            itemStack.set(DataComponentTypes.ITEM_NAME, Text.translatable(this.nameKey));
            itemStack.set(DataComponentTypes.MAP_COLOR, new MapColorComponent(this.mapColor));
            return new TradeOffer(new TradedItem(Items.EMERALD, this.price), Optional.of(new TradedItem(Items.PAPER)), itemStack, this.maxUses, this.experience, 0.2F);
        }
        return null;
    }

    // locateStructure counts its radius in placement-grid steps (spacing chunks), not in chunks.
    private int searchRadius(ServerWorld world) {
        StructurePlacementCalculator calculator = world.getChunkManager().getStructurePlacementCalculator();
        return world.getRegistryManager().get(RegistryKeys.STRUCTURE).getEntryList(this.structure)
                .map(entries -> entries.stream()
                        .flatMap(entry -> calculator.getPlacements(entry).stream())
                        .filter(placement -> placement instanceof RandomSpreadStructurePlacement)
                        .mapToInt(placement -> ((RandomSpreadStructurePlacement) placement).getSpacing())
                        .min()
                        .orElse(0))
                .filter(spacing -> spacing > 0)
                .map(spacing -> Math.ceilDiv(SEARCH_BLOCKS, spacing * 16))
                .orElse(FALLBACK_RADIUS);
    }
}
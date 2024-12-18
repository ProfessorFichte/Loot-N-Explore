package more_rpg_loot.worldgen.structures;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.gen.structure.Structure;

public class LNESellMapFactory implements TradeOffers.Factory {
    private final int price;
    private final TagKey<Structure> structure;
    private final String nameKey;
    private final MapIcon.Type iconType;
    private final int maxUses;
    private final int experience;

    public LNESellMapFactory(int price, TagKey<Structure> structure, String nameKey, MapIcon.Type iconType, int maxUses, int experience) {
        this.price = price;
        this.structure = structure;
        this.nameKey = nameKey;
        this.iconType = iconType;
        this.maxUses = maxUses;
        this.experience = experience;
    }

    @Override
    @Nullable
    public TradeOffer create(Entity entity, Random random) {

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) {
            return null;
        }


        BlockPos blockPos = serverWorld.locateStructure(this.structure, entity.getBlockPos(), 100, true);
        if (blockPos != null) {
            ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
            FilledMapItem.fillExplorationMap(serverWorld, itemStack);
            MapState.addDecorationsNbt(itemStack, blockPos, "+", this.iconType);
            itemStack.setCustomName(Text.translatable(this.nameKey));
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(Items.PAPER), itemStack, this.maxUses, this.experience, 0.2f);
        }
        return null;
    }
}
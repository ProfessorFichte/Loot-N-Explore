package lne.blocks.frozen_depths;

import lne.blocks.generic.VariantControls;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FrozenDecoratedPotBlock extends DecoratedPotBlock {
    public static final EnumProperty<Pattern> PATTERN = EnumProperty.of("pattern", Pattern.class);

    public FrozenDecoratedPotBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(PATTERN, Pattern.PLAIN));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PATTERN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) return null;
        BlockPos pos = context.getBlockPos();
        Pattern[] patterns = Pattern.values();
        return state.with(PATTERN, patterns[Math.floorMod(MathHelper.hashCode(pos.getX(), pos.getY(), pos.getZ()), patterns.length)]);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!VariantControls.canChangeVariant(player)) {
            return super.onUse(state, world, pos, player, hit);
        }
        if (!world.isClient) {
            world.setBlockState(pos, state.cycle(PATTERN), Block.NOTIFY_ALL);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        VariantControls.appendTooltip(tooltip);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        var stack = super.getPickStack(world, pos, state);
        return stack.isOf(asItem()) ? stack : stack.copyComponentsToNewStack(this, stack.getCount());
    }

    public enum Pattern implements StringIdentifiable {
        PLAIN("plain", "frozen_decorated_pot_side"),
        CROWN("crown", "frozen_crown_pattern"),
        FROST_HAUNTED("frost_haunted", "frozen_frost_haunted_pattern"),
        GLAZE("glaze", "frozen_glaze_pattern"),
        MIXED("mixed", null);

        private static final List<Pattern[]> SIDE_ORDERS = sideOrders(new Pattern[0], List.of(PLAIN, CROWN, FROST_HAUNTED, GLAZE));

        private final String name;
        public final String texture;

        Pattern(String name, String texture) {
            this.name = name;
            this.texture = texture;
        }

        @Override
        public String asString() {
            return name;
        }

        public Pattern forSide(BlockPos pos, int side) {
            if (this != MIXED) return this;
            return SIDE_ORDERS.get(Math.floorMod(MathHelper.hashCode(pos.getX(), pos.getY(), pos.getZ()), SIDE_ORDERS.size()))[side];
        }

        private static List<Pattern[]> sideOrders(Pattern[] prefix, List<Pattern> remaining) {
            if (remaining.isEmpty()) return Collections.singletonList(prefix);
            List<Pattern[]> orders = new ArrayList<>();
            for (Pattern next : remaining) {
                Pattern[] order = Arrays.copyOf(prefix, prefix.length + 1);
                order[prefix.length] = next;
                List<Pattern> rest = new ArrayList<>(remaining);
                rest.remove(next);
                orders.addAll(sideOrders(order, rest));
            }
            return orders;
        }
    }
}

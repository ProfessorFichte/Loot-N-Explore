package lne.blocks.generic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class VariantBookshelfBlock extends Block {
    private static final int[] WEIGHTS = {3, 3, 2, 2, 2};
    private static final int TOTAL_WEIGHT = 12;
    public static final IntProperty VARIANT = IntProperty.of("variant", 0, WEIGHTS.length - 1);

    public VariantBookshelfBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(VARIANT, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = context.getBlockPos();
        int roll = Math.floorMod(MathHelper.hashCode(pos.getX(), pos.getY(), pos.getZ()), TOTAL_WEIGHT);
        int variant = 0;
        while (roll >= WEIGHTS[variant]) {
            roll -= WEIGHTS[variant];
            variant++;
        }
        return getDefaultState().with(VARIANT, variant);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!VariantControls.canChangeVariant(player)) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            world.setBlockState(pos, state.cycle(VARIANT), Block.NOTIFY_ALL);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        VariantControls.appendTooltip(tooltip);
    }
}

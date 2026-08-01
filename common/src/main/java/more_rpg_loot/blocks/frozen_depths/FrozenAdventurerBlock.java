package more_rpg_loot.blocks.frozen_depths;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FrozenAdventurerBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
    public static final IntProperty VARIANT = IntProperty.of("variant", 0, 2);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public FrozenAdventurerBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(VARIANT, 0)
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
                .with(VARIANT, ctx.getWorld().getRandom().nextInt(3))
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANT, FACING);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().creativeMode) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            if (player.isSneaking()) {
                Direction current = state.get(FACING);
                Direction next = current.rotateYClockwise();
                world.setBlockState(pos, state.with(FACING, next));
            } else {
                int current = state.get(VARIANT);
                int next = (current + 1) % 3;
                world.setBlockState(pos, state.with(VARIANT, next));
            }
        }
        return ActionResult.SUCCESS;
    }
}

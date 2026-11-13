package lne.blocks.frozen_depths;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FrozenKingdomFlagBlock extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.of("wall");

    private static final VoxelShape STANDING = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static final Map<Direction, VoxelShape> WALL_SHAPES = Map.of(
            Direction.NORTH, Block.createCuboidShape(2.0, 0.0, 14.0, 14.0, 16.0, 16.0),
            Direction.SOUTH, Block.createCuboidShape(2.0, 0.0, 0.0, 14.0, 16.0, 2.0),
            Direction.WEST, Block.createCuboidShape(14.0, 0.0, 2.0, 16.0, 16.0, 14.0),
            Direction.EAST, Block.createCuboidShape(0.0, 0.0, 2.0, 2.0, 16.0, 14.0));

    public FrozenKingdomFlagBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(WALL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WALL);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(WALL) ? WALL_SHAPES.get(state.get(FACING)) : STANDING;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        Direction side = context.getSide();
        if (side.getAxis().isHorizontal()) {
            var wall = getDefaultState().with(WALL, true).with(FACING, side);
            if (wall.canPlaceAt(world, pos)) return wall;
        }
        var standing = getDefaultState().with(WALL, false).with(FACING, context.getHorizontalPlayerFacing().getOpposite());
        return standing.canPlaceAt(world, pos) ? standing : null;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(WALL)) {
            Direction facing = state.get(FACING);
            BlockPos support = pos.offset(facing.getOpposite());
            return world.getBlockState(support).isSideSolidFullSquare(world, support, facing);
        }
        return Block.sideCoversSmallSquare(world, pos.down(), Direction.UP);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world,
                                                   BlockPos pos, BlockPos neighborPos) {
        Direction support = state.get(WALL) ? state.get(FACING).getOpposite() : Direction.DOWN;
        if (direction == support && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}

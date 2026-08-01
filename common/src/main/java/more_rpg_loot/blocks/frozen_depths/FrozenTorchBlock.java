package more_rpg_loot.blocks.frozen_depths;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class FrozenTorchBlock extends Block {
    public static final BooleanProperty FLOOR = BooleanProperty.of("floor");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private static final VoxelShape FLOOR_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 10, 10);
    private static final VoxelShape WALL_NORTH_SHAPE = Block.createCuboidShape(6, 2, 11, 10, 12, 16);
    private static final VoxelShape WALL_SOUTH_SHAPE = Block.createCuboidShape(6, 2, 0, 10, 12, 5);
    private static final VoxelShape WALL_WEST_SHAPE  = Block.createCuboidShape(11, 2, 6, 16, 12, 10);
    private static final VoxelShape WALL_EAST_SHAPE  = Block.createCuboidShape(0, 2, 6, 5, 12, 10);

    public FrozenTorchBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FLOOR, true)
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FLOOR, FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction side = ctx.getSide();
        if (side == Direction.UP) {
            return getDefaultState().with(FLOOR, true).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        }
        if (side.getAxis().isHorizontal()) {
            return getDefaultState().with(FLOOR, false).with(FACING, side);
        }
        return null;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(FLOOR)) {
            return Block.sideCoversSmallSquare(world, pos.down(), Direction.UP);
        }
        Direction facing = state.get(FACING);
        return Block.sideCoversSmallSquare(world, pos.offset(facing.getOpposite()), facing);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(FLOOR)) return FLOOR_SHAPE;
        return switch (state.get(FACING)) {
            case NORTH -> WALL_NORTH_SHAPE;
            case SOUTH -> WALL_SOUTH_SHAPE;
            case WEST  -> WALL_WEST_SHAPE;
            case EAST  -> WALL_EAST_SHAPE;
            default    -> VoxelShapes.fullCube();
        };
    }
}

package more_rpg_loot.blocks;

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

public class HangingInnSignBlock extends Block {
    private static final VoxelShape HANGING_SHAPE  = Block.createCuboidShape(2,  0, 7, 14, 12,  9);
    private static final VoxelShape NORTH_SHAPE    = Block.createCuboidShape(2,  1, 13, 14, 12, 16);
    private static final VoxelShape SOUTH_SHAPE    = Block.createCuboidShape(2,  1,  0, 14, 12,  3);
    private static final VoxelShape EAST_SHAPE     = Block.createCuboidShape(0,  1,  2,  3, 12, 14);
    private static final VoxelShape WEST_SHAPE     = Block.createCuboidShape(13, 1,  2, 16, 12, 14);

    public static final BooleanProperty HANGING = BooleanProperty.of("hanging");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public HangingInnSignBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(HANGING, false)
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HANGING)) return HANGING_SHAPE;
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST  -> EAST_SHAPE;
            case WEST  -> WEST_SHAPE;
            default    -> VoxelShapes.fullCube();
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HANGING, FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction side = ctx.getSide();
        if (side == Direction.DOWN) {
            return getDefaultState().with(HANGING, true)
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        }
        if (side.getAxis().isHorizontal()) {
            return getDefaultState().with(HANGING, false).with(FACING, side);
        }
        return null;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HANGING)) {
            return world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos.up(), Direction.DOWN);
        }
        Direction facing = state.get(FACING);
        return world.getBlockState(pos.offset(facing.getOpposite()))
                .isSideSolidFullSquare(world, pos.offset(facing.getOpposite()), facing);
    }
}

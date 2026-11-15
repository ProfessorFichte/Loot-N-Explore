package lne.blocks.frozen_depths;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

public class FrozenBonesBlock extends Block {
    private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 8, 14);
    private static final float BREAK_CHANCE = 0.1F;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public FrozenBonesBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world instanceof ServerWorld serverWorld && world.getDifficulty() != Difficulty.PEACEFUL && entity instanceof PlayerEntity player
                && !player.isCreative() && !player.isSpectator()
                && steppedOnThisTick(pos, player) && world.random.nextFloat() < BREAK_CHANCE) {
            world.breakBlock(pos, false, player);
            FrozenBonesAmbush.schedule(serverWorld, pos, player);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    // onSteppedOn runs every tick the player stands here, the chance should only be rolled once per walk-over.
    private static boolean steppedOnThisTick(BlockPos pos, PlayerEntity player) {
        return MathHelper.floor(player.prevX) != pos.getX() || MathHelper.floor(player.prevZ) != pos.getZ() || player.prevY > player.getY();
    }
}

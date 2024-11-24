package more_rpg_loot.blocks;

import more_rpg_loot.entity.ModEntities;
import more_rpg_loot.entity.mob.FrostMonarchEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;


public class MonarchsCrownBlock extends Block{
    public static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.5, 0.75)
    );
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    @Nullable
    private static BlockPattern monarchBossPattern;
    public MonarchsCrownBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        FluidState fluidState = world.getFluidState(blockPos);
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Boolean.TRUE.equals(state.get(WATERLOGGED)) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockState blockState = world.getBlockState(pos);
        if(blockState.isOf(ModBlocks.MONARCHS_CROWN.block())){
            BlockPattern.Result result = getWitherBossPattern().searchAround(world, pos);
            if (result != null) {
                if (pos.getY() >= world.getBottomY() && world.getDifficulty() != Difficulty.PEACEFUL) {
                    FrostMonarchEntity frostMonarchEntity = (FrostMonarchEntity) ModEntities.FROST_MONARCH.create(world);
                    if (frostMonarchEntity != null) {
                        CarvedPumpkinBlock.breakPatternBlocks(world, result);
                        BlockPos blockPos = result.translate(1, 2, 0).getBlockPos();
                        frostMonarchEntity.refreshPositionAndAngles((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.55, (double)blockPos.getZ() + 0.5, result.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F);
                        frostMonarchEntity.bodyYaw = result.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;
                        Iterator var8 = world.getNonSpectatingEntities(ServerPlayerEntity.class, frostMonarchEntity.getBoundingBox().expand(50.0)).iterator();

                        while(var8.hasNext()) {
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var8.next();
                            Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, frostMonarchEntity);
                        }
                        world.spawnEntity(frostMonarchEntity);
                        CarvedPumpkinBlock.updatePatternBlocks(world, result);
                    }
                }
            }
        }

    }

    private static BlockPattern getWitherBossPattern() {
        if (monarchBossPattern == null) {
            monarchBossPattern = BlockPatternBuilder.start().aisle(new String[]{"~^~", "~#~"}).where('#', (pos) -> {
                return pos.getBlockState().isOf(ModBlocks.FROZEN_SOULS.block());
            }).where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.MONARCHS_CROWN.block()))).where('~', (pos) -> {
                return pos.getBlockState().isAir();
            }).build();
        }

        return monarchBossPattern;
    }


}

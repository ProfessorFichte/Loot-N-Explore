package more_rpg_loot.blocks.frozen_depths;

import net.minecraft.block.*;
import net.minecraft.block.LandingBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class IcicleBlock extends Block implements LandingBlock {
    public static final DirectionProperty VERTICAL_DIRECTION = Properties.VERTICAL_DIRECTION;
    public static final EnumProperty<Thickness> THICKNESS = Properties.THICKNESS;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape TIP_MERGE_SHAPE = Block.createCuboidShape(5, 0, 5, 11, 16, 11);
    private static final VoxelShape TIP_DOWN_SHAPE  = Block.createCuboidShape(5, 5, 5, 11, 16, 11);
    private static final VoxelShape TIP_UP_SHAPE    = Block.createCuboidShape(5, 0, 5, 11, 11, 11);
    private static final VoxelShape FRUSTUM_SHAPE   = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
    private static final VoxelShape MIDDLE_SHAPE    = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
    private static final VoxelShape BASE_SHAPE      = Block.createCuboidShape(2, 0, 2, 14, 16, 14);

    private static final int MAX_FALL_SCAN = 12;
    private static final float FALL_CHANCE = 0.05f;

    public IcicleBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(VERTICAL_DIRECTION, Direction.DOWN)
                .with(THICKNESS, Thickness.TIP)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_DIRECTION, THICKNESS, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction side = ctx.getSide();
        Direction dir = (side == Direction.UP) ? Direction.UP : Direction.DOWN;
        boolean waterlogged = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
        Thickness thickness = getThickness(ctx.getWorld(), ctx.getBlockPos(), dir);
        return getDefaultState()
                .with(VERTICAL_DIRECTION, dir)
                .with(THICKNESS, thickness)
                .with(WATERLOGGED, waterlogged);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        }

        Direction dir = state.get(VERTICAL_DIRECTION);

        if (direction == dir.getOpposite() && !canPlaceAt(state, world, pos)) {
            int delay = dir == Direction.DOWN ? 2 : 1;
            world.scheduleBlockTick(pos, this, delay);
            return state;
        }

        return state.with(THICKNESS, getThickness(world, pos, dir));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(VERTICAL_DIRECTION) == Direction.UP) {
            if (!canPlaceAt(state, world, pos)) {
                world.breakBlock(pos, true);
            }
        } else {
            spawnFallingIcicleChain(state, world, pos);
        }
    }

    private static final int LANDING_FREEZE_TICKS = 60;

    @Override
    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        if (world instanceof ServerWorld serverWorld) {
            double impact = Math.min(fallingBlockEntity.fallDistance, 40.0);
            if (impact > 0) {
                serverWorld.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(0.3, 0.5, 0.3),
                        e -> !e.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES))
                        .forEach(e -> {
                            e.damage(serverWorld.getDamageSources().fallingBlock(fallingBlockEntity),
                                    (float) (impact * 2.0));
                            if (e.canFreeze()) {
                                e.setFrozenTicks(e.getFrozenTicks() + LANDING_FREEZE_TICKS);
                            }
                        });
            }
        }
    }

    @Override
    public net.minecraft.fluid.FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.FLOWING_WATER.getDefaultState() : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(VERTICAL_DIRECTION);
        return switch (state.get(THICKNESS)) {
            case TIP_MERGE -> TIP_MERGE_SHAPE;
            case TIP -> dir == Direction.DOWN ? TIP_DOWN_SHAPE : TIP_UP_SHAPE;
            case FRUSTUM -> FRUSTUM_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case BASE -> BASE_SHAPE;
        };
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction dir = state.get(VERTICAL_DIRECTION);
        BlockPos attachPos = pos.offset(dir.getOpposite());
        BlockState attachState = world.getBlockState(attachPos);
        return attachState.isSideSolidFullSquare(world, attachPos, dir) || isIcicle(attachState, dir);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof LivingEntity living) {
            if (entity instanceof PlayerEntity player && player.getAbilities().invulnerable) return;
            if (!living.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                living.setFrozenTicks(living.getFrozenTicks() + 2);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(VERTICAL_DIRECTION) != Direction.DOWN) return;
        if (random.nextFloat() >= FALL_CHANCE) return;

        Box scanBox = new Box(
                pos.getX(), pos.getY() - MAX_FALL_SCAN, pos.getZ(),
                pos.getX() + 1, pos.getY(), pos.getZ() + 1);

        if (!world.getEntitiesByType(TypeFilter.instanceOf(PlayerEntity.class), scanBox, p -> true).isEmpty()) {
            spawnFallingIcicleChain(state, world, pos);
        }
    }

    private void spawnFallingIcicleChain(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutablePos = pos.mutableCopy();
        BlockState currentState = state;

        while (isPointingDown(currentState)) {
            FallingBlockEntity.spawnFromBlock(world, mutablePos, currentState);
            if (isTip(currentState)) break;
            mutablePos.move(Direction.DOWN);
            currentState = world.getBlockState(mutablePos);
        }
    }

    private static Thickness getThickness(WorldView world, BlockPos pos, Direction dir) {
        Direction opposite = dir.getOpposite();
        BlockState blockInDir = world.getBlockState(pos.offset(dir));

        if (isIcicle(blockInDir, opposite)) {
            return Thickness.TIP_MERGE;
        }
        if (!isIcicle(blockInDir, dir)) {
            return Thickness.TIP;
        }

        Thickness inDirThickness = blockInDir.get(THICKNESS);
        if (inDirThickness == Thickness.TIP || inDirThickness == Thickness.TIP_MERGE) {
            return Thickness.FRUSTUM;
        }

        BlockState blockOpposite = world.getBlockState(pos.offset(opposite));
        if (!isIcicle(blockOpposite, dir)) {
            return Thickness.BASE;
        }
        return Thickness.MIDDLE;
    }

    private static boolean isPointingDown(BlockState state) {
        return state.getBlock() instanceof IcicleBlock && state.get(VERTICAL_DIRECTION) == Direction.DOWN;
    }

    private static boolean isTip(BlockState state) {
        if (!(state.getBlock() instanceof IcicleBlock)) return false;
        Thickness t = state.get(THICKNESS);
        return t == Thickness.TIP || t == Thickness.TIP_MERGE;
    }

    private static boolean isIcicle(BlockState state, Direction dir) {
        return state.getBlock() instanceof IcicleBlock && state.get(VERTICAL_DIRECTION) == dir;
    }
}
